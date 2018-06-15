package com.mmall.controller;

import com.google.common.collect.Maps;
import com.mmall.beans.PageQuery;
import com.mmall.beans.PageResult;
import com.mmall.common.JsonData;
import com.mmall.model.SysUser;
import com.mmall.param.UserParam;
import com.mmall.service.SysRoleService;
import com.mmall.service.SysTreeService;
import com.mmall.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Alan Brown
 * @date 2018/5/25 17:35
 */
// UserController和SysUserController的区别是
// 前者是用户自己进行相关操作的方法
// 后者是非用户自己进行的相关操作的方法
@Controller
@RequestMapping("/sys/user")
@Slf4j
public class SysUserController {

	@Resource
	private SysUserService sysUserService;
	@Resource
	private SysTreeService sysTreeService;
	@Resource
	private SysRoleService sysRoleService;

	// 添加用户
	@RequestMapping("/save.json")
	@ResponseBody
	public JsonData saveUser(UserParam param){
		sysUserService.save(param);
		return JsonData.success();
	}

	// 更新用户
	@RequestMapping("/update.json")
	@ResponseBody
	public JsonData updateUser(UserParam param){
		sysUserService.update(param);
		return JsonData.success();
	}

	// 查询某个部门及其子部门的所有用户
	@RequestMapping("/page.json")
	@ResponseBody
	public JsonData page(@RequestParam("deptId")int deptId,
	                     PageQuery pageQuery){
		PageResult<SysUser> result = sysUserService.getPageByDeptId(deptId, pageQuery);
		return JsonData.success(result);
	}

	// 获取分配给该用户的角色和权限点
	@RequestMapping("/acls.json")
	@ResponseBody
	public JsonData acls(@RequestParam("userId") int userId) {
		Map<String, Object> map = Maps.newHashMap();
		map.put("acls", sysTreeService.userAclTree(userId));
		map.put("roles", sysRoleService.getRoleListByUserId(userId));
		return JsonData.success(map);
	}

	// 如果用户对某个url无权限访问，则返回noAuth页面
	@RequestMapping("/noAuth.page")
	public ModelAndView noAuth(){
		return new ModelAndView("noAuth");
	}

}
