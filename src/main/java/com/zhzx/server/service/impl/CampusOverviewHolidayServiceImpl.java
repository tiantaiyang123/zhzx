/**
 * 项目：中华中学管理平台
 * 模型分组：一日常规管理
 * 模型名称：假期校园概况表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.CampusOverviewHoliday;
import com.zhzx.server.domain.LeaderDuty;
import com.zhzx.server.domain.User;
import com.zhzx.server.repository.CampusOverviewHolidayMapper;
import com.zhzx.server.repository.LeaderDutyMapper;
import com.zhzx.server.repository.base.CampusOverviewHolidayBaseMapper;
import com.zhzx.server.service.CampusOverviewHolidayService;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;

@Service
public class CampusOverviewHolidayServiceImpl extends ServiceImpl<CampusOverviewHolidayMapper, CampusOverviewHoliday> implements CampusOverviewHolidayService {

    @Resource
    private LeaderDutyMapper leaderDutyMapper;

    @Override
    public int updateAllFieldsById(CampusOverviewHoliday entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    @Override
    public CampusOverviewHoliday padSearch(Date time) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        LeaderDuty leaderDuty = this.leaderDutyMapper.selectOne(
                Wrappers.<LeaderDuty>lambdaQuery()
                        .eq(LeaderDuty::getLeaderId, user.getStaffId())
                        .apply("to_days(start_time)" + "=" + "to_days({0})", time)
        );

        CampusOverviewHoliday campusOverviewHoliday = new CampusOverviewHoliday();
        if (null != leaderDuty) {
            campusOverviewHoliday.setLeaderDutyId(leaderDuty.getId());
            campusOverviewHoliday.setSchoolyardId(leaderDuty.getSchoolyardId());

            campusOverviewHoliday = this.baseMapper.selectOne(
                    Wrappers.<CampusOverviewHoliday>lambdaQuery()
                            .eq(CampusOverviewHoliday::getLeaderDutyId, leaderDuty.getId())
            );
        }
        return campusOverviewHoliday;
    }

    @Override
    public CampusOverviewHoliday padSave(CampusOverviewHoliday entity) {
        entity.setDefault().validate(true);
        this.getBaseMapper().insert(entity);
        return entity;
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
    public boolean saveBatch(Collection<CampusOverviewHoliday> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(CampusOverviewHolidayBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


}
