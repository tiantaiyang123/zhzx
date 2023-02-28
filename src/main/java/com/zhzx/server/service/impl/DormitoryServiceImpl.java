/**
* 项目：中华中学管理平台
* 模型分组：系统管理
* 模型名称：宿舍表
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
import com.zhzx.server.service.DormitoryService;
import com.zhzx.server.repository.DormitoryMapper;
import com.zhzx.server.domain.Dormitory;
import com.zhzx.server.rest.req.DormitoryParam;

@Service
public class DormitoryServiceImpl extends ServiceImpl<DormitoryMapper, Dormitory> implements DormitoryService {

    @Override
    public int updateAllFieldsById(Dormitory entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }



}
