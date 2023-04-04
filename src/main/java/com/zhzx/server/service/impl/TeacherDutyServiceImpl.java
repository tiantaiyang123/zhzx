/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：教师值班表
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
import com.zhzx.server.enums.RoutineEnum;
import com.zhzx.server.enums.TeacherDutyModeEnum;
import com.zhzx.server.enums.TeacherDutyTypeEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.repository.*;
import com.zhzx.server.repository.base.TeacherDutyBaseMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.TeacherDutyService;
import com.zhzx.server.util.CellUtils;
import com.zhzx.server.util.DateUtils;
import com.zhzx.server.util.StringUtils;
import com.zhzx.server.vo.ClazzVo;
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
import java.util.stream.Collectors;

@Service
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
        // TODO: 2022/9/2 2312ewdqwd
        Page<Map<String,Object>> page = this.getTeacherDutyForm(1, 9999, timeFrom, timeTo, null, gradeId, null);
        List<Map<String,Object>> list = page.getRecords();
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        }
        Map<String, Object> curr = list.get(0);
        List<TeacherServerFormDto> gradeList = (List<TeacherServerFormDto>) curr.get("gradeList");
        List<Map<String, Object>> clazzList = (List<Map<String, Object>>) curr.get("clazzList");
        clazzList.sort(Comparator.comparing(o -> Integer.parseInt(o.get("name").toString().replace("班", "")), Comparator.naturalOrder()));

        int padding = 2;
        int columnWidth = this.getStringWidthHeight("综治办")[0];
        int rowHeight = this.getStringWidthHeight("综治办")[1];
        int rowHeightDay = rowHeight * 2 + padding * 3;
        int rowHeightOther = rowHeight + padding * 3 / 2;
        int ascent = this.getStringWidthHeight("综治办")[2];
        int imageWidth = 80 + (clazzList.size() + 1) * (columnWidth + padding * 3) * 2;
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
        graphics.drawLine(90, 10 + rowHeightOther, 90 + (columnWidth + 3 * padding) * clazzList.size() * 2, 10 + rowHeightOther);
        int x = 90, middle = (rowHeight + padding * 3) / 2;
        graphics.drawString("日期", 10 + (80 - this.getStringWidthHeight("日期")[0]) / 2, 5 + middle + ascent);
        for (int i = 0; i < clazzList.size() * 2; i++) {
            int p = x;
            if (i == 0 || i == clazzList.size()) {
                graphics.drawLine(x, 10, x, 10 + imageHeight);
                graphics.drawString(i == 0 ? "第一阶段" : "第二阶段", x + ((columnWidth + 3 * padding)* clazzList.size() - this.getStringWidthHeight("第一阶段")[0]) / 2, 10 + ascent + padding);
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
                graphics.drawString("总值班", x + 3 * padding / 2, 10 + ascent + middle);
            }
        }

        int y = rowHeightDay + 10;
        for (TeacherServerFormDto teacherServerFormDto : gradeList) {
            teacherServerFormDto.getClazzVoList().sort(Comparator.comparing(o -> Integer.parseInt(o.getName().replace("班", "")), Comparator.naturalOrder()));
            String time = DateUtils.format(teacherServerFormDto.getTime(), "MM-dd");
            graphics.drawString(time, 10 + (80 + 3 * padding - this.getStringWidthHeight(time)[0]) / 2, y + ascent + padding);
            int xx = 90;
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
            graphics.drawString(g, xx + (columnWidth + 3 * padding - this.getStringWidthHeight(g)[0]) / 2, y + ascent + padding);
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
    public  Map<Object,Object> nightRoutine(Date time,RoutineEnum type) {

        Map<Object,Object> map = new HashMap<>();

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Staff staff = staffMapper.selectById(user.getStaffId());
        List<TeacherDutyDto> teacherDutyDtoList = this.baseMapper.nightRoutine(time,type.toString(),staff.getId(),new ArrayList<String>(){{
            this.add(TeacherDutyTypeEnum.STAGE_ONE.toString());
            this.add(TeacherDutyTypeEnum.STAGE_TWO.toString());
        }});

        //查询年级及总值班信息
        TeacherDutyDto gradeTeacherDuty = this.getGradeTeacherDuty(time);
        map.put("totalDutyTeacher",gradeTeacherDuty.getTotalDutyTeacher());
        map.put("gradeOneTeacher",gradeTeacherDuty.getGradeOneTeacher());
        map.put("gradeTwoTeacher",gradeTeacherDuty.getGradeTwoTeacher());
        map.put("gradeThreeTeacher",gradeTeacherDuty.getGradeThreeTeacher());


        for (TeacherDutyDto teacherDutyDto : teacherDutyDtoList) {
            teacherDutyDto.getTeacherDutyClazzList().stream().forEach(nightDutyClassDto ->{
                List<CommentImages> commentImagesList = new ArrayList<>();
                if(CollectionUtils.isNotEmpty(nightDutyClassDto.getCommentDtoList())){
                    for(CommentDto commentDto : nightDutyClassDto.getCommentDtoList()){
                        if(commentDto.getCommentImagesList() != null){
                            commentImagesList.addAll(commentDto.getCommentImagesList());
                        }
                    }
                }
                nightDutyClassDto.setPicList(commentImagesList);
                if(CollectionUtils.isNotEmpty(nightDutyClassDto.getNightStudyAttendances())){
                    NightStudyAttendance nightStudyAttendance = nightDutyClassDto.getNightStudyAttendances().get(nightDutyClassDto.getNightStudyAttendances().size()-1);
                    nightDutyClassDto.setActualStudentCount(nightStudyAttendance.getActualNum());
                    nightDutyClassDto.setShouldStudentCount(nightStudyAttendance.getShouldNum());
                    Map<String,List<NightStudyAttendance>> detailMap = nightDutyClassDto.getNightStudyAttendances().stream()
                            .collect(Collectors.groupingBy(entity->entity.getClassify()));
                    nightDutyClassDto.setNightStudyDetailMap(detailMap);
                }
            });
        }
        Map<TeacherDutyTypeEnum,List<TeacherDutyDto>> stageMap = teacherDutyDtoList.stream().collect(Collectors.groupingBy(TeacherDutyDto::getDutyType));
        stageMap.keySet().stream().forEach(teacherDutyTypeEnum -> {
            TeacherDutyDto teacherDutyDto = stageMap.get(teacherDutyTypeEnum).get(0);
            if(CollectionUtils.isNotEmpty(teacherDutyDto.getTeacherDutyClazzList()) && teacherDutyDto.getTeacherDutyClazzList().get(0).getTeacherDutyClassId() != null){
                map.put(teacherDutyTypeEnum,stageMap.get(teacherDutyTypeEnum).get(0));
            }
        });
        List<Label> labels = labelMapper.selectList(Wrappers.<Label>lambdaQuery().eq(Label::getClassify,"WZXKQ"));
        map.put("classify",labels);
        return map;
    }

    @Override
    public Page<Map<String,Object>> getTeacherDutyForm(Integer pageNum, Integer pageSize, Date timeFrom, Date timeTo, String teacherDutyName,Long gradeId, Long schoolyardId) {
        Page<Map<String,Object>> page = new Page();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        List<TeacherServerFormDto> timeList = this.baseMapper.getTeacherDutyForm(page,timeFrom,timeTo,teacherDutyName,null, schoolyardId);
//        List<Date> dateList = timeList.stream().map(teacherServerFormDto -> teacherServerFormDto.getTime()).collect(Collectors.toList());
        Map<Date,List<TeacherServerFormDto>> timeMap = timeList.stream().collect(Collectors.groupingBy(item->item.getTime()));
        List<Date> dateList = timeMap.keySet()
                .stream()
                .collect(Collectors.toList());
        List<Map<String,Object>> formList = new ArrayList<>();
        if(CollectionUtils.isEmpty(dateList)) return page;
        List<ClazzVo> clazzVoList = this.baseMapper.getFormList(dateList,gradeId, schoolyardId);

        if(CollectionUtils.isNotEmpty(clazzVoList)){
            Map<String,List<ClazzVo>> gradeMap = clazzVoList.stream().collect(Collectors.groupingBy(ClazzVo::getGradeName));
            for (String key : gradeMap.keySet()) {
                List<Map<String,Object>> headerList = new ArrayList<>();
                Map<String ,Object> formMap = new HashMap<>();
                Map<Date,List<ClazzVo>> timeTeacherMap = gradeMap.get(key).stream().collect(Collectors.groupingBy(ClazzVo::getTime));
                List<TeacherServerFormDto> list = new ArrayList<>();
                Map<Date, List<ClazzVo>> resultMap = new TreeMap<>((str1, str2) -> str1.compareTo(str2));
                resultMap.putAll(timeTeacherMap);
                Boolean flag = false;
                for (Date date : resultMap.keySet()) {
                    List<ClazzVo> clazzVos = timeTeacherMap.get(date);

                    TeacherServerFormDto teacherServerFormDto = new TeacherServerFormDto();
                    teacherServerFormDto.setDutyMode(TeacherDutyModeEnum.NORMAL);
                    if(timeMap.containsKey(date)){
                        //todo 这里需要修改
                        TeacherDutyGradeTotalDto teacherDuty2 = this.baseMapper.selectTeacherDutyGradeTotalDto(date, 1L, clazzVos.get(0).getGradeId());
                        if (teacherDuty2 != null)
                            teacherServerFormDto.setDutyMode(teacherDuty2.getDutyMode());
                    }
                    teacherServerFormDto.setTime(date);
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.CHINA);
                    teacherServerFormDto.setWeek(sdf.format(date));
                    for (ClazzVo item : clazzVos) {
                        if(!flag){
                            Map<String,Object> headerMap = new HashMap<>();
                            headerMap.put("name",item.getName());
                            headerList.add(headerMap);
                        }
                        if(item.getGradeDutyTeacher() != null){
                            teacherServerFormDto.setGradeDutyTeacher(item.getGradeDutyTeacher());
                        }
                        if(item.getTotalDutyTeacher() != null){
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
                formMap.put("gradeList",list);
                formMap.put("gradeName",key);
                formMap.put("clazzList",headerList);
                formList.add(formMap);
            }
        }
        page.setRecords(formList);
        return page;
    }

    public TeacherDutyDto getGradeTeacherDuty (Date time){
        //查询年级及总值班信息
        List<TeacherDutyDto> totalDutyList = this.baseMapper.getTotalDutyList(time);

        List<TeacherDutyDto> totalDuty = totalDutyList.stream().filter(teacherDutyDto -> TeacherDutyTypeEnum.TOTAL_DUTY.equals(teacherDutyDto.getDutyType())).collect(Collectors.toList());
        String totalDutyTeacher = "";
        String gradeOneTeacher = "";
        String gradeTwoTeacher = "";
        String gradeThreeTeacher = "";
        if(CollectionUtils.isNotEmpty(totalDuty))  totalDutyTeacher = totalDuty.stream().map(TeacherDutyDto::getTeacherName).collect(Collectors.joining(","));

        totalDutyList = totalDutyList.stream().filter(item -> !StringUtils.isNullOrEmpty(item.getGradeName())).collect(Collectors.toList());
        List<TeacherDutyDto> gradeTotalDutyList = totalDutyList.stream().filter(teacherDutyDto -> TeacherDutyTypeEnum.GRADE_TOTAL_DUTY.equals(teacherDutyDto.getDutyType())).collect(Collectors.toList());

        List<TeacherDutyDto> gradeOneTotalDutyList = gradeTotalDutyList.stream().filter(gradeTotalDuty ->gradeTotalDuty.getGradeName().contains("一")).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(gradeOneTotalDutyList)) gradeOneTeacher = gradeOneTotalDutyList.stream().map(TeacherDutyDto::getTeacherName).collect(Collectors.joining(","));

        List<TeacherDutyDto> gradeTwoTotalDutyList = gradeTotalDutyList.stream().filter(gradeTotalDuty ->gradeTotalDuty.getGradeName().contains("二")).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(gradeTwoTotalDutyList)) gradeTwoTeacher = gradeTwoTotalDutyList.stream().map(TeacherDutyDto::getTeacherName).collect(Collectors.joining(","));

        List<TeacherDutyDto> gradeThreeTotalDutyList = gradeTotalDutyList.stream().filter(gradeTotalDuty ->gradeTotalDuty.getGradeName().contains("三")).collect(Collectors.toList());

        if(CollectionUtils.isNotEmpty(gradeThreeTotalDutyList)) gradeThreeTeacher = gradeThreeTotalDutyList.stream().map(TeacherDutyDto::getTeacherName).collect(Collectors.joining(","));
        TeacherDutyDto teacherDutyDto = new TeacherDutyDto() ;
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
        QueryWrapper<Staff> queryWrapper =  new QueryWrapper<>();
        queryWrapper.eq("is_delete",YesNoEnum.NO);
        List<Staff> staffList = this.staffMapper.selectList(queryWrapper);
        Map<String,List<Staff>> staffMap = staffList.stream().collect(Collectors.groupingBy(Staff::getName));
        Integer existClazzCount = clazzMapper.selectCount(Wrappers.<Clazz>lambdaQuery()
                .eq(Clazz::getGradeId,gradeId)
                .eq(Clazz::getSchoolyardId, schoolyardId)
                .eq(Clazz::getAcademicYearSemesterId,academicYearSemesterId)
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
            // 教师值班列表
            List<TeacherDuty> teacherDutyList = new ArrayList<>();
            // 读取单元格数据
            TeacherDuty teacherDuty = null;
            int clazz = (columnNum - 4)%2;
            if(clazz != 0){
                throw new ApiCode.ApiException(-5,"格式错误");
            }
            int stage = (columnNum - 4)/2;
            if(!Objects.equals(stage,existClazzCount)){
                throw new ApiCode.ApiException(-5,"班级行数错误");
            }

            String stageOne = sheet.getRow(0).getCell(1).toString().replace("（","(").replace("）",")");
            String stageTwo = sheet.getRow(0).getCell(stage+1).toString().replace("（","(").replace("）",")");

            if(!(stageOne.contains("(") && stageOne.contains(")"))){
                throw new ApiCode.ApiException(-5,"第一阶段开始结束时间必传！");
            }
            if(!(stageTwo.contains("(") && stageTwo.contains(")"))){
                throw new ApiCode.ApiException(-5,"第二阶段开始结束时间必传！");
            }
            String stageOneStartTime = stageOne.split("\\(")[1].split("\\)")[0].split("-")[0];
            String stageOneEndTime = stageOne.split("\\(")[1].split("\\)")[0].split("-")[1];
            String stageTwoStartTime = stageTwo.split("\\(")[1].split("\\)")[0].split("-")[0];
            String stageTwoEndTime = stageTwo.split("\\(")[1].split("\\)")[0].split("-")[1];

            courseTimeMapper.update(new CourseTime(),Wrappers.<CourseTime>lambdaUpdate()
                    .set(CourseTime::getStartTime,stageOneStartTime)
                    .set(CourseTime::getEndTime,stageOneEndTime)
                    .eq(CourseTime::getSortOrder,11)
                    .eq(CourseTime::getGradeId,gradeId)
            );
            courseTimeMapper.update(new CourseTime(),Wrappers.<CourseTime>lambdaUpdate()
                    .set(CourseTime::getStartTime,stageTwoStartTime)
                    .set(CourseTime::getEndTime,stageTwoEndTime)
                    .eq(CourseTime::getSortOrder,12)
                    .eq(CourseTime::getGradeId,gradeId)
            );

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            List<Clazz> clazzList = this.clazzMapper.selectList(Wrappers.<Clazz>lambdaQuery()
                    .eq(Clazz::getGradeId, gradeId)
                    .eq(Clazz::getAcademicYearSemesterId, academicYearSemesterId)
                    .eq(Clazz::getSchoolyardId, schoolyardId));
            if (CollectionUtils.isEmpty(clazzList))
                throw new ApiCode.ApiException(-5,"校区无效！");

            StringBuilder sb = new StringBuilder();
            List<Date> emptyList = new ArrayList<>();
            Set<String> dateSet = new HashSet<>();

            for (int rowIndex = 2; rowIndex < rowNum; rowIndex = rowIndex + 1) {
                String dateCell = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(0),"yyyy-MM-dd");
                // 检查一下日期唯一性
                if (!dateSet.add(dateCell))
                    throw new ApiCode.ApiException(-5,"日期重复！");

                Date rowDate = DateUtils.parse(dateCell,"yyyy-MM-dd");
                if(rowDate.getTime() < DateUtils.parse(DateUtils.format(currentDate,"yyyy-MM-dd"),"yyyy-MM-dd").getTime()){
                    sb.append("第").append(rowIndex + 1).append("行: ").append("日期早于当前日期").append("\r\n");
                    continue;
                }

                // 本行是否全为空
                boolean hasValue = false;

                Map<String,List<NightDutyClassDto>> teacherDutyClazzMap = new HashMap<>();
                Map<String,TeacherDuty> teacherDutyDtoMap = new HashMap<>();

                StringBuilder child = new StringBuilder();
                for (int columnIndex = 1; columnIndex < columnNum - 3; columnIndex = columnIndex + 1) {
                    teacherDuty = new TeacherDuty();
                    teacherDuty.setSchoolyardId(schoolyardId);
                    if(columnIndex > stage){
                        teacherDuty.setDutyType(TeacherDutyTypeEnum.STAGE_TWO);
                        teacherDuty.setStartTime(sdf.parse(dateCell+" "+stageTwoStartTime));
                        teacherDuty.setEndTime(sdf.parse(dateCell+" "+stageTwoEndTime));
                    }else{
                        teacherDuty.setDutyType(TeacherDutyTypeEnum.STAGE_ONE);
                        teacherDuty.setStartTime(sdf.parse(dateCell+" "+stageOneStartTime));
                        teacherDuty.setEndTime(sdf.parse(dateCell+" "+stageOneEndTime));
                    }
                    XSSFCell cell = sheet.getRow(rowIndex).getCell(columnIndex);
                    String cellValue = "";
                    if(cell == null){
                        cellValue = "";
                    }else if (cell.getCellType().equals(CellType.STRING))
                        cellValue = cell.getStringCellValue();
                    else if (CellType.NUMERIC.equals(cell.getCellType()))
                        cellValue = String.valueOf(cell.getNumericCellValue());

                    String dateCellOffset = CellUtils.getCellValue(sheet.getRow(1).getCell(columnIndex)).replace("班", "");
                    Long offset = Long.parseLong(dateCellOffset);

                    if(staffMap.containsKey(cellValue)){
                        hasValue = true;

                        teacherDuty.setTeacherId(staffMap.get(cellValue).get(0).getId());
                        teacherDuty.setDefault().validate(true);
                        //查询今天是否老师值日两个班级，是就合并
                        if(teacherDutyClazzMap.containsKey(cellValue + teacherDuty.getDutyType())){
                            NightDutyClassDto nightDutyClassDto = new NightDutyClassDto();
                            if(teacherDuty.getDutyType().equals(TeacherDutyTypeEnum.STAGE_ONE)){
                                nightDutyClassDto.setClazzId(offset);
                            }else{
                                nightDutyClassDto.setClazzId(offset);
                            }

                            List<NightDutyClassDto> nightDutyClassDtoList = teacherDutyClazzMap.get(cellValue + teacherDuty.getDutyType());
                            nightDutyClassDtoList.add(nightDutyClassDto);
                            if(teacherDutyDtoMap.containsKey(cellValue + teacherDuty.getDutyType())){
                                teacherDutyDtoMap.get(cellValue + teacherDuty.getDutyType()).setTeacherDutyClazzList(nightDutyClassDtoList);
                            }else{
                                teacherDuty.setTeacherDutyClazzList(nightDutyClassDtoList);
                                teacherDutyDtoMap.put(cellValue + teacherDuty.getDutyType(),teacherDuty);
                                teacherDutyList.add(teacherDuty);
                            }
                        }else{
                            List<NightDutyClassDto> clazzes = new ArrayList<>();
                            NightDutyClassDto nightDutyClassDto = new NightDutyClassDto();
                            if(teacherDuty.getDutyType().equals(TeacherDutyTypeEnum.STAGE_ONE)){
                                nightDutyClassDto.setClazzId(offset);
                            }else{
                                nightDutyClassDto.setClazzId(offset);
                            }
                            clazzes.add(nightDutyClassDto);
                            teacherDutyClazzMap.put(cellValue + teacherDuty.getDutyType(),clazzes);
                            if(teacherDutyDtoMap.containsKey(cellValue + teacherDuty.getDutyType())){
                                teacherDutyDtoMap.get(cellValue + teacherDuty.getDutyType()).getTeacherDutyClazzList().add(nightDutyClassDto);
                            }else{
                                teacherDuty.setTeacherDutyClazzList(clazzes);
                                teacherDutyDtoMap.put(cellValue + teacherDuty.getDutyType(),teacherDuty);
                                teacherDutyList.add(teacherDuty);
                            }
                        }
                    } else if (!StringUtils.isNullOrEmpty(cellValue)){
                        child.append("第").append(columnIndex + 1).append("列").append(",");
                    }
                }
                if(!(rowDate.getTime() < DateUtils.parse(DateUtils.format(currentDate,"yyyy-MM-dd"),"yyyy-MM-dd").getTime())){
                    //年级总值班
                    String key = String.valueOf(sheet.getRow(rowIndex).getCell(columnNum-3));
                    if(staffMap.containsKey(key)){
                        hasValue = true;

                        String dateCell1 = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(columnNum - 1));
                        TeacherDuty totalTeacherDuty = new TeacherDuty();
                        totalTeacherDuty.setTeacherId(staffMap.get(key).get(0).getId());
                        totalTeacherDuty.setDutyType(TeacherDutyTypeEnum.GRADE_TOTAL_DUTY);
                        totalTeacherDuty.setStartTime(sdf.parse(dateCell+" "+stageOneStartTime));
                        totalTeacherDuty.setEndTime(sdf.parse(dateCell+" "+stageTwoEndTime));
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
                    } else if (!StringUtils.isNullOrEmpty(key)){
                        child.append("第").append(columnNum - 1).append("列").append(",");
                    }
                    //总值班
                    key = String.valueOf(sheet.getRow(rowIndex).getCell(columnNum-2));
                    if(staffMap.containsKey(key)){
                        hasValue = true;

                        TeacherDuty totalTeacherDuty = new TeacherDuty();
                        totalTeacherDuty.setSchoolyardId(schoolyardId);
                        totalTeacherDuty.setTeacherId(staffMap.get(key).get(0).getId());
                        totalTeacherDuty.setDutyType(TeacherDutyTypeEnum.TOTAL_DUTY);
                        totalTeacherDuty.setStartTime(sdf.parse(dateCell+" "+stageOneStartTime));
                        totalTeacherDuty.setEndTime(sdf.parse(dateCell+" "+stageTwoEndTime));
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

                if (!hasValue)
                    emptyList.add(rowDate);
            }

            if (sb.length() != 0) {
                throw new ApiCode.ApiException(-1, sb.toString());
            }

            for (Date date : emptyList)
                this.baseMapper.removeByTime(date,schoolyardId,gradeId,academicYearSemesterId);

            Map<String,Integer> map = new HashMap<>();
            for (TeacherDuty teacherDuty1 : teacherDutyList) {
                if(!map.containsKey(DateUtils.format(teacherDuty1.getStartTime(),"yyyy-MM-dd"))){
                    map.put(DateUtils.format(teacherDuty1.getStartTime(),"yyyy-MM-dd"),1);
                    this.baseMapper.removeByTime(teacherDuty1.getStartTime(),schoolyardId,gradeId,academicYearSemesterId);
                }
                importTeacherDuty(teacherDuty1,gradeId,academicYearSemesterId);
            }
            return "导入成功";
        } catch (IOException | InvalidFormatException e) {
            throw new ApiCode.ApiException(-1, "导入数据失败！");
        } catch(ParseException p){
            throw new ApiCode.ApiException(-1, "时间解析失败");
        }
    }

    private void importTeacherDuty(TeacherDuty teacherDuty,Long gradeId,Long academicYearSemesterId){
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
        TeacherDuty teacherDuty1 = this.baseMapper.getByTimeDutyType(teacherDuty.getStartTime(),teacherDuty.getDutyType().toString(),teacherDuty.getTeacherId());
        if(teacherDuty1 != null){
            teacherDuty1.setStartTime(teacherDuty.getStartTime());
            teacherDuty1.setEndTime(teacherDuty.getEndTime());
            teacherDuty1.setTeacherId(teacherDuty.getTeacherId());
            this.baseMapper.updateById(teacherDuty1);
            teacherDuty.setId(teacherDuty1.getId());
        }else{
            this.baseMapper.insert(teacherDuty);
        }
        List<TeacherDutyClazz> teacherDutyClazzes = new ArrayList<>();
        List<NightStudy> nightStudyList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(teacherDuty.getTeacherDutyClazzList())) {
            teacherDuty.getTeacherDutyClazzList().stream().forEach(teacherDutyClazzDto -> {
                TeacherDutyClazz teacherDutyClazz = new TeacherDutyClazz();
                teacherDutyClazz.setClazzId(teacherDutyClazzDto.getClazzId());
                teacherDutyClazz.setTeacherDutyId(teacherDuty.getId());
                teacherDutyClazz.setIsComfirm(YesNoEnum.NO);
                teacherDutyClazz.setIsLeaderComfirm(YesNoEnum.NO);
                teacherDutyClazzes.add(teacherDutyClazz);
            });
            this.teacherDutyClazzMapper.batchInsertWithId(teacherDutyClazzes);
            teacherDutyClazzes.stream().forEach(item->{
                NightStudy nightStudy = new NightStudy();
                nightStudy.setShouldStudentCount(0);
                nightStudy.setActualStudentCount(0);
                nightStudy.setTeacherDutyClazzId(item.getId());
                nightStudyList.add(nightStudy);
            });
            this.nightStudyMapper.batchInsert(nightStudyList);
            List<Long> teacherDutyClazzIds = teacherDutyClazzes.stream().map(item->item.getId()).collect(Collectors.toList());
            teacherDutyClazzMapper.updateClazzId(teacherDutyClazzIds,gradeId,academicYearSemesterId);
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

    @Override
    public Integer updateTeacher(NightDutyClassDto nightDutyClassDto) {
        // 对应领导晚班
        NightStudyDutyClazz nightStudyDutyClazz = this.nightStudyDutyClazzMapper.selectById(nightDutyClassDto.getNightStudyDutyClazzId());
        NightStudyDuty nightStudyDuty = this.nightStudyDutyMapper.selectById(nightStudyDutyClazz.getNightStudyDutyId());
        Long schoolyardId = nightStudyDuty.getLeaderDuty().getSchoolyardId();

        //原始班级值班教师
        TeacherDuty teacherDuty = this.baseMapper.getByTimeAndClazzId(nightDutyClassDto.getClazzId(),nightDutyClassDto.getTime(),nightDutyClassDto.getTeacherDutyTypeEnum().toString());
        //新班级值班老师
        TeacherDuty teacherDuty1 = this.baseMapper.getByClazz(nightDutyClassDto.getTeacherNewId(),nightDutyClassDto.getTime(),nightDutyClassDto.getTeacherDutyTypeEnum().toString());
        if(teacherDuty == null){
            if(teacherDuty1 == null ){
                teacherDuty1 = new TeacherDuty();
                teacherDuty1.setSchoolyardId(schoolyardId);
                teacherDuty1.setTeacherId(nightDutyClassDto.getTeacherNewId());
                Clazz clazz = clazzMapper.selectById(nightDutyClassDto.getClazzId());
                CourseTime courseTime = courseTimeMapper.selectOne(Wrappers.<CourseTime>lambdaQuery()
                        .eq(CourseTime::getSortOrder,Objects.equals(nightDutyClassDto.getTeacherDutyTypeEnum(),TeacherDutyTypeEnum.STAGE_ONE) ? 11:12)
                        .eq(CourseTime::getGradeId,clazz.getGradeId()));
                teacherDuty1.setStartTime(DateUtils.parse(courseTime.getStartTime(),nightDutyClassDto.getTime()));
                teacherDuty1.setEndTime(DateUtils.parse(courseTime.getEndTime(),nightDutyClassDto.getTime()));
                teacherDuty1.setDutyType(nightDutyClassDto.getTeacherDutyTypeEnum());
                teacherDuty1.setDefault().validate(true);
                this.baseMapper.insert(teacherDuty1);
            }
            TeacherDutyClazz teacherDutyClazz = new TeacherDutyClazz();
            teacherDutyClazz.setClazzId(nightDutyClassDto.getClazzId());
            teacherDutyClazz.setTeacherDutyId(teacherDuty1.getId());
            teacherDutyClazz.setDefault().validate(true);
            return teacherDutyClazzMapper.insert(teacherDutyClazz);
        }else if(teacherDuty1 != null && teacherDuty.getId().equals(teacherDuty1.getId())){
            return 1;
        }
        if(teacherDuty1 == null ){
            teacherDuty1 = new TeacherDuty();
            teacherDuty1.setSchoolyardId(schoolyardId);
            teacherDuty1.setTeacherId(nightDutyClassDto.getTeacherNewId());
            teacherDuty1.setStartTime(teacherDuty.getStartTime());
            teacherDuty1.setEndTime(teacherDuty.getEndTime());
            teacherDuty1.setDutyType(nightDutyClassDto.getTeacherDutyTypeEnum());
            teacherDuty1.setDefault().validate(true);
            this.baseMapper.insert(teacherDuty1);
        }

//        teacherDutyClazzMapper.update(new TeacherDutyClazz(),Wrappers.<TeacherDutyClazz>lambdaUpdate()
//                .set(TeacherDutyClazz::getTeacherDutyId,teacherDuty1.getId())
//                .eq(TeacherDutyClazz::getTeacherDutyId,teacherDuty.getId())
//        );
        teacherDutyClazzMapper.update(new TeacherDutyClazz(),Wrappers.<TeacherDutyClazz>lambdaUpdate()
                .set(TeacherDutyClazz::getTeacherDutyId,teacherDuty1.getId())
                .eq(TeacherDutyClazz::getTeacherDutyId,teacherDuty.getId())
        );
        teacherDutySubstituteMapper.update(new TeacherDutySubstitute(),Wrappers.<TeacherDutySubstitute>lambdaUpdate()
                .set(TeacherDutySubstitute::getTeacherDutyId,teacherDuty1.getId())
                .eq(TeacherDutySubstitute::getTeacherDutyId,teacherDuty.getId())
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
        return teacherDutyClazzMapper.update(new TeacherDutyClazz(),Wrappers.<TeacherDutyClazz>lambdaUpdate()
                .set(TeacherDutyClazz::getIsComfirm,YesNoEnum.YES)
                .eq(TeacherDutyClazz::getId,nightDutyClassDto.getTeacherDutyClassId())
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
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        Staff staff = staffMapper.selectById(user.getStaffId());
        TeacherDuty teacherDuty = this.baseMapper.getByTimeAndClazzId(nightDutyClassDto.getClazzId(),nightDutyClassDto.getTime(),nightDutyClassDto.getTeacherDutyTypeEnum().toString());

        if(teacherDuty == null || teacherDuty.getTeacherId().equals(staff.getId())){
            throw new ApiCode.ApiException(-5,"未查询到教师值班或您无法替自己带班");
        }
        Integer count = teacherDutySubstituteMapper.selectCount(Wrappers.<TeacherDutySubstitute>lambdaQuery()
                .eq(TeacherDutySubstitute::getIsAgree,YesNoEnum.YES)
                .eq(TeacherDutySubstitute::getTeacherDutyId,teacherDuty.getId())
        );
        if(count > 0){
            throw new ApiCode.ApiException(-5,"领导修改值班，无法被带班");
        }
        TeacherDuty teacherDuty1 = this.baseMapper.getByClazz(staff.getId(),nightDutyClassDto.getTime(),nightDutyClassDto.getTeacherDutyTypeEnum().toString());
        if(teacherDuty1 == null){
            teacherDuty1 = new TeacherDuty();
            teacherDuty1.setSchoolyardId(teacherDuty.getSchoolyardId());
            teacherDuty1.setTeacherId(staff.getId());
            teacherDuty1.setStartTime(teacherDuty.getStartTime());
            teacherDuty1.setEndTime(teacherDuty.getEndTime());
            teacherDuty1.setDutyType(nightDutyClassDto.getTeacherDutyTypeEnum());
            teacherDuty1.setDefault().validate(true);
            this.baseMapper.insert(teacherDuty1);
        }else{
            List<TeacherDutyClazz> teacherDutyClazzes = teacherDutyClazzMapper.selectList(Wrappers.<TeacherDutyClazz>lambdaQuery()
                    .eq(TeacherDutyClazz::getTeacherDutyId,teacherDuty1.getId())
            );
            if(CollectionUtils.isNotEmpty(teacherDutyClazzes)){
                throw new ApiCode.ApiException(-5,"您当天已有值班，无法带班！");
            }
        }
        List<Long> ids = new ArrayList<>();
        ids.add(teacherDuty1.getId());
        ids.add(teacherDuty.getId());
        Integer leaderConfirmCount = this.teacherDutyClazzMapper.selectCount(Wrappers.<TeacherDutyClazz>lambdaQuery()
                .in(TeacherDutyClazz::getTeacherDutyId,ids)
                .eq(TeacherDutyClazz::getIsLeaderComfirm,YesNoEnum.YES)
        );
        if(leaderConfirmCount > 0){
            throw new ApiCode.ApiException(-5,"领导已确认您的值班或您代值的班级，无法代值");
        }
// 帮替一个班级
//        teacherDutyClazzMapper.deleteById(teacherDuty.getTeacherDutyClazzId());
//        TeacherDutyClazz teacherDutyClazz = new TeacherDutyClazz();
//        teacherDutyClazz.setTeacherDutyId(teacherDuty1.getId());
//        teacherDutyClazz.setClazzId(nightDutyClassDto.getClazzId());
//        teacherDutyClazzMapper.insert(teacherDutyClazz);
// 帮替一个所有班级
        teacherDutyClazzMapper.update(new TeacherDutyClazz(),Wrappers.<TeacherDutyClazz>lambdaUpdate()
                .set(TeacherDutyClazz::getTeacherDutyId,teacherDuty1.getId())
                .eq(TeacherDutyClazz::getTeacherDutyId,teacherDuty.getId())
        );
        teacherDutySubstituteMapper.update(new TeacherDutySubstitute(),Wrappers.<TeacherDutySubstitute>lambdaUpdate()
                .set(TeacherDutySubstitute::getTeacherDutyId,teacherDuty1.getId())
                .eq(TeacherDutySubstitute::getTeacherDutyId,teacherDuty.getId())
        );
        TeacherDutySubstitute teacherDutySubstitute = new TeacherDutySubstitute();
        teacherDutySubstitute.setTeacherDutyId(teacherDuty1.getId());
        teacherDutySubstitute.setTeacherId(teacherDuty1.getTeacherId());
        teacherDutySubstitute.setTeacherOldId(teacherDuty.getTeacherId());
        return teacherDutySubstituteMapper.insert(teacherDutySubstitute);
    }

    @Override
    public IPage pageDetail(IPage teacherPage, TeacherDutyTypeEnum dutyType, String name, String clazzName,Date startTime, Date endTime, Long gradeId, Long clazzId, Long schoolyardId) {
        teacherPage.setTotal(this.baseMapper.countPageDetail(dutyType,name,clazzName,startTime,endTime,gradeId,clazzId,schoolyardId));
        List<NightDutyClassDto> nightDutyClassDtoList = this.baseMapper.pageDetail(teacherPage,dutyType,name,clazzName,startTime,endTime,gradeId,clazzId,schoolyardId);
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
                if (nightDutyClassDto1.getNightStudyDutyClazzDeductions() != null) nightDutyClassDto.setNightStudyDutyClazzDeductions(nightDutyClassDto1.getNightStudyDutyClazzDeductions());
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
                .eq(TeacherDutyClazz::getTeacherDutyId,nightDutyClassDto.getTeacherDutyId())
        );
        if(teacherDutyClazzList == null || teacherDutyClazzList.size() == 0){
            throw new ApiCode.ApiException(-5,"您当天没有值班，无法取消");
        }
        Integer count = teacherDutySubstituteMapper.selectCount(Wrappers.<TeacherDutySubstitute>lambdaQuery()
                .eq(TeacherDutySubstitute::getIsAgree,YesNoEnum.YES)
                .eq(TeacherDutySubstitute::getTeacherDutyId,teacherDuty.getId())
        );
        if(count > 0){
            throw new ApiCode.ApiException(-5,"领导修改值班，无法取消");
        }
        Long leaderConfirmCount = teacherDutyClazzList.stream().filter(item->YesNoEnum.YES.equals(item.getIsLeaderComfirm())).count();
        if(leaderConfirmCount > 0){
            throw new ApiCode.ApiException(-5,"领导已确认，无法取消");
        }
        TeacherDuty teacherDuty1 = this.baseMapper.getByClazz(nightDutyClassDto.getTeacherNewId(),teacherDuty.getStartTime(),teacherDuty.getDutyType().toString());
        if(teacherDuty1 == null){
            teacherDuty1 = new TeacherDuty();
            teacherDuty1.setSchoolyardId(teacherDuty.getSchoolyardId());
            teacherDuty1.setTeacherId(nightDutyClassDto.getTeacherNewId());
            teacherDuty1.setStartTime(teacherDuty.getStartTime());
            teacherDuty1.setEndTime(teacherDuty.getEndTime());
            teacherDuty1.setDutyType(teacherDuty.getDutyType());
            teacherDuty1.setDefault().validate(true);
            this.baseMapper.insert(teacherDuty1);
        }
        return teacherDutyClazzMapper.update(new TeacherDutyClazz(),Wrappers.<TeacherDutyClazz>lambdaUpdate()
                .set(TeacherDutyClazz::getTeacherDutyId,teacherDuty1.getId())
                .eq(TeacherDutyClazz::getTeacherDutyId,teacherDuty.getId())
        );
    }

    @Override
    public List<Staff> cancelTeacherList(Long teacherDutyId) {
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        TeacherDutySubstitute teacherDutySubstitute = teacherDutySubstituteMapper.selectOne(Wrappers.<TeacherDutySubstitute>lambdaQuery()
                .eq(TeacherDutySubstitute::getTeacherDutyId,teacherDutyId)
                .eq(TeacherDutySubstitute::getTeacherId,user.getStaffId())
                .orderByDesc(TeacherDutySubstitute::getId)
        );
        List<TeacherDutySubstitute> teacherDutySubstitutes = teacherDutySubstituteMapper.selectList(Wrappers.<TeacherDutySubstitute>lambdaQuery()
                .eq(TeacherDutySubstitute::getTeacherDutyId,teacherDutyId)
                .le(TeacherDutySubstitute::getId,teacherDutySubstitute.getId())
        );
        List<Long> ids = new ArrayList<>();
        teacherDutySubstitutes.stream().forEach(item -> {
            if(!item.getTeacherOldId().equals(user.getStaffId())){
                ids.add(item.getTeacherOldId());
            }
        });
        return this.staffMapper.selectBatchIds(ids);
    }

    @Override
    public List<TeacherServerFormDto> getTeacherDutyByStaffId(Date timeFrom, Date timeTo) {
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        QueryWrapper<TeacherDuty> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_id",user.getStaffId());
        queryWrapper.ge("start_time",timeFrom);
        queryWrapper.le("start_time",timeTo);
        List<TeacherDuty> teacherDuties = this.baseMapper.selectList(queryWrapper);
        Map<String,List<TeacherDuty>> teacherDutyMap =  teacherDuties.stream().collect(Collectors.groupingBy(item->DateUtils.format(item.getStartTime(),"yyyy-MM-dd")));
        Map<String,TeacherServerFormDto> exist = new HashMap<>();

        for (String time : teacherDutyMap.keySet()) {
            for (TeacherDuty teacherDuty : teacherDutyMap.get(time)) {
                List<TeacherDutyClazz> teacherDutyClazzes = teacherDutyClazzMapper.selectList(Wrappers.<TeacherDutyClazz>lambdaQuery()
                        .eq(TeacherDutyClazz::getTeacherDutyId,teacherDuty.getId())
                );
                if(CollectionUtils.isEmpty(teacherDutyClazzes)){
                    continue;
                }
                List<ClazzVo> clazzVos = this.parseClazzVo(teacherDutyClazzes);
                if(exist.containsKey(time)){
                    TeacherServerFormDto teacherServerFormDto = exist.get(time);
                    Map<TeacherDutyTypeEnum,List<ClazzVo>> map = teacherServerFormDto.getClazzVoListMap();
                    map.put(teacherDuty.getDutyType(),clazzVos);
                    teacherServerFormDto.setClazzVoListMap(map);

                    List<ClazzVo> clazzList = teacherServerFormDto.getClazzVoList();
                    clazzList.addAll(clazzVos);
                    clazzList = clazzList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(()
                            -> new TreeSet<>(Comparator.comparing(ClazzVo :: getId))), ArrayList::new));
                    teacherServerFormDto.setClazzVoList(clazzList);
                    exist.put(time,teacherServerFormDto);
                }else{
                    List<ClazzVo> clazzList = new ArrayList<>();
                    clazzList.addAll(clazzVos);
                    TeacherServerFormDto teacherServerFormDto = new TeacherServerFormDto();
                    teacherServerFormDto.setDutyType(teacherDuty.getDutyType());
                    teacherServerFormDto.setTime(teacherDuty.getStartTime());
                    teacherServerFormDto.setClazzVoList(clazzList);
                    teacherServerFormDto.setClazzVoListMap(new HashMap<TeacherDutyTypeEnum,List<ClazzVo>>(){{this.put(teacherDuty.getDutyType(),clazzVos);}});
                    exist.put(time,teacherServerFormDto);
                }
            }
        }
        QueryWrapper<LeaderDuty> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("leader_id",user.getStaffId());
        queryWrapper1.ge("start_time",timeFrom);
        queryWrapper1.le("start_time",timeTo);
        List<LeaderDuty> leaderDutyList = this.leaderDutyMapper.selectList(queryWrapper1);
        for (LeaderDuty leaderDuty : leaderDutyList) {
            String time = DateUtils.format(leaderDuty.getStartTime(),"yyyy-MM-dd");
            if(!exist.containsKey(time)){
                TeacherServerFormDto teacherServerFormDto = new TeacherServerFormDto();
                TeacherDutyClazz teacherDutyClazz = teacherDutyClazzMapper.selectOne(Wrappers.<TeacherDutyClazz>lambdaQuery()
                        .eq(TeacherDutyClazz::getIsLeaderComfirm,YesNoEnum.NO)
                        .inSql(TeacherDutyClazz::getTeacherDutyId,"select id from day_teacher_duty where to_days(start_time) = "+"to_days("+time+")")
                );
                ClazzVo clazzVo = new ClazzVo();
                clazzVo.setIsLeaderConfirm(YesNoEnum.YES);
                if(teacherDutyClazz == null){
                    clazzVo.setIsLeaderConfirm(YesNoEnum.NO);
                }
                teacherServerFormDto.setClazzVoList(new ArrayList<ClazzVo>(){{this.add(clazzVo);}});
                teacherServerFormDto.setDutyType(TeacherDutyTypeEnum.TOTAL_DUTY);
                teacherServerFormDto.setTime(leaderDuty.getStartTime());
                teacherServerFormDto.setClazzVoListMap(new HashMap<TeacherDutyTypeEnum,List<ClazzVo>>(){{this.put(TeacherDutyTypeEnum.TOTAL_DUTY,teacherServerFormDto.getClazzVoList());}});
                exist.put(time,teacherServerFormDto);
            }
        }
        return exist.values().stream().map(item -> (TeacherServerFormDto)item).collect(Collectors.toList());
    }

    @Override
    public Integer updateDutyMode(TeacherServerFormDto teacherServerFormDto) {
        if(teacherServerFormDto.getTime() == null || teacherServerFormDto.getDutyMode() == null || StringUtils.isNullOrEmpty(teacherServerFormDto.getGradeDutyTeacher())){
            throw new ApiCode.ApiException(-5,"请传入时间及模式及值班老师");
        }
        return this.teacherDutyMapper.updateByTime(teacherServerFormDto.getTime(),teacherServerFormDto.getDutyMode(), teacherServerFormDto.getGradeDutyTeacher());
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

            row = sheet.createRow(startRow++);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue(value.get(0).getTeacher().getName());

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
                return TeacherDutyTypeEnum.TOTAL_DUTY.equals(item.getDutyType())  && !teacherDutyGradeTotalMap.containsKey(date);
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

        Page<Map<String,Object>> page = this.getTeacherDutyForm(1, 9999, timeFrom, timeTo, null, gradeId, schoolyardId);
        List<Map<String,Object>> records = page.getRecords();

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

    @SuppressWarnings("unchecked")
    private void putData(XSSFWorkbook book, XSSFCellStyle style, Map<String,Object> data) {
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

    private List<ClazzVo> parseClazzVo(List<TeacherDutyClazz> teacherDutyClazzes){
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
