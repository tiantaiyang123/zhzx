/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：教学成果附件表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import com.zhzx.server.domain.TeachingResultAttachment;
import com.zhzx.server.repository.base.TeachingResultAttachmentBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface TeachingResultAttachmentMapper extends TeachingResultAttachmentBaseMapper {

    void updateBatch(@Param("entity") Collection<TeachingResultAttachment> entity);
}
