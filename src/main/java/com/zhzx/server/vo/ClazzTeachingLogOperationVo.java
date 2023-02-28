package com.zhzx.server.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zhzx.server.domain.Clazz;
import com.zhzx.server.domain.ClazzTeachingLogOperation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ClazzTeachingLogOperationVo extends ClazzTeachingLogOperation {
    /**
     * 班级ID sys_clazz.id
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "班级ID sys_clazz.id", required = true)
    private Long clazzId;
    /**
     * 班级ID sys_clazz.id
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "班级ID sys_clazz.id")
    private Clazz clazz;
}
