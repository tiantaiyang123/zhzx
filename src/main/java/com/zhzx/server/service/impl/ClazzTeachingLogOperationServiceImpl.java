/**
 * 项目：中华中学管理平台
 * 模型分组：学生管理
 * 模型名称：班级教学日志作业量反馈表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import java.io.InputStream;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.Subject;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.repository.SubjectMapper;
import com.zhzx.server.util.TwxUtils;
import com.zhzx.server.vo.ClazzTeachingLogOperationVo;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhzx.server.service.ClazzTeachingLogOperationService;
import com.zhzx.server.repository.ClazzTeachingLogOperationMapper;
import com.zhzx.server.repository.base.ClazzTeachingLogOperationBaseMapper;
import com.zhzx.server.domain.ClazzTeachingLogOperation;
import com.zhzx.server.rest.req.ClazzTeachingLogOperationParam;

@Service
public class ClazzTeachingLogOperationServiceImpl extends ServiceImpl<ClazzTeachingLogOperationMapper, ClazzTeachingLogOperation> implements ClazzTeachingLogOperationService {

    @Resource
    private SubjectMapper subjectMapper;

    @Override
    public int updateAllFieldsById(ClazzTeachingLogOperation entity) {
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
    public boolean saveBatch(Collection<ClazzTeachingLogOperation> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(ClazzTeachingLogOperationBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateList(List<ClazzTeachingLogOperation> entityList) {
        for (ClazzTeachingLogOperation clazzTeachingLogOperation : entityList) {
            this.updateById(clazzTeachingLogOperation);
        }
        return true;
    }

    @Override
    @SneakyThrows
    public XSSFWorkbook exportExcel(Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long clazzId, Integer week, Date startTime, Date endTime) {
        List<ClazzTeachingLogOperationVo> clazzTeachingLogOperationVoList = this.baseMapper.pageDetail(new Page<>(1, 99999), schoolyardId, academicYearSemesterId, gradeId, clazzId, week, startTime, endTime);
        InputStream is = getClass().getResourceAsStream("/static/templates/作业量统计.xlsx");
        XSSFWorkbook book = new XSSFWorkbook(is);

        XSSFCellStyle style = book.createCellStyle();
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        if (CollectionUtils.isNotEmpty(clazzTeachingLogOperationVoList)) {
            Map<String, List<ClazzTeachingLogOperationVo>> map = clazzTeachingLogOperationVoList
                    .stream()
                    .collect(Collectors.groupingBy(item -> item.getClazz().getGrade().getName()));
            List<Subject> subjects = subjectMapper.selectList(Wrappers.<Subject>lambdaQuery().eq(Subject::getIsMain, YesNoEnum.YES));
            Map<String, Subject> subjectMap = subjects.stream().collect(Collectors.toMap(Subject::getName, Function.identity()));
            for (Map.Entry<String, List<ClazzTeachingLogOperationVo>> entry : map.entrySet()) {
                putData(book, style, entry.getKey(), entry.getValue(), subjectMap);
            }
        }
        return book;
    }

    private void putData(XSSFWorkbook book, XSSFCellStyle style, String sheetName, List<ClazzTeachingLogOperationVo> data, Map<String, Subject> subjectMap) {
        XSSFSheet sheet = book.getSheet(sheetName);
        XSSFRow row;
        XSSFCell cell;
        int startRow = 2;
        Map<Integer, List<ClazzTeachingLogOperationVo>> clazzMap = data
                .stream()
                .collect(Collectors.groupingBy(item -> Integer.parseInt(item.getClazz().getName().replace("班", "")), TreeMap::new, Collectors.toList()));
        for (Map.Entry<Integer, List<ClazzTeachingLogOperationVo>> entry : clazzMap.entrySet()) {
            row = sheet.createRow(startRow++);

            Map<String, List<ClazzTeachingLogOperationVo>> clazzSubjectMap = entry.getValue().stream().collect(Collectors.groupingBy(item -> item.getSubject().getName()));
            String[] arr = TwxUtils.arr;

            cell = row.createCell(0, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue(sheetName.replace("年级", "").concat("< ") + entry.getKey() + " >班");

            for (int i = 1; i <= arr.length; i++) {
                cell = row.createCell(i, CellType.NUMERIC);
                cell.setCellStyle(style);
                if (clazzSubjectMap.containsKey(arr[i - 1])) {
                    cell.setCellValue(clazzSubjectMap.get(arr[i - 1]).stream().collect(Collectors.averagingInt(ClazzTeachingLogOperationVo::getOperationTime)));
                } else {
                    cell.setCellValue(0);
                }
            }
        }
    }
}
