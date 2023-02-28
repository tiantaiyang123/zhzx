/**
* 项目：中华中学管理平台
* 模型分组：系统管理
* 模型名称：老师微信发送人关联表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.StaffMessageReceiverRelation;
import com.zhzx.server.dto.StaffCategoryDto;
import org.apache.ibatis.annotations.Param;

public interface StaffMessageReceiverRelationService extends IService<StaffMessageReceiverRelation> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(StaffMessageReceiverRelation entity);


    List<StaffCategoryDto> getReceiverTree(Long staffId);

    int batchInsertOrUpdate(@Param("records") List<StaffMessageReceiverRelation> records);
}
