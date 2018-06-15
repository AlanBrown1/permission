package com.mmall.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author Alan Brown
 * @date 2018/5/24 19:21
 */
// 部门参数，这里面是必须提供的参数
// 用于校验
@Getter
@Setter
@ToString
public class DeptParam {

	private Integer id;

	@NotBlank(message = "部门名称不能为空")
	@Length(max = 15, min = 2, message = "部门名称长度应在2~15字之内")
	private String name;

	private Integer parentId = 0;

	@NotNull(message = "展示顺序不能为空")
	private Integer seq;

	@Length(max = 150, message = "备注长度在150字以内")
	private String remark;


	// 以下字段不需要
	// level，因为这是需要我们计算出来的
	// operator, operate_time和operate_ip是与登录信息有关的，暂时不需要


}
