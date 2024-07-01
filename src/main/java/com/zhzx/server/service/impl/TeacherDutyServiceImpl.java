/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：教师值班表
 *
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
import com.zhzx.server.domain.Label;
import com.zhzx.server.domain.*;
import com.zhzx.server.dto.*;
import com.zhzx.server.enums.*;
import com.zhzx.server.repository.*;
import com.zhzx.server.repository.base.TeacherDutyBaseMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.TeacherDutyService;
import com.zhzx.server.util.CellUtils;
import com.zhzx.server.util.DateUtils;
import com.zhzx.server.util.StringUtils;
import com.zhzx.server.vo.ClazzVo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TeacherDutyServiceImpl extends ServiceImpl<TeacherDutyMapper, TeacherDuty> implements TeacherDutyService {
    @Value("${web.upload-path}")
    private String uploadPath;
    @Resource
    private StaffMapper staffMapper;
    @Resource
    private TeacherDutyClazzMapper teacherDutyClazzMapper;
    @Resource
    private TeacherDutySubstituteMapper teacherDutySubstituteMapper;
    @Resource
    private LabelMapper labelMapper;
    @Resource
    private NightStudyMapper nightStudyMapper;
    @Resource
    private NightStudyDutyMapper nightStudyDutyMapper;
    @Resource
    private NightStudyDutyClazzMapper nightStudyDutyClazzMapper;
    @Resource
    private CourseTimeMapper courseTimeMapper;
    @Resource
    private ClazzMapper clazzMapper;
    @Resource
    private LeaderDutyMapper leaderDutyMapper;
    @Resource
    private TeacherDutyMapper teacherDutyMapper;
    @Resource
    private AcademicYearSemesterMapper academicYearSemesterMapper;

    @Override
    public int updateAllFieldsById(TeacherDuty entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    private Font imageFont = new Font("宋体", Font.PLAIN, 14);

    private int[] getStringWidthHeight(String str) {
        JLabel label = new JLabel();
        label.setFont(imageFont);
        label.setText(str);
        FontMetrics metrics = label.getFontMetrics(label.getFont());
        int[] num = new int[3];
        num[0] = metrics.stringWidth(str);
        num[1] = metrics.getHeight();
        num[2] = metrics.getAscent();
        return num;
    }

    private BufferedImage rotateImage(final BufferedImage src,
                                      final int angel) {
        int src_width = src.getWidth(null);
        int src_height = src.getHeight(null);
        // 计算旋转后图片的尺寸
        Rectangle rect_des = CalcRotatedSize(new Rectangle(new Dimension(
                src_width, src_height)), angel);
        BufferedImage res = null;
        res = new BufferedImage(rect_des.width, rect_des.height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = res.createGraphics();
        // 进行转换
        g2.translate((rect_des.width - src_width) / 2,
                (rect_des.height - src_height) / 2);
        g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);
        g2.drawImage(src, null, null);
        return res;
    }

    private Rectangle CalcRotatedSize(Rectangle src, int angel) {
        // 如果旋转的角度大于90度做相应的转换
        if (angel >= 90) {
            if (angel / 90 % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }

        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angel_dalta_width = Math.atan((double) src.height / src.width);
        double angel_dalta_height = Math.atan((double) src.width / src.height);

        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha
                - angel_dalta_width));
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha
                - angel_dalta_height));
        int des_width = src.width + len_dalta_width * 2;
        int des_height = src.height + len_dalta_height * 2;
        return new Rectangle(new Dimension(des_width, des_height));
    }

    @Override
    public BufferedImage getImage(Date timeFrom, Date timeTo, Long gradeId) throws Exception {
        if (gradeId == null) {
            return new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        }
        Page<Map<String, Object>> page = this.getTeacherDutyFormV2(1, 9999, timeFrom, timeTo, null, gradeId, null, YesNoEnum.YES);
        List<Map<String, Object>> list = page.getRecords();
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        }
        Map<String, Object> curr = list.get(0);
        List<TeacherServerFormDto> gradeList = (List<TeacherServerFormDto>) curr.get("gradeList");
        List<Map<String, Object>> clazzList = (List<Map<String, Object>>) curr.get("clazzList");
        clazzList.sort(Comparator.comparing(o -> Integer.parseInt(o.get("name").toString().replace("班", "")), Comparator.naturalOrder()));

        int padding = 2;
        // 值班老师人名自适应为5个字
        // 总值班分校区显示，字数稍长
        int columnWithLarge = this.getStringWidthHeight("综治办半半综治办半半半半半半")[0];
        int columnWidth = this.getStringWidthHeight("综治办半半")[0];
        int rowHeight = this.getStringWidthHeight("综治办半半")[1];
        int rowHeightDay = rowHeight * 2 + padding * 3;
        int rowHeightOther = rowHeight + padding * 3 / 2;
        int ascent = this.getStringWidthHeight("综治办半半")[2];
        int imageWidth = 80 + 80 + (2 * clazzList.size() + 1) * (columnWidth + padding * 3) + (columnWithLarge + padding * 3);
        int imageHeight = rowHeightDay + rowHeightOther * gradeList.size();
        BufferedImage image = new BufferedImage(imageWidth + 20, imageHeight + 20, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.setStroke(new BasicStroke(0.1f));
        graphics.setFont(imageFont);
        graphics.fillRect(0, 0, imageWidth + 20, imageHeight + 20);
        graphics.setColor(Color.black);

        graphics.drawRect(10, 10, imageWidth, imageHeight);
        graphics.drawLine(10, 10 + rowHeightDay, 10 + imageWidth, 10 + rowHeightDay);
        graphics.drawLine(90 + 80, 10 + rowHeightOther, 90 + 80 + (columnWidth + 3 * padding) * clazzList.size() * 2, 10 + rowHeightOther);
        graphics.drawLine(90, 10, 90, imageHeight + 10);
        int x = 90 + 80, middle = (rowHeight + padding * 3) / 2;
        // 日期星期单元格 宽度固定80
        graphics.drawString("日期", 10 + (80 - this.getStringWidthHeight("日期")[0]) / 2, 10 + middle + ascent);

        graphics.drawString("星期", 90 + (80 - this.getStringWidthHeight("星期")[0]) / 2, 10 + middle + ascent);

        for (int i = 0; i < clazzList.size() * 2; i++) {
            int p = x;
            if (i == 0 || i == clazzList.size()) {
                graphics.drawLine(x, 10, x, 10 + imageHeight);
                graphics.drawString(i == 0 ? "第一阶段" : "第二阶段", x + ((columnWidth + 3 * padding) * clazzList.size() - this.getStringWidthHeight("第一阶段")[0]) / 2, 10 + ascent + padding);
                x += columnWidth + padding * 3;
            } else {
                x += columnWidth + padding * 3;
            }
            graphics.drawLine(x, 10 + rowHeightOther, x, 10 + imageHeight);
            String name = clazzList.get(i % clazzList.size()).get("name").toString();
            graphics.drawString(name, p + (columnWidth + 3 * padding - this.getStringWidthHeight(name)[0]) / 2, 10 + ascent + rowHeightOther + padding);
            if (i == 2 * clazzList.size() - 1) {
                graphics.drawLine(x, 10, x, 10 + imageHeight);
                graphics.drawString("年级", x + (columnWidth + 3 * padding - this.getStringWidthHeight("年级")[0]) / 2, 10 + ascent + middle);
                x += columnWidth + padding * 3;
                graphics.drawLine(x, 10, x, 10 + imageHeight);
                graphics.drawString("总值班", x + (columnWithLarge + 3 * padding - this.getStringWidthHeight("总值班")[0]) / 2, 10 + ascent + middle);
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.CHINA);

        int y = rowHeightDay + 10;
        for (TeacherServerFormDto teacherServerFormDto : gradeList) {
            if (CollectionUtils.isEmpty(teacherServerFormDto.getClazzVoList())) {
                teacherServerFormDto.setClazzVoList(new ArrayList<>());
            }
            teacherServerFormDto.getClazzVoList().sort(Comparator.comparing(o -> Integer.parseInt(o.getName().replace("班", "")), Comparator.naturalOrder()));
            // 日期
            String time = DateUtils.format(teacherServerFormDto.getTime(), "MM-dd");
            graphics.drawString(time, 10 + (80 + 3 * padding - this.getStringWidthHeight(time)[0]) / 2, y + ascent + padding);
            // 星期
            graphics.drawString(sdf.format(teacherServerFormDto.getTime()), 10 + 80 + (80 + 3 * padding - this.getStringWidthHeight("星期一")[0]) / 2, y + ascent + padding);

            int xx = 90 + 80;
            for (ClazzVo clazzVo : teacherServerFormDto.getClazzVoList()) {
                String s = clazzVo.getStageOneTeacher() == null ? "" : clazzVo.getStageOneTeacher();
                graphics.drawString(s, xx + (columnWidth + 3 * padding - this.getStringWidthHeight(s)[0]) / 2, y + ascent + padding);
                xx += columnWidth + padding * 3;
            }
            for (ClazzVo clazzVo : teacherServerFormDto.getClazzVoList()) {
                String s = clazzVo.getStageTwoTeacher() == null ? "" : clazzVo.getStageTwoTeacher();
                graphics.drawString(s, xx + (columnWidth + 3 * padding - this.getStringWidthHeight(s)[0]) / 2, y + ascent + padding);
                xx += columnWidth + padding * 3;
            }
            String g = teacherServerFormDto.getGradeDutyTeacher() == null ? "" : teacherServerFormDto.getGradeDutyTeacher();
            graphics.drawString(g, xx + (columnWidth + 3 * padding - this.getStringWidthHeight(g)[0]) / 2, y + ascent + padding);
            xx += columnWidth + padding * 3;
            g = teacherServerFormDto.getTotalDutyTeacher() == null ? "" : teacherServerFormDto.getTotalDutyTeacher();
            graphics.drawString(g, xx + (columnWithLarge + 3 * padding - this.getStringWidthHeight(g)[0]) / 2, y + ascent + padding);
            graphics.drawLine(10, y, 10 + imageWidth, y);
            y += rowHeightOther;
        }

        image = this.rotateImage(image, 90);
//        ImageIO.write(image, "jpg", new File("d:/image.jpg"));
        return image;
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
    public boolean saveBatch(Collection<TeacherDuty> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(TeacherDutyBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    public Map<Object, Object> nightRoutine(Date time, RoutineEnum type) {

        Map<Object, Object> map = new HashMap<>();

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Staff staff = staffMapper.selectById(user.getStaffId());
        List<TeacherDutyDto> teacherDutyDtoList = this.baseMapper.nightRoutine(time, type.toString(), staff.getId(), new ArrayList<String>() {{
            this.add(TeacherDutyTypeEnum.STAGE_ONE.toString());
            this.add(TeacherDutyTypeEnum.STAGE_TWO.toString());
        }});

        //查询年级及总值班信息
        TeacherDutyDto gradeTeacherDuty = this.getGradeTeacherDuty(time);
        map.put("totalDutyTeacher", gradeTeacherDuty.getTotalDutyTeacher());
        map.put("gradeOneTeacher", gradeTeacherDuty.getGradeOneTeacher());
        map.put("gradeTwoTeacher", gradeTeacherDuty.getGradeTwoTeacher());
        map.put("gradeThreeTeacher", gradeTeacherDuty.getGradeThreeTeacher());


        for (TeacherDutyDto teacherDutyDto : teacherDutyDtoList) {
            teacherDutyDto.getTeacherDutyClazzList().stream().forEach(nightDutyClassDto -> {
                List<CommentImages> commentImagesList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(nightDutyClassDto.getCommentDtoList())) {
                    for (CommentDto commentDto : nightDutyClassDto.getCommentDtoList()) {
                        if (commentDto.getCommentImagesList() != null) {
                            commentImagesList.addAll(commentDto.getCommentImagesList());
                        }
                    }
                }
                nightDutyClassDto.setPicList(commentImagesList);
                if (CollectionUtils.isNotEmpty(nightDutyClassDto.getNightStudyAttendances())) {
                    NightStudyAttendance nightStudyAttendance = nightDutyClassDto.getNightStudyAttendances().get(nightDutyClassDto.getNightStudyAttendances().size() - 1);
                    nightDutyClassDto.setActualStudentCount(nightStudyAttendance.getActualNum());
                    nightDutyClassDto.setShouldStudentCount(nightStudyAttendance.getShouldNum());
                    Map<String, List<NightStudyAttendance>> detailMap = nightDutyClassDto.getNightStudyAttendances().stream()
                            .collect(Collectors.groupingBy(entity -> entity.getClassify()));
                    nightDutyClassDto.setNightStudyDetailMap(detailMap);
                }
            });
        }
        Map<TeacherDutyTypeEnum, List<TeacherDutyDto>> stageMap = teacherDutyDtoList.stream().collect(Collectors.groupingBy(TeacherDutyDto::getDutyType));
        stageMap.keySet().stream().forEach(teacherDutyTypeEnum -> {
            TeacherDutyDto teacherDutyDto = stageMap.get(teacherDutyTypeEnum).get(0);
            if (CollectionUtils.isNotEmpty(teacherDutyDto.getTeacherDutyClazzList()) && teacherDutyDto.getTeacherDutyClazzList().get(0).getTeacherDutyClassId() != null) {
                // 动态插入时间
                int sortOrder = teacherDutyTypeEnum.equals(TeacherDutyTypeEnum.STAGE_ONE) ? 11 : 12;
                String gradeName = teacherDutyDto.getTeacherDutyClazzList().get(0).getGradeName();
                Long gradeId = gradeName.startsWith("高一") ? 1L : gradeName.startsWith("高二") ? 2L : 3L;
                CourseTime courseTime = this.courseTimeMapper.selectOne(
                        Wrappers.<CourseTime>lambdaQuery()
                                .eq(CourseTime::getSortOrder, sortOrder)
                                .eq(CourseTime::getGradeId, gradeId)
                );
                Date now = teacherDutyDto.getStartTime();
                teacherDutyDto.setStartTime(DateUtils.parse(courseTime.getStartTime(), now));
                teacherDutyDto.setEndTime(DateUtils.parse(courseTime.getEndTime(), now));

                map.put(teacherDutyTypeEnum, stageMap.get(teacherDutyTypeEnum).get(0));
            }
        });
        List<Label> labels = labelMapper.selectList(Wrappers.<Label>lambdaQuery().eq(Label::getClassify, "WZXKQ"));
        map.put("classify", labels);
        return map;
    }

    @Override
    public Page<Map<String, Object>> getTeacherDutyForm(Integer pageNum, Integer pageSize, Date timeFrom, Date timeTo, String teacherDutyName, Long gradeId, Long schoolyardId, YesNoEnum fromApp) {
        Page<Map<String, Object>> page = new Page();
        page.setCurrent(pageNum);
        page.setSize(pageSize);

        // 如果是APP端按年级查看当月值班表，则dateList默认当月所有日期
        List<Date> dateList = null;
        if (YesNoEnum.NO.equals(fromApp)) {
            List<TeacherServerFormDto> timeList = this.baseMapper.getTeacherDutyForm(page, timeFrom, timeTo, teacherDutyName, null, schoolyardId);
            dateList = timeList.stream().map(TeacherServerFormDto::getTime).collect(Collectors.toList());
        } else {
            dateList = DateUtils.getMonthDays(timeFrom, timeTo);
            page.setTotal(dateList.size());
        }

        if (CollectionUtils.isEmpty(dateList)) return page;

        // 按月查询每次查询量近万，数据加载很慢
        List<ClazzVo> clazzVoList = this.baseMapper.getFormList(dateList, gradeId, schoolyardId);

        List<Map<String, Object>> formList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(clazzVoList)) {
            Map<String, List<ClazzVo>> gradeMap = clazzVoList.stream().collect(Collectors.groupingBy(ClazzVo::getGradeName));
            for (String key : gradeMap.keySet()) {
                List<Map<String, Object>> headerList = new ArrayList<>();
                Map<String, Object> formMap = new HashMap<>();
                Map<Date, List<ClazzVo>> timeTeacherMap = gradeMap.get(key).stream().collect(Collectors.groupingBy(ClazzVo::getTime));
                List<TeacherServerFormDto> list = new ArrayList<>();
                Map<Date, List<ClazzVo>> resultMap = new TreeMap<>((str1, str2) -> str1.compareTo(str2));
                resultMap.putAll(timeTeacherMap);
                Boolean flag = false;
                for (Date date : resultMap.keySet()) {
                    TeacherServerFormDto teacherServerFormDto = new TeacherServerFormDto();
                    teacherServerFormDto.setDutyMode(TeacherDutyModeEnum.NORMAL);
                    teacherServerFormDto.setTime(date);
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.CHINA);
                    teacherServerFormDto.setWeek(sdf.format(date));

                    List<ClazzVo> clazzVos = timeTeacherMap.get(date);

                    if (dateList.contains(date)) {
                        TeacherDutyGradeTotalDto teacherDuty2 = this.baseMapper.selectTeacherDutyGradeTotalDto(date, 1L, clazzVos.get(0).getGradeId());
                        if (teacherDuty2 != null)
                            teacherServerFormDto.setDutyMode(teacherDuty2.getDutyMode());
                    }
                    for (ClazzVo item : clazzVos) {
                        if (!flag) {
                            Map<String, Object> headerMap = new HashMap<>();
                            headerMap.put("name", item.getName());
                            headerList.add(headerMap);
                        }
                        if (item.getGradeDutyTeacher() != null) {
                            teacherServerFormDto.setGradeDutyTeacher(item.getGradeDutyTeacher());
                        }
                        if (item.getTotalDutyTeacher() != null) {
                            teacherServerFormDto.setTotalDutyTeacher(item.getTotalDutyTeacher());
                        }
                    }
                    List<ClazzVo> distinctBySchoolyard = clazzVos.stream().collect
                            (Collectors.collectingAndThen
                                    (Collectors.toCollection
                                            (() -> new TreeSet<>(Comparator.comparing(ClazzVo::getSchoolyardName))), ArrayList::new));
                    teacherServerFormDto.setGradeDutyTeacherYard(distinctBySchoolyard.stream()
                            .map(item -> (item.getGradeDutyTeacher() == null ? "" : item.getGradeDutyTeacher()).concat("(").concat(item.getSchoolyardName()).concat(")"))
                            .collect(Collectors.joining(" ")));
                    teacherServerFormDto.setTotalDutyTeacherYard(distinctBySchoolyard.stream()
                            .filter(item -> !StringUtils.isNullOrEmpty(item.getTotalDutyTeacher()))
                            .map(item -> item.getTotalDutyTeacher().concat("(").concat(item.getSchoolyardName().replace("校区", "")).concat(")"))
                            .collect(Collectors.joining(" ")));
                    teacherServerFormDto.setClazzVoList(timeTeacherMap.get(date));

                    list.add(teacherServerFormDto);
                    flag = true;
                }
                formMap.put("gradeList", list);
                formMap.put("gradeName", key);
                formMap.put("clazzList", headerList);
                formList.add(formMap);
            }
        }
        page.setRecords(formList);
        return page;
    }

    private Date parse(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (ParseException ignored) {
        }
        throw new ApiCode.ApiException(-5, "时间格式化失败");
    }

    private TeacherDutyDto findInMap(Map<String, TeacherDutyDto> stageMap, String key) {
        String keyEnhance = "C" + key + "C";
        for (Map.Entry<String, TeacherDutyDto> mest : stageMap.entrySet()) {
            if (mest.getKey().contains(keyEnhance)) {
                return mest.getValue();
            }
        }
        return null;
    }


    @Override
    public Page<Map<String, Object>> getTeacherDutyFormV2(Integer pageNum, Integer pageSize, Date timeFrom, Date timeTo, String teacherDutyName, Long gradeId, Long schoolyardId, YesNoEnum fromApp) {
        Page<Map<String, Object>> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);

        // 如果是APP端按年级查看当月值班表，则dateList默认当月所有日期
        List<Date> dateList = null;
        if (YesNoEnum.NO.equals(fromApp)) {
            List<TeacherServerFormDto> timeList = this.baseMapper.getTeacherDutyForm(page, timeFrom, timeTo, teacherDutyName, null, schoolyardId);
            dateList = timeList.stream().map(TeacherServerFormDto::getTime).collect(Collectors.toList());
        } else {
            dateList = DateUtils.getMonthDays(timeFrom, timeTo);
            page.setTotal(dateList.size());
        }

        if (CollectionUtils.isEmpty(dateList)) return page;

        AcademicYearSemester academicYearSemester = this.academicYearSemesterMapper.selectOne(Wrappers.<AcademicYearSemester>lambdaQuery().eq(AcademicYearSemester::getIsDefault, YesNoEnum.YES));
        //年级内每个班级的name
        List<Clazz> clazzList = this.clazzMapper.selectList(
                Wrappers.<Clazz>lambdaQuery()
                        .eq(null != gradeId, Clazz::getGradeId, gradeId)
                        .eq(null != schoolyardId, Clazz::getSchoolyardId, schoolyardId)
                        .eq(Clazz::getAcademicYearSemesterId, academicYearSemester.getId())
                        .ne(Clazz::getName, "23班")
        );

        List<TeacherDutyDto> teacherDutyList = this.baseMapper.selectListWithClazz(schoolyardId, gradeId, timeFrom, timeTo);
        teacherDutyList = teacherDutyList
                .stream()
                .filter(t -> TeacherDutyTypeEnum.TOTAL_DUTY.equals(t.getDutyType()) || CollectionUtils.isNotEmpty(t.getTeacherDutyClazzList()))
                .collect(Collectors.toList());

        List<Map<String, Object>> formList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(teacherDutyList)) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.CHINA);

            List<String> dateListStr = dateList.stream().sorted(Comparator.naturalOrder()).map(t -> DateUtils.format(t, "yyyy-MM-dd")).collect(Collectors.toList());

            Map<String, List<Clazz>> clazzMap = clazzList.stream().collect(Collectors.groupingBy(t -> t.getGrade().getName()));

            Map<String, Map<TeacherDutyTypeEnum, List<TeacherDutyDto>>> dateTypeDutyMap = teacherDutyList
                    .stream()
                    .collect(Collectors.groupingBy(t -> DateUtils.format(t.getStartTime(), "yyyy-MM-dd"), Collectors.groupingBy(TeacherDuty::getDutyType)));

            clazzMap.forEach((k, v) -> {
                Map<String, Object> formMap = new HashMap<>();

                formMap.put("gradeName", k);

                //每个阶段开始的时间
                List<String> startEndTimeList = this.courseTimeMapper.getNightDutyTime(
                        new ArrayList<Long>() {{
                            this.add(v.get(0).getGradeId());
                        }}
                );
                formMap.put("startEndTimeList", startEndTimeList);

                List<Map<String, Object>> headerList = v.stream().map(t -> {
                    Map<String, Object> map = new HashMap<>(4);
                    map.put("name", t.getName());
                    return map;
                }).collect(Collectors.toList());
                formMap.put("clazzList", headerList);

                //日期、星期
                List<TeacherServerFormDto> gradeList = new ArrayList<>();

                dateListStr.forEach(dateStr -> {
                    TeacherServerFormDto teacherServerFormDto = new TeacherServerFormDto();
                    teacherServerFormDto.setTime(parse(dateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
                    teacherServerFormDto.setWeek(sdf.format(teacherServerFormDto.getTime()));
                    teacherServerFormDto.setDutyMode(TeacherDutyModeEnum.NORMAL);

                    Map<TeacherDutyTypeEnum, List<TeacherDutyDto>> typeDutyMap = dateTypeDutyMap.get(dateStr);
                    if (CollectionUtils.isNotEmpty(typeDutyMap)) {
                        TeacherDutyModeEnum dutyModeGradeTeacher = null, dutyModeTotalTeacher = null;

                        if (typeDutyMap.containsKey(TeacherDutyTypeEnum.GRADE_TOTAL_DUTY)) {
                            // 年级值班按兴隆校区统计
                            List<TeacherDutyDto> dutyGradeTeachers = typeDutyMap.get(TeacherDutyTypeEnum.GRADE_TOTAL_DUTY);
                            dutyGradeTeachers = dutyGradeTeachers.stream()
                                    .filter(t -> t.getSchoolyardId().equals(1L) && t.getTeacherDutyClazzList().get(0).getGradeName().equals(k))
                                    .collect(Collectors.toList());
                            if (CollectionUtils.isNotEmpty(dutyGradeTeachers)) {
                                TeacherDutyDto dutyGradeTeacher = dutyGradeTeachers.get(0);
                                teacherServerFormDto.setGradeDutyTeacher(dutyGradeTeacher.getTeacher().getName());
                                teacherServerFormDto.setGradeDutyTeacherYard(teacherServerFormDto.getGradeDutyTeacher().concat("(兴隆)"));
                                dutyModeGradeTeacher = dutyGradeTeacher.getDutyMode();
                            } else {
                                teacherServerFormDto.setGradeDutyTeacher("");
                                teacherServerFormDto.setGradeDutyTeacherYard("");
                                dutyModeGradeTeacher = TeacherDutyModeEnum.NORMAL;
                            }
                        }

                        // 总值班分校区
                        List<TeacherDutyDto> totalDutyList = typeDutyMap.getOrDefault(TeacherDutyTypeEnum.TOTAL_DUTY, new ArrayList<>());
                        teacherServerFormDto.setTotalDutyTeacher(
                                totalDutyList
                                        .stream()
                                        .map(t -> t.getTeacher().getName().concat("(").concat(t.getSchoolyardId().equals(1L) ? "兴隆" : "雨花").concat(")"))
                                        .collect(Collectors.joining(","))
                        );
                        teacherServerFormDto.setTotalDutyTeacherYard(teacherServerFormDto.getTotalDutyTeacher());
                        if (CollectionUtils.isNotEmpty(totalDutyList)) {
                            dutyModeTotalTeacher = totalDutyList.get(0).getDutyMode();
                        }

                        Map<Long, String> totalDutyMap = totalDutyList.stream().collect(Collectors.toMap(TeacherDuty::getSchoolyardId, t -> t.getTeacher().getName()));
                        Map<String, TeacherDutyDto> stageOneDutyMap = typeDutyMap.getOrDefault(TeacherDutyTypeEnum.STAGE_ONE, new ArrayList<>()).stream().collect(
                                Collectors.toMap(t -> t.getTeacherDutyClazzList().stream().map(p -> "C" + p.getClazzId().toString() + "C").collect(Collectors.joining(",")), Function.identity())
                        );
                        Map<String, TeacherDutyDto> stageTwoDutyMap = typeDutyMap.getOrDefault(TeacherDutyTypeEnum.STAGE_TWO, new ArrayList<>()).stream().collect(
                                Collectors.toMap(t -> t.getTeacherDutyClazzList().stream().map(p -> "C" + p.getClazzId().toString() + "C").collect(Collectors.joining(",")), Function.identity())
                        );

                        List<ClazzVo> clazzVoList = new ArrayList<>();
                        v.forEach(clazz -> {
                            Long clazzId = clazz.getId();
                            ClazzVo clazzVo = new ClazzVo();
                            clazzVo.setId(clazzId);
                            clazzVo.setName(clazz.getName());
                            clazzVo.setGradeId(clazz.getGradeId());
                            clazzVo.setGradeName(k);
                            clazzVo.setSchoolyardName(clazz.getSchoolyard().getName());
                            clazzVo.setTime(teacherServerFormDto.getTime());
                            if (clazz.getSchoolyardId().equals(1L)) {
                                clazzVo.setGradeDutyTeacher(teacherServerFormDto.getGradeDutyTeacher());
                            }
                            clazzVo.setTotalDutyTeacher(totalDutyMap.get(clazz.getSchoolyardId()));
                            TeacherDutyDto stageOne = findInMap(stageOneDutyMap, clazzId.toString());
                            if (null != stageOne) {
                                clazzVo.setStageOneTeacher(stageOne.getTeacher().getName());
                                teacherServerFormDto.setDutyMode(stageOne.getDutyMode());
                            }
                            TeacherDutyDto stageTwo = findInMap(stageTwoDutyMap, clazzId.toString());
                            if (null != stageTwo) {
                                clazzVo.setStageTwoTeacher(stageTwo.getTeacher().getName());
                                teacherServerFormDto.setDutyMode(stageTwo.getDutyMode());
                            }
                            clazzVoList.add(clazzVo);
                        });
                        teacherServerFormDto.setClazzVoList(clazzVoList);

                        if (null != dutyModeGradeTeacher) {
                            teacherServerFormDto.setDutyMode(dutyModeGradeTeacher);
                        } else if (null != dutyModeTotalTeacher) {
                            teacherServerFormDto.setDutyMode(dutyModeTotalTeacher);
                        }
                    }
                    gradeList.add(teacherServerFormDto);
                });
                formMap.put("gradeList", gradeList);
                formList.add(formMap);
            });
        } else {
            page.setTotal(0L);
        }

        page.setRecords(formList);
        return page;
    }

    public TeacherDutyDto getGradeTeacherDuty(Date time) {
        //查询年级及总值班信息
        List<TeacherDutyDto> totalDutyList = this.baseMapper.getTotalDutyList(time);

        List<TeacherDutyDto> totalDuty = totalDutyList.stream().filter(teacherDutyDto -> TeacherDutyTypeEnum.TOTAL_DUTY.equals(teacherDutyDto.getDutyType())).collect(Collectors.toList());
        String totalDutyTeacher = "";
        String gradeOneTeacher = "";
        String gradeTwoTeacher = "";
        String gradeThreeTeacher = "";
        if (CollectionUtils.isNotEmpty(totalDuty))
            totalDutyTeacher = totalDuty.stream().map(TeacherDutyDto::getTeacherName).collect(Collectors.joining(","));

        totalDutyList = totalDutyList.stream().filter(item -> !StringUtils.isNullOrEmpty(item.getGradeName())).collect(Collectors.toList());
        List<TeacherDutyDto> gradeTotalDutyList = totalDutyList.stream().filter(teacherDutyDto -> TeacherDutyTypeEnum.GRADE_TOTAL_DUTY.equals(teacherDutyDto.getDutyType())).collect(Collectors.toList());

        List<TeacherDutyDto> gradeOneTotalDutyList = gradeTotalDutyList.stream().filter(gradeTotalDuty -> gradeTotalDuty.getGradeName().contains("一")).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(gradeOneTotalDutyList))
            gradeOneTeacher = gradeOneTotalDutyList.stream().map(TeacherDutyDto::getTeacherName).collect(Collectors.joining(","));

        List<TeacherDutyDto> gradeTwoTotalDutyList = gradeTotalDutyList.stream().filter(gradeTotalDuty -> gradeTotalDuty.getGradeName().contains("二")).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(gradeTwoTotalDutyList))
            gradeTwoTeacher = gradeTwoTotalDutyList.stream().map(TeacherDutyDto::getTeacherName).collect(Collectors.joining(","));

        List<TeacherDutyDto> gradeThreeTotalDutyList = gradeTotalDutyList.stream().filter(gradeTotalDuty -> gradeTotalDuty.getGradeName().contains("三")).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(gradeThreeTotalDutyList))
            gradeThreeTeacher = gradeThreeTotalDutyList.stream().map(TeacherDutyDto::getTeacherName).collect(Collectors.joining(","));
        TeacherDutyDto teacherDutyDto = new TeacherDutyDto();
        teacherDutyDto.setTotalDutyTeacher(totalDutyTeacher);
        teacherDutyDto.setGradeThreeTeacher(gradeThreeTeacher);
        teacherDutyDto.setGradeTwoTeacher(gradeTwoTeacher);
        teacherDutyDto.setGradeOneTeacher(gradeOneTeacher);
        return teacherDutyDto;
    }

    @Override
    @Transactional
    public String importTeacherDuty(Long schoolyardId, Long academicYearSemesterId, Long gradeId, String fileUrl) {
        if (StringUtils.isNullOrEmpty(fileUrl))
            throw new ApiCode.ApiException(-1, "没有上传文件！");
        String[] items = fileUrl.split("/");
        File file = new File(uploadPath + File.separator + items[items.length - 2] + File.separator + items[items.length - 1]);

        Date currentDate = new Date();
        //查询老师
        QueryWrapper<Staff> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", YesNoEnum.NO);
        //查询未被删除的老师
        List<Staff> staffList = this.staffMapper.selectList(queryWrapper);
        //对老师进行去重操作
        Map<String, List<Staff>> staffMap = staffList.stream().collect(Collectors.groupingBy(Staff::getName));
        //符合条件的班级总数
        Integer existClazzCount = clazzMapper.selectCount(Wrappers.<Clazz>lambdaQuery()
                .eq(Clazz::getGradeId, gradeId)
                .eq(Clazz::getSchoolyardId, schoolyardId)
                .eq(Clazz::getAcademicYearSemesterId, academicYearSemesterId)
        );

        if (!file.exists())
            throw new ApiCode.ApiException(-1, "文件不存在！");
        try {
            // 读取 Excel
            XSSFWorkbook book = new XSSFWorkbook(file);
            XSSFSheet sheet = book.getSheetAt(0);
            file.delete();
            //获得总行数
            int rowNum = sheet.getPhysicalNumberOfRows();
            // 获得总列数
            int columnNum = sheet.getRow(0).getPhysicalNumberOfCells();
            //打散之后的总列数
            int cells = sheet.getRow(2).getPhysicalNumberOfCells();
            // 教师值班列表
            List<TeacherDuty> teacherDutyList = new ArrayList<>();
            // 读取单元格数据
            TeacherDuty teacherDuty = null;
            int clazz = (columnNum - 4) % 2;
            //通过excel的列数是否存在余数来判断excel的格式是否正确
            if (clazz != 0) {
                throw new ApiCode.ApiException(-5, "格式错误");
            }
            //16
            int stage = (cells - 4) / 2;
            if (!Objects.equals(stage, existClazzCount)) {
                throw new ApiCode.ApiException(-5, "班级行数错误");
            }
            //截取第一阶段
            String stageOne = sheet.getRow(0).getCell(1).toString().replace("（", "(").replace("）", ")");
            //截取第二阶段
            String stageTwo = sheet.getRow(0).getCell(stage + 1).toString().replace("（", "(").replace("）", ")");

            if (!(stageOne.contains("(") && stageOne.contains(")"))) {
                throw new ApiCode.ApiException(-5, "第一阶段开始结束时间必传！");
            }
            if (!(stageTwo.contains("(") && stageTwo.contains(")"))) {
                throw new ApiCode.ApiException(-5, "第二阶段开始结束时间必传！");
            }
            String stageOneStartTime = stageOne.split("\\(")[1].split("\\)")[0].split("-")[0];
            String stageOneEndTime = stageOne.split("\\(")[1].split("\\)")[0].split("-")[1];
            String stageTwoStartTime = stageTwo.split("\\(")[1].split("\\)")[0].split("-")[0];
            String stageTwoEndTime = stageTwo.split("\\(")[1].split("\\)")[0].split("-")[1];

            //11节次的时间设置为第一阶段的时间--->主要是开始时间和结束时间
            courseTimeMapper.update(new CourseTime(), Wrappers.<CourseTime>lambdaUpdate()
                    .set(CourseTime::getStartTime, stageOneStartTime)
                    .set(CourseTime::getEndTime, stageOneEndTime)
                    .eq(CourseTime::getSortOrder, 11)
                    .eq(CourseTime::getGradeId, gradeId)
            );
            //12节次的时间设置为第一阶段的时间--->主要是开始时间和结束时间
            courseTimeMapper.update(new CourseTime(), Wrappers.<CourseTime>lambdaUpdate()
                    .set(CourseTime::getStartTime, stageTwoStartTime)
                    .set(CourseTime::getEndTime, stageTwoEndTime)
                    .eq(CourseTime::getSortOrder, 12)
                    .eq(CourseTime::getGradeId, gradeId)
            );

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            //符合条件的校区班级的集合
            List<Clazz> clazzList = this.clazzMapper.selectList(Wrappers.<Clazz>lambdaQuery()
                    .eq(Clazz::getGradeId, gradeId)
                    .eq(Clazz::getAcademicYearSemesterId, academicYearSemesterId)
                    .eq(Clazz::getSchoolyardId, schoolyardId));
            //验证集合是否存在空值
            if (CollectionUtils.isEmpty(clazzList))
                throw new ApiCode.ApiException(-5, "校区无效！");

            StringBuilder sb = new StringBuilder();
            List<Date> emptyList = new ArrayList<>();
            Set<String> dateSet = new HashSet<>();

            //开始循环取数据
            for (int rowIndex = 2; rowIndex < rowNum; rowIndex = rowIndex + 1) {
                String dateCell = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(0), "yyyy-MM-dd");
                // 检查一下日期唯一性
                if (!dateSet.add(dateCell))
                    throw new ApiCode.ApiException(-5, "日期重复！");

                Date rowDate = DateUtils.parse(dateCell, "yyyy-MM-dd");
                if (rowDate.getTime() < DateUtils.parse(DateUtils.format(currentDate, "yyyy-MM-dd"), "yyyy-MM-dd").getTime()) {
                    sb.append("第").append(rowIndex + 1).append("行: ").append("日期早于当前日期").append("\r\n");
                    continue;
                }

                // 一、二阶段开始结束时间
                Date stageOneStart = sdf.parse(dateCell + " " + stageOneStartTime);
                Date stageOneEnd = sdf.parse(dateCell + " " + stageOneEndTime);
                Date stageTwoStart = sdf.parse(dateCell + " " + stageTwoStartTime);
                Date stageTwoEnd = sdf.parse(dateCell + " " + stageTwoEndTime);

                // 本行是否全为空
                boolean hasValue = false;

                Map<String, List<NightDutyClassDto>> teacherDutyClazzMap = new HashMap<>();
                Map<String, TeacherDuty> teacherDutyDtoMap = new HashMap<>();
                Set<String> teacherStageOneSet = new HashSet<>();

                StringBuilder child = new StringBuilder();
                //判断是否存在第一阶段是否存在重复的教师名称
                StringBuilder childDuplicate = new StringBuilder();
                for (int columnIndex = 1; columnIndex < cells - 3; columnIndex = columnIndex + 1) {
                    teacherDuty = new TeacherDuty();
                    //设置校区
                    teacherDuty.setSchoolyardId(schoolyardId);
                    //列数量大于阶段数量，则说明是第二阶段的，反之则是第一阶段的
                    if (columnIndex > stage) {
                        teacherDuty.setDutyType(TeacherDutyTypeEnum.STAGE_TWO);
                        teacherDuty.setStartTime(stageTwoStart);
                        teacherDuty.setEndTime(stageTwoEnd);
                    } else {
                        teacherDuty.setDutyType(TeacherDutyTypeEnum.STAGE_ONE);
                        teacherDuty.setStartTime(stageOneStart);
                        teacherDuty.setEndTime(stageOneEnd);
                    }
                    //循环读取第二行数据
                    XSSFCell cell = sheet.getRow(rowIndex).getCell(columnIndex);
                    String cellValue = "";
                    if (cell == null) {
                        cellValue = "";
                    } else if (cell.getCellType().equals(CellType.STRING))
                        cellValue = cell.getStringCellValue();
                    else if (CellType.NUMERIC.equals(cell.getCellType()))
                        cellValue = String.valueOf(cell.getNumericCellValue());

                    String dateCellOffset = CellUtils.getCellValue(sheet.getRow(1).getCell(columnIndex)).replace("班", "");
                    Long offset = Long.parseLong(dateCellOffset);

                    if (staffMap.containsKey(cellValue) && columnIndex <= stage && !teacherStageOneSet.add(cellValue)) {
                        childDuplicate.append("第").append(columnIndex + 1).append("列").append(",");
                    } else if (staffMap.containsKey(cellValue)) {

                        hasValue = true;

                        teacherDuty.setTeacherId(staffMap.get(cellValue).get(0).getId());
                        teacherDuty.setDefault().validate(true);
                        //查询今天是否老师值日两个班级，是就合并
                        if (teacherDutyClazzMap.containsKey(cellValue + teacherDuty.getDutyType())) {
                            NightDutyClassDto nightDutyClassDto = new NightDutyClassDto();
                            if (teacherDuty.getDutyType().equals(TeacherDutyTypeEnum.STAGE_ONE)) {
                                nightDutyClassDto.setClazzId(offset);
                            } else {
                                nightDutyClassDto.setClazzId(offset);
                            }

                            List<NightDutyClassDto> nightDutyClassDtoList = teacherDutyClazzMap.get(cellValue + teacherDuty.getDutyType());
                            nightDutyClassDtoList.add(nightDutyClassDto);
                            if (teacherDutyDtoMap.containsKey(cellValue + teacherDuty.getDutyType())) {
                                teacherDutyDtoMap.get(cellValue + teacherDuty.getDutyType()).setTeacherDutyClazzList(nightDutyClassDtoList);
                            } else {
                                teacherDuty.setTeacherDutyClazzList(nightDutyClassDtoList);
                                teacherDutyDtoMap.put(cellValue + teacherDuty.getDutyType(), teacherDuty);
                                teacherDutyList.add(teacherDuty);
                            }
                        } else {
                            List<NightDutyClassDto> clazzes = new ArrayList<>();
                            NightDutyClassDto nightDutyClassDto = new NightDutyClassDto();
                            if (teacherDuty.getDutyType().equals(TeacherDutyTypeEnum.STAGE_ONE)) {
                                nightDutyClassDto.setClazzId(offset);
                            } else {
                                nightDutyClassDto.setClazzId(offset);
                            }
                            clazzes.add(nightDutyClassDto);
                            teacherDutyClazzMap.put(cellValue + teacherDuty.getDutyType(), clazzes);
                            if (teacherDutyDtoMap.containsKey(cellValue + teacherDuty.getDutyType())) {
                                teacherDutyDtoMap.get(cellValue + teacherDuty.getDutyType()).getTeacherDutyClazzList().add(nightDutyClassDto);
                            } else {
                                teacherDuty.setTeacherDutyClazzList(clazzes);
                                teacherDutyDtoMap.put(cellValue + teacherDuty.getDutyType(), teacherDuty);
                                teacherDutyList.add(teacherDuty);
                            }
                        }
                    } else if (!StringUtils.isNullOrEmpty(cellValue)) {
                        child.append("第").append(columnIndex + 1).append("列").append(",");
                    }

                }
                if (!(rowDate.getTime() < DateUtils.parse(DateUtils.format(currentDate, "yyyy-MM-dd"), "yyyy-MM-dd").getTime())) {
                    //年级总值班
                    String key = String.valueOf(sheet.getRow(rowIndex).getCell(columnNum - 3));
                    if (staffMap.containsKey(key)) {
                        hasValue = true;

                        String dateCell1 = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(columnNum - 1));
                        TeacherDuty totalTeacherDuty = new TeacherDuty();
                        totalTeacherDuty.setTeacherId(staffMap.get(key).get(0).getId());
                        totalTeacherDuty.setDutyType(TeacherDutyTypeEnum.GRADE_TOTAL_DUTY);
                        totalTeacherDuty.setStartTime(sdf.parse(dateCell + " " + stageOneStartTime));
                        totalTeacherDuty.setEndTime(sdf.parse(dateCell + " " + stageTwoEndTime));
                        totalTeacherDuty.setDutyMode("是".equals(dateCell1) ? TeacherDutyModeEnum.HOLIDAY : TeacherDutyModeEnum.NORMAL);
                        totalTeacherDuty.setSchoolyardId(schoolyardId);
                        totalTeacherDuty.setDefault().validate(true);
                        //默认年级总值班值本校区年级所有班级
                        List<NightDutyClassDto> nightDutyClassDtoList = clazzList.stream().map(item -> {
                            NightDutyClassDto nightDutyClassDto = new NightDutyClassDto();
                            nightDutyClassDto.setClazzId(item.getId());
                            return nightDutyClassDto;
                        }).collect(Collectors.toList());
                        totalTeacherDuty.setTeacherDutyClazzList(nightDutyClassDtoList);
                        teacherDutyList.add(totalTeacherDuty);
                    } else if (!StringUtils.isNullOrEmpty(key)) {
                        child.append("第").append(columnNum - 1).append("列").append(",");
                    }
                    //总值班
                    key = String.valueOf(sheet.getRow(rowIndex).getCell(columnNum - 2));
                    if (staffMap.containsKey(key)) {
                        hasValue = true;

                        TeacherDuty totalTeacherDuty = new TeacherDuty();
                        totalTeacherDuty.setSchoolyardId(schoolyardId);
                        totalTeacherDuty.setTeacherId(staffMap.get(key).get(0).getId());
                        totalTeacherDuty.setDutyType(TeacherDutyTypeEnum.TOTAL_DUTY);
                        totalTeacherDuty.setStartTime(sdf.parse(dateCell + " " + stageOneStartTime));
                        totalTeacherDuty.setEndTime(sdf.parse(dateCell + " " + stageTwoEndTime));
                        totalTeacherDuty.setDefault().validate(true);
                        teacherDutyList.add(totalTeacherDuty);
                    } else if (!StringUtils.isNullOrEmpty(key)) {
                        child.append("第").append(columnNum).append("列").append(",");
                    }
                }
                int len;
                if ((len = child.length()) > 0) {
                    child.deleteCharAt(len - 1);
                    child.append("老师名称有误");
                    sb.append("第").append(rowIndex + 1).append("行: ").append(child).append("\r\n");
                }

                if ((len = childDuplicate.length()) > 0) {
                    childDuplicate.deleteCharAt(len - 1);
                    childDuplicate.append("存在一阶段重复");
                    sb.append("第").append(rowIndex + 1).append("行: ").append(childDuplicate).append("\r\n");
                }

                if (!hasValue)
                    emptyList.add(rowDate);
            }

            if (sb.length() != 0) {
                throw new ApiCode.ApiException(-1, sb.toString());
            }

            for (Date date : emptyList)
                this.baseMapper.removeByTime(date, schoolyardId, gradeId, academicYearSemesterId);

            Map<String, Integer> map = new HashMap<>();
            for (TeacherDuty teacherDuty1 : teacherDutyList) {
                if (!map.containsKey(DateUtils.format(teacherDuty1.getStartTime(), "yyyy-MM-dd"))) {
                    map.put(DateUtils.format(teacherDuty1.getStartTime(), "yyyy-MM-dd"), 1);
                    this.baseMapper.removeByTime(teacherDuty1.getStartTime(), schoolyardId, gradeId, academicYearSemesterId);
                }
                importTeacherDuty(teacherDuty1, gradeId, academicYearSemesterId);
            }
            return "导入成功";
        } catch (IOException | InvalidFormatException e) {
            throw new ApiCode.ApiException(-1, "导入数据失败！");
        } catch (ParseException p) {
            throw new ApiCode.ApiException(-1, "时间解析失败");
        }
    }

    private void importTeacherDuty(TeacherDuty teacherDuty, Long gradeId, Long academicYearSemesterId) {
        // 如果是总值班 直接更新teacherId
        if (TeacherDutyTypeEnum.TOTAL_DUTY.equals(teacherDuty.getDutyType())) {
            TeacherDuty teacherDuty2 = this.baseMapper.selectOne(new QueryWrapper<TeacherDuty>()
                    .eq("schoolyard_id", teacherDuty.getSchoolyardId())
                    .eq("duty_type", "TOTAL_DUTY")
                    .apply("to_days(start_time)" + "=" + "to_days({0})", teacherDuty.getStartTime()));
            if (teacherDuty2 == null) {
                this.baseMapper.insert(teacherDuty);
            } else {
                teacherDuty2.setStartTime(teacherDuty.getStartTime());
                teacherDuty2.setEndTime(teacherDuty.getEndTime());
                teacherDuty2.setTeacherId(teacherDuty.getTeacherId());
                this.baseMapper.updateById(teacherDuty2);
            }
            return;
        }
        TeacherDuty teacherDuty1 = this.baseMapper.getByTimeDutyType(teacherDuty.getStartTime(), teacherDuty.getDutyType().toString(), teacherDuty.getTeacherId());
        if (teacherDuty1 != null) {
            teacherDuty1.setStartTime(teacherDuty.getStartTime());
            teacherDuty1.setEndTime(teacherDuty.getEndTime());
            teacherDuty1.setTeacherId(teacherDuty.getTeacherId());
            this.baseMapper.updateById(teacherDuty1);
            teacherDuty.setId(teacherDuty1.getId());
        } else {
            this.baseMapper.insert(teacherDuty);
        }
        List<TeacherDutyClazz> teacherDutyClazzes = new ArrayList<>();
        List<NightStudy> nightStudyList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(teacherDuty.getTeacherDutyClazzList())) {
            teacherDuty.getTeacherDutyClazzList().stream().forEach(teacherDutyClazzDto -> {
                TeacherDutyClazz teacherDutyClazz = new TeacherDutyClazz();
                teacherDutyClazz.setClazzId(teacherDutyClazzDto.getClazzId());
                teacherDutyClazz.setTeacherDutyId(teacherDuty.getId());
                teacherDutyClazz.setIsComfirm(YesNoEnum.NO);
                teacherDutyClazz.setIsLeaderComfirm(YesNoEnum.NO);
                teacherDutyClazzes.add(teacherDutyClazz);
            });
            this.teacherDutyClazzMapper.batchInsertWithId(teacherDutyClazzes);
            teacherDutyClazzes.stream().forEach(item -> {
                NightStudy nightStudy = new NightStudy();
                nightStudy.setShouldStudentCount(0);
                nightStudy.setActualStudentCount(0);
                nightStudy.setTeacherDutyClazzId(item.getId());
                nightStudyList.add(nightStudy);
            });
            this.nightStudyMapper.batchInsert(nightStudyList);
            List<Long> teacherDutyClazzIds = teacherDutyClazzes.stream().map(item -> item.getId()).collect(Collectors.toList());
            teacherDutyClazzMapper.updateClazzId(teacherDutyClazzIds, gradeId, academicYearSemesterId);
        }
    }

