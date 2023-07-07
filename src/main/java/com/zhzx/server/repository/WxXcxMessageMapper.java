/**
 * 项目：中华中学管理平台
 * 模型分组：第三方数据管理
 * 模型名称：微信小程序通知表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhzx.server.dto.MessageCombineDto;
import com.zhzx.server.repository.base.WxXcxMessageBaseMapper;
import com.zhzx.server.vo.MessageCombineVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WxXcxMessageMapper extends WxXcxMessageBaseMapper {


    List<MessageCombineDto> pageApp(IPage<MessageCombineDto> iPage,
                                    @Param("orderByClause") String orderByClause,
                                    @Param("messageCombineVo") MessageCombineVo messageCombineVo);
}
