/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：课程表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.*;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.repository.*;
import com.zhzx.server.repository.base.CourseBaseMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.CourseService;
import com.zhzx.server.util.DateUtils;
import com.zhzx.server.util.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {
    @Value("${web.upload-path}")
    private String uploadPath;
    @Resource
    private AcademicYearSemesterMapper academicYearSemesterMapper;
    @Resource
    private CourseTimeMapper courseTimeMapper;
    @Resource
    private StaffLessonTeacherMapper staffLessonTeacherMapper;
    @Resource
    private ClazzMapper clazzMapper;
    @Resource
    private SettingsMapper settingsMapper;

    @Override
    public int updateAllFieldsById(Course entity) {
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
    public boolean saveBatch(Collection<Course> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(CourseBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    public List<Map<String, String>> getList(Long academicYearSemesterId, Long gradeId,Long clazzId) {
        List<Map<String, String>> items = new ArrayList<>();
        if (academicYearSemesterId == null || academicYearSemesterId <= 0L) return items;
        if (gradeId == null || gradeId <= 0L) return items;
        int rowId = 0;
        for (int week = 1; week <= 5; week++) {
            for (int sortOrder = 1; sortOrder <= 10; sortOrder++) {
                Map<String, String> itemMap = new HashMap<>();
                itemMap.put("id", String.valueOf(rowId));
                rowId++;
                itemMap.put("week", String.valueOf(week));
                itemMap.put("sortOrder", String.valueOf(sortOrder));
                QueryWrapper<Course> queryWrapper = new QueryWrapper<Course>()
                        .eq(academicYearSemesterId != null,"academic_year_semester_id", academicYearSemesterId)
                        .eq(gradeId != null,"grade_id", gradeId)
                        .eq(clazzId != null,"clazz_id", clazzId)
                        .eq("week", week)
                        .eq("sort_order", sortOrder)
                        .orderBy(true, true, "clazz_id");
                List<Course> courses = this.list(queryWrapper);
                if(clazzId == null){
                    for (Course course : courses) {
                        String key = "C" + String.valueOf(course.getClazzId());
                        String value = course.getCourseName();
                        if (itemMap.get(key) != null) {
                            value = value + ',' + (course.getTeacher() == null ? course.getTeacherName() : course.getTeacher().getName()) + ',' + itemMap.get(key);
                        } else {
                            value = value + ',' + (course.getTeacher() == null ? course.getTeacherName() : course.getTeacher().getName());
                        }
                        itemMap.put(key, value.replace(",null", ""));
                    }
                }else{
                    for (Course course : courses) {
                        String key = "clazz";
                        String value = course.getCourseName();
                        if (itemMap.get(key) != null) {
                            value = value + ',' + (course.getTeacher() == null ? course.getTeacherName() : course.getTeacher().getName()) + ',' + itemMap.get(key);
                        } else {
                            value = value + ',' + (course.getTeacher() == null ? course.getTeacherName() : course.getTeacher().getName());
                        }
                        itemMap.put(key, value.replace(",null", ""));
                    }
                }
                items.add(itemMap);
            }
        }
        return items;
    }

    private String parseNumber(String order) {
        int s = Integer.parseInt(order);
        if (s == 1)
            return "一";
        else if (s == 2)
            return "二";
        else if (s == 3)
            return "三";
        else if (s == 4)
            return "四";
        else if (s == 5)
            return "五";
        else if (s == 6)
            return "六";
        else if (s == 7)
            return "七";
        else if (s == 8)
            return "八";
        else if (s == 9)
            return "九";
        else
            return "十";
    }

    private void save(BufferedImage image) throws Exception {
        ImageIO.write(image, "jpg", new File("d:/image.jpg"));
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

    private String getCourseString(String str) {
        return str.replace("逻辑与批判性思维/", "批思,")
                .replace("逻辑与批判性思维", "批思")
                .replace("口语/", "口语,");
    }

    @Override
    public BufferedImage getImage(Long academicYearSemesterId, Long gradeId, Long clazzId) throws Exception {
        if (gradeId == null)
            return null;
        if (academicYearSemesterId == null) {
            AcademicYearSemester curr = this.academicYearSemesterMapper.selectOne(
                    Wrappers.<AcademicYearSemester>lambdaQuery()
                            .select(AcademicYearSemester::getId)
                            .eq(AcademicYearSemester::getIsDefault, YesNoEnum.YES));
            academicYearSemesterId = curr.getId();
        }
        Settings settings = this.settingsMapper.selectOne(Wrappers.<Settings>lambdaQuery()
                .like(Settings::getCode, "COURSE\\_" + academicYearSemesterId + "\\_" + gradeId + "\\_" + clazzId)
        );
        if (settings == null)
            return null;
        List<Map<String, String>> items = JSON.parseArray(settings.getParams()).stream().map(item -> {
            Map<String, String> map = (Map<String, String>) item;
            return map;
        }).collect(Collectors.toList());
        // 所有班级
        List<Clazz> clazzList = this.clazzMapper.selectList(Wrappers.<Clazz>lambdaQuery()
                .select(Clazz::getId, Clazz::getName)
                .eq(Clazz::getAcademicYearSemesterId, academicYearSemesterId)
                .eq(Clazz::getGradeId, gradeId)
                .eq(clazzId != null, Clazz::getId, clazzId));
        clazzList.sort(new Comparator<Clazz>() {
            @Override
            public int compare(Clazz o1, Clazz o2) {
                return Integer.parseInt(o1.getName().replace("班", "")) - Integer.parseInt(o2.getName().replace("班", ""));
            }
        });
        if (CollectionUtils.isNotEmpty(items) && CollectionUtils.isNotEmpty(clazzList)) {
            int columnWidth = 0;
            int rowHeight = this.getStringWidthHeight("星期五")[1];
            int ascent = this.getStringWidthHeight("星期五")[2];
            for (int i = 0; i < clazzList.size(); i++) {
                for (int j = 0; j < items.size(); j++) {
                    if (!items.get(j).containsKey("C" + clazzList.get(i).getId())) continue;
                    String text = this.getCourseString(items.get(j).get("C" + clazzList.get(i).getId()));
                    if (!text.contains(",")) {
                        if (columnWidth < this.getStringWidthHeight(text)[0])
                            columnWidth = this.getStringWidthHeight(text)[0];
                        continue;
                    }
                    String[] texts = text.split(",");
                    if (texts.length == 2) {
                        int width1 = this.getStringWidthHeight(texts[0])[0];
                        int width2 = this.getStringWidthHeight(texts[1])[0];
                        if (columnWidth < width1)
                            columnWidth = width1;
                        if (columnWidth < width2)
                            columnWidth = width2;
                        continue;
                    }
                    if (texts.length == 3) {
                        int width1 = this.getStringWidthHeight(texts[0] + texts[2])[0];
                        int width2 = this.getStringWidthHeight(texts[1])[0];
                        if (columnWidth < width1)
                            columnWidth = width1;
                        if (columnWidth < width2)
                            columnWidth = width2;
                        continue;
                    }
                    if (texts.length == 4) {
                        int width1 = this.getStringWidthHeight(texts[0] + texts[2])[0];
                        int width2 = this.getStringWidthHeight(texts[1] + texts[3])[0];
                        if (columnWidth < width1)
                            columnWidth = width1;
                        if (columnWidth < width2)
                            columnWidth = width2;
                        continue;
                    }
                }
            }

            int padding = 2;
            int imageWidth = 160 + (columnWidth + padding * 3) * clazzList.size();
            int imageHeight = (rowHeight * 2 + padding * 3) * 51;

            BufferedImage image = new BufferedImage(imageWidth + 10, imageHeight + 10, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.setStroke(new BasicStroke(0.1f));
            graphics.setFont(imageFont);
            graphics.fillRect(0, 0, imageWidth + 10, imageHeight + 10);
            graphics.setColor(Color.black);
            graphics.drawRect(5, 5, imageWidth, imageHeight);

            int x = 165, y = 5;
            graphics.drawLine(85, 5, 85, imageHeight + 5);
            graphics.drawLine(165, 5, 165, imageHeight + 5);
            for (int i = 0; i < clazzList.size() - 1; i++) {
                x += columnWidth + padding * 3;
                graphics.drawLine(x, 5, x, imageHeight + 5);
            }
            for (int i = 0; i < 50; i++) {
                y += 2 * rowHeight + padding * 3;
                graphics.drawLine(5, y, imageWidth + 5, y);
            }
            x = 5;
            y = 5;
            graphics.drawString("星期", x + (80 + 2 * padding - this.getStringWidthHeight("星期")[0]) / 2, y + (rowHeight + padding * 3) / 2 + ascent);
            x = 85;
            graphics.drawString("节次", x + (80 + 2 * padding - this.getStringWidthHeight("节次")[0]) / 2, y + (rowHeight + padding * 3) / 2 + ascent);
            x = 165;
            for (int i = 0; i < clazzList.size(); i++) {
                y = 5;
                String title = clazzList.get(i).getGrade().getName().replace("年级", "") + clazzList.get(i).getName();
                graphics.drawString(title, x + (columnWidth + 3 * padding - this.getStringWidthHeight(title)[0]) / 2, y + (rowHeight + padding * 3) / 2 + ascent);
                y = 2 * rowHeight + padding * 3 + 5;
                for (int j = 0; j < items.size(); j++) {
                    if (!items.get(j).containsKey("C" + clazzList.get(i).getId())) continue;
                    String text = this.getCourseString(items.get(j).get("C" + clazzList.get(i).getId()));
                    if (!text.contains(",")) {
                        graphics.drawString(text, x + (columnWidth + 3 * padding - this.getStringWidthHeight(text)[0]) / 2, y + (rowHeight + padding * 3) / 2 + ascent);
                        y += 2 * rowHeight + padding * 3;
                        continue;
                    }
                    String[] texts = text.split(",");
                    if (texts.length == 2) {
                        int w1 = this.getStringWidthHeight(texts[0])[0];
                        int w2 = this.getStringWidthHeight(texts[1])[0];
                        graphics.drawString(texts[0], x + (columnWidth + 3 * padding - w1) / 2, y + padding + ascent);
                        graphics.drawString(texts[1], x + (columnWidth + 3 * padding - w2) / 2, y + rowHeight + padding * 2 + ascent);
                    }
                    if (texts.length == 3) {
                        int w1 = this.getStringWidthHeight(texts[0])[0];
                        int w2 = this.getStringWidthHeight(texts[1])[0];
                        int w3 = this.getStringWidthHeight(texts[2])[0];
                        graphics.drawString(texts[0], x + (columnWidth / 2 - w1) / 2 + padding, y + padding + ascent);
                        graphics.drawString(texts[2], x + (columnWidth / 2 - w3) / 2 + columnWidth / 2 + padding * 2, y + padding + ascent);
                        graphics.drawString(texts[1], x + (columnWidth / 2 - w2) / 2 + padding, y + rowHeight + padding * 2 + ascent);
                    }
                    if (texts.length == 4) {
                        int w1 = this.getStringWidthHeight(texts[0])[0];
                        int w2 = this.getStringWidthHeight(texts[1])[0];
                        int w3 = this.getStringWidthHeight(texts[2])[0];
                        int w4 = this.getStringWidthHeight(texts[3])[0];
                        graphics.drawString(texts[0], x + (columnWidth / 2 - w1) / 2 + padding, y + padding + ascent);
                        graphics.drawString(texts[2], x + (columnWidth / 2 - w3) / 2 + columnWidth / 2 + padding * 2, y + padding + ascent);
                        graphics.drawString(texts[1], x + (columnWidth / 2 - w2) / 2 + padding, y + rowHeight + padding * 2 + ascent);
                        graphics.drawString(texts[3], x + (columnWidth / 2 - w4) / 2 + columnWidth / 2 + padding * 2, y + rowHeight + padding * 2 + ascent);
                    }
                    y += 2 * rowHeight + padding * 3;
                }
                x += columnWidth + padding * 3;
            }

            y = 5;
            for (int i = 1; i <= 50; i++) {
                y += rowHeight * 2 + padding * 3;
                int q = i % 10;
                if (q != 0) {
                    graphics.setColor(Color.white);
                    graphics.drawLine(5, y + rowHeight * 2 + padding * 3, 85, y + rowHeight * 2 + padding * 3);
                } else {
                    int p = y - 4 * (rowHeight * 2 + padding * 3) - (rowHeight * 2 + padding * 3) / 2;
                    int j = i / 10;
                    String curr = "星期".concat(this.parseNumber(String.valueOf(j)));
                    graphics.drawString(curr, 5 + (80 + 2 * padding - this.getStringWidthHeight("星期五")[0]) / 2, p + (rowHeight + padding * 3) / 2 + ascent);
                }
                graphics.setColor(Color.black);
                String curr = "第" + this.parseNumber(String.valueOf(q)) + "节";
                graphics.drawString(curr, 85 + (80 + 2 * padding - this.getStringWidthHeight("星期五")[0]) / 2, y + (rowHeight + padding * 3) / 2 + ascent);
            }
//             this.save(image);
            return image;
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importExcel(Long academicYearSemesterId, Long gradeId, String fileUrl) {
        if (StringUtils.isNullOrEmpty(fileUrl))
            throw new ApiCode.ApiException(-1, "没有上传文件！");
        String[] items = fileUrl.split("/");
        File file = new File(uploadPath + "\\" + items[items.length - 2] + "\\" + items[items.length - 1]);
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
            // 课程列表
            List<Course> courses = new ArrayList<>();
            // 读取单元格数据
            Course course = null;
            for (int rowIndex = 2; rowIndex < rowNum; rowIndex = rowIndex + 2) {
                for (int columnIndex = 4; columnIndex < columnNum; columnIndex++) {
                    XSSFCell cell = sheet.getRow(rowIndex).getCell(columnIndex);
                    String cellValue = "";
                    if (cell.getCellType().equals(CellType.STRING))
                        cellValue = cell.getStringCellValue();
                    else if (cell.getCellType().equals(CellType.NUMERIC))
                        cellValue = String.valueOf(cell.getNumericCellValue());

                    if (StringUtils.isNullOrEmpty(cellValue)) {
                        continue;
                    }

                    if (columnIndex % 2 == 0) {
                        course = new Course();
                        course.setAcademicYearSemesterId(academicYearSemesterId);
                        course.setGradeId(gradeId);
                        course.setWeek((int) Math.ceil((double) rowIndex / 20));
                        course.setSortOrder(rowIndex % 20 / 2 == 0 ? 10 : rowIndex % 20 / 2);
                        course.setClazzId(Long.valueOf(columnIndex / 2 - 1));
                        course.setClazzName(sheet.getRow(0).getCell(columnIndex).getStringCellValue());
                        courses.add(course);
                    }
                    if (columnIndex % 2 == 1 && cellValue != "") {
                        course = new Course();
                        BeanUtils.copyProperties(courses.get(courses.size() - 1), course);
                        courses.add(course);
                    }
                    if (cellValue.indexOf("\n") < 0) {
                        if (cellValue != "")
                            course.setCourseName(cellValue);
                    } else {
                        course.setCourseName(cellValue.split("\n")[0]);
                        course.setTeacherName(cellValue.split("\n")[1]);
                    }
                    course.setTeacherId(-1L);
                    course.setDefault();
                }
            }
            QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<Course>().eq("academic_year_semester_id", academicYearSemesterId).eq("grade_id", gradeId);
            this.remove(courseQueryWrapper);
            this.saveBatch(courses, courses.size());
            this.getBaseMapper().updateClazzId(academicYearSemesterId, gradeId);
            this.getBaseMapper().updateTeacherId(academicYearSemesterId, gradeId);
        } catch (IOException | InvalidFormatException e) {
            throw new ApiCode.ApiException(-1, "导入数据失败！");
        }
    }

    @Override
    public Map<String,Object> getCurrentCourse(Integer gradeId, Date time) {
        Map<String,Object> map = new HashMap<>();
        AcademicYearSemester academicYearSemester = academicYearSemesterMapper.selectOne(Wrappers.<AcademicYearSemester>lambdaQuery()
                .eq(AcademicYearSemester::getIsDefault, YesNoEnum.YES)
        );
        if(academicYearSemester == null)
            throw new ApiCode.ApiException(-5,"查询当前学年失败！");
        map.put("academicYearSemesterId",academicYearSemester.getId());
        map.put("gradeId",gradeId);
        String timeStr = DateUtils.format(time,"HH:mm");
        QueryWrapper<CourseTime> courseTimeQueryWrapper = new QueryWrapper<CourseTime>();
        courseTimeQueryWrapper.eq("grade_id",gradeId);
        courseTimeQueryWrapper.le("start_time",timeStr);
        courseTimeQueryWrapper.ge("end_time",timeStr);
        courseTimeQueryWrapper.or().eq("grade_id",gradeId).ge("start_time" ,timeStr);
        CourseTime courseTime = courseTimeMapper.selectOne(courseTimeQueryWrapper);

        if(courseTime == null)
            return map;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        Integer week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        QueryWrapper<Course> queryWrapper = new QueryWrapper<Course>()
                .eq("academic_year_semester_id", academicYearSemester.getId())
                .eq("grade_id", gradeId)
                .eq("week", week)
                .eq("sort_order", courseTime.getSortOrder())
                .orderBy(true, true, "clazz_id");
        List<Course> courseList = this.list(queryWrapper);
        map.put("courseList",courseList);
        return map;
    }

    @Override
    public Map<String,Object> getStaffTeacherCourse(Long staffId) {
        Map<String,Object> map = new HashMap<>();
        AcademicYearSemester academicYearSemester = academicYearSemesterMapper.selectOne(Wrappers.<AcademicYearSemester>lambdaQuery()
                .eq(AcademicYearSemester::getIsDefault, YesNoEnum.YES)
        );
        if(academicYearSemester == null)
            throw new ApiCode.ApiException(-5,"查询当前学年失败！");
        if(staffId == null){
            User user = (User)SecurityUtils.getSubject().getPrincipal();
            staffId = user.getStaffId();
        }

        QueryWrapper<Course> queryWrapper = new QueryWrapper<Course>()
                .eq("academic_year_semester_id", academicYearSemester.getId())
                .eq("teacher_id",staffId)
                .orderBy(true, true, "clazz_id");
        List<Course> courses = this.list(queryWrapper);
        map.put("courseList",courses);
        map.put("academicYearSemesterId",academicYearSemester.getId());
        return map;
    }

    @Override
    public Course getCurrentCourseByClazzId(Clazz clazz, Date time) {
        Map<String,Object> map = new HashMap<>();
        AcademicYearSemester academicYearSemester = academicYearSemesterMapper.selectOne(Wrappers.<AcademicYearSemester>lambdaQuery()
                .eq(AcademicYearSemester::getIsDefault, YesNoEnum.YES)
        );
        if(academicYearSemester == null)
            throw new ApiCode.ApiException(-5,"查询当前学年失败！");
        String timeStr = DateUtils.format(time,"HH:mm");
        QueryWrapper<CourseTime> courseTimeQueryWrapper = new QueryWrapper<CourseTime>();
        courseTimeQueryWrapper.eq("grade_id",clazz.getGradeId());
        courseTimeQueryWrapper.le("start_time",timeStr);
        courseTimeQueryWrapper.ge("end_time",timeStr);
        courseTimeQueryWrapper.or().eq("grade_id",clazz.getGradeId()).ge("start_time" ,timeStr);
        CourseTime courseTime = courseTimeMapper.selectOne(courseTimeQueryWrapper);

        if(courseTime == null)
            return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        Integer week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        QueryWrapper<Course> queryWrapper = new QueryWrapper<Course>()
                .eq("academic_year_semester_id", academicYearSemester.getId())
                .eq("clazz_id", clazz.getId())
                .eq("week", week)
                .eq("sort_order", courseTime.getSortOrder());
        Course course = this.getOne(queryWrapper);
        return course;
    }

    @Override
    public Map<String,Object> getStaffTeacherClazz(Long staffId) {
        Map<String,Object> map = new HashMap<>();
        AcademicYearSemester academicYearSemester = academicYearSemesterMapper.selectOne(Wrappers.<AcademicYearSemester>lambdaQuery()
                .eq(AcademicYearSemester::getIsDefault, YesNoEnum.YES)
        );
        if(academicYearSemester == null)
            throw new ApiCode.ApiException(-5,"查询当前学年失败！");
        if(staffId == null){
            User user = (User)SecurityUtils.getSubject().getPrincipal();
            staffId = user.getStaffId();
        }
        List<StaffLessonTeacher> staffLessonTeachers = staffLessonTeacherMapper.selectList(Wrappers.<StaffLessonTeacher>lambdaQuery()
                .eq(StaffLessonTeacher::getStaffId,staffId)
                .eq(StaffLessonTeacher::getIsCurrent, YesNoEnum.YES)
        );
        List<Long> clazzIds = staffLessonTeachers.stream().map(staffLessonTeacher -> staffLessonTeacher.getClazzId()).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(clazzIds)){
            List<Clazz> clazzList = clazzMapper.selectList(Wrappers.<Clazz>lambdaQuery().in(Clazz::getId,clazzIds));
            map.put("clazzList",clazzList);
        }
        map.put("academicYearSemesterId",academicYearSemester.getId());
        return map;
    }

    @Override
    public List<Long> getMeetingTime(List<Long> teacherIdList, Integer week) {
        return this.baseMapper.getMeetingTime(teacherIdList,week);
    }

    @Override
    public List<Map<String, String>> getListSubject(Long academicYearSemesterId, Long gradeId, Long subjectId) {
        List<Map<String, String>> items = new ArrayList<>();
        if (academicYearSemesterId == null)
            academicYearSemesterId = ((User) SecurityUtils.getSubject().getPrincipal()).getAcademicYearSemester().getId();
        if (gradeId == null || gradeId <= 0L) return items;
        List<StaffLessonTeacher> staffLessonTeacherList = staffLessonTeacherMapper.selectList(Wrappers.<StaffLessonTeacher>lambdaQuery()
                .eq(StaffLessonTeacher::getIsCurrent,YesNoEnum.YES)
                .eq(StaffLessonTeacher::getSubjectId,subjectId)
                .inSql(StaffLessonTeacher::getClazzId,"select id from sys_clazz where grade_id = "+gradeId+ " and academic_year_semester_id = "+academicYearSemesterId)
        );
        if(CollectionUtils.isEmpty(staffLessonTeacherList )){
            return items;
        }
        //获取所有备课组人员课程
        List<Long> staffId = staffLessonTeacherList.stream().map(item->item.getStaffId()).collect(Collectors.toList());
        List<Course> courseList = this.baseMapper.selectList(Wrappers.<Course>lambdaQuery()
                .eq(Course::getAcademicYearSemesterId,academicYearSemesterId)
                .eq(Course::getGradeId,gradeId)
                .in(Course::getTeacherId,staffId)
        );
        int rowId = 0;

        for (int week = 1; week <= 5; week++) {
            for (int sortOrder = 1; sortOrder <= 10; sortOrder++) {
                Map<String, String> itemMap = new HashMap<>();
                itemMap.put("id", String.valueOf(rowId));
                rowId++;
                itemMap.put("week", String.valueOf(week));
                itemMap.put("sortOrder", String.valueOf(sortOrder));
                for (StaffLessonTeacher staffLessonTeacher : staffLessonTeacherList){
                    final int finalWeek = week;
                    final int finalSortOrder = sortOrder;
                    List<Course> resultCourse = courseList.stream()
                            .filter(item->Objects.equals(staffLessonTeacher.getStaffId(),item.getTeacherId())
                                    && Objects.equals(item.getWeek(),finalWeek)
                                    && Objects.equals(item.getSortOrder(),finalSortOrder))
                            .collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(resultCourse)){
                        String clazzName = resultCourse.stream().map(item->"高"+item.getClazz().getGradeId() +"-"+item.getClazz().getName()).collect(Collectors.joining(","));
                        itemMap.put(staffLessonTeacher.getStaff().getName(),clazzName);
                    }else{
                        itemMap.put(staffLessonTeacher.getStaff().getName(),"");
                    }
                }
                items.add(itemMap);
            }
        }
        return items;
    }
}