//    @Override
//    public Integer updateTeacher(NightDutyClassDto nightDutyClassDto) {
//        TeacherDuty teacherDuty = this.baseMapper.getByTimeAndClazzId(nightDutyClassDto.getClazzId(),nightDutyClassDto.getTime(),nightDutyClassDto.getTeacherDutyTypeEnum().toString());
//        TeacherDuty teacherDuty1 = this.baseMapper.getByTimeAndClazzId(nightDutyClassDto.getUpdateClazzId(),nightDutyClassDto.getTime(),nightDutyClassDto.getTeacherDutyTypeEnum().toString());
//        if(teacherDuty == null || teacherDuty1 == null ){
//            throw new ApiCode.ApiException(-5,"未查询到教师值班");
//        }else if(teacherDuty.getId().equals(teacherDuty1.getId())){
//            return 1;
//        }
//        teacherDutyClazzMapper.delete(Wrappers.<TeacherDutyClazz>lambdaQuery()
//                .eq(TeacherDutyClazz::getTeacherDutyId,teacherDuty.getId())
//                .eq(TeacherDutyClazz::getClazzId,nightDutyClassDto.getClazzId())
//        );
//
//        TeacherDutyClazz teacherDutyClazz = new TeacherDutyClazz();
//        teacherDutyClazz.setTeacherDutyId(teacherDuty1.getId());
//        teacherDutyClazz.setClazzId(nightDutyClassDto.getClazzId());
//        teacherDutyClazzMapper.insert(teacherDutyClazz);
//        TeacherDutySubstitute teacherDutySubstitute = new TeacherDutySubstitute();
//        teacherDutySubstitute.setTeacherDutyId(teacherDuty.getId());
//        teacherDutySubstitute.setTeacherId(teacherDuty1.getTeacherId());
//        teacherDutySubstitute.setTeacherOldId(teacherDuty.getTeacherId());
//        teacherDutySubstitute.setIsAgree(YesNoEnum.YES);
//        return teacherDutySubstituteMapper.insert(teacherDutySubstitute);
//    }


    /**
     * 更新值班老师逻辑变更
     *
     * @param nightDutyClassDto
     * @return
     */
    @Override
    public Integer updateTeacher(NightDutyClassDto nightDutyClassDto) {
        // 对应领导晚班
        NightStudyDutyClazz nightStudyDutyClazz = this.nightStudyDutyClazzMapper.selectById(nightDutyClassDto.getNightStudyDutyClazzId());
        NightStudyDuty nightStudyDuty = this.nightStudyDutyMapper.selectById(nightStudyDutyClazz.getNightStudyDutyId());
        Long schoolyardId = nightStudyDuty.getLeaderDuty().getSchoolyardId();

        //原始班级值班教师
        TeacherDuty teacherDuty = this.baseMapper.getByTimeAndClazzId(nightDutyClassDto.getClazzId(), nightDutyClassDto.getTime(), nightDutyClassDto.getTeacherDutyTypeEnum().toString());
        //新班级值班老师
        TeacherDuty teacherDuty1 = this.baseMapper.getByClazz(nightDutyClassDto.getTeacherNewId(), nightDutyClassDto.getTime(), nightDutyClassDto.getTeacherDutyTypeEnum().toString());
        //在day_teacher_duty表中插入新的数据，原来老数据不删除，实际上是需要删除的，不然导出老师工作量不正确
        if (teacherDuty == null) {
            if (teacherDuty1 == null) {
                teacherDuty1 = new TeacherDuty();
                teacherDuty1.setSchoolyardId(schoolyardId);
                teacherDuty1.setTeacherId(nightDutyClassDto.getTeacherNewId());
                Clazz clazz = clazzMapper.selectById(nightDutyClassDto.getClazzId());
                CourseTime courseTime = courseTimeMapper.selectOne(Wrappers.<CourseTime>lambdaQuery()
                        .eq(CourseTime::getSortOrder, Objects.equals(nightDutyClassDto.getTeacherDutyTypeEnum(), TeacherDutyTypeEnum.STAGE_ONE) ? 11 : 12)
                        .eq(CourseTime::getGradeId, clazz.getGradeId()));
                teacherDuty1.setStartTime(DateUtils.parse(courseTime.getStartTime(), nightDutyClassDto.getTime()));
                teacherDuty1.setEndTime(DateUtils.parse(courseTime.getEndTime(), nightDutyClassDto.getTime()));
                teacherDuty1.setDutyType(nightDutyClassDto.getTeacherDutyTypeEnum());
                teacherDuty1.setDefault().validate(true);
                this.baseMapper.insert(teacherDuty1);
            }
            TeacherDutyClazz teacherDutyClazz = new TeacherDutyClazz();
            teacherDutyClazz.setClazzId(nightDutyClassDto.getClazzId());
            teacherDutyClazz.setTeacherDutyId(teacherDuty1.getId());
            teacherDutyClazz.setDefault().validate(true);
            return teacherDutyClazzMapper.insert(teacherDutyClazz);
        } else if (teacherDuty1 != null && teacherDuty.getId().equals(teacherDuty1.getId())) {
            return 1;
        }
        if (teacherDuty1 == null) {
            teacherDuty1 = new TeacherDuty();
            teacherDuty1.setSchoolyardId(schoolyardId);
            teacherDuty1.setTeacherId(nightDutyClassDto.getTeacherNewId());
            teacherDuty1.setStartTime(teacherDuty.getStartTime());
            teacherDuty1.setEndTime(teacherDuty.getEndTime());
            teacherDuty1.setDutyType(nightDutyClassDto.getTeacherDutyTypeEnum());
            teacherDuty1.setDefault().validate(true);
            this.baseMapper.insert(teacherDuty1);
        }

        //替换表day_teacher_duty_clazz中teacher_duty_id为原值班教师的数据
        teacherDutyClazzMapper.update(new TeacherDutyClazz(), Wrappers.<TeacherDutyClazz>lambdaUpdate()
                .set(TeacherDutyClazz::getTeacherDutyId, teacherDuty1.getId())
                .eq(TeacherDutyClazz::getTeacherDutyId, teacherDuty.getId())
                .eq(TeacherDutyClazz::getClazzId, nightStudyDutyClazz.getClazzId())
        );
        teacherDutySubstituteMapper.update(new TeacherDutySubstitute(), Wrappers.<TeacherDutySubstitute>lambdaUpdate()
                .set(TeacherDutySubstitute::getTeacherDutyId, teacherDuty1.getId())
                .eq(TeacherDutySubstitute::getTeacherDutyId, teacherDuty.getId())
        );
        TeacherDutySubstitute teacherDutySubstitute = new TeacherDutySubstitute();
        teacherDutySubstitute.setTeacherDutyId(teacherDuty.getId());
        teacherDutySubstitute.setTeacherId(teacherDuty1.getTeacherId());
        teacherDutySubstitute.setTeacherOldId(teacherDuty.getTeacherId());
        teacherDutySubstitute.setIsAgree(YesNoEnum.YES);
        return teacherDutySubstituteMapper.insert(teacherDutySubstitute);
    }

    @Override
    @Transactional
    public Integer nightStudyConfirm(NightDutyClassDto nightDutyClassDto) {
        return teacherDutyClazzMapper.update(new TeacherDutyClazz(), Wrappers.<TeacherDutyClazz>lambdaUpdate()
                .set(TeacherDutyClazz::getIsComfirm, YesNoEnum.YES)
                .eq(TeacherDutyClazz::getId, nightDutyClassDto.getTeacherDutyClassId())
        );
//        NightStudy nightStudy = new NightStudy();
//        nightStudy.setTeacherDutyClazzId(nightDutyClassDto.getTeacherDutyClassId());
//        nightStudy.setActualStudentCount(nightDutyClassDto.getShouldStudentCount() == null ? nightDutyClassDto.getActualStudentCount(): nightDutyClassDto.getShouldStudentCount());
//        nightStudy.setShouldStudentCount(nightDutyClassDto.getShouldStudentCount());
//        nightStudy.setDefault().validate(true);
//        return nightStudyMapper.insert(nightStudy);
    }

    @Override
    @Transactional
    public Integer updateTeacherDuty(NightDutyClassDto nightDutyClassDto) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Staff staff = staffMapper.selectById(user.getStaffId());
        List<Long> clazzIds = nightDutyClassDto.getClazzIds();
        TeacherDuty teacherDuty = null;
        TeacherDutyClazz teacherDutyClazz = new TeacherDutyClazz();
        //查询原来班级绑定的信息
        if (!clazzIds.isEmpty()) {
            teacherDuty = this.baseMapper.getByTimeAndClazzIds(clazzIds, nightDutyClassDto.getTime(), nightDutyClassDto.getTeacherDutyTypeEnum().toString());
            teacherDutyClazz.setClazzIds(clazzIds);
            teacherDutyClazz.setTeacherDutyId(teacherDuty.getId());
        }
        if (teacherDuty == null || teacherDuty.getTeacherId().equals(staff.getId())) {
            throw new ApiCode.ApiException(-5, "未查询到教师值班或您无法替自己带班");
        }
        Integer count = teacherDutySubstituteMapper.selectCount(Wrappers.<TeacherDutySubstitute>lambdaQuery()
                .eq(TeacherDutySubstitute::getIsAgree, YesNoEnum.YES)
                .eq(TeacherDutySubstitute::getTeacherDutyId, teacherDuty.getId())
        );
        if (count > 0) {
            throw new ApiCode.ApiException(-5, "领导修改值班，无法被带班");
        }
        TeacherDuty teacherDuty1 = this.baseMapper.getByClazz(staff.getId(), nightDutyClassDto.getTime(), nightDutyClassDto.getTeacherDutyTypeEnum().toString());
        if (teacherDuty1 == null) {
            teacherDuty1 = new TeacherDuty();
            teacherDuty1.setSchoolyardId(teacherDuty.getSchoolyardId());
            teacherDuty1.setTeacherId(staff.getId());
            teacherDuty1.setStartTime(teacherDuty.getStartTime());
            teacherDuty1.setEndTime(teacherDuty.getEndTime());
            teacherDuty1.setDutyType(nightDutyClassDto.getTeacherDutyTypeEnum());
            teacherDuty1.setDefault().validate(true);
            this.baseMapper.insert(teacherDuty1);
        } else {
            List<TeacherDutyClazz> teacherDutyClazzes = teacherDutyClazzMapper.selectList(Wrappers.<TeacherDutyClazz>lambdaQuery()
                    .eq(TeacherDutyClazz::getTeacherDutyId, teacherDuty1.getId())
            );
            if (CollectionUtils.isNotEmpty(teacherDutyClazzes)) {
                throw new ApiCode.ApiException(-5, "您当天已有值班，无法带班！");
            }
        }
        //存在的两个老师的id是否被值班领导所确定
        List<Long> ids = new ArrayList<>();
        ids.add(teacherDuty1.getId());
        ids.add(teacherDuty.getId());
        Integer leaderConfirmCount = this.teacherDutyClazzMapper.selectCount(Wrappers.<TeacherDutyClazz>lambdaQuery()
                .in(TeacherDutyClazz::getTeacherDutyId, ids)
                .eq(TeacherDutyClazz::getIsLeaderComfirm, YesNoEnum.YES)
        );
        if (leaderConfirmCount > 0) {
            throw new ApiCode.ApiException(-5, "领导已确认您的值班或您代值的班级，无法代值");
        }
