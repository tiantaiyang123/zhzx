package com.zhzx.server.vo;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.zhzx.server.domain.ExamGoal;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExamGoalVo extends ExamGoal {

    @ApiModelProperty(value = "各班目标")
    @TableField(exist = false)
    private JSONObject jo;
}
