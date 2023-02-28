package com.zhzx.server.vo;

import com.zhzx.server.enums.StudentTypeEnum;
import com.zhzx.server.rest.req.StudentParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "学生表查询参数", description = "")
public class StudentParamVo implements Serializable {
    /**
     * ID系统自动生成
     */
    @ApiModelProperty(value = "ID系统自动生成")
    private Long id;

    @ApiModelProperty(value = "学号")
    private String studentNumber;
    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号")
    private String idNumber;
    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    private String orderNumber;
    /**
     * 一卡通号
     */
    @ApiModelProperty(value = "一卡通号")
    private String cardNumber;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String name;
    /**
     * 学生类型
     */
    @ApiModelProperty(value = "学生类型")
    private StudentTypeEnum studentType;
    /**
     * 班级ID
     */
    @ApiModelProperty(value = "班级ID")
    private Long clazzId;
    /**
     * 班级
     */
    @ApiModelProperty(value = "班级")
    private String clazzName;
    /**
     * 宿舍
     */
    @ApiModelProperty(value = "宿舍")
    private String bedName;
}