// 帮替一个班级
//        teacherDutyClazzMapper.deleteById(teacherDuty.getTeacherDutyClazzId());
//        TeacherDutyClazz teacherDutyClazz = new TeacherDutyClazz();
//        teacherDutyClazz.setTeacherDutyId(teacherDuty1.getId());
//        teacherDutyClazz.setClazzId(nightDutyClassDto.getClazzId());
//        teacherDutyClazzMapper.insert(teacherDutyClazz);
// 帮替一个所有班级
        teacherDutyClazzMapper.updateByClazzIds(teacherDuty1.getId(), teacherDutyClazz.getClazzIds(), teacherDutyClazz.getTeacherDutyId());
        //全部更新，实际上应该是根据选择的班级进行更新
        /*teacherDutyClazzMapper.update(new TeacherDutyClazz(), Wrappers.<TeacherDutyClazz>lambdaUpdate()
                .set(TeacherDutyClazz::getTeacherDutyId, teacherDuty1.getId())
                .in(TeacherDutyClazz::getClazzIds,nightDutyClassDto.getClazzIds())
                .eq(TeacherDutyClazz::getTeacherDutyId, teacherDuty.getId())
        );*/
        teacherDutySubstituteMapper.update(new TeacherDutySubstitute(), Wrappers.<TeacherDutySubstitute>lambdaUpdate()
                .set(TeacherDutySubstitute::getTeacherDutyId, teacherDuty1.getId())
                .eq(TeacherDutySubstitute::getTeacherDutyId, teacherDuty.getId())
        );
        TeacherDutySubstitute teacherDutySubstitute = new TeacherDutySubstitute();
        teacherDutySubstitute.setTeacherDutyId(teacherDuty1.getId());
        teacherDutySubstitute.setTeacherId(teacherDuty1.getTeacherId());
        teacherDutySubstitute.setTeacherOldId(teacherDuty.getTeacherId());
        return teacherDutySubstituteMapper.insert(teacherDutySubstitute);
    }

    @Override
    public IPage pageDetail(IPage teacherPage, TeacherDutyTypeEnum dutyType, String name, String clazzName, Date startTime, Date endTime, Long gradeId, Long clazzId, Long schoolyardId) {
        teacherPage.setTotal(this.baseMapper.countPageDetail(dutyType, name, clazzName, startTime, endTime, gradeId, clazzId, schoolyardId));
        List<NightDutyClassDto> nightDutyClassDtoList = this.baseMapper.pageDetail(teacherPage, dutyType, name, clazzName, startTime, endTime, gradeId, clazzId, schoolyardId);
//        nightDutyClassDtoList.stream().forEach(nightDutyClassDto->{
//            if(CollectionUtils.isNotEmpty(nightDutyClassDto.getNightStudyAttendances())){
//                NightStudyAttendance nightStudyAttendance = nightDutyClassDto.getNightStudyAttendances().get(nightDutyClassDto.getNightStudyAttendances().size()-1);
//                nightDutyClassDto.setActualStudentCount(nightStudyAttendance.getActualNum());
//                nightDutyClassDto.setShouldStudentCount(nightStudyAttendance.getShouldNum());
//            }
//        });
        for (NightDutyClassDto nightDutyClassDto : nightDutyClassDtoList) {
            nightDutyClassDto.setNightStudyDutyClazzDeductions(new ArrayList<>());
            // 这里用总值班
            TeacherDuty totalTeacherDuty = this.baseMapper.selectOne(Wrappers.<TeacherDuty>lambdaQuery()
                    .eq(TeacherDuty::getDutyType, "TOTAL_DUTY")
                    .eq(TeacherDuty::getSchoolyardId, nightDutyClassDto.getSchoolyardIdd())
                    .apply("to_days(start_time)" + "=" + "to_days({0})", nightDutyClassDto.getStartTime()));
            nightDutyClassDto.setLeaderName(totalTeacherDuty == null ? "" : totalTeacherDuty.getTeacher().getName());

            List<LeaderNightStudyDutyDto> leaderNightStudyDutyDtoList = this.nightStudyDutyMapper.getDetail(nightDutyClassDto.getStartTime(), nightDutyClassDto.getClazzId());
            if (CollectionUtils.isEmpty(leaderNightStudyDutyDtoList)) continue;
            LeaderNightStudyDutyDto leaderNightStudyDutyDto = leaderNightStudyDutyDtoList.get(TeacherDutyTypeEnum.STAGE_ONE.equals(dutyType) ? 0 : 1);

            if (CollectionUtils.isNotEmpty(leaderNightStudyDutyDto.getNightDutyClassDtoList())) {
                NightDutyClassDto nightDutyClassDto1 = leaderNightStudyDutyDto.getNightDutyClassDtoList().get(0);
                nightDutyClassDto.setScore(nightDutyClassDto1.getScore());
                if (nightDutyClassDto1.getNightStudyDutyClazzDeductions() != null)
                    nightDutyClassDto.setNightStudyDutyClazzDeductions(nightDutyClassDto1.getNightStudyDutyClazzDeductions());
            }
        }
        teacherPage.setRecords(nightDutyClassDtoList);
        return teacherPage;
    }

    @Override
    @Transactional
    public Integer cancelTeacherDuty(NightDutyClassDto nightDutyClassDto) {
        TeacherDuty teacherDuty = this.baseMapper.selectById(nightDutyClassDto.getTeacherDutyId());
        List<TeacherDutyClazz> teacherDutyClazzList = this.teacherDutyClazzMapper.selectList(Wrappers.<TeacherDutyClazz>lambdaQuery()
                .eq(TeacherDutyClazz::getTeacherDutyId, nightDutyClassDto.getTeacherDutyId())
        );
        if (teacherDutyClazzList == null || teacherDutyClazzList.size() == 0) {
            throw new ApiCode.ApiException(-5, "您当天没有值班，无法取消");
        }
        Integer count = teacherDutySubstituteMapper.selectCount(Wrappers.<TeacherDutySubstitute>lambdaQuery()
                .eq(TeacherDutySubstitute::getIsAgree, YesNoEnum.YES)
                .eq(TeacherDutySubstitute::getTeacherDutyId, teacherDuty.getId())
        );
        if (count > 0) {
            throw new ApiCode.ApiException(-5, "领导修改值班，无法取消");
        }
        Long leaderConfirmCount = teacherDutyClazzList.stream().filter(item -> YesNoEnum.YES.equals(item.getIsLeaderComfirm())).count();
        if (leaderConfirmCount > 0) {
            throw new ApiCode.ApiException(-5, "领导已确认，无法取消");
        }
        TeacherDuty teacherDuty1 = this.baseMapper.getByClazz(nightDutyClassDto.getTeacherNewId(), teacherDuty.getStartTime(), teacherDuty.getDutyType().toString());
        if (teacherDuty1 == null) {
            teacherDuty1 = new TeacherDuty();
            teacherDuty1.setSchoolyardId(teacherDuty.getSchoolyardId());
            teacherDuty1.setTeacherId(nightDutyClassDto.getTeacherNewId());
            teacherDuty1.setStartTime(teacherDuty.getStartTime());
            teacherDuty1.setEndTime(teacherDuty.getEndTime());
            teacherDuty1.setDutyType(teacherDuty.getDutyType());
            teacherDuty1.setDefault().validate(true);
            this.baseMapper.insert(teacherDuty1);
        }
        return teacherDutyClazzMapper.update(new TeacherDutyClazz(), Wrappers.<TeacherDutyClazz>lambdaUpdate()
                .set(TeacherDutyClazz::getTeacherDutyId, teacherDuty1.getId())
                .eq(TeacherDutyClazz::getTeacherDutyId, teacherDuty.getId())
        );
    }

    @Override
    public List<Staff> cancelTeacherList(Long teacherDutyId) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        TeacherDutySubstitute teacherDutySubstitute = teacherDutySubstituteMapper.selectOne(Wrappers.<TeacherDutySubstitute>lambdaQuery()
                .eq(TeacherDutySubstitute::getTeacherDutyId, teacherDutyId)
                .eq(TeacherDutySubstitute::getTeacherId, user.getStaffId())
                .orderByDesc(TeacherDutySubstitute::getId)
        );
        List<TeacherDutySubstitute> teacherDutySubstitutes = teacherDutySubstituteMapper.selectList(Wrappers.<TeacherDutySubstitute>lambdaQuery()
                .eq(TeacherDutySubstitute::getTeacherDutyId, teacherDutyId)
                .le(TeacherDutySubstitute::getId, teacherDutySubstitute.getId())
        );
        List<Long> ids = new ArrayList<>();
        teacherDutySubstitutes.stream().forEach(item -> {
            if (!item.getTeacherOldId().equals(user.getStaffId())) {
                ids.add(item.getTeacherOldId());
            }
        });
        return this.staffMapper.selectBatchIds(ids);
    }

    @Override
    public List<TeacherServerFormDto> getTeacherDutyByStaffId(Date timeFrom, Date timeTo) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        QueryWrapper<TeacherDuty> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_id", user.getStaffId());
        queryWrapper.ge("start_time", timeFrom);
        queryWrapper.le("start_time", timeTo);
        List<TeacherDuty> teacherDuties = this.baseMapper.selectList(queryWrapper);
        Map<String, List<TeacherDuty>> teacherDutyMap = teacherDuties.stream().collect(Collectors.groupingBy(item -> DateUtils.format(item.getStartTime(), "yyyy-MM-dd")));
        Map<String, TeacherServerFormDto> exist = new HashMap<>();

        for (String time : teacherDutyMap.keySet()) {
            for (TeacherDuty teacherDuty : teacherDutyMap.get(time)) {
                List<TeacherDutyClazz> teacherDutyClazzes = teacherDutyClazzMapper.selectList(Wrappers.<TeacherDutyClazz>lambdaQuery()
                        .eq(TeacherDutyClazz::getTeacherDutyId, teacherDuty.getId())
                );
                if (CollectionUtils.isEmpty(teacherDutyClazzes)) {
                    continue;
                }
                List<ClazzVo> clazzVos = this.parseClazzVo(teacherDutyClazzes);
                if (exist.containsKey(time)) {
                    TeacherServerFormDto teacherServerFormDto = exist.get(time);
                    Map<TeacherDutyTypeEnum, List<ClazzVo>> map = teacherServerFormDto.getClazzVoListMap();
                    map.put(teacherDuty.getDutyType(), clazzVos);
                    teacherServerFormDto.setClazzVoListMap(map);

                    List<ClazzVo> clazzList = teacherServerFormDto.getClazzVoList();
                    clazzList.addAll(clazzVos);
                    clazzList = clazzList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(()
                            -> new TreeSet<>(Comparator.comparing(ClazzVo::getId))), ArrayList::new));
                    teacherServerFormDto.setClazzVoList(clazzList);
                    exist.put(time, teacherServerFormDto);
                } else {
                    List<ClazzVo> clazzList = new ArrayList<>();
                    clazzList.addAll(clazzVos);
                    TeacherServerFormDto teacherServerFormDto = new TeacherServerFormDto();
                    teacherServerFormDto.setDutyType(teacherDuty.getDutyType());
                    teacherServerFormDto.setTime(teacherDuty.getStartTime());
                    teacherServerFormDto.setClazzVoList(clazzList);
                    teacherServerFormDto.setClazzVoListMap(new HashMap<TeacherDutyTypeEnum, List<ClazzVo>>() {{
                        this.put(teacherDuty.getDutyType(), clazzVos);
                    }});
                    exist.put(time, teacherServerFormDto);
                }
            }
        }
        QueryWrapper<LeaderDuty> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("leader_id", user.getStaffId());
        queryWrapper1.ge("start_time", timeFrom);
        queryWrapper1.le("start_time", timeTo);
        List<LeaderDuty> leaderDutyList = this.leaderDutyMapper.selectList(queryWrapper1);
        for (LeaderDuty leaderDuty : leaderDutyList) {
            String time = DateUtils.format(leaderDuty.getStartTime(), "yyyy-MM-dd");
            if (!exist.containsKey(time)) {
                TeacherServerFormDto teacherServerFormDto = new TeacherServerFormDto();
                TeacherDutyClazz teacherDutyClazz = teacherDutyClazzMapper.selectOne(Wrappers.<TeacherDutyClazz>lambdaQuery()
                        .eq(TeacherDutyClazz::getIsLeaderComfirm, YesNoEnum.NO)
                        .inSql(TeacherDutyClazz::getTeacherDutyId, "select id from day_teacher_duty where to_days(start_time) = " + "to_days(" + time + ")")
                );
                ClazzVo clazzVo = new ClazzVo();
                clazzVo.setIsLeaderConfirm(YesNoEnum.YES);
                if (teacherDutyClazz == null) {
                    clazzVo.setIsLeaderConfirm(YesNoEnum.NO);
                }
                teacherServerFormDto.setClazzVoList(new ArrayList<ClazzVo>() {{
                    this.add(clazzVo);
                }});
                teacherServerFormDto.setDutyType(TeacherDutyTypeEnum.TOTAL_DUTY);
                teacherServerFormDto.setTime(leaderDuty.getStartTime());
                teacherServerFormDto.setClazzVoListMap(new HashMap<TeacherDutyTypeEnum, List<ClazzVo>>() {{
                    this.put(TeacherDutyTypeEnum.TOTAL_DUTY, teacherServerFormDto.getClazzVoList());
                }});
                exist.put(time, teacherServerFormDto);
            }
        }
        return exist.values().stream().map(item -> (TeacherServerFormDto) item).collect(Collectors.toList());
    }

    @Override
    public Integer updateDutyMode(TeacherServerFormDto teacherServerFormDto) {
        if (teacherServerFormDto.getTime() == null || teacherServerFormDto.getDutyMode() == null || StringUtils.isNullOrEmpty(teacherServerFormDto.getGradeDutyTeacher())) {
            throw new ApiCode.ApiException(-5, "请传入时间及模式及值班老师");
        }
        return this.teacherDutyMapper.updateByTime(teacherServerFormDto.getTime(), teacherServerFormDto.getDutyMode(), teacherServerFormDto.getGradeDutyTeacher());
    }

    @Override
    @SneakyThrows
    public XSSFWorkbook exportExcelWorkAmount(Long schoolyardId, Long gradeId, Date startTime, Date endTime) {
        if (endTime == null) endTime = new Date();

        InputStream is = getClass().getResourceAsStream("/static/templates/晚自习工作量统计.xlsx");
        XSSFWorkbook book = new XSSFWorkbook(is);

        List<TeacherDutyDto> teacherDutyList = this.baseMapper.selectListWithClazz(schoolyardId, gradeId, startTime, endTime);
        if (CollectionUtils.isEmpty(teacherDutyList)) return book;

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
        int startRow = 1;

        Map<String, List<TeacherDutyDto>> teacherDutyGradeTotalMap = teacherDutyList.stream().filter(item ->
                TeacherDutyTypeEnum.GRADE_TOTAL_DUTY.equals(item.getDutyType()) && CollectionUtils.isNotEmpty(item.getTeacherDutyClazzList()) && TeacherDutyModeEnum.HOLIDAY.equals(item.getDutyMode())
        ).collect(Collectors.groupingBy(item -> DateUtils.format(item.getStartTime(), "yyyyMMdd")));

        Map<Long, List<TeacherDutyDto>> map = teacherDutyList.stream().collect(Collectors.groupingBy(TeacherDutyDto::getTeacherId));
        for (Map.Entry<Long, List<TeacherDutyDto>> entry : map.entrySet()) {
            List<TeacherDutyDto> value = entry.getValue();

            Staff currStaff = value.get(0).getTeacher();
            if (null == currStaff) {
                continue;
            }

            row = sheet.createRow(startRow++);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue(currStaff.getName());

            cell = row.createCell(1, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue(value.stream().filter(item ->
                    TeacherDutyTypeEnum.STAGE_ONE.equals(item.getDutyType()) && CollectionUtils.isNotEmpty(item.getTeacherDutyClazzList())
            ).count());

            cell = row.createCell(2, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue(value.stream().filter(item ->
                    TeacherDutyTypeEnum.STAGE_TWO.equals(item.getDutyType()) && CollectionUtils.isNotEmpty(item.getTeacherDutyClazzList())
            ).count());

            cell = row.createCell(3, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue(value.stream().filter(item ->
                    TeacherDutyTypeEnum.GRADE_TOTAL_DUTY.equals(item.getDutyType()) && CollectionUtils.isNotEmpty(item.getTeacherDutyClazzList()) && TeacherDutyModeEnum.HOLIDAY.equals(item.getDutyMode())
            ).count());

            cell = row.createCell(4, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue(value.stream().filter(item -> {
                String date = DateUtils.format(item.getStartTime(), "yyyyMMdd");
                return TeacherDutyTypeEnum.TOTAL_DUTY.equals(item.getDutyType()) && !teacherDutyGradeTotalMap.containsKey(date);
            }).count());
        }
        return book;
    }

    @Override
    @SneakyThrows
    public XSSFWorkbook exportExcel(Long schoolyardId, Long gradeId, Date timeFrom, Date timeTo) {
        if (timeTo == null) timeTo = new Date();

        InputStream is = getClass().getResourceAsStream("/static/templates/晚自习值班统计.xlsx");
        XSSFWorkbook book = new XSSFWorkbook(is);

        Page<Map<String, Object>> page = this.getTeacherDutyFormV2(1, 9999, timeFrom, timeTo, null, gradeId, schoolyardId, YesNoEnum.NO);
        List<Map<String, Object>> records = page.getRecords();

        XSSFCellStyle style = book.createCellStyle();
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        for (Map<String, Object> record : records) {
            this.putData(book, style, record);
        }

        return book;
    }


    /**
     * 查询指定日期之内的老师
     *
     * @param schoolyardId
     * @param time
     * @param classId
     * @param stage
     * @return
     */
    @Override
    public String searchOneTeacher(Long schoolyardId, Long gradeId, Date time, Long classId, TeacherDutyTypeEnum stage) {
        String str = new String();
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        String format = sdf.format(time);
        String gradeName = StringUtils.gradeName(gradeId);
        str = format+" ";
        //查询当前用户的登录信息(管理员角色)
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        log.info("登录用户的信息是---->{}", user);
        List<Role> roles = user.getRoleList();
        List<String> roleName = roles.stream() // 将roles集合转化为流
                .map(Role::getName) // 提取每个Role的name属性
                .filter(org.apache.commons.lang3.StringUtils::isNotEmpty) // 过滤掉空的name
                .collect(Collectors.toList());
        if (!roleName.contains("ROLE_ADMIN")) {
            return str = "您暂无当前操作的权限";
        }

        Date endTime = DateUtils.parse(DateUtils.format(time).replace("00:00:00", "23:59:59"));
        //首先根据时间、校区、阶段将这个时间内的所有老师都拿出来
        List<TeacherDuty> teacherDuties = this.teacherDutyMapper.selectList(Wrappers.<TeacherDuty>lambdaQuery()
                .eq(TeacherDuty::getSchoolyardId, schoolyardId)
                .eq(TeacherDuty::getDutyType, stage)
                .gt(TeacherDuty::getStartTime, time)
                .lt(TeacherDuty::getEndTime, endTime)
        );
        if (CollectionUtils.isNotEmpty(teacherDuties)) {
            //将id收集起来
            List<Long> ids = teacherDuties.stream().map(TeacherDuty::getId).collect(Collectors.toList());
            List<TeacherDutyClazz> clazzes = this.teacherDutyClazzMapper.selectList(Wrappers.<TeacherDutyClazz>lambdaQuery()
                    .eq(TeacherDutyClazz::getClazzId, classId)
                    .in(TeacherDutyClazz::getTeacherDutyId, ids));
            if (CollectionUtils.isNotEmpty(clazzes)) {
                Clazz clazz = this.clazzMapper.selectById(clazzes.get(0).getClazzId());
                TeacherDuty teacherDuty = this.teacherDutyMapper.selectById(clazzes.get(0).getTeacherDutyId());
                if (teacherDuty != null && clazz != null) {
                    str += "值班教师: ";
                    Staff staff = this.staffMapper.selectById(teacherDuty.getTeacherId());
                    if (staff != null) {
                        str += staff.getName();
                    }
                }
            }
        }
        return str;
    }


    /**
     * 变更老师
     *
     * @param schoolyardId
     * @param gradeId
     * @param time
     * @param classId
     * @param stage
     * @param name
     * @return
     */
    @Override
    public Integer updateOneTeacher(NightDutyClassDto dto) {
        log.info("request param-->{}", dto);
        Long schoolyardId = null;
        Long classId = null;
        Date time = null;
        TeacherDutyTypeEnum stage = null;
        String name = null;
        if (dto != null) {
            classId = dto.getClazzId();
            time = dto.getTime();
            stage = dto.getTeacherDutyTypeEnum();
            name = dto.getTeacherName();
            schoolyardId = dto.getSchoolyardIdd();
        }
        Date endTime = DateUtils.parse(DateUtils.format(time).replace("00:00:00", "23:59:59"));


        //原先绑定的教师
        TeacherDuty duty = this.teacherDutyMapper.selectByClassId(classId, time, endTime, stage);

        if (duty != null) {
            //根据老师的名称查找老师的教职工的id
            Staff staff = this.staffMapper.selectOne(Wrappers.<Staff>lambdaQuery().eq(Staff::getName, name));
            if (staff != null) {
                //查班级和值班老师的关联表
                TeacherDutyClazz teacherDutyClazz = this.teacherDutyClazzMapper.selectOne(Wrappers.<TeacherDutyClazz>lambdaQuery()
                        .eq(TeacherDutyClazz::getTeacherDutyId, duty.getId())
                        .eq(TeacherDutyClazz::getClazzId, classId));
                if (teacherDutyClazz != null) {
                    //查看教职工原先是否存在
                    TeacherDuty teacherDuty1 = this.teacherDutyMapper.selectOne(Wrappers.<TeacherDuty>lambdaQuery()
                            .eq(TeacherDuty::getTeacherId, staff.getId())
                            .eq(TeacherDuty::getSchoolyardId, schoolyardId)
                            .eq(TeacherDuty::getDutyType, stage)
                            .gt(TeacherDuty::getStartTime, time)
                            .lt(TeacherDuty::getEndTime, endTime)
                    );
                    if (teacherDuty1 != null) {
                        //比对原先绑定的id
                        if (teacherDutyClazz.getTeacherDutyId() != teacherDuty1.getId()) {
                            Integer integer = this.teacherDutyClazzMapper.updateTeacherDutyClazzByClassId(classId, teacherDutyClazz.getId(), teacherDuty1.getId());
                            return integer;
                        }
                    } else {
                        TeacherDuty teacherDuty = new TeacherDuty();
                        teacherDuty.setTeacherId(staff.getId());
                        teacherDuty.setSchoolyardId(schoolyardId);
                        teacherDuty.setDutyType(stage);
                        teacherDuty.setStartTime(duty.getStartTime());
                        teacherDuty.setEndTime(duty.getEndTime());
                        Integer insert = this.teacherDutyMapper.insertReturnId(teacherDuty);
                        Integer integer = this.teacherDutyClazzMapper.updateTeacherDutyClazzByClassId(classId, teacherDutyClazz.getId(), teacherDuty.getId());
                        return integer;
                    }
                }
            } else {
                return 1002;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void putData(XSSFWorkbook book, XSSFCellStyle style, Map<String, Object> data) {
        String gradeName = data.get("gradeName").toString();
        XSSFSheet sheet = book.getSheet(gradeName);

        List<Map<String, Object>> headerList = (List<Map<String, Object>>) data.get("clazzList");
        List<TeacherServerFormDto> dataList = (List<TeacherServerFormDto>) data.get("gradeList");

        if (CollectionUtils.isNotEmpty(dataList)) {
            this.setHeader(sheet, style, headerList);
            this.setCell(sheet, style, dataList, headerList.size());
        }
    }

    private void setCell(XSSFSheet sheet, XSSFCellStyle style, List<TeacherServerFormDto> dataList, int size) {
        XSSFRow row;
        XSSFCell cell;
        int startRow = 2;

        for (TeacherServerFormDto teacherServerFormDto : dataList) {
            String[] timeArray = DateUtils.format(teacherServerFormDto.getTime(), "yyyy-MM-dd E").split("\\s");

            row = sheet.createRow(startRow++);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue(timeArray[0]);

            cell = row.createCell(1, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue(timeArray[1]);

            List<ClazzVo> clazzVos = teacherServerFormDto.getClazzVoList();
            if (CollectionUtils.isNotEmpty(clazzVos)) {
                clazzVos.sort(Comparator.comparing(item -> Integer.parseInt(item.getName().replace("班", ""))));
                for (int j = 0; j < clazzVos.size(); j++) {
                    ClazzVo clazzVo = clazzVos.get(j);

                    cell = row.createCell(j + 2, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(clazzVo.getStageOneTeacher());

                    cell = row.createCell(j + 2 + clazzVos.size(), CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(clazzVo.getStageTwoTeacher());
                }
            }

            int last = 2 + (size << 1);
            cell = row.createCell(last, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue(teacherServerFormDto.getGradeDutyTeacher());

            cell = row.createCell(last + 1, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue(teacherServerFormDto.getTotalDutyTeacherYard());

            cell = row.createCell(last + 2, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue(teacherServerFormDto.getDutyMode().equals(TeacherDutyModeEnum.HOLIDAY) ? "是" : "否");
        }

    }

    private void setHeader(XSSFSheet sheet, XSSFCellStyle style, List<Map<String, Object>> headerList) {
        XSSFRow row;
        XSSFCell cell;
        int size = headerList.size() + 1;

        CellRangeAddress rangeAddress = new CellRangeAddress(0, 0, 2, size);
        sheet.addMergedRegion(rangeAddress);

        int end = size + headerList.size();

        CellRangeAddress rangeAddress1 = new CellRangeAddress(0, 0, size + 1, end);
        sheet.addMergedRegion(rangeAddress1);

        row = sheet.createRow(0);
        cell = row.createCell(2, CellType.STRING);
        cell.setCellStyle(style);
        cell.setCellValue("第一阶段");

        cell = row.createCell(size + 1, CellType.STRING);
        cell.setCellStyle(style);
        cell.setCellValue("第二阶段");

        CellRangeAddress rangeAddressRq = new CellRangeAddress(0, 1, 0, 0);
        sheet.addMergedRegion(rangeAddressRq);
        cell = row.createCell(0, CellType.STRING);
        cell.setCellStyle(style);
        cell.setCellValue("日期");

        CellRangeAddress rangeAddressXq = new CellRangeAddress(0, 1, 1, 1);
        sheet.addMergedRegion(rangeAddressXq);
        cell = row.createCell(1, CellType.STRING);
        cell.setCellStyle(style);
        cell.setCellValue("星期");

        CellRangeAddress rangeAddress2 = new CellRangeAddress(0, 1, end + 1, end + 1);
        sheet.addMergedRegion(rangeAddress2);
        cell = row.createCell(end + 1, CellType.STRING);
        cell.setCellStyle(style);
        cell.setCellValue("年级值班(兴隆)");

        CellRangeAddress rangeAddress3 = new CellRangeAddress(0, 1, end + 2, end + 2);
        sheet.addMergedRegion(rangeAddress3);
        cell = row.createCell(end + 2, CellType.STRING);
        cell.setCellStyle(style);
        cell.setCellValue("总值班");

        CellRangeAddress rangeAddress4 = new CellRangeAddress(0, 1, end + 3, end + 3);
        sheet.addMergedRegion(rangeAddress4);
        cell = row.createCell(end + 3, CellType.STRING);
        cell.setCellStyle(style);
        cell.setCellValue("是否假期");

        row = sheet.createRow(1);
        headerList.sort(Comparator.comparing(item -> Integer.parseInt(item.get("name").toString().replace("班", ""))));
        for (int i = 0; i < headerList.size(); i++) {
            Map<String, Object> header = headerList.get(i);
            String name = header.get("name").toString();

            cell = row.createCell(2 + i, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue(name);

            cell = row.createCell(1 + i + size, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue(name);

        }
    }

    private List<ClazzVo> parseClazzVo(List<TeacherDutyClazz> teacherDutyClazzes) {
        return teacherDutyClazzes.stream().map(teacherDutyClazz -> {
            ClazzVo clazzVo = new ClazzVo();
            clazzVo.setGradeName(teacherDutyClazz.getClazz().getGrade().getName());
            clazzVo.setName(teacherDutyClazz.getClazz().getName());
            clazzVo.setId(teacherDutyClazz.getClazzId());
            clazzVo.setIsConfirm(teacherDutyClazz.getIsComfirm());
            clazzVo.setIsLeaderConfirm(teacherDutyClazz.getIsLeaderComfirm());
            return clazzVo;
        }).collect(Collectors.toList());
    }
}
