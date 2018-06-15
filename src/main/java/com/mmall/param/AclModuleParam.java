package com.mmall.param;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Alan Brown
 * @date 2018/5/27 21:39
 */

@Getter
@Setter
@ToString
public class AclModuleParam {

	private Integer Id;

	@NotBlank(message = "权限模块名称不能为空")
	@Length(min = 2, max = 20, message = "权限名称长度应在2~20之间")
	private String name;

	private Integer parentId = 0;

	@NotNull(message = "权限模块展示顺序不能为空")
	private Integer seq;

	@NotNull(message = "权限模块状态不能为空")
	@Min(value = 0, message = "权限模块状态不合法")
	@Max(value = 1, message = "权限模块状态不合法")
	private Integer status;

	@Length(max = 200, message = "备注应在200字以内")
	private String remark;






}
