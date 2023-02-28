/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：成绩单表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.*;
import com.zhzx.server.repository.*;
import com.zhzx.server.repository.base.ExamScoreReportBaseMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.ExamScoreReportService;
import com.zhzx.server.util.CellUtils;
import com.zhzx.server.util.StringUtils;
import lombok.SneakyThrows;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExamScoreReportServiceImpl extends ServiceImpl<ExamScoreReportMapper, ExamScoreReport> implements ExamScoreReportService {

    @Override
    public int updateAllFieldsById(ExamScoreReport entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    @Value("${web.upload-path}")
    private String uploadPath;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private ExamMapper examMapper;

    @Resource
    private SubjectMapper subjectMapper;

    @Resource
    private ExamResultMapper examResultMapper;

    @Override
    public void importExcel(String fileUrl, Long academicYearSemesterId, Long subjectId, Long clazzId) {
        String[] items = fileUrl.split("/");
        File file = new File(uploadPath + "\\" + items[items.length - 2] + "\\" + items[items.length - 1]);
        if (!file.exists())
            throw new ApiCode.ApiException(-1, "文件不存在！");
        try {
            XSSFWorkbook book = new XSSFWorkbook(file);
            XSSFSheet sheet = book.getSheetAt(0);
            file.delete();
            int rowNum = sheet.getPhysicalNumberOfRows();
            StringBuilder sb = new StringBuilder();
            String errText;
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            // 读取单元格数据
            for (int rowIndex = 1; rowIndex < rowNum; rowIndex++) {
                errText = "";
                String orderNumber = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(0));
                String usualScoreStr = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(2));
                if (StringUtils.isNullOrEmpty(orderNumber) || StringUtils.isNullOrEmpty(usualScoreStr))
                    errText += "\t编号或者平时成绩不能为空";
                Student student = this.studentMapper.selectOne(Wrappers.<Student>lambdaQuery().eq(Student::getOrderNumber, orderNumber));
                if (student == null)
                    errText += "\t编号不存在";
                if (!StringUtils.isNullOrEmpty(errText))
                    sb.append(errText = "第" + rowIndex + "行 " + errText + "\n");
                if (StringUtils.isNullOrEmpty(errText)) {
                    Long usualScore = Long.parseLong(usualScoreStr);
                    Wrapper<ExamScoreReport> wrapper = Wrappers.<ExamScoreReport>lambdaQuery().eq(ExamScoreReport::getClazzId, clazzId).eq(ExamScoreReport::getStudentId, student.getId())
                            .eq(ExamScoreReport::getSubjectId, subjectId);
                    ExamScoreReport examScoreReport = this.baseMapper.selectOne(wrapper);
                    if (examScoreReport == null) {
                        examScoreReport = new ExamScoreReport();
                        examScoreReport.setClazzId(clazzId);
                        examScoreReport.setSubjectId(subjectId);
                        examScoreReport.setStudentId(student.getId());
                        examScoreReport.setUsualScore(usualScore);
                        examScoreReport.setEndScore(0L);
                        examScoreReport.setMidScore(0L);
                        examScoreReport.setTotalScore(0L);
                        examScoreReport.setEditorId(user.getId());
                        examScoreReport.setEditorName(user.getRealName());
                        this.baseMapper.insert(examScoreReport);
                    } else {
                        examScoreReport.setUsualScore(usualScore);
                        examScoreReport.setEditorId(user.getId());
                        examScoreReport.setEditorName(user.getRealName());
                        this.baseMapper.updateById(examScoreReport);
                    }
                }
            }
            if (sb.length() != 0)
                throw new ApiCode.ApiException(-1, "数据有错！\n" + sb);
        } catch (IOException | InvalidFormatException e) {
            throw new ApiCode.ApiException(-1, "导入数据失败！");
        }
    }

    @Override
    @SneakyThrows
    public void calculate(Long subjectId, Long clazzId, Long examIdMiddle, Long examIdEnd) {
        List<ExamScoreReport> examScoreReports = this.baseMapper.selectList(Wrappers.<ExamScoreReport>lambdaQuery().eq(ExamScoreReport::getSubjectId, subjectId).eq(ExamScoreReport::getClazzId, clazzId));
        if (CollectionUtils.isNotEmpty(examScoreReports)) {
            Map<Long, String> subjectMap = this.subjectMapper.selectList(new QueryWrapper<>()).stream().collect(Collectors.toMap(Subject::getId, Subject::getSubjectAlias));
            if (subjectMap.containsKey(subjectId)) {
                Exam middle = this.examMapper.selectById(examIdMiddle);
                Exam end = this.examMapper.selectById(examIdEnd);
                int weightMiddle = middle.getExamType().getWeight();
                int weightEnd = end.getExamType().getWeight();
                int weightUsual = 100 - weightMiddle - weightEnd;
                if (weightUsual <= 0) {
                    throw new ApiCode.ApiException(-2, "期中期末权重和大于100");
                }
                User user = (User) SecurityUtils.getSubject().getPrincipal();
                for (ExamScoreReport examScoreReport : examScoreReports) {
                    String reflectField = subjectMap.get(subjectId).substring(0, 1).toUpperCase().concat(subjectMap.get(subjectId).substring(1)).concat("Score");
                    Method method = ExamResult.class.getMethod("get" + reflectField);
                    ExamResult examResultMiddle = this.examResultMapper.selectOne(Wrappers.<ExamResult>lambdaQuery().eq(ExamResult::getExamId, examIdMiddle).eq(ExamResult::getClazzId, clazzId).eq(ExamResult::getStudentId, examScoreReport.getStudentId()));
                    BigDecimal scoreMiddle = (BigDecimal) method.invoke(examResultMiddle);
                    ExamResult examResultEnd = this.examResultMapper.selectOne(Wrappers.<ExamResult>lambdaQuery().eq(ExamResult::getExamId, examIdEnd).eq(ExamResult::getClazzId, clazzId).eq(ExamResult::getStudentId, examScoreReport.getStudentId()));
                    BigDecimal scoreEnd = (BigDecimal) method.invoke(examResultEnd);

                    long endScore = scoreEnd.setScale(0, RoundingMode.HALF_UP).longValue();
                    long midScore = scoreMiddle.setScale(0, RoundingMode.HALF_UP).longValue();
                    long totalScore = examScoreReport.getUsualScore() * weightUsual + midScore + weightMiddle + endScore * weightEnd;

                    examScoreReport.setMidScore(midScore);
                    examScoreReport.setEditorId(user.getId());
                    examScoreReport.setEditorName(user.getRealName());
                    examScoreReport.setEndScore(endScore);
                    examScoreReport.setTotalScore(new BigDecimal(totalScore).divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP).longValue());
                    this.baseMapper.updateById(examScoreReport);
                }
            }
        }
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
    public boolean saveBatch(Collection<ExamScoreReport> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(ExamScoreReportBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


}
