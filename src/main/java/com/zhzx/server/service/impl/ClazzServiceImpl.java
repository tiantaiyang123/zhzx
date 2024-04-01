/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：班级表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.*;
import com.zhzx.server.enums.*;
import com.zhzx.server.repository.*;
import com.zhzx.server.repository.base.ClazzBaseMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.ClazzService;
import com.zhzx.server.service.StudentService;
import com.zhzx.server.util.CellUtils;
import com.zhzx.server.util.StringUtils;
import com.zhzx.server.vo.ClazzVo;
import com.zhzx.server.vo.GradeVo;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz> implements ClazzService {
    @Resource
    private AcademicYearSemesterMapper academicYearSemesterMapper;

    @Resource
    private StaffClazzAdviserMapper staffClazzAdviserMapper;

    @Resource
    private StaffMapper staffMapper;

    @Resource
    private GradeMapper gradeMapper;

    @Resource
    private SubjectMapper subjectMapper;

    @Resource
    private DailyAttendanceMapper dailyAttendanceMapper;

    @Resource
    private NightStudyAttendanceMapper nightStudyAttendanceMapper;

    @Value("${web.upload-path}")
    private String uploadPath;

    @Resource
    private StudentService studentService;

    @Override
    public int updateAllFieldsById(Clazz entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    @Override
    public IPage<ClazzVo> pageDetail(IPage<ClazzVo> page, QueryWrapper<Clazz> wrapper) {
        return this.getBaseMapper().pageDetail(page, wrapper);
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
    public boolean saveBatch(Collection<Clazz> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(ClazzBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    public List<ClazzVo> getListTeacherDutyClazz(Date date, TeacherDutyTypeEnum dutyType) {
        List<ClazzVo> clazzVoList = this.baseMapper.clazzWithDutyTeacher(date, dutyType.toString());
        clazzVoList = clazzVoList.stream().filter(clazzVo -> (clazzVo.getTeacherDutyName() != null && clazzVo.getTeacherDutyName() != "")).collect(Collectors.toList());
        return clazzVoList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importExcel(Long schoolyardId, Long academicYearSemesterId, String fileUrl) {
        if (fileUrl == null || fileUrl.equals(""))
            throw new ApiCode.ApiException(-1, "没有上传文件！");
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        String[] items = fileUrl.split("/");
        File file = new File(uploadPath + "\\" + items[items.length - 2] + "\\" + items[items.length - 1]);
        if (!file.exists())
            throw new ApiCode.ApiException(-1, "文件不存在！");
        AcademicYearSemester academicYearSemesterCurr = user.getAcademicYearSemester();
        // 当前学年之前所有班级
//        List<AcademicYearSemester> academicYearSemesters = this.academicYearSemesterMapper.selectList(Wrappers.<AcademicYearSemester>lambdaQuery().lt(AcademicYearSemester::getStartTime, academicYearSemesterCurr.getStartTime()));
//        List<Clazz> clazzList = this.baseMapper.selectList(
//                Wrappers.<Clazz>lambdaQuery()
//                        .in(Clazz::getAcademicYearSemesterId, academicYearSemesters.stream().map(AcademicYearSemester::getId).collect(Collectors.toList())));
//        if (CollectionUtils.isNotEmpty(clazzList)) {
//            // 当前学年之前的班主任状态都更新为NO
//            this.staffClazzAdviserMapper.update(null,
//                    Wrappers.<StaffClazzAdviser>lambdaUpdate()
//                            .set(StaffClazzAdviser::getIsCurrent, YesNoEnum.NO)
//                            .eq(StaffClazzAdviser::getIsCurrent, YesNoEnum.YES).eq(StaffClazzAdviser::getClazzId, clazzList.get(0).getId()));
//            this.staffClazzAdviserMapper.update(null,
//                    Wrappers.<StaffClazzAdviser>lambdaUpdate()
//                            .set(StaffClazzAdviser::getIsCurrent, YesNoEnum.NO)
//                            .eq(StaffClazzAdviser::getIsCurrent, YesNoEnum.YES).in(StaffClazzAdviser::getClazzId, clazzList));
//        }
        List<Subject> subjectList = this.subjectMapper.selectList(new QueryWrapper<>());
        Map<String, Long> subjectMap = subjectList.stream().collect(Collectors.toMap(Subject::getName, Subject::getId));
        try {
            // 读取 Excel
            XSSFWorkbook book = new XSSFWorkbook(file);
            XSSFSheet sheet = book.getSheetAt(0);
            file.delete();
            //获得总行数
            int rowNum = sheet.getPhysicalNumberOfRows();
            // 错误行
            StringBuilder sb = new StringBuilder();
            String errText;
            // 读取单元格数据
            for (int rowIndex = 1; rowIndex < rowNum; rowIndex++) {
                errText = "";
                String gradeName = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(2));
                String clazzName = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(3));
                String headerTeacher = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(6));
                String clazzNature = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(7));
                String clazzDivision = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(8));
//                String subjectLevel = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(5));
                String clazzLevel = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(9));
                if (StringUtils.isNullOrEmpty(gradeName)) {
                    errText += "\t年级不能为空";
                }
                if (StringUtils.isNullOrEmpty(clazzName)) {
                    errText += "\t班级不能为空";
                }
                Staff staff = null;
                if (!StringUtils.isNullOrEmpty(headerTeacher)) {
                    headerTeacher = headerTeacher.trim();
                    staff = this.staffMapper.selectOne(Wrappers.<Staff>lambdaQuery().eq(Staff::getName, headerTeacher));
                    if (staff == null) {
                        errText += "\t不存在班主任";
                    }
                }
                QueryWrapper<Grade> gradeQueryWrapper = new QueryWrapper<Grade>().eq("name", gradeName).eq("schoolyard_id", schoolyardId);
                Grade grade = this.gradeMapper.selectOne(gradeQueryWrapper);
                if (grade == null) {
                    errText += "\t年级不存在";
                }
                if (!StringUtils.isNullOrEmpty(errText))
                    sb.append(errText = "第" + rowIndex + "行 " + errText + "\r\n");
                if (StringUtils.isNullOrEmpty(errText)) {
                    QueryWrapper<Clazz> clazzQueryWrapper = new QueryWrapper<Clazz>()
                            .eq("name", clazzName)
                            .eq("academic_year_semester_id", academicYearSemesterId)
                            .eq("grade_id", grade.getId());
                    Clazz checkClazz = this.getOne(clazzQueryWrapper);
                    Clazz clazz = new Clazz();
                    if (checkClazz != null)
                        clazz = checkClazz;
                    clazz.setAcademicYearSemesterId(academicYearSemesterId);
                    clazz.setSchoolyardId(schoolyardId);
                    clazz.setGradeId(grade.getId());
                    clazz.setName(clazzName);
                    clazz.setHeadTeacher(headerTeacher);
                    if (StringUtils.isNullOrEmpty(clazzDivision))
                        clazz.setClazzDivision("");
                    else {
                        String[] clazzDivisionList = clazzDivision.split(",");
                        String clazzDivisionStr = "";
                        for (String subjectName : clazzDivisionList) {
                            if (subjectMap.containsKey(subjectName))
                                clazzDivisionStr = clazzDivisionStr + "," + String.valueOf(subjectMap.get(subjectName));
                        }
                        if (!StringUtils.isNullOrEmpty(clazzDivisionStr))
                            clazzDivisionStr = clazzDivisionStr.substring(1);
                        clazz.setClazzDivision(clazzDivisionStr);
                    }
                    clazz.setSubjectLevel("");
//                    if (StringUtils.isNullOrEmpty(subjectLevel))
//                        clazz.setSubjectLevel("");
//                    else {
//                        String[] subjectLevelList = subjectLevel.split(",");
//                        String subjectLevelStr = "";
//                        for (String subjectName : subjectLevelList) {
//                            if (subjectMap.containsKey(subjectName))
//                                subjectLevelStr = subjectLevelStr + "," + String.valueOf(subjectMap.get(subjectName));
//                        }
//                        if (!StringUtils.isNullOrEmpty(subjectLevelStr)) subjectLevelStr = subjectLevelStr.substring(1);
//                        clazz.setSubjectLevel(subjectLevelStr);
//                    }
                    if (StringUtils.isNullOrEmpty(clazzNature)) {
                        clazz.setClazzNature(ClazzNatureEnum.OTHER);
                    } else if (clazzNature.equals(ClazzNatureEnum.LIBERAL.getName())) {
                        clazz.setClazzNature(ClazzNatureEnum.LIBERAL);

                    } else if (clazzNature.equals(ClazzNatureEnum.SCIENCE.getName())) {
                        clazz.setClazzNature(ClazzNatureEnum.SCIENCE);
                    } else {
                        clazz.setClazzNature(ClazzNatureEnum.OTHER);
                    }
                    if (StringUtils.isNullOrEmpty(clazzLevel)) {
                        clazz.setClazzLevel("");
                    } else {
                        clazz.setClazzLevel(clazzLevel);
                    }
                    if (checkClazz != null)
                        this.updateById(clazz);
                    else
                        this.save(clazz);
                    // 添加班主任表
                    if (staff != null) {
                        // 当前clazzId 所有advisor 更新为NO
                        this.staffClazzAdviserMapper.update(null,
                                Wrappers.<StaffClazzAdviser>lambdaUpdate()
                                        .set(StaffClazzAdviser::getIsCurrent, YesNoEnum.NO)
                                        .eq(StaffClazzAdviser::getIsCurrent, YesNoEnum.YES)
                                        .eq(StaffClazzAdviser::getClazzId, clazz.getId()));
                        StaffClazzAdviser staffClazzAdviser = this.staffClazzAdviserMapper.selectOne(Wrappers.<StaffClazzAdviser>lambdaQuery()
                                .eq(StaffClazzAdviser::getClazzId, clazz.getId()).eq(StaffClazzAdviser::getStaffId, staff.getId()));
                        YesNoEnum state = academicYearSemesterId.equals(academicYearSemesterCurr.getId()) ? YesNoEnum.YES : YesNoEnum.NO;
                        if (staffClazzAdviser != null && YesNoEnum.YES.equals(state)) {
                            this.staffClazzAdviserMapper.update(null, Wrappers.<StaffClazzAdviser>lambdaUpdate()
                                    .eq(StaffClazzAdviser::getId, staffClazzAdviser.getId())
                                    .set(StaffClazzAdviser::getIsCurrent, state));
                        } else {
                            staffClazzAdviser = new StaffClazzAdviser();
                            staffClazzAdviser.setClazzId(clazz.getId());
                            staffClazzAdviser.setStaffId(staff.getId());
                            staffClazzAdviser.setIsCurrent(state);
                            this.staffClazzAdviserMapper.insert(staffClazzAdviser);
                        }
                    }
                }
            }
            if (sb.length() != 0) {
                throw new ApiCode.ApiException(-1, "数据有错！\n" + sb.toString());
            }
        } catch (IOException | InvalidFormatException e) {
            throw new ApiCode.ApiException(-1, "导入数据失败！");
        }
    }

    @Override
    public List<GradeVo> getGradeStatistics(List<Long> gradeIdList,Long academicYearSemesterId,Long schoolyardId,Integer week,
                                            String registerDateFrom,String registerDateTo) {
        QueryWrapper<Clazz> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(CollectionUtils.isNotEmpty(gradeIdList),"grade_id",gradeIdList);
        queryWrapper.eq(academicYearSemesterId != null ,"academic_year_semester_id",academicYearSemesterId);
        queryWrapper.orderByAsc("id");
        List<Clazz> clazzList = this.baseMapper.selectList(queryWrapper);
        List<GradeVo> gradeVos = new ArrayList<>();
        for (Long gradeId : gradeIdList) {
            IPage<DailyAttendance> page =  new Page<>(1, 99999);
            IPage<DailyAttendance> dailyAttendanceIPage = dailyAttendanceMapper.searchDailyAttendance(page,schoolyardId,academicYearSemesterId,gradeId,null,week,registerDateFrom,registerDateTo,null);
            IPage<NightStudyAttendance> nightPage =  new Page<>(1, 99999);
            IPage<NightStudyAttendance> nightStudyAttendanceIPage = nightStudyAttendanceMapper.searchNightStudyAttendance(nightPage,schoolyardId,academicYearSemesterId,gradeId,null,week,null,registerDateFrom,registerDateTo,null);
            GradeVo gradeVo = new GradeVo();
            Integer count = studentService.studentCount(gradeId,null,StudentTypeEnum.LIVE);
            gradeVo.setAccommodationNum(count);
            gradeVo.setGradeId(gradeId);
            gradeVo.setGradeName(gradeMapper.selectById(gradeId).getName());
            List<Clazz> gradeClazz = clazzList.stream().filter(clazz -> clazz.getGradeId().equals(gradeId)).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(gradeClazz)){
                gradeVo.setStudentNum(gradeClazz.stream().mapToInt(Clazz::getStudentCount).sum());

            }
            if(CollectionUtils.isNotEmpty(dailyAttendanceIPage.getRecords())){
                List<DailyAttendance> dailyAttendances = dailyAttendanceIPage.getRecords();
                gradeVo.setDailyAttendanceMap(dailyAttendances.stream().collect(Collectors.groupingBy(DailyAttendance::getClassify)));
            }

            if(CollectionUtils.isNotEmpty(nightStudyAttendanceIPage.getRecords())){
                List<NightStudyAttendance> dailyAttendances = nightStudyAttendanceIPage.getRecords();
                List<NightStudyAttendance> nightStudyAttendanceListOne= dailyAttendances.stream().filter(item->StudentNightDutyTypeEnum.STAGE_ONE.equals(item.getStage())).collect(Collectors.toList());
                List<NightStudyAttendance> nightStudyAttendanceListTwo= dailyAttendances.stream().filter(item->StudentNightDutyTypeEnum.STAGE_TWO.equals(item.getStage())).collect(Collectors.toList());
                gradeVo.setNightStudyAttendance(dailyAttendances.stream().collect(Collectors.groupingBy(NightStudyAttendance::getClassify)));
                gradeVo.setNightStudyAttendanceOne(nightStudyAttendanceListOne.stream().collect(Collectors.groupingBy(NightStudyAttendance::getClassify)));
                gradeVo.setNightStudyAttendanceTwo(nightStudyAttendanceListTwo.stream().collect(Collectors.groupingBy(NightStudyAttendance::getClassify)));
            }
            gradeVos.add(gradeVo);
        }
        return gradeVos;
    }

    @Override
    public  List<ClazzVo> getClazzStatistics(List<Long> clazzIdList,List<Long> gradeIdList, Long academicYearSemesterId, Long schoolyardId, Integer week,String registerDateFrom, String registerDateTo) {
        QueryWrapper<Clazz> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(CollectionUtils.isNotEmpty(clazzIdList),"id",clazzIdList);
        queryWrapper.in(CollectionUtils.isNotEmpty(gradeIdList),"grade_id",gradeIdList);
        queryWrapper.eq(academicYearSemesterId != null ,"academic_year_semester_id",academicYearSemesterId);
        queryWrapper.orderByAsc("id");
        List<Clazz> clazzList = this.baseMapper.selectList(queryWrapper);
        List<ClazzVo> clazzVos = new ArrayList<>();
        for (Clazz clazz : clazzList) {
            ClazzVo clazzVo = new ClazzVo();
            Integer count = studentService.studentCount(null,clazz.getId(),StudentTypeEnum.LIVE);
            clazzVo.setAccommodationNum(count);
            BeanUtils.copyProperties(clazz,clazzVo);
            IPage<DailyAttendance> page =  new Page<>(1, 99999);
            IPage<DailyAttendance> dailyAttendanceIPage = dailyAttendanceMapper.searchDailyAttendance(page,schoolyardId,academicYearSemesterId,null,clazz.getId(),week,registerDateFrom,registerDateTo,null);
            IPage<NightStudyAttendance> nightPage =  new Page<>(1, 99999);
            IPage<NightStudyAttendance> nightStudyAttendanceIPage = nightStudyAttendanceMapper.searchNightStudyAttendance(nightPage,schoolyardId,academicYearSemesterId,null,clazz.getId(),week,null,registerDateFrom,registerDateTo,null);
            if(CollectionUtils.isNotEmpty(dailyAttendanceIPage.getRecords())){
                List<DailyAttendance> dailyAttendances = dailyAttendanceIPage.getRecords();
                clazzVo.setDailyAttendanceList(dailyAttendances);
                clazzVo.setDailyAttendanceMap(dailyAttendances.stream().collect(Collectors.groupingBy(DailyAttendance::getClassify)));
            }

            if(CollectionUtils.isNotEmpty(nightStudyAttendanceIPage.getRecords())){
                List<NightStudyAttendance> dailyAttendances = nightStudyAttendanceIPage.getRecords();
                List<NightStudyAttendance> nightStudyAttendanceListOne= dailyAttendances.stream().filter(item->StudentNightDutyTypeEnum.STAGE_ONE.equals(item.getStage())).collect(Collectors.toList());
                List<NightStudyAttendance> nightStudyAttendanceListTwo= dailyAttendances.stream().filter(item->StudentNightDutyTypeEnum.STAGE_TWO.equals(item.getStage())).collect(Collectors.toList());
                clazzVo.setNightStudyAttendanceList(dailyAttendances);
                clazzVo.setNightStudyAttendanceMap(dailyAttendances.stream().collect(Collectors.groupingBy(NightStudyAttendance::getClassify)));
                clazzVo.setNightStudyAttendanceMapOne(nightStudyAttendanceListOne.stream().collect(Collectors.groupingBy(NightStudyAttendance::getClassify)));
                clazzVo.setNightStudyAttendanceMapTwo(nightStudyAttendanceListTwo.stream().collect(Collectors.groupingBy(NightStudyAttendance::getClassify)));
            }
            clazzVos.add(clazzVo);
        }
        return clazzVos;
    }
}
