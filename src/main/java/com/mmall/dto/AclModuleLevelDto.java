package com.mmall.dto;

import com.google.common.collect.Lists;
import com.mmall.model.SysAclModule;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author Alan Brown
 * @date 2018/5/28 14:36
 */
@Setter
@Getter
@ToString
public class AclModuleLevelDto extends SysAclModule {

	// 该权限模块下的权限模块
	private List<AclModuleLevelDto> aclModuleList = Lists.newArrayList();

	// 该权限模块下的权限点
	private List<AclDto> aclList = Lists.newArrayList();

	public static AclModuleLevelDto adapt(SysAclModule aclModule){
		AclModuleLevelDto dto = new AclModuleLevelDto();
		BeanUtils.copyProperties(aclModule, dto);
		return dto;
	}


}
