/**
 * 项目：中华中学管理平台
 * 模型分组：一日常规管理
 * 模型名称：领导值班代班表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.Staff;
import com.zhzx.server.domain.User;
import com.zhzx.server.dto.TeacherDutySubstituteDto;
import com.zhzx.server.repository.StaffMapper;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhzx.server.service.LeaderDutySubstituteService;
import com.zhzx.server.repository.LeaderDutySubstituteMapper;
import com.zhzx.server.repository.base.LeaderDutySubstituteBaseMapper;
import com.zhzx.server.domain.LeaderDutySubstitute;
import com.zhzx.server.rest.req.LeaderDutySubstituteParam;

@Service
public class LeaderDutySubstituteServiceImpl extends ServiceImpl<LeaderDutySubstituteMapper, LeaderDutySubstitute> implements LeaderDutySubstituteService {

    @Resource
    private StaffMapper staffMapper;

    public int updateAllFieldsById(LeaderDutySubstitute entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
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
    public boolean saveBatch(Collection<LeaderDutySubstitute> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(LeaderDutySubstituteBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    public IPage searchMyLogPage(IPage page, LeaderDutySubstituteParam param, Boolean bool) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Staff staff = staffMapper.selectById(user.getStaffId());
        if(bool == null){
            param.setLeaderOldId(staff.getId());
            param.setLeaderId(staff.getId());
        }else if(bool){
            param.setLeaderId(staff.getId());
        }else {
            param.setLeaderOldId(staff.getId());
        }
        List<TeacherDutySubstituteDto> teacherDutySubstituteDtoList = this.baseMapper.searchMyLogPage(page,param);
        page.setRecords(teacherDutySubstituteDtoList);
        return page;
    }
}
