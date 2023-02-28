/**
* 项目：中华中学管理平台
* 模型分组：系统管理
* 模型名称：学生家长表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.rest.req;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import com.zhzx.server.enums.RelationEnum;
import com.zhzx.server.enums.MessageParentEnum;
import com.zhzx.server.domain.StudentParent;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "学生家长表参数", description = "")
public class StudentParentParam implements Serializable {
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
     * 学生ID sys_student.id
     */
    @ApiModelProperty(value = "学生ID sys_student.id")
    private Long studentId;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String name;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;
    /**
     * 关系
     */
    @ApiModelProperty(value = "关系")
    private RelationEnum relation;
    /**
     * 关系 IN值List
     */
    @ApiModelProperty(value = "关系 IN值List")
    private List<String> relationList;
    /**
     * 是否在微信创建家长联系人发送消息
     */
    @ApiModelProperty(value = "是否在微信创建家长联系人发送消息")
    private MessageParentEnum type;
    /**
     * 是否在微信创建家长联系人发送消息 IN值List
     */
    @ApiModelProperty(value = "是否在微信创建家长联系人发送消息 IN值List")
    private List<String> typeList;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<StudentParent> toQueryWrapper() {
        QueryWrapper<StudentParent> wrapper = Wrappers.<StudentParent>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getStudentId() != null, "student_id", this.getStudentId());
        if (this.getName() != null) {
            if (this.getName().startsWith("%") && this.getName().endsWith("%")) {
                wrapper.like("name", this.getName().substring(1, this.getName().length() - 1));
            } else if (this.getName().startsWith("%") && !this.getName().endsWith("%")) {
                wrapper.likeLeft("name", this.getName().substring(1));
            } else if (this.getName().endsWith("%")) {
                wrapper.likeRight("name", this.getName().substring(0, this.getName().length() - 1));
            } else {
                wrapper.eq("name", this.getName());
            }
        }
        if (this.getPhone() != null) {
            if (this.getPhone().startsWith("%") && this.getPhone().endsWith("%")) {
                wrapper.like("phone", this.getPhone().substring(1, this.getPhone().length() - 1));
            } else if (this.getPhone().startsWith("%") && !this.getPhone().endsWith("%")) {
                wrapper.likeLeft("phone", this.getPhone().substring(1));
            } else if (this.getPhone().endsWith("%")) {
                wrapper.likeRight("phone", this.getPhone().substring(0, this.getPhone().length() - 1));
            } else {
                wrapper.eq("phone", this.getPhone());
            }
        }
        wrapper.eq(this.getRelation() != null, "relation", this.getRelation());
        wrapper.in(this.getRelationList() != null && this.getRelationList().size() > 0, "relation", this.getRelationList());
        wrapper.eq(this.getType() != null, "type", this.getType());
        wrapper.in(this.getTypeList() != null && this.getTypeList().size() > 0, "type", this.getTypeList());
        return wrapper;
    }

}
