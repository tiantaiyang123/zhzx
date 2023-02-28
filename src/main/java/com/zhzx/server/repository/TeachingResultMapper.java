/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：教学成果表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zhzx.server.domain.TeachingResult;
import com.zhzx.server.repository.base.TeachingResultBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeachingResultMapper extends TeachingResultBaseMapper {
    IPage<TeachingResult> queryPage(IPage<TeachingResult> page,
                                             @Param(Constants.WRAPPER) QueryWrapper<TeachingResult> wrapper);
}
