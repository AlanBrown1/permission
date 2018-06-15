package com.mmall.service;

import com.alibaba.druid.sql.PagerUtils;
import com.google.common.base.Preconditions;
import com.mmall.beans.PageQuery;
import com.mmall.beans.PageResult;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysAclMapper;
import com.mmall.dao.SysAclModuleMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysAcl;
import com.mmall.model.SysAclModule;
import com.mmall.model.SysDept;
import com.mmall.param.AclParam;
import com.mmall.util.BeanValidator;
import com.mmall.util.IpUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Alan Brown
 * @date 2018/5/28 16:57
 */
@Service
public class SysAclService {

	@Resource
	private SysAclMapper sysAclMapper;
	@Resource
	private SysAclModuleMapper sysAclModuleMapper;
	@Resource
	private SysLogService sysLogService;

	// 保存权限点
	public void save(AclParam param){
		BeanValidator.check(param);
		if(checkExist(param.getAclModuleId(), param.getName(), param.getId())){
			throw new ParamException("当前权限模块中存在相同名称的权限点");
		}
		SysAcl acl = SysAcl.builder()
					 .name(param.getName()).aclModuleId(param.getAclModuleId())
					 .url(param.getUrl()).type(param.getType())
					 .status(param.getStatus()).seq(param.getSeq())
					 .remark(param.getRemark())
					 .build();
		acl.setCode(generateCode());
		acl.setOperator(RequestHolder.getCurrentUser().getUsername());
		acl.setOperateTime(new Date());
		acl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
		sysAclMapper.insertSelective(acl);
		sysLogService.saveAclLog(null, acl);
	}

	// 更新权限点
	public void update(AclParam param){
		BeanValidator.check(param);
		if(checkExist(param.getAclModuleId(), param.getName(), param.getId())){
			throw new ParamException("当前权限模块中存在相同名称的权限点");
		}
		SysAcl before = sysAclMapper.selectByPrimaryKey(param.getId());
		Preconditions.checkNotNull(before,"待更新的权限点不存在" );
		SysAcl after = SysAcl.builder().id(param.getId())
				.name(param.getName()).aclModuleId(param.getAclModuleId())
				.url(param.getUrl()).type(param.getType())
				.status(param.getStatus()).seq(param.getSeq())
				.remark(param.getRemark())
				.build();
		after.setOperator(RequestHolder.getCurrentUser().getUsername());
		after.setOperateTime(new Date());
		after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
		sysAclMapper.updateByPrimaryKeySelective(after);
		sysLogService.saveAclLog(before, after);
	}

	// 根据权限模块id获取该模块下的所有权限点
	public PageResult<SysAcl> getPageByAclModuleId(Integer aclModuleId, PageQuery page){
		BeanValidator.check(page);
		int count = sysAclMapper.countByAclModuleId(aclModuleId);
		if(count > 0 ){
			List<SysAcl> aclList = sysAclMapper.getPageByAclModuleId(aclModuleId, page);
			return PageResult.<SysAcl>builder().data(aclList).total(count).build();
		}
		return PageResult.<SysAcl>builder().build();
	}

	// 检测同一级中是否有相同名称的权限点
	private boolean checkExist(int aclModuleId, String name, Integer id){
		return sysAclMapper.countByNameAndAclModuleId(aclModuleId, name, id) > 0;
	}

	// 根据时间戳生成权限点的code值
	private String generateCode(){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		return format.format(new Date()) + "_" + Math.round(Math.random()*100);
	}


}
