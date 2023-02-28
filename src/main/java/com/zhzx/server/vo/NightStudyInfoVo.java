package com.zhzx.server.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class NightStudyInfoVo extends DutyBaseInfoVo {
    /**
     * 年级列表
     */
    @ApiModelProperty(value = "年级列表")
    @TableField(exist = false)
    private List<GradeNightStudyInfoVo> gradeList;
}