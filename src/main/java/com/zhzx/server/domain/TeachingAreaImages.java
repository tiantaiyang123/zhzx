/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：教学区秩序照片表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.domain;

import java.io.Serializable;

import com.zhzx.server.rest.res.ApiCode;
import lombok.*;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zhzx.server.enums.GradeEnum;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`day_teaching_area_images`")
@ApiModel(value = "TeachingAreaImages", description = "教学区秩序照片表")
public class TeachingAreaImages extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 教学区秩序ID day_teaching_area.id
     */
    @TableField(value = "teaching_area_id")
    @ApiModelProperty(value = "教学区秩序ID day_teaching_area.id", required = true)
    private Long teachingAreaId;
    /**
     * 图片分类
     */
    @TableField(value = "image_classify")
    @ApiModelProperty(value = "图片分类", required = true)
    private GradeEnum imageClassify;
    /**
     * 图片链接
     */
    @TableField(value = "url")
    @ApiModelProperty(value = "图片链接", required = true)
    private String url;

    /**
     * 设置默认值
     */
    public TeachingAreaImages setDefault() {
        if (this.getImageClassify() == null) {
            this.setImageClassify(GradeEnum.ONE);
        }
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getTeachingAreaId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "教学区秩序ID day_teaching_area.id不能为空！");
            return false;
        }
        if (this.getImageClassify() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "图片分类不能为空！");
            return false;
        }
        if (this.getUrl() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "图片链接不能为空！");
            return false;
        }
        return true;
    }

    /**
     * 数据验证
     */
    public Boolean validate() {
        return this.validate(false);
    }

}
