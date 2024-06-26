package com.zhzx.server.msdomain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhzx.server.domain.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@TableName("dbo.ViewAccounts")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfo extends BaseDomain {

    /**
     * 账号
     */
    @TableId(value = "[帐号]")
    private Long id;

    /**
     * 部门
     */
    @TableField(value = "[部门]")
    private String department;

    /**
     * 编号
     */
    @TableField(value = "[编号]")
    private String orderNumber;

    /**
     * 姓名
     */
    @TableField(value = "[姓名]")
    private String name;

    /**
     * 身份
     */
    @TableField(value = "[身份]")
    private String type;

    /**
     * 性别
     */
    @TableField(value = "[性别]")
    private String sex;

    /**
     * 物理卡号
     */
    @TableField(value = "C_PhysicalNo")
    private String cPhysicalNo;

    /**
     * 淡化
     */
    @TableField(value = "Mobile")
    private String mobile;

    /**
     * 身份证号
     */
    @TableField(value = "PIN")
    private String idNumber;

    /**
     * 标志
     */
    @TableField(value = "[标志]")
    private String flag;

    /**
     * 预撤户
     */
    @TableField(value = "[预撤户]")
    private String ych;

    /**
     * 状态
     */
    @TableField(exist = false)
    private String situation;

}
