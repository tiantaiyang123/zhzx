/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：访问日志表表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import com.zhzx.server.domain.VisitLog;
import com.zhzx.server.repository.base.VisitLogBaseMapper;

@Repository
public interface VisitLogMapper extends VisitLogBaseMapper {


    List<Map<String,Object>> statistics(@Param("time") Date time,
                                        @Param("editorName")String editorName,
                                        @Param("page")IPage page,
                                        @Param("orderByClause")String orderByClause);
}
