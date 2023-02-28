package com.zhzx.server.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zhzx.server.domain.Exam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExamVo extends Exam {

    /**
     * 用户信息
     */
    @ApiModelProperty(value = "年级名称")
    @TableField(exist = false)
    private String gradeName;
    /**
     * 权限列表
     */
    @ApiModelProperty(value = "是否分科")
    @TableField(exist = false)
    private Boolean hasNature;

}
