/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：目标模板详情表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.google.common.collect.Maps;
import com.zhzx.server.domain.*;
import com.zhzx.server.repository.*;
import com.zhzx.server.repository.base.ExamGoalTemplateSubBaseMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.ExamGoalService;
import com.zhzx.server.service.ExamGoalTemplateSubService;
import com.zhzx.server.util.StringUtils;
import com.zhzx.server.util.TwxUtils;
import com.zhzx.server.vo.ExamGoalTemplateVo;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ExamGoalTemplateSubServiceImpl extends ServiceImpl<ExamGoalTemplateSubMapper, ExamGoalTemplateSub> implements ExamGoalTemplateSubService {

    @Resource
    private ExamMapper examMapper;

    @Resource
    private ClazzMapper clazzMapper;

    @Resource
    private ExamGoalTemplateMapper examGoalTemplateMapper;

    @Resource
    private ExamGoalService examGoalService;

    @Resource
    private ExamGoalMapper examGoalMapper;

    @Resource
    private ExamGoalClazzMapper examGoalClazzMapper;

    @Override
    public int updateAllFieldsById(ExamGoalTemplateSub entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ExamGoalTemplateSub> createOrUpdate(ExamGoalTemplateVo examGoalTemplateVo) {
        List<ExamGoalTemplateSub> entity = examGoalTemplateVo.getEntity();
        Long id = examGoalTemplateVo.getId();
        String examGoalTemplateName = examGoalTemplateVo.getExamGoalTemplateName();
        Long gradeId = examGoalTemplateVo.getGradeId();
        Long academicYearSemesterId = examGoalTemplateVo.getAcademicYearSemesterId();
        if (CollectionUtils.isEmpty(entity)
                || gradeId == null
                || academicYearSemesterId == null
                || StringUtils.isNullOrEmpty(examGoalTemplateName)) {
            throw new ApiCode.ApiException(-1, "班级目标人数，年级，学年，模板名称不能为空");
        }

        Integer alreadyNameTemplateCnt = this.examGoalTemplateMapper.selectCount(Wrappers.<ExamGoalTemplate>lambdaQuery()
                .eq(ExamGoalTemplate::getGradeId, gradeId)
                .eq(ExamGoalTemplate::getAcademicYearSemesterId, academicYearSemesterId)
                .eq(ExamGoalTemplate::getName, examGoalTemplateName)
                .ne(null != id, ExamGoalTemplate::getId, id));
        if (alreadyNameTemplateCnt > 0)
            throw new ApiCode.ApiException(-1, "模板名称重复");

        Integer count = this.clazzMapper.selectCount(Wrappers.<Clazz>lambdaQuery()
                .eq(Clazz::getGradeId, gradeId)
                .eq(Clazz::getAcademicYearSemesterId, academicYearSemesterId));

        Map<Long, List<ExamGoalTemplateSub>> longListMap = entity.stream().collect(Collectors.groupingBy(ExamGoalTemplateSub::getClazzId));
        if (longListMap.size() != count) throw new ApiCode.ApiException(-1, "班级数量不匹配");
        Long clazzId = entity.get(0).getClazzId();
        List<ExamGoalTemplateSub> examGoalTemplateSubListStd = longListMap.get(clazzId);
        if (examGoalTemplateSubListStd.size() != (int) examGoalTemplateSubListStd.stream().map(ExamGoalTemplateSub::getGoalName).distinct().count())
            throw new ApiCode.ApiException(-1, "目标重复");

        List<String> stdList = examGoalTemplateSubListStd.stream().map(item -> "C" + item.getSortOrder() + item.getGoalName()).collect(Collectors.toList());
        longListMap.forEach((k, v) -> v.forEach(i -> {
            if (!stdList.contains("C" + i.getSortOrder() + i.getGoalName())) {
                throw new ApiCode.ApiException(-1, "班级目标不统一");
            }
        }));

        if (entity.size() % count != 0)
            throw new ApiCode.ApiException(-1, "数量校验失败");

        ExamGoalTemplate examGoalTemplate;
        if (id == null) {
            examGoalTemplate = new ExamGoalTemplate();
            examGoalTemplate.setGradeId(gradeId);
            examGoalTemplate.setAcademicYearSemesterId(academicYearSemesterId);
            examGoalTemplate.setName(examGoalTemplateName);
            examGoalTemplate.setDefault().validate(true);
            this.examGoalTemplateMapper.insert(examGoalTemplate);
        } else {
            Integer cnt = this.examMapper.selectCount(Wrappers.<Exam>lambdaQuery().eq(Exam::getExamGoalTemplateId, id));
            if (cnt > 0)
                throw new ApiCode.ApiException(-1, "该模板已被使用，无法编辑");

            examGoalTemplate = this.examGoalTemplateMapper.selectById(id);
            if (null == examGoalTemplate)
                throw new ApiCode.ApiException(-1, "无效的考试模板ID");
            if (!examGoalTemplate.getGradeId().equals(gradeId) || !examGoalTemplate.getAcademicYearSemesterId().equals(academicYearSemesterId))
                throw new ApiCode.ApiException(-1, "不能修改学年学期和年级");

            if (!examGoalTemplateName.equals(examGoalTemplate.getName())) {
                this.examGoalTemplateMapper.update(null, Wrappers.<ExamGoalTemplate>lambdaUpdate()
                        .eq(ExamGoalTemplate::getId, id)
                        .set(ExamGoalTemplate::getName, examGoalTemplateName));
            }
        }

        Long examGoalTemplateId = examGoalTemplate.getId();
        // delete all
        this.baseMapper.delete(Wrappers.<ExamGoalTemplateSub>lambdaQuery().eq(ExamGoalTemplateSub::getExamGoalTemplateId, examGoalTemplateId));
        // add
        entity.forEach(item -> {
                item.setExamGoalTemplateId(examGoalTemplateId);
                item.validate(true);
                this.baseMapper.insert(item);
        });
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertExamGoals(Long examGoalTemplateId, Long examId) {
        if (null == examGoalTemplateId || null == examId) return;

        Exam exam = this.examMapper.selectById(examId);
        ExamGoalTemplate examGoalTemplate = this.examGoalTemplateMapper.selectById(examGoalTemplateId);
        if (null == exam || null == examGoalTemplate) throw new ApiCode.ApiException(-1, "无效ID");
        if (!exam.getAcademicYearSemesterId().equals(examGoalTemplate.getAcademicYearSemesterId())
                || !exam.getGradeId().equals(examGoalTemplate.getGradeId())) {
            throw new ApiCode.ApiException(-1, "考试和目标模板无法对应");
        }

        List<ExamGoalTemplateSub> examGoalTemplateSubList = this.baseMapper.selectList(Wrappers.<ExamGoalTemplateSub>lambdaQuery()
                .eq(ExamGoalTemplateSub::getExamGoalTemplateId, examGoalTemplateId));
        if (CollectionUtils.isEmpty(examGoalTemplateSubList)) return;

        Map<String, ExamGoal> examGoalMap = this.examGoalMapper
                .selectList(Wrappers.<ExamGoal>lambdaQuery().eq(ExamGoal::getExamId, examId))
                .stream().collect(Collectors.toMap(ExamGoal::getName, Function.identity()));
        Map<String, List<ExamGoalTemplateSub>> map = examGoalTemplateSubList.stream().collect(Collectors.groupingBy(ExamGoalTemplateSub::getGoalName));

        // delete
        examGoalMap.forEach((k, v) -> {
            if (!map.containsKey(k)) {
                this.examGoalService.removeAll(v.getId());
            }
        });

        // add or update
        map.forEach((k, v) -> {
            ExamGoal examGoalCurr = examGoalMap.get(k);
            if (examGoalCurr == null) {
                ExamGoal examGoal = new ExamGoal();
                examGoal.setExamId(examId);
                examGoal.setName(k);
                examGoal.setDefault().validate(true);
                this.examGoalMapper.insert(examGoal);
                v.forEach(item -> {
                    ExamGoalClazz examGoalClazz = new ExamGoalClazz();
                    examGoalClazz.setExamGoalId(examGoal.getId());
                    examGoalClazz.setGoalValue(item.getGoalValue());
                    examGoalClazz.setClazzId(item.getClazzId());
                    examGoalClazz.setDefault().validate(true);
                    this.examGoalClazzMapper.insert(examGoalClazz);
                });
            } else {
                List<ExamGoalClazz> examGoalClazzList = this.examGoalClazzMapper.selectList(Wrappers.<ExamGoalClazz>lambdaQuery()
                        .eq(ExamGoalClazz::getExamGoalId, examGoalCurr.getId()));
                Map<Long, ExamGoalTemplateSub> map1 = v.stream().collect(Collectors.toMap(ExamGoalTemplateSub::getClazzId, Function.identity()));
                examGoalClazzList.forEach(item -> {
                    item.setGoalValue(map1.get(item.getClazzId()).getGoalValue());
                    this.examGoalClazzMapper.updateById(item);
                });
            }
        });
    }

    @Override
    public Map<String, Object> searchDetail(Long academicYearSemesterId, Long gradeId, Long examGoalTemplateId) {
        Map<String, Object> map = Maps.newHashMap();
        if (academicYearSemesterId == null || gradeId == null) return map;
        List<Clazz> clazzList = this.clazzMapper.selectList(Wrappers.<Clazz>lambdaQuery()
                .eq(Clazz::getAcademicYearSemesterId, academicYearSemesterId)
                .eq(Clazz::getGradeId, gradeId));
        clazzList.sort(Comparator.comparing(item -> Integer.parseInt(item.getName().replace("班", "")), Comparator.naturalOrder()));
        map.put("clazzList", clazzList);
        List<Map<String, Object>> dataList = new ArrayList<>();
        map.put("dataList", dataList);
        if (null != examGoalTemplateId) {
            List<Map<String, Object>> columnList = new ArrayList<>();
            map.put("columnList", columnList);
            List<ExamGoalTemplateSub> examGoalTemplateSubList = this.baseMapper.selectList(Wrappers.<ExamGoalTemplateSub>lambdaQuery()
                    .eq(ExamGoalTemplateSub::getExamGoalTemplateId, examGoalTemplateId));
            if (CollectionUtils.isNotEmpty(examGoalTemplateSubList)) {
                Map<Long, List<ExamGoalTemplateSub>> examGoalTemplateSubClazzMap = examGoalTemplateSubList.stream().collect(
                        Collectors
                                .groupingBy(
                                        ExamGoalTemplateSub::getClazzId, Collectors.collectingAndThen(Collectors.toList(), list -> list.stream().sorted(Comparator.comparing(ExamGoalTemplateSub::getSortOrder)).collect(Collectors.toList()))));
                clazzList.forEach(item -> {
                    Map<String, Object> rowMap = new HashMap<>();
                    List<ExamGoalTemplateSub> examGoalTemplateSubListCurr = examGoalTemplateSubClazzMap.get(item.getId());
                    examGoalTemplateSubListCurr.forEach(curr -> rowMap.put(curr.getGoalName(), curr));
                    dataList.add(rowMap);
                });
                examGoalTemplateSubClazzMap.get(clazzList.get(0).getId()).forEach(item -> {
                    Map<String, Object> mapColumn = new HashMap<>();
                    mapColumn.put("name", item.getGoalName());
                    mapColumn.put("sortOrder", item.getSortOrder());
                    columnList.add(mapColumn);
                });
                map.put("dataList", dataList);
            }
        } else {
            map.put("columnList", TwxUtils.goalTemplateDefaultList);
        }
        return map;
    }

    @Override
    public XSSFWorkbook exportExcel(Long id) {
        List<ExamGoalTemplateSub> examGoalTemplateSubs = this.baseMapper.selectList(Wrappers.<ExamGoalTemplateSub>lambdaQuery()
                .eq(ExamGoalTemplateSub::getExamGoalTemplateId, id));
        XSSFWorkbook book = new XSSFWorkbook();

        XSSFCellStyle style = book.createCellStyle();
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFSheet sheet = book.getSheetAt(0);

        if (CollectionUtils.isNotEmpty(examGoalTemplateSubs)) {
            examGoalTemplateSubs.sort(Comparator
                    .comparing((ExamGoalTemplateSub item) -> Integer.parseInt(item.getClazz().getName().replace("班", "")))
                    .thenComparing(ExamGoalTemplateSub::getSortOrder));
            XSSFCell cell;
            XSSFRow row0 = sheet.createRow(0), rowClazz;
            for (int rn = 1, cn = 1, idx = 0; idx < examGoalTemplateSubs.size(); idx++) {
                ExamGoalTemplateSub curr = examGoalTemplateSubs.get(idx);

                rowClazz = sheet.getRow(rn) == null ? sheet.createRow(rn) : sheet.getRow(rn);

                if (rn == 1) {
                    cell = row0.createCell(cn);
                    cell.setCellStyle(style);
                    cell.setCellValue(curr.getGoalName());
                }

                cell = rowClazz.createCell(cn);
                cell.setCellStyle(style);
                cell.setCellValue(curr.getGoalValue());

                if (cn == 1) {
                    cell = rowClazz.createCell(0);
                    cell.setCellStyle(style);
                    cell.setCellValue(curr.getClazz().getName());
                }
                cn++;

                if (idx != examGoalTemplateSubs.size() - 1) {
                    ExamGoalTemplateSub next = examGoalTemplateSubs.get(idx + 1);
                    if (!next.getClazzId().equals(curr.getClazzId())) {
                        rn ++;
                        cn = 1;
                    }
                }
            }
        }
        return book;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(Long examGoalTemplateId) {
        Integer cnt = this.examMapper.selectCount(Wrappers.<Exam>lambdaQuery().eq(Exam::getExamGoalTemplateId, examGoalTemplateId));
        if (cnt > 0)
            throw new ApiCode.ApiException(-1, "该模板已被使用，无法删除");
        this.baseMapper.delete(Wrappers.<ExamGoalTemplateSub>lambdaQuery().eq(ExamGoalTemplateSub::getExamGoalTemplateId, examGoalTemplateId));
        this.examGoalTemplateMapper.deleteById(examGoalTemplateId);
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
    public boolean saveBatch(Collection<ExamGoalTemplateSub> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(ExamGoalTemplateSubBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


}
