/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：教学成果参评申报历史表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
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
import com.zhzx.server.domain.TeachingResultHistory;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "教学成果参评申报历史表参数", description = "")
public class TeachingResultHistoryParam implements Serializable {
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
     * 教学成果ID tmg_teaching_result.id
     */
    @ApiModelProperty(value = "教学成果ID tmg_teaching_result.id")
    private Long teachingResultId;
    /**
     * 申报类别ID1 sys_teaching_result_declaration_classify.id
     */
    @ApiModelProperty(value = "申报类别ID1 sys_teaching_result_declaration_classify.id")
    private Long classifyId1;
    /**
     * 申报类别ID2 sys_teaching_result_declaration_classify.id
     */
    @ApiModelProperty(value = "申报类别ID2 sys_teaching_result_declaration_classify.id")
    private Long classifyId2;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<TeachingResultHistory> toQueryWrapper() {
        QueryWrapper<TeachingResultHistory> wrapper = Wrappers.<TeachingResultHistory>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getTeachingResultId() != null, "teaching_result_id", this.getTeachingResultId());
        wrapper.eq(this.getClassifyId1() != null, "classify_id1", this.getClassifyId1());
        wrapper.eq(this.getClassifyId2() != null, "classify_id2", this.getClassifyId2());
        return wrapper;
    }

}
