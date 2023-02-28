package com.zhzx.server.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zhzx.server.domain.Clazz;
import com.zhzx.server.domain.Grade;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GradeNightStudyInfoVo {
    /**
     * 年级ID
     */
    @ApiModelProperty(value = "年级ID")
    @TableField(exist = false)
    private Long gradeId;

    /**
     * 年级
     */
    @ApiModelProperty(value = "年级")
    @TableField(exist = false)
    private Grade grade;

    /**
     * 班级列表
     */
    @ApiModelProperty(value = "班级列表")
    @TableField(exist = false)
    private List<Clazz> clazzList;

    /**
     * 班级信息列表
     */
    @ApiModelProperty(value = "班级信息列表")
    @TableField(exist = false)
    private List<Map<String, Object>> clazzInfoList;
}