/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：晚自习行政值班年级图片表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.NightStudyDutyGradeImages;
import com.zhzx.server.rest.req.NightStudyDutyGradeImagesParam;

public interface NightStudyDutyGradeImagesService extends IService<NightStudyDutyGradeImages> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(NightStudyDutyGradeImages entity);


    Integer padUpdateImg(List<NightStudyDutyGradeImages> nightStudyDutyGradeImagesList);
}
