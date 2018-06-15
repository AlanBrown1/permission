package com.mmall.dao;

import com.mmall.model.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    // 获取所有角色
    List<SysRole> getAllRoles();

    // 检查角色是否存在
    int countByName(@Param("name") String name, @Param("id")Integer id);

    // 获取分配给某个用户的角色
    List<SysRole> getByIdList(@Param("idList") List<Integer> roleIdList);
}