package com.zhzx.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhzx.server.dto.card.ConsumeRecordDto;
import com.zhzx.server.msdomain.AccountInfo;
import com.zhzx.server.vo.ConsumeRecordVo;

import java.util.Map;

public interface CardService {

    Map<String, Object> cardLogin(String username, String password);

    AccountInfo getInfo(Long cPhysicalNo, String mobile, String idNumber);

    IPage<ConsumeRecordDto> selectConsumePage(ConsumeRecordVo consumeRecordVo, Integer pageNum, Integer pageSize);
}
