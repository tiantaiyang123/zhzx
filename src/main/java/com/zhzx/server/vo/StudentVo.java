package com.zhzx.server.vo;

import com.zhzx.server.domain.Student;
import lombok.Data;

/**
 * Created by A2 on 2022/2/28.
 */
@Data
public class StudentVo extends Student{

    private Long clazzId;

    private String clazzName;

    private Long gradeId;

    private String gradeName;
}
