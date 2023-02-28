/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：考试发布表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.*;
import com.zhzx.server.enums.ExamPrintEnum;
import com.zhzx.server.enums.SemesterEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.repository.AcademicYearSemesterMapper;
import com.zhzx.server.repository.ExamPrintLogMapper;
import com.zhzx.server.repository.ExamPublishMapper;
import com.zhzx.server.repository.ExamPublishRelationMapper;
import com.zhzx.server.repository.base.ExamPublishBaseMapper;
import com.zhzx.server.rest.req.ExamPublishParam;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.ExamPublishService;
import com.zhzx.server.vo.ExamPublishVo;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamPublishServiceImpl extends ServiceImpl<ExamPublishMapper, ExamPublish> implements ExamPublishService {

    @Resource
    private ExamPublishRelationMapper examPublishRelationMapper;

    @Resource
    private ExamPrintLogMapper examPrintLogMapper;

    @Resource
    private AcademicYearSemesterMapper academicYearSemesterMapper;

    @Override
    public int updateAllFieldsById(ExamPublish entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamPublish createUpdate(ExamPublishVo entity) {
        if (CollectionUtils.isEmpty(entity.getExamIds())) {
            throw new ApiCode.ApiException(-1, "考试列表为空");
        }
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Long[] arr = new Long[1];
        arr[0] = entity.getId();
        if (entity.getId() == null) {
            ExamPublish examPublish = new ExamPublish();
            examPublish.setGradeId(entity.getGradeId());
            examPublish.setPublishTime(new Date());
            examPublish.setEditor(user.getRealName());
            examPublish.setDefault().validate(true);
            this.baseMapper.insert(examPublish);
            arr[0] = examPublish.getId();
        } else {
            ExamPublish examPublish = this.baseMapper.selectById(entity.getId());
            if (!examPublish.getIsPublish().equals(ExamPrintEnum.NO)) {
                throw new ApiCode.ApiException(-2, "已发布, 不能修改");
            }
            this.update(Wrappers.<ExamPublish>lambdaUpdate().eq(ExamPublish::getId, entity.getId())
                    .set(ExamPublish::getPublishTime, new Date()).set(ExamPublish::getGradeId, entity.getGradeId()).set(ExamPublish::getEditor, user.getRealName()));
            this.examPublishRelationMapper.delete(Wrappers.<ExamPublishRelation>lambdaQuery().eq(ExamPublishRelation::getExamPublishId, entity.getId()));
        }
        List<ExamPublishRelation> examPublishRelationList = entity.getExamIds().stream().map(item -> {
            ExamPublishRelation examPublishRelation = new ExamPublishRelation();
            examPublishRelation.setExamPublishId(arr[0]);
            examPublishRelation.setExamId(item);
            examPublishRelation.setDefault().validate(true);
            return examPublishRelation;
        }).collect(Collectors.toList());
        this.examPublishRelationMapper.batchInsert(examPublishRelationList);
        return this.baseMapper.selectById(arr[0]);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer removeAll(Long id) {
        this.baseMapper.deleteById(id);
        this.examPublishRelationMapper.delete(Wrappers.<ExamPublishRelation>lambdaQuery().eq(ExamPublishRelation::getExamPublishId, id));
        // 学生打印记录也一起删除
        this.examPrintLogMapper.delete(Wrappers.<ExamPrintLog>lambdaQuery().eq(ExamPrintLog::getExamPublishId, id));
        return 1;
    }

    @Override
    public IPage<ExamPublish> listByGrade(IPage<ExamPublish> page, ExamPublishParam param, YesNoEnum containsCurrent, Long gradeId, Long academicYearSemesterId, String orderByClause) {
        List<String> paramList = new ArrayList<>();
        if (academicYearSemesterId == null)
            academicYearSemesterId = ((User) SecurityUtils.getSubject().getPrincipal()).getAcademicYearSemester().getId();
        if (YesNoEnum.YES.equals(containsCurrent)) {
            paramList.add(gradeId + "_" + academicYearSemesterId);
        } else {
            AcademicYearSemester academicYearSemester = this.academicYearSemesterMapper.selectById(academicYearSemesterId);
            List<AcademicYearSemester> academicYearSemesters = this.academicYearSemesterMapper.selectList(
                    Wrappers.<AcademicYearSemester>lambdaQuery()
                            .le(AcademicYearSemester::getEndTime, academicYearSemester.getEndTime())
                            .orderByDesc(AcademicYearSemester::getEndTime)
            );
            int idx = 0;
            while (gradeId > 0) {
                if (idx >= academicYearSemesters.size())
                    break;
                AcademicYearSemester curr = academicYearSemesters.get(idx++);
                paramList.add(gradeId + "_" + curr.getId());
                if (SemesterEnum.Q2.equals(curr.getSemester()) && idx < academicYearSemesters.size())
                    paramList.add(gradeId + "_" + academicYearSemesters.get(idx++).getId());
                gradeId--;
            }
        }
        return this.baseMapper.listByGrade(page, param, paramList, orderByClause);
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
    public boolean saveBatch(Collection<ExamPublish> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(ExamPublishBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


}
