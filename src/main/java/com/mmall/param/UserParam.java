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
 * @date 2018/5/25 17:36
 */
// 用户参数，这里面是必须提供的参数
// 用于校验
@Getter
@Setter
@ToString
public class UserParam {

	private Integer id;

	@NotBlank(message = "用户名不可以为空")
	@Length(min = 1, max = 20, message = "用户名长度应在1~20以内")
	private String username;

	@NotBlank(message = "电话不能为空")
	@Length(min = 1, max = 13, message = "电话长度应在1~13以内")
	private String telephone;

	@NotBlank(message = "邮箱不能为空")
	@Length(min = 5, max = 50, message = "邮箱长度应在5~50以内")
	private String mail;

	@NotNull(message = "上级部门不能为空")
	private Integer deptId;

	@NotNull(message = "必须指定用户的状态")
	@Min(value = 0, message = "用户状态不合法")
	@Max(value = 2, message = "用户状态不合法")
	private Integer status;

	@Length(max = 200, message = "备注长度应在200字以内")
	private String remark = "";

}
