/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：早读情况照片表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest.req;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import com.zhzx.server.enums.GradeEnum;
import com.zhzx.server.domain.MorningReadingImages;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "早读情况照片表参数", description = "")
public class MorningReadingImagesParam implements Serializable {
    /**
     * ID系统自动生成
     */
    @ApiModelProperty(value = "ID系统自动生成")
    private Long id;
    /**
     * ID系统自动生成 IN值List
     */
    @ApiModelProperty(value = "ID系统自动生成 IN值List")
    private List<Long> idList;
    /**
     * 早餐情况ID day_morning_reading.id
     */
    @ApiModelProperty(value = "早餐情况ID day_morning_reading.id")
    private Long morningReadingId;
    /**
     * 图片分类
     */
    @ApiModelProperty(value = "图片分类")
    private GradeEnum imageClassify;
    /**
     * 图片分类 IN值List
     */
    @ApiModelProperty(value = "图片分类 IN值List")
    private List<String> imageClassifyList;
    /**
     * 图片链接
     */
    @ApiModelProperty(value = "图片链接")
    private String url;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<MorningReadingImages> toQueryWrapper() {
        QueryWrapper<MorningReadingImages> wrapper = Wrappers.<MorningReadingImages>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getMorningReadingId() != null, "morning_reading_id", this.getMorningReadingId());
        wrapper.eq(this.getImageClassify() != null, "image_classify", this.getImageClassify());
        wrapper.in(this.getImageClassifyList() != null && this.getImageClassifyList().size() > 0, "image_classify", this.getImageClassifyList());
        if (this.getUrl() != null) {
            if (this.getUrl().startsWith("%") && this.getUrl().endsWith("%")) {
                wrapper.like("url", this.getUrl().substring(1, this.getUrl().length() - 1));
            } else if (this.getUrl().startsWith("%") && !this.getUrl().endsWith("%")) {
                wrapper.likeLeft("url", this.getUrl().substring(1));
            } else if (this.getUrl().endsWith("%")) {
                wrapper.likeRight("url", this.getUrl().substring(0, this.getUrl().length() - 1));
            } else {
                wrapper.eq("url", this.getUrl());
            }
        }
        return wrapper;
    }

}
