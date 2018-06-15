package com.mmall.controller;

import com.google.common.collect.Maps;
import com.mmall.beans.PageQuery;
import com.mmall.common.JsonData;
import com.mmall.param.AclParam;
import com.mmall.service.SysAclService;
import com.mmall.service.SysRoleService;
import com.mmall.service.SysTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Alan Brown
 * @date 2018/5/27 21:35
 */
// 权限点管理
@Controller
@RequestMapping("/sys/acl")
@Slf4j
public class SysAclController {

	@Resource
	private SysAclService sysAclService;
	@Resource
	private SysTreeService sysTreeService;
	@Resource
	private SysRoleService sysRoleService;

	// 保存权限点
	@RequestMapping("/save.json")
	@ResponseBody
	public JsonData save(AclParam param){
		sysAclService.save(param);
		return JsonData.success();
	}

	// 更新权限点
	@RequestMapping("/update.json")
	@ResponseBody
	public JsonData update(AclParam param){
		sysAclService.update(param);
		return JsonData.success();
	}

	// 根据权限模块id获取该模块下的所有权限点
	@RequestMapping("/page.json")
	@ResponseBody
	public JsonData list(@RequestParam("aclModuleId") Integer aclModuleId, PageQuery page){
		return JsonData.success(sysAclService.getPageByAclModuleId(aclModuleId, page));
	}

	// 获取分配了该权限点的角色和用户
	@RequestMapping("/acls.json")
	@ResponseBody
	public JsonData acls(@RequestParam("aclId") int aclId) {
		Map<String, Object> map = Maps.newHashMap();
		map.put("roles", sysRoleService.getRoleListByUserId(aclId));
		map.put("users", sysTreeService.userAclTree(aclId));
		return JsonData.success(map);
	}

}
