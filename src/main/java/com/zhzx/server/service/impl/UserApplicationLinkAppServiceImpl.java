/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：手机app用户应用跳转表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.User;
import com.zhzx.server.domain.UserApplicationLinkApp;
import com.zhzx.server.repository.UserApplicationLinkAppMapper;
import com.zhzx.server.repository.base.UserApplicationLinkAppBaseMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.UserApplicationLinkAppService;
import com.zhzx.server.util.DateUtils;
import com.zhzx.server.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

@Service
@Slf4j
public class UserApplicationLinkAppServiceImpl extends ServiceImpl<UserApplicationLinkAppMapper, UserApplicationLinkApp> implements UserApplicationLinkAppService {

    @Override
    public int updateAllFieldsById(UserApplicationLinkApp entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    @Value("${xcx.expire_time}")
    private Long xcxExpireTime;
    @Value("${xcx.auth_prefix}")
    private String xcxAuthPrefix;
    @Value("${xcx.request_url}")
    private String xcxRequestUrl;
    @Resource
    private RestTemplate restTemplate;

    private String request(String xcxCode) {
        Date now = new Date();
        String dateString = DateUtils.format(now, "yyyy-MM-dd");

        JSONObject jsonObject = restTemplate.getForObject(
                xcxRequestUrl,
                JSONObject.class,
                new HashMap<String, Object>(){
                    {
                        this.put("code", Base64Utils.encodeToString((xcxAuthPrefix + dateString).getBytes(StandardCharsets.UTF_8)));
                        this.put("module", xcxCode);
                    }
                }
        );
        if (null == jsonObject) {
            throw new ApiCode.ApiException(-1, "用户跳转链接接口调用错误");
        }
        String openLink = jsonObject.getString("openLink");
        if (StringUtils.isNullOrEmpty(openLink)) {
            throw new ApiCode.ApiException(-2, "用户跳转链接接口响应错误，" + jsonObject.getString("msg"));
        }
        return openLink;
    }

    @Override
    public String getUserLink(String xcxCode, Boolean force) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Long userId = user.getId();

        UserApplicationLinkApp userApplicationLinkApp = this.baseMapper.selectOne(
                Wrappers.<UserApplicationLinkApp>lambdaQuery()
                        .eq(UserApplicationLinkApp::getXcxCode, xcxCode)
                        .eq(UserApplicationLinkApp::getUserId, userId)
        );

        // 强制刷新
        if (force) {
            if (null == userApplicationLinkApp) {
                throw new ApiCode.ApiException(500, "status exception");
            }
            userApplicationLinkApp.setLinkPath(this.request(xcxCode));
            this.getBaseMapper().updateById(userApplicationLinkApp);
            return userApplicationLinkApp.getLinkPath();
        }

        if (null != userApplicationLinkApp
                && (userApplicationLinkApp.getCreateTime().getTime() + xcxExpireTime * 1000) > new Date().getTime()) {
            return userApplicationLinkApp.getLinkPath();
        }

        UserApplicationLinkApp userApplicationLinkAppNew = new UserApplicationLinkApp();
        userApplicationLinkAppNew.setLinkPath(this.request(xcxCode));
        if (null == userApplicationLinkApp) {
            userApplicationLinkAppNew.setUserId(userId);
            userApplicationLinkAppNew.setXcxCode(xcxCode);
            this.getBaseMapper().insert(userApplicationLinkAppNew);
        } else {
            userApplicationLinkAppNew.setId(userApplicationLinkApp.getId());
            this.getBaseMapper().updateById(userApplicationLinkAppNew);
        }
        return userApplicationLinkAppNew.getLinkPath();
    }

    /**
     * 批量插入
     *
     * @param entityList ignore
     * @param batchSize  ignore
     * @return ignore
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<UserApplicationLinkApp> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(UserApplicationLinkAppBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


}
