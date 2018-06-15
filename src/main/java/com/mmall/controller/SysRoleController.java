package com.mmall.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mmall.common.JsonData;
import com.mmall.model.SysUser;
import com.mmall.param.RoleParam;
import com.mmall.service.*;
import com.mmall.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alan Brown
 * @date 2018/5/28 19:41
 */
@Controller
@RequestMapping("/sys/role")
public class SysRoleController {

	@Resource
	private SysRoleService sysRoleService;
	@Resource
	private SysTreeService sysTreeService;
	@Resource
	private SysRoleAclService sysRoleAclService;
	@Resource
	private SysRoleUserService sysRoleUserService;
	@Resource
	private SysUserService sysUserService;

	// 页面
	@RequestMapping("/role.page")
	public ModelAndView page(){
		return new ModelAndView("role");
	}

	// 保存角色
	@RequestMapping("/save.json")
	@ResponseBody
	public JsonData save(RoleParam param){
		sysRoleService.save(param);
		return JsonData.success();
	}

	// 更新角色
	@RequestMapping("/update.json")
	@ResponseBody
	public JsonData update(RoleParam param){
		sysRoleService.update(param);
		return JsonData.success();
	}

	// 获取全部角色
	@RequestMapping("/list.json")
	@ResponseBody
	public JsonData getAllRoles(){
		return JsonData.success(sysRoleService.getAllRoles());
	}

	// 获取指定角色所拥有的权限
	@RequestMapping("/roleTree.json")
	@ResponseBody
	public JsonData roleTree(@RequestParam("roleId") Integer roleId){
		return JsonData.success(sysTreeService.roleTree(roleId));
	}

	// 更新角色的权限
	@RequestMapping("/changeAcls.json")
	@ResponseBody
	public JsonData changeAcls(@RequestParam("roleId")Integer roleId,
	                           @RequestParam(value = "aclIds", required = false, defaultValue = "")String aclIds){
		List<Integer> aclIdList = StringUtil.splitToListInt(aclIds);
		sysRoleAclService.changeRoleAcls(roleId, aclIdList);
		return JsonData.success();
	}


	// 获取具有某个角色的全部用户
	@RequestMapping("/users.json")
	@ResponseBody
	public JsonData users(@RequestParam("roleId")int roleId){
		// 拥有该角色的用户
		List<SysUser> selectedUserList = sysRoleUserService.getListByRoleId(roleId);
		// 所有用户
		List<SysUser> allUserList = sysUserService.getAll();
		// 不拥有该角色的用户
		List<SysUser> unselectedUserList = Lists.newArrayList();
		Set<Integer> selectedUserIdSet = selectedUserList.stream().map(SysUser::getId).collect(Collectors.toSet());
		for(SysUser user : allUserList){
			if(user.getStatus() == 1 && !selectedUserIdSet.contains(user.getId())){
				unselectedUserList.add(user);
			}
		}
		Map<String, List<SysUser>> map = Maps.newHashMap();
		map.put("selected", selectedUserList);
		map.put("unselected", unselectedUserList);
		return JsonData.success(map);
	}

	// 更新角色用户关系
	@RequestMapping("/changeUsers.json")
	@ResponseBody
	public JsonData changeUsers(@RequestParam("roleId") int roleId, @RequestParam(value = "userIds", required = false, defaultValue = "") String userIds) {
		List<Integer> userIdList = StringUtil.splitToListInt(userIds);
		sysRoleUserService.changeRoleUsers(roleId, userIdList);
		return JsonData.success();
	}

}
