package com.zhzx.server.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 用于恢复一卡通号
 */
@Data
public class CardIdentifier implements Serializable {

    private String UserBH;
    private String UserName;
    private String UserIDcard;
    private String UserYktNumber;

}
