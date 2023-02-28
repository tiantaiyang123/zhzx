/**
* 项目：中华中学管理平台
* 模型分组：成绩管理
* 模型名称：考试赋分表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.zhzx.server.service.ExamGoalNaturalService;
import com.zhzx.server.repository.ExamGoalNaturalMapper;
import com.zhzx.server.domain.ExamGoalNatural;
import com.zhzx.server.rest.req.ExamGoalNaturalParam;

@Service
public class ExamGoalNaturalServiceImpl extends ServiceImpl<ExamGoalNaturalMapper, ExamGoalNatural> implements ExamGoalNaturalService {

    @Override
    public int updateAllFieldsById(ExamGoalNatural entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }



}
