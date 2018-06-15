package com.mmall.controller;

import com.mmall.common.JsonData;
import com.mmall.dto.DeptLevelDto;
import com.mmall.param.DeptParam;
import com.mmall.service.SysDeptService;
import com.mmall.service.SysTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Alan Brown
 * @date 2018/5/24 19:44
 */
@Controller
@RequestMapping("/sys/dept")
@Slf4j
public class SysDeptController {

	@Resource
	private SysDeptService sysDeptService;
	@Resource
	private SysTreeService sysTreeService;

	// 新增部门
	@RequestMapping("/save.json")
	@ResponseBody
	public JsonData saveDept(DeptParam param){
		sysDeptService.save(param);
		return JsonData.success();
	}

	// 获取部门树
	@RequestMapping("/tree.json")
	@ResponseBody
	public JsonData deptTree() {
		List<DeptLevelDto> dtoList = sysTreeService.deptTree();
		return JsonData.success(dtoList);
	}

	// 更新部门
	@RequestMapping("/update.json")
	@ResponseBody
	public JsonData updateDept(DeptParam param){
		sysDeptService.update(param);
		return JsonData.success();
	}

	// 删除部门
	@RequestMapping("/delete.json")
	@ResponseBody
	public JsonData delete(@RequestParam("id") int id) {
		sysDeptService.delete(id);
		return JsonData.success();
	}

	// 进入页面
	@RequestMapping("/dept.page")
	public ModelAndView page(){
		return new ModelAndView("dept");
	}




}
