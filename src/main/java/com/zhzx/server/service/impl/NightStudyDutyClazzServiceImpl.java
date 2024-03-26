/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：晚自习行政值班班级情况表
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
import com.zhzx.server.domain.*;
import com.zhzx.server.dto.CommentDto;
import com.zhzx.server.dto.NightDutyClassDto;
import com.zhzx.server.dto.NightStudyDutyClazzDto;
import com.zhzx.server.enums.LeaderDutyTypeEnum;
import com.zhzx.server.enums.TeacherDutyTypeEnum;
import com.zhzx.server.repository.*;
import com.zhzx.server.repository.base.NightStudyDutyClazzBaseMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.NightStudyDutyClazzService;
import com.zhzx.server.service.TeacherDutyService;
import com.zhzx.server.util.DateUtils;
import com.zhzx.server.vo.NightStudyDutyClazzVo;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NightStudyDutyClazzServiceImpl extends ServiceImpl<NightStudyDutyClazzMapper, NightStudyDutyClazz> implements NightStudyDutyClazzService {

    @Resource
    private NightStudyDutyClazzDeductionMapper nightStudyDutyClazzDeductionMapper;
    @Resource
    private TeacherDutyService teacherDutyService;
    @Resource
    private TeacherDutyClazzMapper teacherDutyClazzMapper;
    @Resource
    private LeaderDutyMapper leaderDutyMapper;
    @Resource
    private NightStudyDutyMapper nightStudyDutyMapper;
    @Resource
    private NightStudyMapper nightStudyMapper;
    @Resource
    private NightStudyAttendanceMapper nightStudyAttendanceMapper;
    @Override
    public int updateAllFieldsById(NightStudyDutyClazz entity) {
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
    public boolean saveBatch(Collection<NightStudyDutyClazz> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(NightStudyDutyClazzBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    @Transactional
    public NightDutyClassDto createOrUpdate(NightDutyClassDto nightDutyClassDto) {
        if(nightDutyClassDto.getNightStudyDutyClazzId() == null){
            throw new ApiCode.ApiException(-5,"nightStudyDutyClazzId 必传");
        }else{
            NightStudyDutyClazz old =  this.baseMapper.selectById(nightDutyClassDto.getNightStudyDutyClazzId());
            NightStudyDutyClazz nightStudyDutyClazz = new NightStudyDutyClazz();
            nightStudyDutyClazz.setId(nightDutyClassDto.getNightStudyDutyClazzId());
            if(nightDutyClassDto.getTeacherName() != null){
                nightStudyDutyClazz.setTeacher(nightDutyClassDto.getTeacherName());
            }
            nightStudyDutyClazz.setScore(nightDutyClassDto.getScore());
            nightStudyDutyClazz.setShouldStudentCount(nightDutyClassDto.getShouldStudentCount());
            nightStudyDutyClazz.setActualStudentCount(nightDutyClassDto.getActualStudentCount());
            nightStudyDutyClazz.setDutySituation(nightDutyClassDto.getDutySituation());
            this.baseMapper.updateById(nightStudyDutyClazz);

            //同步修改教师端，学生端应到实到数量
            if(!(nightDutyClassDto.getShouldStudentCount() == 0 && nightDutyClassDto.getActualStudentCount() == 0) && old != null){
                nightStudyMapper.updateStudentNum(old.getClazzId(),nightDutyClassDto.getTime(),
                        nightDutyClassDto.getShouldStudentCount(),nightDutyClassDto.getActualStudentCount(),nightDutyClassDto.getTeacherDutyTypeEnum().toString());

                nightStudyAttendanceMapper.updateStudentNum(old.getClazzId(),nightDutyClassDto.getTime(),
                        nightDutyClassDto.getShouldStudentCount(),nightDutyClassDto.getActualStudentCount(),nightDutyClassDto.getTeacherDutyTypeEnum().toString());
            }
        }
        nightStudyDutyClazzDeductionMapper.delete(Wrappers.<NightStudyDutyClazzDeduction>lambdaQuery()
                .eq(NightStudyDutyClazzDeduction::getNightStudyDutyClazzId,nightDutyClassDto.getNightStudyDutyClazzId())
        );
        if(CollectionUtils.isNotEmpty(nightDutyClassDto.getNightStudyDutyClazzDeductions())){
            nightDutyClassDto.getNightStudyDutyClazzDeductions().stream().forEach(nightStudyDutyClazzDeduction -> {
                nightStudyDutyClazzDeduction.setNightStudyDutyClazzId(nightDutyClassDto.getNightStudyDutyClazzId());
                nightStudyDutyClazzDeduction.setDefault().validate(true);
            });
            nightStudyDutyClazzDeductionMapper.batchInsert( nightDutyClassDto.getNightStudyDutyClazzDeductions());
        }
        if(nightDutyClassDto.getTeacherNewId() != null){
            teacherDutyService.updateTeacher(nightDutyClassDto);
        }
        return nightDutyClassDto;
    }

    @Override
    public IPage pageDetail(IPage leaderPage, TeacherDutyTypeEnum dutyType, String name, String clazzName,Date startTime, Date endTime, Long gradeId, Long clazzId, Long schoolyardId) {
        List<NightStudyDutyClazzDto> nightStudyDutyClazzDtoList =  this.baseMapper.pageDetail(leaderPage,dutyType.toString(),name,clazzName,startTime,endTime,gradeId,clazzId, schoolyardId);
        List<NightStudyDutyClazzVo> resultList = new ArrayList<>();
        nightStudyDutyClazzDtoList.stream().forEach(nightStudyDutyClazzDto -> {
            List<NightStudyDutyClazzVo> nightStudyDutyClazzVos = nightStudyDutyClazzDto.getNightStudyDutyClazzVoList();
            if(nightStudyDutyClazzVos != null ){
                if(nightStudyDutyClazzVos.size() == 2){
                    if(TeacherDutyTypeEnum.STAGE_ONE.equals(dutyType)){
                        resultList.add(nightStudyDutyClazzVos.get(1));
                    }else{
                        resultList.add(nightStudyDutyClazzVos.get(0));
                    }
                }else if(nightStudyDutyClazzVos.size() == 1){
                    List<NightStudyDuty> nightStudyDuties = nightStudyDutyMapper.selectList(Wrappers.<NightStudyDuty>lambdaQuery()
                            .ge(NightStudyDuty::getStartTime,DateUtils.parse(nightStudyDutyClazzDto.getGroupTime().split(",")[1]+" " + "00:00:00"))
                            .le(NightStudyDuty::getStartTime,DateUtils.parse(nightStudyDutyClazzDto.getGroupTime().split(",")[1]+" " + "23:59:59"))
                            .orderByDesc(NightStudyDuty::getStartTime,NightStudyDuty::getId)
                    );
                    if(TeacherDutyTypeEnum.STAGE_ONE.equals(dutyType) && nightStudyDuties.get(1).getId().equals(nightStudyDutyClazzVos.get(0).getNightStudyDutyId())){
                        resultList.add(nightStudyDutyClazzVos.get(0));
                    }else if(TeacherDutyTypeEnum.STAGE_TWO.equals(dutyType) && nightStudyDuties.get(0).getId().equals(nightStudyDutyClazzVos.get(0).getNightStudyDutyId())){
                        resultList.add(nightStudyDutyClazzVos.get(0));
                    }
                }
            }
        });

        leaderPage.setRecords(resultList);
        return leaderPage;
    }

    @Override
    public Integer leaderConfirm(Date time, Long clazzId,TeacherDutyTypeEnum dutyType) {
        Long id = teacherDutyClazzMapper.getTeacherDutyId(time,clazzId,dutyType.toString());
        return teacherDutyClazzMapper.leaderConfirm(id);
    }

    private int getOrder(String gradeName) {
        if (gradeName.contains("高一"))
            return 1;
        else if (gradeName.contains("高二"))
            return 2;
        else
            return 3;
    }

    @Override
    @SneakyThrows
    @SuppressWarnings({"unchecked"})
    public XSSFWorkbook exportExcel(TeacherDutyTypeEnum dutyType, String name, String clazzName,Date startTime, Date endTime, Long gradeId, Long clazzId, Long schoolyardId) {
        InputStream is = getClass().getResourceAsStream("/static/templates/晚自习值班情况.xlsx");
        XSSFWorkbook book = new XSSFWorkbook(is);
        IPage<?> page = new Page<>(1, 9999);
//        IPage<?> page1 = new Page<>(1, 9999);
        IPage<NightDutyClassDto> teacher = this.teacherDutyService.pageDetail(page,dutyType,name,clazzName,startTime,endTime,gradeId,clazzId, schoolyardId);
//        IPage<NightStudyDutyClazzVo> leader = this.pageDetail(page1,dutyType,name,clazzName,startTime,endTime,gradeId,clazzId, schoolyardId);
        List<NightDutyClassDto> nightDutyClassDtoList = teacher.getRecords();
//        List<NightStudyDutyClazzVo> nightStudyDutyClazzVoList = leader.getRecords();

        XSSFCellStyle style = book.createCellStyle();
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        XSSFSheet sheet = book.getSheetAt(0);
        XSSFRow row;
        XSSFCell cell;

//        Map<String, NightStudyDutyClazzVo> nightStudyDutyClazzVoMap = nightStudyDutyClazzVoList.stream().collect(
//                Collectors.toMap(item -> item.getGradeName().concat(item.getClazzName()).concat(DateUtils.format(item.getStartTime(),"yyyy-MM-dd")), Function.identity()));
        if (CollectionUtils.isNotEmpty(nightDutyClassDtoList)) {
            nightDutyClassDtoList.sort(Comparator
                    .comparing((NightDutyClassDto item) -> this.getOrder(item.getGradeName()))
                    .thenComparing(item -> Integer.parseInt(item.getClazzName().replace("班", ""))));
            for (int i = 0; i < nightDutyClassDtoList.size(); ++i) {
                NightDutyClassDto nightDutyClassDto = nightDutyClassDtoList.get(i);
                int columnAdd = 0;
                row = sheet.createRow(i + 1);

                String dateStr = DateUtils.region(nightDutyClassDto.getStartTime(), nightDutyClassDto.getEndTime());
                cell = row.createCell(columnAdd++, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue(dateStr.substring(11));

                String date = dateStr.substring(0, 10);
                cell = row.createCell(columnAdd++, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue(date);

                cell = row.createCell(columnAdd++, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue(DateUtils.getWeek(nightDutyClassDto.getStartTime()));

                cell = row.createCell(columnAdd++, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue(nightDutyClassDto.getGradeName());

                cell = row.createCell(columnAdd++, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue(nightDutyClassDto.getClazzName());

                cell = row.createCell(columnAdd++, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue(nightDutyClassDto.getTeacherName());

                cell = row.createCell(columnAdd++, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue(nightDutyClassDto.getShouldStudentCount() == null ? "": nightDutyClassDto.getShouldStudentCount().toString());

                cell = row.createCell(columnAdd++, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue(nightDutyClassDto.getActualStudentCount() == null ? "" : nightDutyClassDto.getActualStudentCount().toString());


                List<NightStudyAttendance> nightStudyAttendances = nightDutyClassDto.getNightStudyAttendances();
                if (CollectionUtils.isNotEmpty(nightStudyAttendances)) {

                    cell = row.createCell(columnAdd++, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(nightStudyAttendances.stream()
                            .filter(item -> item.getClassify().contains("缺席"))
                            .map(item -> item.getStudent().getName())
                            .collect(Collectors.joining(",")));

                    cell = row.createCell(columnAdd++, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(nightStudyAttendances.stream()
                            .filter(item -> item.getClassify().contains("迟到"))
                            .map(item -> item.getStudent().getName())
                            .collect(Collectors.joining(",")));

                    cell = row.createCell(columnAdd++, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(nightStudyAttendances.stream()
                            .filter(item -> item.getClassify().contains("中途出入教室"))
                            .map(item -> item.getStudent().getName())
                            .collect(Collectors.joining(",")));

                    cell = row.createCell(columnAdd++, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(nightStudyAttendances.stream()
                            .filter(item -> item.getClassify().contains("其他"))
                            .map(item -> item.getStudent().getName())
                            .collect(Collectors.joining(",")));
                }else{
                    cell = row.createCell(columnAdd++, CellType.STRING);
                    cell.setCellStyle(style);
                    cell = row.createCell(columnAdd++, CellType.STRING);
                    cell.setCellStyle(style);
                    cell = row.createCell(columnAdd++, CellType.STRING);
                    cell.setCellStyle(style);
                    cell = row.createCell(columnAdd++, CellType.STRING);
                    cell.setCellStyle(style);
                }

                List<CommentDto> commentDtoList = nightDutyClassDto.getCommentDtoList();
                if (CollectionUtils.isNotEmpty(commentDtoList)) {
                    cell = row.createCell(columnAdd++, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(commentDtoList.stream()
                            .map(Comment::getContent)
                            .collect(Collectors.joining(",")));
                }else{
                    cell = row.createCell(columnAdd++, CellType.STRING);
                    cell.setCellStyle(style);
                }

//                String key = nightDutyClassDto.getGradeName() + nightDutyClassDto.getClazzName() + date;
//                if (nightStudyDutyClazzVoMap.containsKey(key)) {
//                    NightStudyDutyClazzVo nightStudyDutyClazzVo = nightStudyDutyClazzVoMap.get(key);
                if (nightDutyClassDto.getScore() != null) {
                    cell = row.createCell(columnAdd++, CellType.NUMERIC);
                    cell.setCellStyle(style);
                    cell.setCellValue(nightDutyClassDto.getScore());
                } else {
                    cell = row.createCell(columnAdd++, CellType.STRING);
                    cell.setCellStyle(style);
                }

                    List<NightStudyDutyClazzDeduction> nightStudyDutyClazzDeductionList = nightDutyClassDto.getNightStudyDutyClazzDeductions();
                    if (CollectionUtils.isNotEmpty(nightStudyDutyClazzDeductionList)) {
                        cell = row.createCell(columnAdd++, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue(nightStudyDutyClazzDeductionList.stream()
                                .map(NightStudyDutyClazzDeduction::getDescription)
                                .collect(Collectors.joining(",")));
                    } else {
                        cell = row.createCell(columnAdd++, CellType.STRING);
                        cell.setCellStyle(style);
                    }

                    cell = row.createCell(columnAdd, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(nightDutyClassDto.getLeaderName());
//                }else{
//                    cell = row.createCell(columnAdd++, CellType.STRING);
//                    cell.setCellStyle(style);
//                    cell = row.createCell(columnAdd++, CellType.STRING);
//                    cell.setCellStyle(style);
//                    cell = row.createCell(columnAdd++, CellType.STRING);
//                    cell.setCellStyle(style);
//                }
            }
        }

        return book;
    }

    /**
     * 刷新，即重新查询晚自习管理的老师值班列表
     * @return
     */
    @Override
    public Object refresh() {
        Map<TeacherDutyTypeEnum, Map<Long, NightStudyDutyClazz>> map = new HashMap<>();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Staff staff = user.getStaff();
        //通过教职工表的id获取当前日期所需要值班的领导 ----即获取领导值班表的id
        LeaderDuty leaderDuty = this.leaderDutyMapper.selectOne(Wrappers.<LeaderDuty>lambdaQuery()
                .eq(LeaderDuty::getDutyType, LeaderDutyTypeEnum.NIGHT_STUDY)
                .eq(LeaderDuty::getLeaderId, staff.getId())
                .apply("to_days(start_time)" + "=" + "to_days({0})", new Date()));
        if (leaderDuty == null) return map;
        //通过领导值班表的id获取晚自习行政值班表对象 ----即两个阶段的晚自习值班信息
        List<NightStudyDuty> nightStudyDutyList = this.nightStudyDutyMapper.selectList(Wrappers.<NightStudyDuty>lambdaQuery()
                .eq(NightStudyDuty::getLeaderDutyId, leaderDuty.getId())
                .orderByAsc(NightStudyDuty::getStartTime));
        if (nightStudyDutyList == null || nightStudyDutyList.size() != 2) return map;
        //循环两次
        for (int i = 0; i < 2; i++) {
            List<NightStudyDutyClazz> nightStudyDutyClazzes = this.baseMapper.selectList(Wrappers.<NightStudyDutyClazz>lambdaQuery()
                    .eq(NightStudyDutyClazz::getNightStudyDutyId, nightStudyDutyList.get(i).getId()));
            if (i == 0) {
                map.put(TeacherDutyTypeEnum.STAGE_ONE, nightStudyDutyClazzes.stream()
                        .collect(Collectors.toMap(NightStudyDutyClazz::getClazzId, Function.identity())));
            } else {
                map.put(TeacherDutyTypeEnum.STAGE_TWO, nightStudyDutyClazzes.stream()
                        .collect(Collectors.toMap(NightStudyDutyClazz::getClazzId, Function.identity())));
            }
        }
        return map;
    }

}
