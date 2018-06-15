package com.mmall.service;

import com.google.common.collect.Lists;
import com.mmall.beans.CacheKeyConstants;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysAclMapper;
import com.mmall.dao.SysRoleAclMapper;
import com.mmall.dao.SysRoleMapper;
import com.mmall.dao.SysRoleUserMapper;
import com.mmall.model.SysAcl;
import com.mmall.model.SysRole;
import com.mmall.model.SysUser;
import com.mmall.util.JsonMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alan Brown
 * @date 2018/5/29 16:11
 */

@Service
public class SysCoreService {

	@Resource
	private SysAclMapper sysAclMapper;
	@Resource
	private SysRoleMapper sysRoleMapper;
	@Resource
	private SysRoleUserMapper sysRoleUserMapper;
	@Resource
	private SysRoleAclMapper sysRoleAclMapper;
	@Resource
	private SysCacheService sysCacheService;


	// 获取当前用户已经拥有的权限点
	public List<SysAcl> getCurrentUserAclList(){
		// 当前用户id
		int userId = RequestHolder.getCurrentUser().getId();
		// 根据用户id拿到该用户拥有的权限点
		return getUserAclList(userId);
	}

	// 根据角色id获取该角色拥有的权限点
	public List<SysAcl> getRoleAclList(int roleId){
		// 拿到该角色拥有的权限点id集合
		List<Integer> roleAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Collections.singletonList(roleId));
		// 如果权限点集合为空
		if(CollectionUtils.isEmpty(roleAclIdList)){
			return Lists.newArrayList();
		}
		// 根据权限点id集合获取权限点集合
		return sysAclMapper.getByIdList(roleAclIdList);
	}

	// 根据用户id获取该用户拥有的权限点集合
	public List<SysAcl> getUserAclList(int userId){
		// 如果是超级管理员，就取出所有权限点
		if(isSuperAdmin()){
			return sysAclMapper.getAll();
		}
		// 如果不是超级管理员，就取出该用户拥有的全部角色的角色id
		List<Integer> userRoleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
		// 如果该用户没有被分配任何角色
		if(CollectionUtils.isEmpty(userRoleIdList)){
			return Lists.newArrayList();
		}
		// 拿到这些角色所拥有的权限点并集
		List<Integer> userAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(userRoleIdList);
		// 如果并集为空
		if(CollectionUtils.isEmpty(userAclIdList)){
			return Lists.newArrayList();
		}
		// 根据权限点id获取权限点
		return sysAclMapper.getByIdList(userAclIdList);
	}

	// 判断是否为超级管理员
	private boolean isSuperAdmin(){
		// 拿到当前登录用户
		SysUser sysUser = RequestHolder.getCurrentUser();
		// 判断该用户是否是超级管理员
		// 拿到该用户所拥有的角色id列表（因为可能拥有多个角色）
		List<Integer> roleIds = sysRoleUserMapper.getRoleIdListByUserId(sysUser.getId());
		// 通过角色id列表拿到角色列表
		List<SysRole> sysRoles = sysRoleMapper.getByIdList(roleIds);
		// 判断这些角色中是否有超级管理员角色
		boolean isSuper = false;
		for(SysRole sysRole : sysRoles){
			if(sysRole.getName().equals("超级管理员")) {
				isSuper = true;
				break;
			}
		}
		return isSuper;
	}

	// 判断某个用户是否具有某个权限
	public boolean hasUrlAcl(String url){
		// 如果是超级管理员，则具有权限
		if(isSuperAdmin()) return true;

		// 取出该url对应的acl列表
		List<SysAcl> aclList = sysAclMapper.getByUrl(url);
		if(CollectionUtils.isEmpty(aclList)){
			return true;
		}
		// 用户已经拥有的权限点列表
		List<SysAcl> userAclList = getCurrentUserAclListFromCache();
		Set<Integer> userAclIdSet = userAclList.stream().map(SysAcl::getId).collect(Collectors.toSet());

		// 规则，如果选出多个，只要其中有一个有权限，则认为有权限
		// /sys/user/action.json对应多个acl
		boolean hasValidAcl = false;
		for(SysAcl acl : aclList){
			// 判断一个用户是否具有某个权限点
			if(acl == null || acl.getStatus() != 1){
				// 如果权限点无效
				continue;
			}
			hasValidAcl = true;
			if(userAclIdSet.contains(acl.getId())){
				return true;
			}
		}
		return !hasValidAcl;
	}

	// 从缓存中读取该用户已经拥有的权限集合
	private List<SysAcl> getCurrentUserAclListFromCache(){
		// 当前用户id
		int userId = RequestHolder.getCurrentUser().getId();
		// 拿到缓存中的值
		String cacheValue = sysCacheService.getFromCache(CacheKeyConstants.USER_ACLS, String.valueOf(userId));
		// 如果cacheValue是空的，那么就用原来的方法去数据库里面取
		if(StringUtils.isBlank(cacheValue)){
			List<SysAcl> aclList = getCurrentUserAclList();
			// 如果从数据库里面拿出来的权限集合不为空，那么写到缓存里面
			if(CollectionUtils.isNotEmpty(aclList)){
				// 缓存到redis，超时时间半小时（1800秒）
				sysCacheService.saveCache(JsonMapper.obj2String(aclList), 1800, CacheKeyConstants.USER_ACLS, String.valueOf(userId));
			}
			return aclList;
		}
		// 如果cacheValue不是空的，那么
		return JsonMapper.string2Obj(cacheValue, new TypeReference<List<SysAcl>>(){});
	}



}
