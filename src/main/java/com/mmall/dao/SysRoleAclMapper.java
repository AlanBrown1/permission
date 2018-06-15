package com.mmall.dao;

import com.mmall.model.SysRoleAcl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleAcl record);

    int insertSelective(SysRoleAcl record);

    SysRoleAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleAcl record);

    int updateByPrimaryKey(SysRoleAcl record);

    // 根据角色id列表获取该这些角色拥有的权限点id集合
    List<Integer> getAclIdListByRoleIdList(@Param("roleIdList")List<Integer> roleIdList);

    // 删除某个角色的全部权限
    void deleteByRoleId(@Param("roleId")Integer roleId);

    // 批量新增角色和权限点的关系
    void batchInsert(@Param("roleAclList")List<SysRoleAcl> roleAclList);

    // 获取具有某个权限点的角色
    List<Integer> getRoleIdListByAclId(@Param("aclId") int aclId);
}