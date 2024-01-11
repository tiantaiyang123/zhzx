/**
 * 项目：中华中学管理平台
 * 模型分组：医疗管理
 * 模型名称：医药费报销条目表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest.req;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhzx.server.domain.MedicalExpenseReimbursementEntries;
import com.zhzx.server.enums.MedicalEntryEnum;
import com.zhzx.server.enums.YesNoEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "医药费报销条目表参数", description = "")
public class MedicalExpenseReimbursementEntriesParam implements Serializable {
    /**
     * ID系统自动生成
     */
    @ApiModelProperty(value = "ID系统自动生成")
    private Long id;
    /**
     * ID系统自动生成 IN值List
     */
    @ApiModelProperty(value = "ID系统自动生成 IN值List")
    private List<Long> idList;
    /**
     * 职工名称
     */
    @ApiModelProperty(value = "职工名称")
    private String staffName;
    /**
     * 是否在职
     */
    @ApiModelProperty(value = "是否在职")
    private YesNoEnum isDelete;
    /**
     * 是否在职 IN值List
     */
    @ApiModelProperty(value = "是否在职 IN值List")
    private List<String> isDeleteList;
    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号")
    private String idNumber;
    /**
     * 就诊结束日期
     */
    @ApiModelProperty(value = "就诊结束日期")
    private String visitEndDate;
    /**
     * 医疗类别
     */
    @ApiModelProperty(value = "医疗类别")
    private String type;
    /**
     * 医疗费合计支出
     */
    @ApiModelProperty(value = "医疗费合计支出")
    private java.math.BigDecimal costTotal;
    /**
     * 医保支出
     */
    @ApiModelProperty(value = "医保支出")
    private java.math.BigDecimal costInsurance;
    /**
     * 统筹基金支出
     */
    @ApiModelProperty(value = "统筹基金支出")
    private java.math.BigDecimal costPollingFund;
    /**
     * 大病救助支出
     */
    @ApiModelProperty(value = "大病救助支出")
    private java.math.BigDecimal costSeriousIllnessSupply;
    /**
     * 大病支出
     */
    @ApiModelProperty(value = "大病支出")
    private java.math.BigDecimal costSeriousIllness;
    /**
     * 非社会保险支付
     */
    @ApiModelProperty(value = "非社会保险支付")
    private java.math.BigDecimal costNonSocialInsurance;
    /**
     * 所属医疗机构名称
     */
    @ApiModelProperty(value = "所属医疗机构名称")
    private String affiliatedMedicalInstitution;
    /**
     * 条目状态
     */
    @ApiModelProperty(value = "条目状态")
    private MedicalEntryEnum entryStatus;
    /**
     * 条目状态 IN值List
     */
    @ApiModelProperty(value = "条目状态 IN值List")
    private List<String> entryStatusList;
    /**
     * 操作人ID sys_user.id
     */
    @ApiModelProperty(value = "操作人ID sys_user.id")
    private Long editorId;
    /**
     * 操作人 sys_user.real_name
     */
    @ApiModelProperty(value = "操作人 sys_user.real_name")
    private String editorName;
    /**
     * 
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "")
    private java.util.Date createdTime;
    /**
     *  下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 下限值(大于等于)")
    private java.util.Date createdTimeFrom;
    /**
     *  上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 上限值(小于)")
    private java.util.Date createdTimeTo;
    /**
     * 
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "")
    private java.util.Date updatedTime;
    /**
     *  下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 下限值(大于等于)")
    private java.util.Date updatedTimeFrom;
    /**
     *  上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 上限值(小于)")
    private java.util.Date updatedTimeTo;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<MedicalExpenseReimbursementEntries> toQueryWrapper() {
        QueryWrapper<MedicalExpenseReimbursementEntries> wrapper = Wrappers.<MedicalExpenseReimbursementEntries>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        if (this.getStaffName() != null) {
            if (this.getStaffName().startsWith("%") && this.getStaffName().endsWith("%")) {
                wrapper.like("staff_name", this.getStaffName().substring(1, this.getStaffName().length() - 1));
            } else if (this.getStaffName().startsWith("%") && !this.getStaffName().endsWith("%")) {
                wrapper.likeLeft("staff_name", this.getStaffName().substring(1));
            } else if (this.getStaffName().endsWith("%")) {
                wrapper.likeRight("staff_name", this.getStaffName().substring(0, this.getStaffName().length() - 1));
            } else {
                wrapper.eq("staff_name", this.getStaffName());
            }
        }
        wrapper.eq(this.getIsDelete() != null, "is_delete", this.getIsDelete());
        wrapper.in(this.getIsDeleteList() != null && this.getIsDeleteList().size() > 0, "is_delete", this.getIsDeleteList());
        if (this.getIdNumber() != null) {
            if (this.getIdNumber().startsWith("%") && this.getIdNumber().endsWith("%")) {
                wrapper.like("id_number", this.getIdNumber().substring(1, this.getIdNumber().length() - 1));
            } else if (this.getIdNumber().startsWith("%") && !this.getIdNumber().endsWith("%")) {
                wrapper.likeLeft("id_number", this.getIdNumber().substring(1));
            } else if (this.getIdNumber().endsWith("%")) {
                wrapper.likeRight("id_number", this.getIdNumber().substring(0, this.getIdNumber().length() - 1));
            } else {
                wrapper.eq("id_number", this.getIdNumber());
            }
        }
        wrapper.eq(this.getVisitEndDate() != null, "visit_end_date", this.getVisitEndDate());
        if (this.getType() != null) {
            if (this.getType().startsWith("%") && this.getType().endsWith("%")) {
                wrapper.like("type", this.getType().substring(1, this.getType().length() - 1));
            } else if (this.getType().startsWith("%") && !this.getType().endsWith("%")) {
                wrapper.likeLeft("type", this.getType().substring(1));
            } else if (this.getType().endsWith("%")) {
                wrapper.likeRight("type", this.getType().substring(0, this.getType().length() - 1));
            } else {
                wrapper.eq("type", this.getType());
            }
        }
        wrapper.eq(this.getCostTotal() != null, "cost_total", this.getCostTotal());
        wrapper.eq(this.getCostInsurance() != null, "cost_insurance", this.getCostInsurance());
        wrapper.eq(this.getCostPollingFund() != null, "cost_polling_fund", this.getCostPollingFund());
        wrapper.eq(this.getCostSeriousIllnessSupply() != null, "cost_serious_illness_supply", this.getCostSeriousIllnessSupply());
        wrapper.eq(this.getCostSeriousIllness() != null, "cost_serious_illness", this.getCostSeriousIllness());
        wrapper.eq(this.getCostNonSocialInsurance() != null, "cost_non_social_insurance", this.getCostNonSocialInsurance());
        if (this.getAffiliatedMedicalInstitution() != null) {
            if (this.getAffiliatedMedicalInstitution().startsWith("%") && this.getAffiliatedMedicalInstitution().endsWith("%")) {
                wrapper.like("affiliated_medical_institution", this.getAffiliatedMedicalInstitution().substring(1, this.getAffiliatedMedicalInstitution().length() - 1));
            } else if (this.getAffiliatedMedicalInstitution().startsWith("%") && !this.getAffiliatedMedicalInstitution().endsWith("%")) {
                wrapper.likeLeft("affiliated_medical_institution", this.getAffiliatedMedicalInstitution().substring(1));
            } else if (this.getAffiliatedMedicalInstitution().endsWith("%")) {
                wrapper.likeRight("affiliated_medical_institution", this.getAffiliatedMedicalInstitution().substring(0, this.getAffiliatedMedicalInstitution().length() - 1));
            } else {
                wrapper.eq("affiliated_medical_institution", this.getAffiliatedMedicalInstitution());
            }
        }
        wrapper.eq(this.getEntryStatus() != null, "entry_status", this.getEntryStatus());
        wrapper.in(this.getEntryStatusList() != null && this.getEntryStatusList().size() > 0, "entry_status", this.getEntryStatusList());
        wrapper.eq(this.getEditorId() != null, "editor_id", this.getEditorId());
        if (this.getEditorName() != null) {
            if (this.getEditorName().startsWith("%") && this.getEditorName().endsWith("%")) {
                wrapper.like("editor_name", this.getEditorName().substring(1, this.getEditorName().length() - 1));
            } else if (this.getEditorName().startsWith("%") && !this.getEditorName().endsWith("%")) {
                wrapper.likeLeft("editor_name", this.getEditorName().substring(1));
            } else if (this.getEditorName().endsWith("%")) {
                wrapper.likeRight("editor_name", this.getEditorName().substring(0, this.getEditorName().length() - 1));
            } else {
                wrapper.eq("editor_name", this.getEditorName());
            }
        }
        wrapper.eq(this.getCreatedTime() != null, "created_time", this.getCreatedTime());
        wrapper.ge(this.getCreatedTimeFrom() != null, "created_time", this.getCreatedTimeFrom());
        wrapper.lt(this.getCreatedTimeTo() != null, "created_time", this.getCreatedTimeTo());
        wrapper.eq(this.getUpdatedTime() != null, "updated_time", this.getUpdatedTime());
        wrapper.ge(this.getUpdatedTimeFrom() != null, "updated_time", this.getUpdatedTimeFrom());
        wrapper.lt(this.getUpdatedTimeTo() != null, "updated_time", this.getUpdatedTimeTo());
        return wrapper;
    }

}
