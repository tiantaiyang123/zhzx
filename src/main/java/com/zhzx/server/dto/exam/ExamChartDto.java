package com.zhzx.server.dto.exam;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ExamChartDto {

    private String name;

    private List<Map<String, Object>> data;
}
