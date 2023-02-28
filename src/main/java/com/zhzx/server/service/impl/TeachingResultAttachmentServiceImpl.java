/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：教学成果附件表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.TeachingResultAttachment;
import com.zhzx.server.repository.TeachingResultAttachmentMapper;
import com.zhzx.server.repository.base.TeachingResultAttachmentBaseMapper;
import com.zhzx.server.service.TeachingResultAttachmentService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeachingResultAttachmentServiceImpl extends ServiceImpl<TeachingResultAttachmentMapper, TeachingResultAttachment> implements TeachingResultAttachmentService {

    @Override
    public int updateAllFieldsById(TeachingResultAttachment entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    @Override
    @Transactional
    public void updateBatch(Collection<TeachingResultAttachment> entity) throws Exception {
        this.getBaseMapper().updateBatch(entity);
    }

    @Override
    @SneakyThrows
    public void transfer() {
        List<TeachingResultAttachment> teachingResultAttachmentList = this.baseMapper.selectList(Wrappers.<TeachingResultAttachment>lambdaQuery()
                .like(TeachingResultAttachment::getUrl, "ids.njzhzx.net:8071"));
        if (CollectionUtils.isNotEmpty(teachingResultAttachmentList)) {
            teachingResultAttachmentList = teachingResultAttachmentList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(TeachingResultAttachment::getUrl))), ArrayList::new));
        }
        if (CollectionUtils.isNotEmpty(teachingResultAttachmentList)) {
            String folder = LocalDate.now(ZoneId.systemDefault()).toString().replaceAll("-", "");
            for (TeachingResultAttachment teachingResultAttachment : teachingResultAttachmentList) {
                int idx = teachingResultAttachment.getUrl().lastIndexOf("/");
                String name = teachingResultAttachment.getUrl().substring(idx + 1);
                String subUrl = teachingResultAttachment.getUrl().substring(0, idx).replace("/resource","");
                String pf = subUrl.substring(subUrl.lastIndexOf("/") + 1);
                String f = folder + "/" + pf + "/" + name;
                Path path = Paths.get("d:/file/upload/" + f);
                Files.createDirectories(path.getParent());
                try {
                    Files.copy(new URL(teachingResultAttachment.getUrl()).openStream(), path);
                } catch (FileNotFoundException fileNotFoundException) {
                    this.baseMapper.delete(Wrappers.<TeachingResultAttachment>lambdaQuery()
                            .eq(TeachingResultAttachment::getUrl, teachingResultAttachment.getUrl()));
                }
//                teachingResultAttachment.setUrl("http://218.94.40.182:18088/upload/" + f);
//                this.baseMapper.updateById(teachingResultAttachment);
            }
        }
    }

    /**
     * 批量插入
     *
     * @param entityList ignore
     * @param batchSize  ignore
     * @return ignore
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<TeachingResultAttachment> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(TeachingResultAttachmentBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


}
