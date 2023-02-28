/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：偶发事件表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhzx.server.domain.Incident;
import com.zhzx.server.repository.base.IncidentBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface IncidentMapper extends IncidentBaseMapper {


    List<Incident> pageDetail(IPage page,
                              @Param("leader") String leaderName,
                              @Param("startTime") Date startTime,
                              @Param("endTime") Date endTime,
                              @Param("schoolyardId") Long schoolyardId);
}
