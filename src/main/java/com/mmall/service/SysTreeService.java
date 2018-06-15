package com.mmall.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.mmall.dao.SysAclMapper;
import com.mmall.dao.SysAclModuleMapper;
import com.mmall.dao.SysDeptMapper;
import com.mmall.dto.AclDto;
import com.mmall.dto.AclModuleLevelDto;
import com.mmall.dto.DeptLevelDto;
import com.mmall.model.SysAcl;
import com.mmall.model.SysAclModule;
import com.mmall.model.SysDept;
import com.mmall.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.acl.Acl;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Alan Brown
 * @date 2018/5/24 20:33
 */
// 用来创建树形结构
@Service
public class SysTreeService {

	@Resource
	private SysDeptMapper sysDeptMapper;
	@Resource
	private SysAclModuleMapper sysAclModuleMapper;
	@Resource
	private SysCoreService sysCoreService;
	@Resource
	private SysAclMapper sysAclMapper;

	// 一个比较器，把DeptLevelDto按seq从小到大排序
	private Comparator<DeptLevelDto> deptSeqComparator = new Comparator<DeptLevelDto>() {
		public int compare(DeptLevelDto o1, DeptLevelDto o2) {
			return o1.getSeq() - o2.getSeq();
		}
	};

	// 一个比较器，把AclModuleDto按seq从小到大排序
	private Comparator<AclModuleLevelDto> aclModuleSeqComparator = new Comparator<AclModuleLevelDto>() {
		public int compare(AclModuleLevelDto o1, AclModuleLevelDto o2) {
			return o1.getSeq() - o2.getSeq();
		}
	};


	// ********* 生成权部门树树 **********
	public List<DeptLevelDto> deptTree(){
		// 拿到所有部门
		List<SysDept> sysDeptList = sysDeptMapper.getAllDept();
		// 把每个部门都转换为对应的dto，并存入deptLevelDtoList
		List<DeptLevelDto> deptLevelDtoList = Lists.newArrayList();
		for(SysDept sysdept : sysDeptList){
			DeptLevelDto deptLevelDto = DeptLevelDto.adapt(sysdept);
			deptLevelDtoList.add(deptLevelDto);
		}
		return deptListToTree(deptLevelDtoList);
	}

	private List<DeptLevelDto> deptListToTree(List<DeptLevelDto> deptLevelDtoList) {
		// 如果当前没有任何部门，则返回一个空列表
		if (CollectionUtils.isEmpty(deptLevelDtoList)) {
			return Lists.newArrayList();
		}
		// level -> [dept1, dept2, ...] Map<String, List<Object>>
		// 在这里，key是部门的level，value是该部门对应的dto
		Multimap<String, DeptLevelDto> levelDeptMap = ArrayListMultimap.create();
		// 根部门
		List<DeptLevelDto> rootList = Lists.newArrayList();
		// 遍历每个部门，来填充levelDeptMap和rootList
		for (DeptLevelDto dto : deptLevelDtoList) {
			levelDeptMap.put(dto.getLevel(), dto);
			if (LevelUtil.ROOT.equals(dto.getLevel())) {
				rootList.add(dto);
			}
		}
		// 把rootList按照seq从小到大排序
		Collections.sort(rootList, deptSeqComparator);
		// 递归生成树
		transformDeptTree(rootList, LevelUtil.ROOT, levelDeptMap);
		return rootList;
	}

	// level:0, 0, all 0->0.1,0.2
	// level:0.1
	// level:0.2
	private void transformDeptTree(List<DeptLevelDto> deptLevelDtoList, String level, Multimap<String, DeptLevelDto> levelDeptMap) {
		// 对于每一个根部门
		for (DeptLevelDto deptLevelDto : deptLevelDtoList) {
			// 处理下一层级的level值
			String nextLevel = LevelUtil.calculateLevel(level, deptLevelDto.getId());
			// 处理下一层的所有部门
			List<DeptLevelDto> tempDeptList = (List<DeptLevelDto>) levelDeptMap.get(nextLevel);
			if (CollectionUtils.isNotEmpty(tempDeptList)) {
				// 排序
				Collections.sort(tempDeptList, deptSeqComparator);
				// 在这个部门的deptList中存放它管辖的所有下一级部门
				deptLevelDto.setDeptList(tempDeptList);
				// 进入到下一层处理
				transformDeptTree(tempDeptList, nextLevel, levelDeptMap);
			}
		}
	}

	// ********* 生成权限模块树 **********
	public List<AclModuleLevelDto> aclModuleTree(){
		List<SysAclModule> aclModuleList = sysAclModuleMapper.getAllAclModule(); // 拿到所有权限模块
		List<AclModuleLevelDto> dtoList = Lists.newArrayList();
		// 把所有权限模块都转换为dto并存放到dtoList
		for(SysAclModule aclModule : aclModuleList){
			dtoList.add(AclModuleLevelDto.adapt(aclModule));
		}
		return aclModuleListToTree(dtoList);
	}

