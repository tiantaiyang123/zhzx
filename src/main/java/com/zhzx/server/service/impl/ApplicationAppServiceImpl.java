/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：手机app应用配置表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.ApplicationApp;
import com.zhzx.server.domain.User;
import com.zhzx.server.dto.ApplicationAppDto;
import com.zhzx.server.repository.ApplicationAppMapper;
import com.zhzx.server.repository.base.ApplicationAppBaseMapper;
import com.zhzx.server.service.ApplicationAppService;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationAppServiceImpl extends ServiceImpl<ApplicationAppMapper, ApplicationApp> implements ApplicationAppService {

    @Override
    public int updateAllFieldsById(ApplicationApp entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    @Override
    public List<ApplicationAppDto> searchByRole() {
        List<ApplicationAppDto> applicationAppDtoList = new ArrayList<>();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        String roleIds = user.getRoleList().stream().map(t -> t.getId().toString()).collect(Collectors.joining(",", "(", ")"));
        List<ApplicationApp> applicationAppList = this.baseMapper.selectList(
                Wrappers.<ApplicationApp>lambdaQuery()
                        .inSql(ApplicationApp::getId, "select sara.application_app_id from sys_application_role_app sara where sara.role_id in" + roleIds)
        );
        if (CollectionUtils.isEmpty(applicationAppList)) return applicationAppDtoList;

        List<ApplicationApp> applicationAppMenuList = applicationAppList.stream()
                .filter(t -> t.getParentId().equals(0L))
                .sorted(Comparator.comparingLong(ApplicationApp::getSortOrder))
                .collect(Collectors.toList());
        for (ApplicationApp applicationApp : applicationAppMenuList) {
            ApplicationAppDto applicationAppDto = new ApplicationAppDto();
            applicationAppDto.setTagName(applicationApp.getName());
            applicationAppDto.setApplicationAppList(applicationAppList.stream()
                    .filter(t -> t.getParentId().equals(applicationApp.getId()))
                    .sorted(Comparator.comparingLong(ApplicationApp::getSortOrder))
                    .collect(Collectors.toList()));
            applicationAppDtoList.add(applicationAppDto);
        }
        return applicationAppDtoList;
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
    public boolean saveBatch(Collection<ApplicationApp> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(ApplicationAppBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


}
