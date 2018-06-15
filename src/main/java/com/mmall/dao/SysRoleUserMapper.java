package com.mmall.dao;

import com.mmall.model.SysRoleUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleUser record);

    int insertSelective(SysRoleUser record);

    SysRoleUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleUser record);

    int updateByPrimaryKey(SysRoleUser record);

    // 根据用户id获取该用户拥有的角色id
    List<Integer> getRoleIdListByUserId(@Param("userId")Integer userId);

    // 根据角色id获取该角色的用户id集合
    List<Integer> getUserIdListByRoleId(@Param("roleId")int roleId);

    // 根据角色id删除角色用户关系
    void deleteByRoleId(@Param(("roleId")) int roleId);

    // 批量插入
    void batchInsert(@Param(("roleUserList"))List<SysRoleUser> roleUserList);

    // 获取角色列表获取用户列表
    List<Integer> getUserIdListByRoleIdList(@Param("roleIdList") List<Integer> roleIdList);
}