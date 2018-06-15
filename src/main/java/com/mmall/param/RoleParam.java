package com.mmall.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Alan Brown
 * @date 2018/5/28 19:44
 */
@Getter
@Setter
@ToString
public class RoleParam {

	private Integer id;

	@NotBlank(message = "角色名不能为空")
	@Length(min = 1, max = 20, message = "角色名长度应在1~20之间")
	private String name;

	@NotNull(message = "角色类型不能为空")
	@Min(value = 1, message = "角色类型不合法")
	@Max(value = 2, message = "角色类型不合法")
	private Integer type = 1;

	@NotNull(message = "角色状态不能为空")
	@Min(value = 0, message = "角色状态不合法")
	@Max(value = 1, message = "角色状态不合法")
	private Integer status;

	@Length(max = 200, message = "备注字数应在200以内")
	private String remark;


}
