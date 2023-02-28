/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：走读生提前出门照片表
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
@TableName("`day_go_out_images`")
@ApiModel(value = "GoOutImages", description = "走读生提前出门照片表")
public class GoOutImages extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 走读生提前出门ID day_go_out.id
     */
    @TableField(value = "go_out_id")
    @ApiModelProperty(value = "走读生提前出门ID day_go_out.id", required = true)
    private Long goOutId;
    /**
     * 图片链接
     */
    @TableField(value = "url")
    @ApiModelProperty(value = "图片链接", required = true)
    private String url;

    /**
     * 设置默认值
     */
    public GoOutImages setDefault() {
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getGoOutId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "走读生提前出门ID day_go_out.id不能为空！");
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
