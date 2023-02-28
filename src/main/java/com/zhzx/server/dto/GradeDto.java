package com.zhzx.server.dto;

import com.zhzx.server.domain.Clazz;
import com.zhzx.server.domain.Incident;
import com.zhzx.server.domain.TeacherDuty;
import com.zhzx.server.vo.ClazzVo;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by A2 on 2022/1/11.
 */
@Data
public class GradeDto extends BaseDto{

   private String gradeName;

   private List<ClazzVo> clazzVoList;
}
