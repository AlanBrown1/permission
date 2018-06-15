package com.mmall.dao;

import com.mmall.beans.PageQuery;
import com.mmall.model.SysAcl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAcl record);

    int insertSelective(SysAcl record);

    SysAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAcl record);

    int updateByPrimaryKey(SysAcl record);

    // 根据权限模块id获取该模块下的权限点数量
    int countByAclModuleId(@Param("aclModuleId")Integer aclModuleId);

    // 根据权限模块id获取该模块下的所有权限点
    List<SysAcl> getPageByAclModuleId(@Param("aclModuleId")Integer aclModuleId,
                                      @Param("page")PageQuery page);

    // 根据权限模块id和权限点名称查找当前模块下相同名称的权限点数量
    int countByNameAndAclModuleId(@Param("aclModuleId")Integer aclModuleId,
                                  @Param("name")String name,
                                  @Param("id")Integer id);

    // 获取全部权限点
    List<SysAcl> getAll();

    // 根据权限点id集合获取对应的权限点集合
    List<SysAcl> getByIdList(@Param("idList")List<Integer> idList);

    // 根据url获取对应的acl
    List<SysAcl> getByUrl(@Param("url")String url);
}