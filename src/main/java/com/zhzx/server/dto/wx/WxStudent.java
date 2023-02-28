package com.zhzx.server.dto.wx;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * Created by A2 on 2022/3/15.
 */
@Data
@ApiModel(value = "WxStudent", description = "WxStudent")
public class WxStudent {
    private String student_userid;

    private String name;

    private List<WxParent> parents;

    private String gradeName;

    private String clazzName;

    private List<Long> department;

    private Long id;
}
