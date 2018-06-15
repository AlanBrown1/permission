package com.mmall.dao;

import com.mmall.beans.PageQuery;
import com.mmall.dto.SearchLogDto;
import com.mmall.model.SysLog;
import com.mmall.model.SysLogWithBLOBs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysLogWithBLOBs record);

    int insertSelective(SysLogWithBLOBs record);

    SysLogWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysLogWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(SysLogWithBLOBs record);

    int updateByPrimaryKey(SysLog record);

    // 根据SearchLogDto进行查询，返回数量
    int countBySearchDto(@Param("dto")SearchLogDto dto);

    // 根据SearchLogDto查询满足条件的日志记录
    List<SysLogWithBLOBs> getPageListBySearchDto(@Param("dto") SearchLogDto dto,
                                        @Param("page") PageQuery page);
}