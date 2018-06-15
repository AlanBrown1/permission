package com.mmall.dto;

import com.mmall.model.SysAcl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

/**
 * @author Alan Brown
 * @date 2018/5/29 15:57
 */

@Getter
@Setter
@ToString
public class AclDto extends SysAcl {

	// 是否已经拥有该权限
	private boolean checked = false;

	// 是否有权限操作(该用户在分配角色权限的时候，不能超过本身的权限)
	private boolean hasAcl = false;

	public static AclDto adapt(SysAcl sysAcl){
		AclDto dto = new AclDto();
		BeanUtils.copyProperties(sysAcl, dto);
		return dto;
	}


}
