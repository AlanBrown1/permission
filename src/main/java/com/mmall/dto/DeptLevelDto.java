package com.mmall.dto;

import com.google.common.collect.Lists;
import com.mmall.model.SysDept;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author Alan Brown
 * @date 2018/5/24 20:27
 */
@Getter
@Setter
@ToString
public class DeptLevelDto extends SysDept {

	// 创建一个List，内部元素是DeptLevelDto，即这个部门管辖的下一级部门，这样就可以形成一棵树状结构
	private List<DeptLevelDto> deptList = Lists.newArrayList();

	public static DeptLevelDto adapt(SysDept dept){
		DeptLevelDto dto = new DeptLevelDto();
		// 这是springframework中的一个工具类
		// 作用是将前者中的属性值拷贝一份到后者相同的属性中
		BeanUtils.copyProperties(dept, dto);
		return dto;
	}


}
