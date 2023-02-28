/**
 * 项目：中华中学管理平台
 * 模型分组：学生管理
 * 模型名称：日常考勤表
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
import com.zhzx.server.domain.Clazz;
import com.zhzx.server.domain.DailyAttendance;
import com.zhzx.server.domain.DailyAttendanceSub;
import com.zhzx.server.repository.ClazzMapper;
import com.zhzx.server.repository.DailyAttendanceMapper;
import com.zhzx.server.repository.DailyAttendanceSubMapper;
import com.zhzx.server.repository.base.DailyAttendanceBaseMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.DailyAttendanceService;
import com.zhzx.server.util.DateUtils;
import com.zhzx.server.util.StringUtils;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DailyAttendanceServiceImpl extends ServiceImpl<DailyAttendanceMapper, DailyAttendance> implements DailyAttendanceService {

    @Resource
    private ClazzMapper clazzMapper;

    @Resource
    private DailyAttendanceSubMapper dailyAttendanceSubMapper;

    @Override
    public int updateAllFieldsById(DailyAttendance entity) {
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
    public boolean saveBatch(Collection<DailyAttendance> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(DailyAttendanceBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    public IPage<DailyAttendance> searchDailyAttendance(IPage<DailyAttendance> page, Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long clazzId, Integer week, String registerDateFrom, String registerDateTo, String orderByClause) {
        return this.getBaseMapper().searchDailyAttendance(page, schoolyardId, academicYearSemesterId, gradeId, clazzId, week, registerDateFrom, registerDateTo, orderByClause);
    }

    @Override
    public List<Map<String,Object>> searchStatistics(Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long clazzId, Integer week, List<String> classifies ,Date registerDateFrom, Date registerDateTo, String orderByClause) {
        List<Map<String,Object>> list = this.baseMapper.searchStatisticsGroupByDate(schoolyardId, academicYearSemesterId, gradeId, clazzId, week, classifies,registerDateFrom, registerDateTo, orderByClause);
        return parse(list,registerDateFrom,registerDateTo);
    }

    @Override
    public List<Map<String, Object>> searchStatisticsGroupByClazz(Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long clazzId, Integer week, List<String> classifies, Date registerDateFrom, Date registerDateTo, String orderByClause) {
        List<Map<String,Object>> list = this.baseMapper.searchStatisticsGroupByClazz(schoolyardId, academicYearSemesterId, gradeId, clazzId, week, classifies,registerDateFrom, registerDateTo, orderByClause);
        List<Clazz> clazzList = clazzMapper.selectList(Wrappers.<Clazz>lambdaQuery()
                .eq(null != academicYearSemesterId,Clazz::getAcademicYearSemesterId,academicYearSemesterId)
                .eq(null != gradeId,Clazz::getGradeId,gradeId)
                .eq(null != clazzId,Clazz::getId,clazzId)
                .orderByAsc(Clazz::getId)
        );
        List<Map<String,Object>> mapList = new ArrayList<>();
        for(Clazz clazz : clazzList){
            Boolean flag = false;
            for (int i = 0; i < list.size(); i++) {
                if(clazz.getName().equals(list.get(i).get("clazzName"))){
                    flag = true;
                    mapList.add(list.get(i));
                    break;
                }
            }
            if(!flag){
                Map<String,Object> map = new HashMap<>();
                map.put("clazzName",clazz.getName());
                mapList.add(map);
            }
        }
        return mapList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DailyAttendanceSub appSave(DailyAttendance entity) {
        DailyAttendanceSub dailyAttendanceSub = this.dailyAttendanceSubMapper.selectOne(Wrappers.<DailyAttendanceSub>lambdaQuery()
                .eq(DailyAttendanceSub::getClazzId, entity.getClazzId())
                .apply("to_days(register_date)" + "=" + "to_days({0})", entity.getRegisterDate()));
        Clazz clazz = this.clazzMapper.selectById(entity.getClazzId());

        List<Long> studentIds = new ArrayList<>();

        if (!StringUtils.isNullOrEmpty(entity.getStudentIds())) {
            studentIds = Arrays.stream(entity.getStudentIds().split(",")).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());

            List<DailyAttendance> already = this.baseMapper.selectList(Wrappers.<DailyAttendance>lambdaQuery()
                    .select(DailyAttendance::getStudentId)
                    .eq(DailyAttendance::getClazzId, entity.getClazzId())
                    .apply("to_days(register_date)" + "=" + "to_days({0})", entity.getRegisterDate())
                    .in(DailyAttendance::getStudentId, studentIds));
            if (CollectionUtils.isNotEmpty(already)) throw new ApiCode.ApiException(-1, "学生：" + already.stream().map(item -> item.getStudent().getName()).collect(Collectors.joining(" ")) + "重复");
        }

        if (dailyAttendanceSub == null) {
            dailyAttendanceSub = new DailyAttendanceSub();
            dailyAttendanceSub.setRegisterDate(entity.getRegisterDate());
            dailyAttendanceSub.setClazzId(entity.getClazzId());
            dailyAttendanceSub.setWeek(entity.getWeek());
            dailyAttendanceSub.setAcademicYearSemesterId(entity.getAcademicYearSemesterId());
            dailyAttendanceSub.setShouldNum(clazz.getStudentCount());
            dailyAttendanceSub.setActualNum(clazz.getStudentCount() - studentIds.size());
            dailyAttendanceSub.setDefault().validate(true);
            this.dailyAttendanceSubMapper.insert(dailyAttendanceSub);
        } else {
            this.dailyAttendanceSubMapper.update(null, Wrappers.<DailyAttendanceSub>lambdaUpdate()
                    .set(DailyAttendanceSub::getActualNum, dailyAttendanceSub.getActualNum() - studentIds.size())
                    .eq(DailyAttendanceSub::getId, dailyAttendanceSub.getId()));
        }

        for (Long studentId : studentIds) {
            entity.setId(null);
            entity.setStudentId(studentId);
            entity.setDefault().validate(true);
            this.baseMapper.insert(entity);
        }

        return this.baseMapper.selectDetailById(dailyAttendanceSub.getId());
    }

    @Override
    public IPage<DailyAttendanceSub> commonQuery(IPage<DailyAttendanceSub> page, Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long clazzId, Integer week, String registerDateFrom, String registerDateTo) {
        return this.getBaseMapper().commonQuery(page, schoolyardId, academicYearSemesterId, gradeId, clazzId, week, registerDateFrom, registerDateTo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer appDelete(Long id) {
        DailyAttendance dailyAttendance = this.baseMapper.selectById(id);
        if (dailyAttendance == null) throw new ApiCode.ApiException(-1, "无效ID");
        DailyAttendanceSub dailyAttendanceSub = this.dailyAttendanceSubMapper.selectOne(Wrappers.<DailyAttendanceSub>lambdaQuery()
                .select(DailyAttendanceSub::getId, DailyAttendanceSub::getActualNum)
                .eq(DailyAttendanceSub::getClazzId, dailyAttendance.getClazzId())
                .apply("to_days(register_date)" + "=" + "to_days({0})", dailyAttendance.getRegisterDate()));

        this.baseMapper.deleteById(id);
        this.dailyAttendanceSubMapper.update(null, Wrappers.<DailyAttendanceSub>lambdaUpdate()
                .set(DailyAttendanceSub::getActualNum, dailyAttendanceSub.getActualNum() + 1)
                .eq(DailyAttendanceSub::getId, dailyAttendanceSub.getId()));
        return 1;
    }

    @Override
    @SneakyThrows
    public XSSFWorkbook exportExcel(Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long clazzId, Integer week, String registerDateFrom, String registerDateTo) {
        XSSFWorkbook book = new XSSFWorkbook();

        XSSFCellStyle style = book.createCellStyle();
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        IPage<DailyAttendanceSub> page = this.commonQuery(new Page<>(1, 99999), schoolyardId, academicYearSemesterId, gradeId, clazzId, week, registerDateFrom, registerDateTo);
        List<DailyAttendanceSub> dailyAttendanceSubs = page.getRecords();
        if (CollectionUtils.isNotEmpty(dailyAttendanceSubs)) {
            XSSFSheet sheet = book.createSheet("sheet");
            XSSFRow row;
            XSSFCell cell;
            int startRow = 0;

            dailyAttendanceSubs.sort((a, b) -> {
                Long ga = a.getClazz().getGradeId();
                Long gb = b.getClazz().getGradeId();
                int res1 = ga.compareTo(gb);
                if (res1 != 0) return res1;
                int res = org.apache.commons.lang3.time.DateUtils.truncatedCompareTo(a.getRegisterDate(), b.getRegisterDate(), Calendar.DATE);
                return res != 0 ? res : Integer.valueOf(a.getClazz().getName().replace("班", "")).compareTo(Integer.valueOf(b.getClazz().getName().replace("班", "")));
            });

            row = sheet.createRow(startRow++);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue("年级");

            cell = row.createCell(1, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue("日期");

            cell = row.createCell(2, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue("班级");

            cell = row.createCell(3, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue("应到");

            cell = row.createCell(4, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue("实到");

            cell = row.createCell(5, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue("病假名单");

            cell = row.createCell(6, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue("病假人数");

            cell = row.createCell(7, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue("事假名单");

            cell = row.createCell(8, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue("事假人数");

            String[] arr = {"病假", "事假"};

            for (DailyAttendanceSub dailyAttendanceSub : dailyAttendanceSubs) {
                row = sheet.createRow(startRow++);
                cell = row.createCell(0, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue(dailyAttendanceSub.getClazz().getGrade().getName());

                cell = row.createCell(1, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue(DateUtils.format(dailyAttendanceSub.getRegisterDate(), "yyyy-MM-dd E"));

                cell = row.createCell(2, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue(dailyAttendanceSub.getClazz().getName());

                cell = row.createCell(3, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue(dailyAttendanceSub.getShouldNum());

                cell = row.createCell(4, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue(dailyAttendanceSub.getActualNum());

                if (CollectionUtils.isNotEmpty(dailyAttendanceSub.getDailyAttendanceList())) {
                    String[] arr1 = Arrays.stream(arr).map(item ->
                            dailyAttendanceSub.getDailyAttendanceList()
                                    .stream()
                                    .filter(item1 -> item1.getClassify().equals(item))
                                    .map(item1 -> item1.getStudent().getName())
                                    .collect(Collectors.joining(","))
                    ).toArray(String[]::new);

                    for (int i = 0; i < arr1.length; i++) {

                        if (!StringUtils.isNullOrEmpty(arr1[i])) {
                            int st = 5 + (i << 1);
                            cell = row.createCell(st, CellType.STRING);
                            cell.setCellStyle(style);
                            cell.setCellValue(arr1[i]);

                            cell = row.createCell(st + 1, CellType.STRING);
                            cell.setCellStyle(style);
                            cell.setCellValue(arr1[i].split(",").length);
                        }

                    }
                }
            }
        }

        return book;
    }

    private List<Map<String, Object>> parse(List<Map<String, Object>> list,Date registerDateFrom, Date registerDateTo) {
        List<Map<String, Object>> newList = new ArrayList<>();
        if(CollectionUtils.isEmpty(list) && registerDateFrom == null){
            return list;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(registerDateFrom);
        while (calendar.getTime().compareTo(registerDateTo) <= 0 ){
            Boolean flag = false;
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).get("registerDate").toString().equals(DateUtils.format(calendar.getTime(),"yyyy-MM-dd"))){
                    flag = true;
                    newList.add(list.get(i));
                    break;
                }
            }
            if(!flag){
                Map<String, Object> map = new HashMap<>();
                map.put("registerDate",DateUtils.format(calendar.getTime(),"yyyy-MM-dd"));
                newList.add(map);
            }
            calendar.add(Calendar.DATE,1);
        }
        return newList;
    }
}
