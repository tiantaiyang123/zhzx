/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：手机app用户首页应用表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.User;
import com.zhzx.server.domain.UserApplicationPreferApp;
import com.zhzx.server.repository.UserApplicationPreferAppMapper;
import com.zhzx.server.repository.base.UserApplicationPreferAppBaseMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.UserApplicationPreferAppService;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserApplicationPreferAppServiceImpl extends ServiceImpl<UserApplicationPreferAppMapper, UserApplicationPreferApp> implements UserApplicationPreferAppService {

    @Override
    public int updateAllFieldsById(UserApplicationPreferApp entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createOrUpdate(List<Long> idList) {
        Long userId = ((User)SecurityUtils.getSubject().getPrincipal()).getId();
        this.getBaseMapper().delete(
                Wrappers.<UserApplicationPreferApp>lambdaQuery()
                        .eq(UserApplicationPreferApp::getUserId, userId)
        );
        if (CollectionUtils.isNotEmpty(idList)) {
            idList = idList.stream().distinct().collect(Collectors.toList());
            if (idList.size() >= 10) throw new ApiCode.ApiException(-1, "应用不能超过10个");
            for (int i = 1; i <= idList.size(); i++) {
                UserApplicationPreferApp userApplicationPreferApp = new UserApplicationPreferApp();
                userApplicationPreferApp.setApplicationAppId(idList.get(i - 1));
                userApplicationPreferApp.setSortOrder((long) i);
                userApplicationPreferApp.setUserId(userId);
                this.getBaseMapper().insert(userApplicationPreferApp);
            }
        }
        return 1;
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
    public boolean saveBatch(Collection<UserApplicationPreferApp> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(UserApplicationPreferAppBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


}
