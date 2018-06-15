package com.mmall.controller;

import com.mmall.common.JsonData;
import com.mmall.dto.AclModuleLevelDto;
import com.mmall.model.SysAclModule;
import com.mmall.param.AclModuleParam;
import com.mmall.service.SysAclModuleService;
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
 * @date 2018/5/27 21:34
 */
// 权限模块管理
@Controller
@RequestMapping("/sys/aclModule")
@Slf4j
public class SysAclModuleController {

	@Resource
	private SysAclModuleService sysAclModuleService;
	@Resource
	private SysTreeService sysTreeService;

	// acl页面
	@RequestMapping("/acl.page")
	public ModelAndView page(){
		return new ModelAndView("acl");
	}

	@RequestMapping("/save.json")
	@ResponseBody
	public JsonData saveAclModule(AclModuleParam param){
		sysAclModuleService.save(param);
		return JsonData.success();
	}

	@RequestMapping("/update.json")
	@ResponseBody
	public JsonData updateAclModule(AclModuleParam param){
		sysAclModuleService.update(param);
		return JsonData.success();
	}

	@RequestMapping("/tree.json")
	@ResponseBody
	public JsonData tree(){
		return JsonData.success(sysTreeService.aclModuleTree());
	}


	// 删除权限模块
	@RequestMapping("/delete.json")
	@ResponseBody
	public JsonData delete(@RequestParam("id") int id) {
		sysAclModuleService.delete(id);
		return JsonData.success();
	}




}
