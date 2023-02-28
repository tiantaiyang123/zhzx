package com.zhzx.server.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zhzx.server.domain.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Data
public class RoutineInfoVo extends DutyBaseInfoVo {
    /**
     * 检查项目列表
     */
    @ApiModelProperty(value = "检查项目列表")
    @TableField(exist = false)
    private List<CheckItemVo> checkItemList;
}
