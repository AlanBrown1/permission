package com.mmall.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysRoleAclMapper;
import com.mmall.dao.SysRoleMapper;
import com.mmall.dao.SysRoleUserMapper;
import com.mmall.dao.SysUserMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysRole;
import com.mmall.model.SysUser;
import com.mmall.param.RoleParam;
import com.mmall.util.BeanValidator;
import com.mmall.util.IpUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alan Brown
 * @date 2018/5/28 19:43
 */
@Service
public class SysRoleService {

	@Resource
	private SysRoleMapper sysRoleMapper;
	@Resource
	private SysRoleAclMapper sysRoleAclMapper;
	@Resource
	private SysRoleUserMapper sysRoleUserMapper;
	@Resource
	private SysUserMapper sysUserMapper;
	@Resource
	private SysLogService sysLogService;

	// 保存角色
	public void save(RoleParam param){
		BeanValidator.check(param);
		if(checkExist(param.getName(), param.getId())){
			throw new ParamException("该角色已经存在");
		}
		SysRole role = SysRole.builder().name(param.getName())
						.type(param.getType()).status(param.getStatus())
						.remark(param.getRemark())
						.build();
		role.setOperator(RequestHolder.getCurrentUser().getUsername());
		role.setOperateTime(new Date());
		role.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
		sysRoleMapper.insertSelective(role);
		sysLogService.saveRoleLog(null, role);
	}

	// 更新角色
	public void update(RoleParam param){
		BeanValidator.check(param);
		if(checkExist(param.getName(), param.getId())){
			throw new ParamException("该角色已经存在");
		}
		SysRole before = sysRoleMapper.selectByPrimaryKey(param.getId());
		Preconditions.checkNotNull(before, "待更新的角色不存在");
		SysRole after = SysRole.builder().id(param.getId()).name(param.getName())
				.type(param.getType()).status(param.getStatus())
				.remark(param.getRemark())
				.build();
		after.setOperator(RequestHolder.getCurrentUser().getUsername());
		after.setOperateTime(new Date());
		after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
		sysRoleMapper.updateByPrimaryKeySelective(after);
		sysLogService.saveRoleLog(before, after);
	}

	// 检验是否已经存在
	private boolean checkExist(String name, Integer id){
		return sysRoleMapper.countByName(name, id) > 0;
	}

	// 获取所有角色
	public List<SysRole> getAllRoles(){
		return sysRoleMapper.getAllRoles();
	}

	// 获取分配给某个用户的角色
	public List<SysRole> getRoleListByAclId(int aclId) {
		List<Integer> roleIdList = sysRoleAclMapper.getRoleIdListByAclId(aclId);
		if (CollectionUtils.isEmpty(roleIdList)) {
			return Lists.newArrayList();
		}
		return sysRoleMapper.getByIdList(roleIdList);
	}

	// 获取角色列表获取用户列表
	public List<SysUser> getUserListByRoleList(List<SysRole> roleList) {
		if (CollectionUtils.isEmpty(roleList)) {
			return Lists.newArrayList();
		}
		List<Integer> roleIdList = roleList.stream().map(role -> role.getId()).collect(Collectors.toList());
		List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleIdList(roleIdList);
		if (CollectionUtils.isEmpty(userIdList)) {
			return Lists.newArrayList();
		}
		return sysUserMapper.getByIdList(userIdList);
	}

	public List<SysRole> getRoleListByUserId(int userId) {
		List<Integer> roleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
		if (CollectionUtils.isEmpty(roleIdList)) {
			return Lists.newArrayList();
		}
		return sysRoleMapper.getByIdList(roleIdList);
	}

}