	private List<AclModuleLevelDto> aclModuleListToTree(List<AclModuleLevelDto> dtoList){
		if(CollectionUtils.isEmpty(dtoList)){
			return Lists.newArrayList();
		}
		// level -> [aclModule1, aclModule2, ...] Map<String, List<Object>>
		// 在这里，key是模块的level，value是该模块对应的dto
		Multimap<String, AclModuleLevelDto> levelAclModuleMap = ArrayListMultimap.create();
		// 根模块
		List<AclModuleLevelDto> rootList = Lists.newArrayList();
		// 遍历每个模块，来填充levelDeptMap和rootList
		for (AclModuleLevelDto dto : dtoList) {
			levelAclModuleMap.put(dto.getLevel(), dto);
			if (LevelUtil.ROOT.equals(dto.getLevel())) {
				rootList.add(dto);
			}
		}
		// 把rootList按照seq从小到大排序
		Collections.sort(rootList, aclModuleSeqComparator);
		// 递归生成树
		transformAclModuleTree(rootList, LevelUtil.ROOT, levelAclModuleMap);
		return rootList;
	}

	// level:0, 0, all 0->0.1,0.2
	// level:0.1
	// level:0.2
	private void transformAclModuleTree(List<AclModuleLevelDto> aclModuleLevelDtoList, String level, Multimap<String, AclModuleLevelDto> levelAclModuleMap) {
		// 对于每一个根部门
		for (AclModuleLevelDto aclModuleLevelDto : aclModuleLevelDtoList) {
			// 处理下一层级的level值
			String nextLevel = LevelUtil.calculateLevel(level, aclModuleLevelDto.getId());
			// 处理下一层的所有部门
			List<AclModuleLevelDto> tempList = (List<AclModuleLevelDto>) levelAclModuleMap.get(nextLevel);
			if (CollectionUtils.isNotEmpty(tempList)) {
				// 排序
				tempList.sort(aclModuleSeqComparator);
				// 在这个模块的deptList中存放它管辖的所有下一级部门
				aclModuleLevelDto.setAclModuleList(tempList);
				// 进入到下一层处理
				transformAclModuleTree(tempList, nextLevel, levelAclModuleMap);
			}
		}
	}


	// ************* 对指定角色，生成该角色对应的权限树 ***************
	public List<AclModuleLevelDto> roleTree(int roleId){
		// 1，当前用户已经拥有的权限点
		List<SysAcl> userAclList = sysCoreService.getCurrentUserAclList();
		// 2，当前角色分配的权限点
		List<SysAcl> roleAclList = sysCoreService.getRoleAclList(roleId);
		// 3，当前系统中的所有权限点
		List<AclDto> aclDtoList = Lists.newArrayList();

		// 当前用户已经拥有的权限点的id集合，用Set去重。使用jdk8的流语法
		Set<Integer> userAclIdSet = userAclList.stream().map(SysAcl::getId).collect(Collectors.toSet());
		// 当前角色已经拥有的权限点的id集合，用Set去重。使用jdk8的流语法
		Set<Integer> roleAclIdSet = roleAclList.stream().map(SysAcl::getId).collect(Collectors.toSet());
		// 所有权限点
		List<SysAcl> allAclList = sysAclMapper.getAll();
		for(SysAcl acl : allAclList){
			AclDto dto = AclDto.adapt(acl);
			// 当前用户的权限点集合是否包含了这个权限点
			if(userAclIdSet.contains(acl.getId())){
				dto.setHasAcl(true);
			}
			if(roleAclIdSet.contains(acl.getId())){
				dto.setChecked(true);
			}
			aclDtoList.add(dto);
		}
		return aclListToTree(aclDtoList);
	}

	// 把权限点集合转为一棵树
	private List<AclModuleLevelDto> aclListToTree(List<AclDto> aclDtoList){
		// 当前权限点集合是否为空
		if(CollectionUtils.isEmpty(aclDtoList)){
			return Lists.newArrayList();
		}
		// 权限模块树
		List<AclModuleLevelDto> aclModuleLevelList = aclModuleTree();
		// 每个模块下面的权限点列表
		Multimap<Integer, AclDto> moduleAclMap = ArrayListMultimap.create();
		for(AclDto dto : aclDtoList){
			// 如果这个权限点没被冻结，是可用的话
			if(dto.getStatus() == 1){
				moduleAclMap.put(dto.getAclModuleId(), dto);
			}
		}
		bindAclWithOrder(aclModuleLevelList, moduleAclMap);
		return aclModuleLevelList;
	}

	// 把权限点绑定到权限树上（附带顺序）
	private void bindAclWithOrder(List<AclModuleLevelDto> aclModuleLevelList,
	                               Multimap<Integer, AclDto> moduleAclMap){
		if (CollectionUtils.isEmpty(aclModuleLevelList)) {
			return;
		}
		for (AclModuleLevelDto dto : aclModuleLevelList) {
			List<AclDto> aclDtoList = (List<AclDto>)moduleAclMap.get(dto.getId());
			if (CollectionUtils.isNotEmpty(aclDtoList)) {
				aclDtoList.sort(aclSeqComparator);
				dto.setAclList(aclDtoList);
			}
			bindAclWithOrder(dto.getAclModuleList(), moduleAclMap);
		}


	}

	private Comparator<AclDto> aclSeqComparator = new Comparator<AclDto>() {
		public int compare(AclDto o1, AclDto o2) {
			return o1.getSeq() - o2.getSeq();
		}
	};

	// 查询某个用户的权限点
	public List<AclModuleLevelDto> userAclTree(int userId) {
		List<SysAcl> userAclList = sysCoreService.getUserAclList(userId);
		List<AclDto> aclDtoList = Lists.newArrayList();
		for (SysAcl acl : userAclList) {
			AclDto dto = AclDto.adapt(acl);
			dto.setHasAcl(true);
			dto.setChecked(true);
			aclDtoList.add(dto);
		}
		return aclListToTree(aclDtoList);
	}




}
