package com.zhzx.server.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zhzx.server.domain.Student;
import com.zhzx.server.domain.StudentParent;
import com.zhzx.server.enums.YesNoEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class StudentInfoVo extends Student {
    @ApiModelProperty(value = "班级ID")
    @TableField(exist = false)
    private Long clazzId;

    @ApiModelProperty(value = "校区ID")
    @TableField(exist = false)
    private Long schoolyardId;

    @ApiModelProperty(value = "班级")
    @TableField(exist = false)
    private String clazzName;

    @ApiModelProperty(value = "宿舍")
    @TableField(exist = false)
    private String bedName;

    @ApiModelProperty(value = "是否值日班长")
    @TableField(exist = false)
    private YesNoEnum isStudentDuty;

    @ApiModelProperty(value = "用户Id")
    @TableField(exist = false)
    private Long userId;

    @ApiModelProperty(value = "宿舍")
    @TableField(exist = false)
    private List<StudentParent> studentParentList;
}
