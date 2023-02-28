/**
* 项目：中华中学管理平台
* 模型分组：成绩管理
* 模型名称：班级练习表
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
import com.zhzx.server.service.PracticeService;
import com.zhzx.server.repository.PracticeMapper;
import com.zhzx.server.domain.Practice;
import com.zhzx.server.rest.req.PracticeParam;

@Service
public class PracticeServiceImpl extends ServiceImpl<PracticeMapper, Practice> implements PracticeService {

    @Override
    public int updateAllFieldsById(Practice entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }



}
