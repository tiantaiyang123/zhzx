package com.zhzx.server.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zhzx.server.domain.BaseDomain;
import com.zhzx.server.domain.Schoolyard;
import com.zhzx.server.domain.Staff;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class DutyBaseInfoVo extends BaseDomain {
    /**
     * 领导ID sys_staff.id
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "领导ID sys_staff.id", required = true)
    private Long leaderId;
    /**
     * 领导ID sys_staff.id
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "领导ID sys_staff.id")
    private Staff leader;

    /**
     * 值班日期
     */
    @TableField(exist = false)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "值班日期", required = true)
    private java.util.Date dutyDate;

    // y
    private Schoolyard schoolyard;
}
