/**
* 项目：中华中学管理平台
* 模型分组：成绩管理
* 模型名称：考试分类表
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
import com.zhzx.server.service.ExamTypeService;
import com.zhzx.server.repository.ExamTypeMapper;
import com.zhzx.server.domain.ExamType;
import com.zhzx.server.rest.req.ExamTypeParam;

@Service
public class ExamTypeServiceImpl extends ServiceImpl<ExamTypeMapper, ExamType> implements ExamTypeService {

    @Override
    public int updateAllFieldsById(ExamType entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }



}
