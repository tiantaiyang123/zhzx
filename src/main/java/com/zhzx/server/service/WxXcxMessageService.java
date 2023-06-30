/**
 * 项目：中华中学管理平台
 * 模型分组：第三方数据管理
 * 模型名称：微信小程序通知表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.WxXcxMessage;

public interface WxXcxMessageService extends IService<WxXcxMessage> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(WxXcxMessage entity);


}
