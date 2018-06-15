package com.mmall.service;

import com.google.common.base.Preconditions;
import com.mmall.beans.PageQuery;
import com.mmall.beans.PageResult;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysDeptMapper;
import com.mmall.dao.SysUserMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysUser;
import com.mmall.param.UserParam;
import com.mmall.util.BeanValidator;
import com.mmall.util.IpUtil;
import com.mmall.util.MD5Util;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Alan Brown
 * @date 2018/5/25 17:52
 */

@Service
public class SysUserService {

	@Resource
	private SysUserMapper sysUserMapper;
	@Resource
	private SysDeptMapper sysDeptMapper;
	@Resource
	private SysLogService sysLogService;

	// 保存用户
	public void save(UserParam param){
		BeanValidator.check(param); // 校验param是否合法
		// 登录时，是使用邮箱或者电话号码登录的，人名可以重复
		if(checkEmailExist(param.getMail(), param.getId() )){
			throw new ParamException("该邮箱已经被注册过了，请更换");
		}
		if(checkTelephoneExist(param.getTelephone(), param.getId())){
			throw new ParamException("该电话号码已经被注册过了，请更换");
		}
		String password = "123456"; // TODO: 使用PasswordUtil来生成
		String salt = UUID.randomUUID().toString().substring(0, 6).replace("-", ""); // 随机盐，6位
		String encryptedPassword = MD5Util.encrypt(password + salt); // MD5加密后的密码
		SysUser user = SysUser.builder().username(param.getUsername())
				.telephone(param.getTelephone()).mail(param.getMail())
				.password(encryptedPassword).salt(salt)
				.deptId(param.getDeptId())
				.status(param.getStatus()).remark(param.getRemark())
				.build();
		user.setOperator(RequestHolder.getCurrentUser().getUsername());
		user.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
		user.setOperateTime(new Date());

		// TODO: 发送邮件

		// 保存这个用户
		sysUserMapper.insertSelective(user);
		sysLogService.saveUserLog(null, user);
	}

	// 更新用户
	public void update(UserParam param){
		BeanValidator.check(param); // 校验param是否合法
		if(checkEmailExist(param.getMail(), param.getId() )){
			throw new ParamException("该邮箱已经被注册过了，请更换");
		}
		if(checkTelephoneExist(param.getTelephone(), param.getId())){
			throw new ParamException("该电话号码已经被注册过了，请更换");
		}
		SysUser before = sysUserMapper.selectByPrimaryKey(param.getId());
		Preconditions.checkNotNull(before, "待更新的用户不存在");
		SysUser after = SysUser.builder().id(param.getId()).username(param.getUsername())
				.telephone(param.getTelephone()).mail(param.getMail())
				.deptId(param.getDeptId())
				.status(param.getStatus()).remark(param.getRemark())
				.build();
		after.setOperator(RequestHolder.getCurrentUser().getUsername());
		after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
		after.setOperateTime(new Date());
		sysUserMapper.updateByPrimaryKeySelective(after);
		sysLogService.saveUserLog(before, after);
	}

	// 查询某个部门的所有用户
	public PageResult<SysUser> getPageByDeptId(int deptId, PageQuery page){
		BeanValidator.check(page);
		String level = sysDeptMapper.selectByPrimaryKey(deptId).getLevel(); // 部门级别
		int count = sysUserMapper.countByDeptId(deptId);
		if (count > 0) {
			List<SysUser> list = sysUserMapper.getPageByDeptId(deptId, page);
			return PageResult.<SysUser>builder().total(count).data(list).build();
		}
		return PageResult.<SysUser>builder().build();
	}


	// 根据电话号码或者邮箱获取用户
	public SysUser findByKeyword(String keyword){
		return sysUserMapper.findByKeyword(keyword);
	}

	// 检查邮箱是否在数据库中存在
	private boolean checkEmailExist(String mail, Integer userId){
		return sysUserMapper.countByMail(mail, userId) > 0;
	}

	// 检查电话是否在数据库中存在
	private boolean checkTelephoneExist(String telephone, Integer userId){
		return sysUserMapper.countByTelephone(telephone, userId) > 0;
	}

	// 获取所有用户
	public List<SysUser> getAll(){
		return sysUserMapper.getAll();
	}

}
