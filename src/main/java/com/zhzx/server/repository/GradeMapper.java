/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：年级表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import com.zhzx.server.domain.Grade;
import com.zhzx.server.repository.base.GradeBaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeMapper extends GradeBaseMapper {

    List<Grade> getGradeList();
}
