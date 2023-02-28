/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：教师值班代班表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.Staff;
import com.zhzx.server.domain.TeacherDuty;
import com.zhzx.server.domain.User;
import com.zhzx.server.dto.TeacherDutySubstituteDto;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.repository.ClazzMapper;
import com.zhzx.server.repository.StaffMapper;
import com.zhzx.server.repository.TeacherDutyMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.vo.ClazzVo;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhzx.server.service.TeacherDutySubstituteService;
import com.zhzx.server.repository.TeacherDutySubstituteMapper;
import com.zhzx.server.repository.base.TeacherDutySubstituteBaseMapper;
import com.zhzx.server.domain.TeacherDutySubstitute;
import com.zhzx.server.rest.req.TeacherDutySubstituteParam;

@Service
public class TeacherDutySubstituteServiceImpl extends ServiceImpl<TeacherDutySubstituteMapper, TeacherDutySubstitute> implements TeacherDutySubstituteService {

    @Resource
    private StaffMapper staffMapper;
    @Resource
    private TeacherDutyMapper teacherDutyMapper;
    @Resource
    private ClazzMapper clazzMapper;
    @Override
    public int updateAllFieldsById(TeacherDutySubstitute entity) {
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
    public boolean saveBatch(Collection<TeacherDutySubstitute> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(TeacherDutySubstituteBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    public IPage<TeacherDutySubstituteDto> searchMyLogPage(IPage<TeacherDutySubstituteDto> page, TeacherDutySubstituteParam param,Boolean bool) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Staff staff = staffMapper.selectById(user.getStaffId());
        if(bool == null){
            param.setTeacherOldId(staff.getId());
            param.setTeacherId(staff.getId());
        }else if(bool){
            param.setTeacherId(staff.getId());
        }else {
            param.setTeacherOldId(staff.getId());
        }
        List<TeacherDutySubstituteDto> teacherDutySubstituteDtoList = this.baseMapper.searchMyLogPage(page,param);
        teacherDutySubstituteDtoList.stream().forEach(teacherDutySubstituteDto -> {
            List<ClazzVo> clazzVos = clazzMapper.selectByTeacherDutyId(teacherDutySubstituteDto.getTeacherDutyId());
            if(CollectionUtils.isNotEmpty(clazzVos)){
                Map<String ,List<ClazzVo>> clazzMap = clazzVos.stream().collect(Collectors.groupingBy(ClazzVo::getGradeName));
                StringBuilder sb = new StringBuilder();
                for (String key :clazzMap.keySet()) {
                    sb.append(key);
                    for (int i = 0; i < clazzMap.get(key).size(); i++) {
                        if(i == clazzMap.size()-1){
                            sb.append(clazzMap.get(key).get(i).getName());
                        }else{
                            sb.append(clazzMap.get(key).get(i).getName()).append("/");
                        }
                    }
                    sb.append(",");
                }
                teacherDutySubstituteDto.setClazz(sb.toString().split(",")[0]);
            }
        });
        page.setRecords(teacherDutySubstituteDtoList);
        return page;
    }

    @Override
    @Transactional
    public Integer agree(TeacherDutySubstitute entity) {
        TeacherDutySubstitute teacherDutySubstitute = this.getBaseMapper().selectById(entity.getId());
        if(teacherDutySubstitute.getIsAgree() != null) throw new ApiCode.ApiException(-5,"该记录已使用，请勿重复使用！");
        if(YesNoEnum.YES.equals(entity.getIsAgree())){
            teacherDutySubstitute.setIsAgree(YesNoEnum.YES);
            this.baseMapper.updateById(teacherDutySubstitute);
            TeacherDuty teacherDuty = new TeacherDuty();
            teacherDuty.setId(teacherDutySubstitute.getTeacherDutyId());
            teacherDuty.setTeacherId(teacherDutySubstitute.getTeacherId());
            return teacherDutyMapper.updateById(teacherDuty);
        }else if(YesNoEnum.NO.equals(entity.getIsAgree())){
            teacherDutySubstitute.setIsAgree(YesNoEnum.NO);
            return this.baseMapper.updateById(teacherDutySubstitute);
        }
        return 0;
    }
}
