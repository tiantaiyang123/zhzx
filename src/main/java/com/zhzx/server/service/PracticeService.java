/**
* 项目：中华中学管理平台
* 模型分组：成绩管理
* 模型名称：班级练习表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.Practice;
import com.zhzx.server.rest.req.PracticeParam;

public interface PracticeService extends IService<Practice> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(Practice entity);


}
