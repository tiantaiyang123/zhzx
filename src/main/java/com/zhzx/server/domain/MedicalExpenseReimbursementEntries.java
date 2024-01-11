/**
 * 项目：中华中学管理平台
 * 模型分组：医疗管理
 * 模型名称：医药费报销条目表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhzx.server.enums.MedicalEntryEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.rest.res.ApiCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`mal_medical_expense_reimbursement_entries`")
@ApiModel(value = "MedicalExpenseReimbursementEntries", description = "医药费报销条目表")
public class MedicalExpenseReimbursementEntries extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 职工名称
     */
    @TableField(value = "staff_name")
    @ApiModelProperty(value = "职工名称", required = true)
    private String staffName;
    /**
     * 是否在职
     */
    @TableField(value = "is_delete")
    @ApiModelProperty(value = "是否在职", required = true)
    private YesNoEnum isDelete;
    /**
     * 身份证号
     */
    @TableField(value = "id_number")
    @ApiModelProperty(value = "身份证号", required = true)
    private String idNumber;
    /**
     * 就诊结束日期
     */
    @TableField(value = "visit_end_date")
    @ApiModelProperty(value = "就诊结束日期")
    private String visitEndDate;
    /**
     * 医疗类别
     */
    @TableField(value = "type")
    @ApiModelProperty(value = "医疗类别", required = true)
    private String type;
    /**
     * 医疗费合计支出
     */
    @TableField(value = "cost_total")
    @ApiModelProperty(value = "医疗费合计支出", required = true)
    private java.math.BigDecimal costTotal;
    /**
     * 医保支出
     */
    @TableField(value = "cost_insurance")
    @ApiModelProperty(value = "医保支出", required = true)
    private java.math.BigDecimal costInsurance;
    /**
     * 统筹基金支出
     */
    @TableField(value = "cost_polling_fund")
    @ApiModelProperty(value = "统筹基金支出", required = true)
    private java.math.BigDecimal costPollingFund;
    /**
     * 大病救助支出
     */
    @TableField(value = "cost_serious_illness_supply")
    @ApiModelProperty(value = "大病救助支出", required = true)
    private java.math.BigDecimal costSeriousIllnessSupply;
    /**
     * 大病支出
     */
    @TableField(value = "cost_serious_illness")
    @ApiModelProperty(value = "大病支出", required = true)
    private java.math.BigDecimal costSeriousIllness;
    /**
     * 其他社会保险支付
     */
    @TableField(value = "cost_other_social_insurance")
    @ApiModelProperty(value = "其他社会保险支付", required = true)
    private java.math.BigDecimal costOtherSocialInsurance;
    /**
     * 非社会保险支付
     */
    @TableField(value = "cost_non_social_insurance")
    @ApiModelProperty(value = "非社会保险支付", required = true)
    private java.math.BigDecimal costNonSocialInsurance;
    /**
     * 所属医疗机构名称
     */
    @TableField(value = "affiliated_medical_institution")
    @ApiModelProperty(value = "所属医疗机构名称", required = true)
    private String affiliatedMedicalInstitution;
    /**
     * 条目状态
     */
    @TableField(value = "entry_status")
    @ApiModelProperty(value = "条目状态", required = true)
    private MedicalEntryEnum entryStatus;
    /**
     * 操作人ID sys_user.id
     */
    @TableField(value = "editor_id")
    @ApiModelProperty(value = "操作人ID sys_user.id", required = true)
    private Long editorId;
    /**
     * 操作人 sys_user.real_name
     */
    @TableField(value = "editor_name")
    @ApiModelProperty(value = "操作人 sys_user.real_name", required = true)
    private String editorName;
    /**
     * 
     */
    @TableField(value = "created_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "", required = true)
    private java.util.Date createdTime;
    /**
     * 
     */
    @TableField(value = "updated_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "", required = true)
    private java.util.Date updatedTime;

    /**
     * 设置默认值
     */
    public MedicalExpenseReimbursementEntries setDefault() {
        if (this.getCostInsurance() == null) {
            this.setCostInsurance(java.math.BigDecimal.valueOf(0));
        }
        if (this.getCostPollingFund() == null) {
            this.setCostPollingFund(java.math.BigDecimal.valueOf(0));
        }
        if (this.getCostSeriousIllnessSupply() == null) {
            this.setCostSeriousIllnessSupply(java.math.BigDecimal.valueOf(0));
        }
        if (this.getCostSeriousIllness() == null) {
            this.setCostSeriousIllness(java.math.BigDecimal.valueOf(0));
        }
        if (this.getCostOtherSocialInsurance() == null) {
            this.setCostOtherSocialInsurance(java.math.BigDecimal.valueOf(0));
        }
        if (this.getCostNonSocialInsurance() == null) {
            this.setCostNonSocialInsurance(java.math.BigDecimal.valueOf(0));
        }
        if (this.getCreatedTime() == null) {
            this.setCreatedTime(new java.util.Date());
        }
        if (this.getUpdatedTime() == null) {
            this.setUpdatedTime(new java.util.Date());
        }
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getStaffName() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "职工名称不能为空！");
            return false;
        }
        if (this.getIsDelete() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "是否在职不能为空！");
            return false;
        }
        if (this.getIdNumber() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "身份证号不能为空！");
            return false;
        }
        if (this.getType() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "医疗类别不能为空！");
            return false;
        }
        if (this.getCostTotal() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "医疗费合计支出不能为空！");
            return false;
        }
        if (this.getCostInsurance() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "医保支出不能为空！");
            return false;
        }
        if (this.getCostPollingFund() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "统筹基金支出不能为空！");
            return false;
        }
        if (this.getCostSeriousIllnessSupply() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "大病救助支出不能为空！");
            return false;
        }
        if (this.getCostSeriousIllness() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "大病支出不能为空！");
            return false;
        }
        if (this.getCostOtherSocialInsurance() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "其他社会保险支付不能为空！");
            return false;
        }
        if (this.getCostNonSocialInsurance() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "非社会保险支付不能为空！");
            return false;
        }
        if (this.getAffiliatedMedicalInstitution() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "所属医疗机构名称不能为空！");
            return false;
        }
        if (this.getEntryStatus() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "条目状态不能为空！");
            return false;
        }
        if (this.getEditorId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "操作人ID sys_user.id不能为空！");
            return false;
        }
        if (this.getEditorName() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "操作人 sys_user.real_name不能为空！");
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
