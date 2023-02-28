package com.zhzx.server.dto.card;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConsumeRecordDto {

    private Long id;

    private Long a_Accounts;

    private Long c_Number;

    private String a_Number;

    private String a_Name;

    private String transType;

    private Long transFlag;

    private BigDecimal t_Money;

    private String t_Date;

    private String accountDay;

    private Long w_ID;

    private String w_Name;

    private Long pos_ID;

    private Long transNo;

    private Long sliceId;

    private String strSliceId;

    private BigDecimal t_LastMoney;
}
