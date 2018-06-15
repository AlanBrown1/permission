package com.mmall.beans;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

/**
 * @author Alan Brown
 * @date 2018/5/26 00:42
 */

// 分页
public class PageQuery {

	@Getter
	@Setter
	@Min(value = 1, message = "页码必须大于0")
	private int pageNo = 1;

	@Getter
	@Setter
	@Min(value = 1, message = "每页展示数量必须大于0")
	private int pageSize = 10;

	@Setter
	private int offset;

	// 拿到偏移量
	public int getOffset() {
		return (pageNo - 1) * pageSize;
	}

}
