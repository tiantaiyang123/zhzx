package com.zhzx.server.dto;

import com.zhzx.server.domain.Comment;
import com.zhzx.server.domain.FunctionDepartment;
import com.zhzx.server.enums.YesNoEnum;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by A2 on 2022/1/11.
 */
@Data
public class CommentDto extends Comment{

    private List<FunctionDepartment> departmentList;

    private List<CommentProcessDto> commentProcessDtoList;

    private Map<Object,Object> columnMap;

    private List<FunctionDepartment> officeDepartmentList;

    private List<FunctionDepartment> gradeDepartmentList;

    private YesNoEnum needInstruction;

    private List<String> picList;

    private String schoolyardName;
}
