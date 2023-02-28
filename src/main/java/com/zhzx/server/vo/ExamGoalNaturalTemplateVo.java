package com.zhzx.server.vo;

import com.zhzx.server.enums.ExamNaturalSettingEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExamGoalNaturalTemplateVo {

    @ApiModelProperty(value = "id逗号拼接")
    private String ids;

    @ApiModelProperty(value = "年级id", required = true)
    private Long gradeId;

    @ApiModelProperty(value = "模板名称", required = true)
    private String name;

    @ApiModelProperty(value = "赋分类型", required = true)
    private ExamNaturalSettingEnum settingType;

    @ApiModelProperty(value = "学科比例逗号拼接")
    private String subjectRatio;

}
