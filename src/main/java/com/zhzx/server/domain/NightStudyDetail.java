/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：晚自习明细表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.domain;

import java.io.Serializable;

import com.zhzx.server.rest.res.ApiCode;
import lombok.*;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zhzx.server.enums.TeacherDutyInfoDetailClassifyEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.domain.Student;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`day_night_study_detail`")
@ApiModel(value = "NightStudyDetail", description = "晚自习明细表")
public class NightStudyDetail extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * ID系统自动生成
     */
    @ApiModelProperty(value = "ID系统自动生成")
    @TableField(exist = false)
    private List<NightStudyDetailImage> nightStudyDetailImageList;
    /**
     * 晚自习ID day_night_study.id
     */
    @TableField(value = "night_study_id")
    @ApiModelProperty(value = "晚自习ID day_night_study.id", required = true)
    private Long nightStudyId;
    /**
     * 分类
     */
    @TableField(value = "classify")
    @ApiModelProperty(value = "分类", required = true)
    private TeacherDutyInfoDetailClassifyEnum classify;
    /**
     * 学生ID sys_student.id
     */
    @TableField(value = "student_id")
    @ApiModelProperty(value = "学生ID sys_student.id", required = true)
    private Long studentId;
    /**
     * 学生ID sys_student.id
     */
    @ApiModelProperty(value = "学生ID sys_student.id")
    @TableField(exist = false)
    private Student student;
    /**
     * 原因
     */
    @TableField(value = "reason")
    @ApiModelProperty(value = "原因", required = true)
    private String reason;
    /**
     * 是否通知班主任
     */
    @TableField(value = "is_notice")
    @ApiModelProperty(value = "是否通知班主任", required = true)
    private YesNoEnum isNotice;

    /**
     * 设置默认值
     */
    public NightStudyDetail setDefault() {
        if (this.getClassify() == null) {
            this.setClassify(TeacherDutyInfoDetailClassifyEnum.OTHER);
        }
        if (this.getIsNotice() == null) {
            this.setIsNotice(YesNoEnum.YES);
        }
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getNightStudyId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "晚自习ID day_night_study.id不能为空！");
            return false;
        }
        if (this.getClassify() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "分类不能为空！");
            return false;
        }
        if (this.getStudentId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "学生ID sys_student.id不能为空！");
            return false;
        }
        if (this.getReason() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "原因不能为空！");
            return false;
        }
        if (this.getIsNotice() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "是否通知班主任不能为空！");
            return false;
        }
        return true;
    }

    /**
     * 数据验证
     */
    public Boolean validate() {
        return this.validate(false);
    }

}
