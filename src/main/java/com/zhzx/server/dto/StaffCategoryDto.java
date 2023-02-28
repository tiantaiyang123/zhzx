package com.zhzx.server.dto;

import com.zhzx.server.domain.*;
import com.zhzx.server.enums.YesNoEnum;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by A2 on 2022/1/11.
 */
@Data
public class StaffCategoryDto extends StaffCategory{
    private Long staffId;

    private List<StaffCategory> children;

    private List<StaffCategoryRelation> staffCategoryRelations;
}
