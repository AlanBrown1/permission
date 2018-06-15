package com.mmall.dao;

import com.mmall.model.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysDeptMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    SysDept selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);

    // 获取所有部门
    List<SysDept> getAllDept();

    // 获取指定部门的子部门列表
    List<SysDept> getChildDeptListByLevel(@Param("level") String level, @Param("id") Integer id);

    // 批量更新level
    void batchUpdateLevel(@Param("sysDeptList") List<SysDept> sysDeptList);

    // 根据指定部门id，部门名称和上级部门id获取与之同级的相同名称的部门的数量
    int countByNameAndParentId(@Param("parentId") Integer parentId,
                               @Param("name") String name,
                               @Param("id") Integer id);

    // 根据部门id获取该部门的子部门数量
    int countByParentId(@Param("deptId") Integer deptId);
}