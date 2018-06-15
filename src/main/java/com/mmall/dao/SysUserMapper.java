package com.mmall.dao;

import com.mmall.beans.PageQuery;
import com.mmall.model.SysUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    // 根据用户名查找用户
    SysUser findByKeyword(@Param("keyword")String keyword);

    // 根据邮箱查看相同邮箱的用户数量（注册时没有id，更新时有id）
    int countByMail(@Param("mail") String mail, @Param("id") Integer id);

    // 根据邮箱查看相同电话的用户数量（注册时没有id，更新时有id）
    int countByTelephone(@Param("telephone") String telephone, @Param("id") Integer id);

    // 获取某个部门内的总员工数
    int countByDeptId(@Param("deptId")int deptId);

    // 获取某个部门的所有员工
    List<SysUser> getPageByDeptId(@Param("deptId")int deptId,
                                  @Param("page")PageQuery page);


    // 根据用户id列表获取对应的用户集合
    List<SysUser> getByIdList(@Param("idList")List<Integer> idList);

    // 获取全部用户
    List<SysUser> getAll();

}