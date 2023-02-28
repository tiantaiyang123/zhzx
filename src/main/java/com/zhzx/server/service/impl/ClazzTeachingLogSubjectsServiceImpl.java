/**
 * 项目：中华中学管理平台
 * 模型分组：学生管理
 * 模型名称：班级教学日志科目表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.ClazzTeachingLog;
import com.zhzx.server.domain.ClazzTeachingLogSubjects;
import com.zhzx.server.domain.User;
import com.zhzx.server.enums.LogStateEnum;
import com.zhzx.server.repository.ClazzTeachingLogMapper;
import com.zhzx.server.repository.ClazzTeachingLogSubjectsMapper;
import com.zhzx.server.repository.base.ClazzTeachingLogSubjectsBaseMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.ClazzTeachingLogSubjectsService;
import com.zhzx.server.util.DateUtils;
import com.zhzx.server.vo.ClazzTeachingLogSubjectsVo;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClazzTeachingLogSubjectsServiceImpl extends ServiceImpl<ClazzTeachingLogSubjectsMapper, ClazzTeachingLogSubjects> implements ClazzTeachingLogSubjectsService {

    @Resource
    private ClazzTeachingLogMapper clazzTeachingLogMapper;

    @Override
    public int updateAllFieldsById(ClazzTeachingLogSubjects entity) {
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
    public boolean saveBatch(Collection<ClazzTeachingLogSubjects> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(ClazzTeachingLogSubjectsBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    public IPage<ClazzTeachingLogSubjectsVo> listAuditPage(IPage<ClazzTeachingLogSubjectsVo> page, Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long clazzId, Integer week, String state) {
        return this.getBaseMapper().listAuditPage(page, schoolyardId, academicYearSemesterId, gradeId, clazzId, week, state, null, null);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchAudit(List<Long> idList) {
        for (Long id : idList) {
            ClazzTeachingLogSubjects clazzTeachingLogSubjects = new ClazzTeachingLogSubjects();
            clazzTeachingLogSubjects.setId(id);
            clazzTeachingLogSubjects.setState(LogStateEnum.S3);
            this.updateById(clazzTeachingLogSubjects);
        }
    }

    @Override
    @SneakyThrows
    public XSSFWorkbook exportExcel(Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long clazzId, Integer week, Date startTime, Date endTime) {
        InputStream is = getClass().getResourceAsStream("/static/templates/教学日志统计.xlsx");
        XSSFWorkbook book = new XSSFWorkbook(is);

        XSSFCellStyle style = book.createCellStyle();
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        IPage<ClazzTeachingLogSubjectsVo> page = this.baseMapper.listAuditPage(new Page<>(1, 99999), schoolyardId, academicYearSemesterId, gradeId, clazzId, week, null, startTime, endTime);
        List<ClazzTeachingLogSubjectsVo> clazzTeachingLogSubjectsVos = page.getRecords();
        if (CollectionUtils.isNotEmpty(clazzTeachingLogSubjectsVos)) {
            Map<String, List<ClazzTeachingLogSubjectsVo>> map = clazzTeachingLogSubjectsVos.stream().collect(Collectors.groupingBy(item -> item.getClazz().getGrade().getName()));
            for (Map.Entry<String, List<ClazzTeachingLogSubjectsVo>> entry : map.entrySet()) {
                this.putData(book, style, entry.getKey(), entry.getValue());
            }
        }
        return book;
    }

    @Override
    public Integer studentSure(Date startTime) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (user.getClazz() == null) throw new ApiCode.ApiException(-1, "无法获取学生班级");
        ClazzTeachingLog clazzTeachingLog = this.clazzTeachingLogMapper.selectOne(Wrappers.<ClazzTeachingLog>lambdaQuery()
                .eq(ClazzTeachingLog::getClazzId, user.getClazz().getId())
                .apply("to_days(register_date)" + "=" + "to_days({0})", startTime));
        if (clazzTeachingLog == null || CollectionUtils.isEmpty(clazzTeachingLog.getClazzTeachingLogSubjectsList())) return 0;
        List<ClazzTeachingLogSubjects> clazzTeachingLogSubjects = clazzTeachingLog.getClazzTeachingLogSubjectsList();
        for (ClazzTeachingLogSubjects clazzTeachingLogSubject : clazzTeachingLogSubjects) {
            if (clazzTeachingLogSubject.getState() == null || clazzTeachingLogSubject.getState().equals(LogStateEnum.S3)) {
                clazzTeachingLogSubject.setState(LogStateEnum.S1);
                this.baseMapper.updateById(clazzTeachingLogSubject);
            }
        }
        return 1;
    }

    private void putData(XSSFWorkbook book, XSSFCellStyle style, String sheetName, List<ClazzTeachingLogSubjectsVo> data) {
        Map<Date, List<ClazzTeachingLogSubjectsVo>> map = data.stream().collect(Collectors.groupingBy(ClazzTeachingLogSubjectsVo::getRegisterDate, TreeMap::new, Collectors.toList()));

        XSSFSheet sheet = book.getSheet(sheetName);
        XSSFRow row;
        XSSFCell cell;
        int startRow = 0;
        for (Map.Entry<Date, List<ClazzTeachingLogSubjectsVo>> entry : map.entrySet()) {
            CellRangeAddress rangeAddress = new CellRangeAddress(startRow, startRow, 0, 4);
            sheet.addMergedRegion(rangeAddress);

            row = sheet.createRow(startRow++);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue(DateUtils.format(entry.getKey(), "yyyy-MM-dd E"));

            row = sheet.createRow(startRow++);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue("班级");

            cell = row.createCell(1, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue("学科");

            cell = row.createCell(2, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue("教师");

            cell = row.createCell(3, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue("备注");

            cell = row.createCell(4, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue("状态");

            Map<Integer, List<ClazzTeachingLogSubjectsVo>> clazzMap = entry.getValue()
                    .stream().collect(Collectors.groupingBy(item -> Integer.parseInt(item.getClazz().getName().replace("班", "")), TreeMap::new, Collectors.toList()));
            for (Map.Entry<Integer, List<ClazzTeachingLogSubjectsVo>> entry1 : clazzMap.entrySet()) {
                List<ClazzTeachingLogSubjectsVo> values = entry1.getValue();
                values.sort(Comparator.comparing(ClazzTeachingLogSubjectsVo::getSortOrder));

                for (ClazzTeachingLogSubjectsVo value : values) {
                    row = sheet.createRow(startRow++);

                    cell = row.createCell(0, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(sheetName.replace("年级", "").concat("< ") + entry1.getKey() + " >班");

                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(value.getSubject() == null ? "" : value.getSubject().getName());

                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(value.getTeacher() == null ? "" : value.getTeacher().getName());

                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(value.getRemarkSubject());

                    cell = row.createCell(4, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(value.getState() == null ? "" : value.getState().getName());
                }
            }


        }

    }
}
