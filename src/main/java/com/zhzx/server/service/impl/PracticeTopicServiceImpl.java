/**
* 项目：中华中学管理平台
* 模型分组：成绩管理
* 模型名称：小题得分情况表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhzx.server.domain.Clazz;
import com.zhzx.server.domain.Practice;
import com.zhzx.server.domain.PracticeTopic;
import com.zhzx.server.domain.User;
import com.zhzx.server.repository.ClazzMapper;
import com.zhzx.server.repository.PracticeMapper;
import com.zhzx.server.repository.PracticeTopicMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.PracticeTopicService;
import com.zhzx.server.util.CellUtils;
import com.zhzx.server.util.StringUtils;
import lombok.SneakyThrows;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PracticeTopicServiceImpl extends ServiceImpl<PracticeTopicMapper, PracticeTopic> implements PracticeTopicService {

    @Resource
    private ClazzMapper clazzMapper;

    @Resource
    private PracticeMapper practiceMapper;

    @Override
    public int updateAllFieldsById(PracticeTopic entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    @Value("${web.upload-path}")
    private String uploadPath;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importExcel(Long schoolyardId, Long academicYearSemesterId, Long gradeId, String fileUrl) {
        String[] items = fileUrl.split("/");
        File file = new File(uploadPath + "\\" + items[items.length - 2] + "\\" + items[items.length - 1]);
        if (!file.exists())
            throw new ApiCode.ApiException(-1, "文件不存在！");
        try {
            // 读取 Excel
            XSSFWorkbook book = new XSSFWorkbook(file);
            XSSFSheet sheet = book.getSheetAt(0);
            file.delete();

            User user = (User) SecurityUtils.getSubject().getPrincipal();
            if (user.getStaffId() <= 0)
                throw new ApiCode.ApiException(-1, "非教职工！");
            List<Clazz> clazzList = this.clazzMapper.selectList(Wrappers.<Clazz>lambdaQuery().eq(Clazz::getAcademicYearSemesterId, academicYearSemesterId)
                    .eq(Clazz::getGradeId, gradeId));
            if (CollectionUtils.isEmpty(clazzList))
                throw new ApiCode.ApiException(-1, "班级不存在！");
            Map<String, Clazz> clazzMap = clazzList.stream().collect(Collectors.toMap(Clazz::getName, o -> o));
            Map<String, List<PracticeTopic>> map = new HashMap<>();
            Set<Long> set = new HashSet<>();

            //获得总行数
            int rowNum = sheet.getPhysicalNumberOfRows();
            // 错误行
            StringBuilder sb = new StringBuilder();
            String errText;
            // 读取单元格数据
            for (int rowIndex = 1; rowIndex < rowNum; rowIndex++) {
                errText = "";
                String practiceName = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(0));
                String topicNumber = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(1));
                String topicName = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(2));
                String topicType = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(3));
                String score = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(4));
                String difficulty = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(5));
                String distinguish = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(6));
                String clazzName = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(7));
                String average = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(8));
                String rate = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(9));
                String gradeAverage = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(10));
                String gradeRate = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(11));
                if (StringUtils.isNullOrEmpty(practiceName)) {
                    errText += "\t练习名称不能为空";
                }
                if (StringUtils.isNullOrEmpty(topicNumber) || StringUtils.isNullOrEmpty(topicType)) {
                    errText += "\t题号，题目类型不能为空";
                }
                if (StringUtils.isNullOrEmpty(score) || StringUtils.isNullOrEmpty(difficulty) || StringUtils.isNullOrEmpty(distinguish)) {
                    errText += "\t分值，难度，区分度不能为空";
                }
                if (StringUtils.isNullOrEmpty(clazzName) || StringUtils.isNullOrEmpty(average)) {
                    errText += "\t班级名称，班均分不能为空";
                }
                if (!clazzMap.containsKey(clazzName)) {
                    errText += "\t班级不存在";
                }
                if (!StringUtils.isNullOrEmpty(errText))
                    sb.append(errText = "第" + rowIndex + "行 " + errText + "\r\n");
                if (StringUtils.isNullOrEmpty(errText)) {
                    Practice practice = this.practiceMapper.selectOne(Wrappers.<Practice>lambdaQuery().eq(Practice::getAcademicYearSemesterId, academicYearSemesterId)
                        .eq(Practice::getGradeId, gradeId)
                        .eq(Practice::getName, practiceName));
                    if (practice == null) {
                        practice = new Practice();
                        practice.setName(practiceName);
                        practice.setAcademicYearSemesterId(academicYearSemesterId);
                        practice.setGradeId(gradeId);
                        practice.setSchoolyardId(schoolyardId);
                        practice.setStaffId(user.getStaffId());
                        this.practiceMapper.insert(practice);
                    } else {
                        practice.setStaffId(user.getStaffId());
                        this.practiceMapper.updateById(practice);
                        set.add(practice.getId());
                    }
                    PracticeTopic practiceTopic = new PracticeTopic();
                    practiceTopic.setPracticeId(practice.getId());
                    practiceTopic.setTopicName(topicName);
                    practiceTopic.setTopicNumber(Integer.parseInt(topicNumber));
                    practiceTopic.setTopicType(topicType);
                    practiceTopic.setScore(new BigDecimal(StringUtils.isNullOrEmpty(score) ? "0" : score));
                    practiceTopic.setDifficulty(new BigDecimal(StringUtils.isNullOrEmpty(difficulty) ? "0" : difficulty));
                    practiceTopic.setDistinguish(new BigDecimal(StringUtils.isNullOrEmpty(distinguish) ? "0" : distinguish));
                    practiceTopic.setClazzId(clazzMap.get(clazzName).getId());
                    practiceTopic.setAverage(new BigDecimal(StringUtils.isNullOrEmpty(average) ? "0" : average));
                    practiceTopic.setRate(new BigDecimal(StringUtils.isNullOrEmpty(rate) ? "0" : rate));
                    practiceTopic.setGradeAverage(new BigDecimal(StringUtils.isNullOrEmpty(gradeAverage) ? "0" : gradeAverage));
                    practiceTopic.setGradeRate(new BigDecimal(StringUtils.isNullOrEmpty(gradeRate) ? "0" : gradeRate));
                    map.computeIfAbsent(practiceName.concat("TH").concat(topicNumber), o -> new ArrayList<>()).add(practiceTopic);
                }
            }
            if (sb.length() != 0) {
                throw new ApiCode.ApiException(-1, "数据有错！\n" + sb);
            }
            map.forEach((k, v) -> v.forEach(item -> item.setRank((int) v.stream().filter(item1 -> item1.getAverage().compareTo(item.getAverage()) > 0).count() + 1)));
            if (!map.isEmpty()) {
                if (!set.isEmpty()) {
                    this.baseMapper.delete(Wrappers.<PracticeTopic>lambdaQuery().in(PracticeTopic::getPracticeId, new ArrayList<>(set)));
                }
                map.forEach((k, v) -> this.baseMapper.batchInsert(v));
            }
        } catch (IOException | InvalidFormatException e) {
            throw new ApiCode.ApiException(-1, "导入数据失败！");
        }
    }

    @Override
    public Map<String, Object> benchTable(IPage<PracticeTopic> page, List<Clazz> clazzList) {
        Map<String, Object> res = new HashMap<>();
        List<PracticeTopic> records = page.getRecords();
        List<Long> idList = records.stream().map(PracticeTopic::getClazzId).collect(Collectors.toList());
        clazzList.removeIf(next -> !idList.contains(next.getId()));
        res.put("column", clazzList);
        Map<Integer, Map<String, Object>> map = new TreeMap<>(Comparator.naturalOrder());
        if (CollectionUtils.isNotEmpty(records)) {
            for (PracticeTopic item : records) {
                if (!map.containsKey(item.getTopicNumber())) {
                    Map<String, Object> map1 = new HashMap<>();
                    map1.put("id", item.getId());
                    map1.put("topicNumber", item.getTopicNumber());
                    map1.put("topicName", item.getTopicName());
                    map1.put("topicType", item.getTopicType());
                    map1.put("score", item.getScore());
                    map1.put("distinguish", item.getDistinguish());
                    map1.put("gradeAverage", item.getGradeAverage());
                    map.put(item.getTopicNumber(), map1);
                }
                Map<String, Object> curr = map.get(item.getTopicNumber());
                curr.put("C" + item.getClazzId() + "_average", item.getAverage());
                curr.put("C" + item.getClazzId() + "_rank", item.getRank());
            }
        }
        res.put("data", map.values());
        return res;
    }

    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public XSSFWorkbook benchTableExportExcel(IPage<PracticeTopic> page) {
        Map<String, Object> map = this.benchTable(page, page.getRecords().stream().map(PracticeTopic::getClazz).collect(Collectors.toList()));
        InputStream is = getClass().getResourceAsStream("/static/templates/小题导出模板.xlsx");
        XSSFWorkbook book = new XSSFWorkbook(is);
        if (CollectionUtils.isNotEmpty(map)) {
            XSSFCellStyle style = book.createCellStyle();
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);

            XSSFSheet sheet = book.getSheetAt(0);
            List<Clazz> column = (List<Clazz>) map.get("column");
            List<Map<String, Object>> data = new ArrayList<>((Collection<Map<String, Object>>) map.get("data"));

            // 生成表头
            XSSFRow row0 = sheet.getRow(0);
            XSSFRow row1 = sheet.getRow(1);
            XSSFCell cell;
            int columnAdd = 5;
            for (Clazz clazz : column) {
                CellRangeAddress rangeAddress = new CellRangeAddress(0, 0, columnAdd, columnAdd + 1);
                sheet.addMergedRegion(rangeAddress);
                cell = row0.createCell(columnAdd, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue(clazz.getName());

                cell = row1.createCell(columnAdd++, CellType.NUMERIC);
                cell.setCellStyle(style);
                cell.setCellValue("均分");

                cell = row1.createCell(columnAdd++, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue("名次");
            }

            XSSFRow rowData;
            for (int i = 0; i < data.size(); i++) {
                int columnIndex = 0;
                Map<String, Object> curr = data.get(i);
                rowData = sheet.createRow(i + 2);

                cell = rowData.createCell(columnIndex++, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue(curr.get("topicNumber").toString());

                cell = rowData.createCell(columnIndex++, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue(curr.get("topicType").toString());

                cell = rowData.createCell(columnIndex++, CellType.NUMERIC);
                cell.setCellStyle(style);
                cell.setCellValue(Double.parseDouble(curr.get("score").toString()));

                cell = rowData.createCell(columnIndex++, CellType.NUMERIC);
                cell.setCellStyle(style);
                cell.setCellValue(Double.parseDouble(curr.get("distinguish").toString()));

                cell = rowData.createCell(columnIndex++, CellType.NUMERIC);
                cell.setCellStyle(style);
                cell.setCellValue(Double.parseDouble(curr.get("gradeAverage").toString()));

                for (Clazz clazz : column) {
                    cell = rowData.createCell(columnIndex++, CellType.NUMERIC);
                    cell.setCellStyle(style);
                    cell.setCellValue(Double.parseDouble(curr.get("C" + clazz.getId() + "_average").toString()));

                    cell = rowData.createCell(columnIndex++, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(curr.get("C" + clazz.getId() + "_rank").toString());
                }
            }

        }
        return book;
    }


}
