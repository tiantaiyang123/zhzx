/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：晚自习明细照片表
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

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`day_night_study_detail_image`")
@ApiModel(value = "NightStudyDetailImage", description = "晚自习明细照片表")
public class NightStudyDetailImage extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 晚自习明细ID day_night_study_detail.id
     */
    @TableField(value = "night_study_detail_id")
    @ApiModelProperty(value = "晚自习明细ID day_night_study_detail.id", required = true)
    private Long nightStudyDetailId;
    /**
     * 图片链接
     */
    @TableField(value = "url")
    @ApiModelProperty(value = "图片链接", required = true)
    private String url;

    /**
     * 设置默认值
     */
    public NightStudyDetailImage setDefault() {
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getNightStudyDetailId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "晚自习明细ID day_night_study_detail.id不能为空！");
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
