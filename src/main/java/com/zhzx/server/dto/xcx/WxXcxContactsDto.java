package com.zhzx.server.dto.xcx;

import com.zhzx.server.domain.BaseDomain;
import lombok.Data;

@Data
public class WxXcxContactsDto extends BaseDomain {
    private String groupNames;
    private String chargedWork;
    private String img;
    private String officePhone;
    private String phone;
    private String subject;
    private String sex;
    private String name;
    private String id;
    private String schoolName;
}
