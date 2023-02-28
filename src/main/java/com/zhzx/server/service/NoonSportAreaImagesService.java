/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：午班运动区秩序照片表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.NoonSportAreaImages;
import com.zhzx.server.rest.req.NoonSportAreaImagesParam;

public interface NoonSportAreaImagesService extends IService<NoonSportAreaImages> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(NoonSportAreaImages entity);


}
