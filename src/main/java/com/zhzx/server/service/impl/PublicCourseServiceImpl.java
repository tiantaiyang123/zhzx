/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：公开课表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
 */

package com.zhzx.server.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.Clazz;
import com.zhzx.server.domain.Course;
import com.zhzx.server.domain.Schoolyard;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.ClazzService;
import com.zhzx.server.service.SchoolyardService;
import com.zhzx.server.util.CellUtils;
import com.zhzx.server.util.DateUtils;
import com.zhzx.server.util.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhzx.server.service.PublicCourseService;
import com.zhzx.server.repository.PublicCourseMapper;
import com.zhzx.server.repository.base.PublicCourseBaseMapper;
import com.zhzx.server.domain.PublicCourse;

@Service
public class PublicCourseServiceImpl extends ServiceImpl<PublicCourseMapper, PublicCourse> implements PublicCourseService {

    @Value("${web.upload-path}")
    private String uploadPath;
    @Resource
    private SchoolyardService schoolyardService;
    @Resource
    private ClazzService clazzService;

    @Override
    public int updateAllFieldsById(PublicCourse entity) {
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
    public boolean saveBatch(Collection<PublicCourse> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(PublicCourseBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importExcel(Long academicYearSemesterId,Long gradeId, String fileUrl) {
        if (StringUtils.isNullOrEmpty(fileUrl)){
            throw new ApiCode.ApiException(-1, "没有上传文件！");
        }
        String[] items = fileUrl.split("/");
        File file = new File(uploadPath + "\\" + items[items.length - 2] + "\\" + items[items.length - 1]);
        if (!file.exists()) {
            throw new ApiCode.ApiException(-1, "文件不存在！");
        }
        try {
            // 读取 Excel
            XSSFWorkbook book = new XSSFWorkbook(file);
            XSSFSheet sheet = book.getSheetAt(0);
            //获得总行数
            int rowNum = sheet.getPhysicalNumberOfRows();
            // 公开课列表
            List<PublicCourse> publicCourses = new ArrayList<>();
            // 获取校区列表
            Map<String,List<Schoolyard>> schoolyardListMap = schoolyardService.list().stream().collect(Collectors.groupingBy(Schoolyard::getName));
            List<Clazz> clazzList = clazzService.list(Wrappers.<Clazz>lambdaQuery()
                    .eq(Clazz::getGradeId,gradeId)
                    .eq(Clazz::getAcademicYearSemesterId,academicYearSemesterId)
            );
            // 读取单元格数据
            PublicCourse publicCourse = null;
            StringBuilder sb = new StringBuilder();
            Set<String> dateSet = new HashSet<>();
            for (int rowIndex = 2; rowIndex < rowNum; rowIndex++) {
                publicCourse = new PublicCourse();
                XSSFRow rowValue = sheet.getRow(rowIndex);
                publicCourse.setAcademicYearSemesterId(academicYearSemesterId);
                String dateCell = CellUtils.getCellValue(rowValue.getCell(0), "yyyy-MM-dd");
                if (DateUtils.parse(dateCell, "yyyy-MM-dd").getTime() <= DateUtils.parse(DateUtils.format(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd").getTime()) {
                    sb.append("第").append(rowIndex + 1).append("行: ").append("日期早于当前日期").append("\r\n");
                    continue;
                }
                List<Schoolyard> schoolyardList = schoolyardListMap.get(CellUtils.getCellValue(rowValue.getCell(6)).trim());
                if(schoolyardList == null){
                    sb.append("第").append(rowIndex + 1).append("行: ").append("校区：").append(CellUtils.getCellValue(rowValue.getCell(6))).append("不存在").append("\r\n");
                    continue;
                }
                Long schoolyardId = schoolyardList.get(0).getId();
                publicCourse.setSchoolyardId(schoolyardId);
                publicCourse.setStartTime(DateUtils.parse(dateCell,"yyyy-MM-dd"));
                //将分割符切换为逗号
                String sortOrder = CellUtils.getCellValue(rowValue.getCell(2));
                sortOrder = sortOrder.substring(1,sortOrder.length()-1)
                        .replace("-",",");
                publicCourse.setSortOrder(sortOrder);
                publicCourse.setCourseName(CellUtils.getCellValue(rowValue.getCell(5)));
                publicCourse.setGradeId(gradeId);
                //查询class id
                String clazzName = CellUtils.getCellValue(rowValue.getCell(4));
                // 将班级拆分成和数据库一致
                List<String> clazzNameList = Arrays.stream(clazzName.substring(2, clazzName.length()-1)
                        .replace("、", ",").split(",")).map(item -> item + "班").collect(Collectors.toList());
                String clazzIds = clazzList.stream().filter(item -> clazzNameList.contains(item.getName()) && item.getSchoolyardId().equals(schoolyardId)).map(item->item.getId().toString()).collect(Collectors.joining(","));
                if(StringUtils.isNullOrEmpty(clazzIds)){
                    sb.append("第").append(rowIndex + 1).append("行: ").append("班级：").append(CellUtils.getCellValue(rowValue.getCell(4))).append("未匹配到").append("\r\n");
                    continue;
                }
                dateSet.add(dateCell);
                publicCourse.setClazzId(clazzIds);
                publicCourse.setClazzName(rowValue.getCell(4).getStringCellValue());
                publicCourse.setTeacherName(CellUtils.getCellValue(rowValue.getCell(3)));
                publicCourse.setSubjectName(CellUtils.getCellValue(rowValue.getCell(1)));
                publicCourse.setAddress(CellUtils.getCellValue(rowValue.getCell(8)));
                publicCourse.setClassify(CellUtils.getCellValue(rowValue.getCell(7)));
                publicCourse.setTeacherId(-1L);
                publicCourse.setDefault();
                publicCourses.add(publicCourse);
            }
            if (sb.length() != 0) {
                throw new ApiCode.ApiException(-1, sb.toString());
            }
            String applyStr = dateSet.stream().map(item -> "to_days('" + item +"')").collect(Collectors.joining(" , "));
            QueryWrapper<PublicCourse> publicCourseQueryWrapper = new QueryWrapper<PublicCourse>()
                    .eq("academic_year_semester_id", academicYearSemesterId)
                    .eq("grade_id", gradeId)
                    .apply("to_days(start_time)" + "in" + "({0})",applyStr);
            this.remove(publicCourseQueryWrapper);
            this.saveBatch(publicCourses, publicCourses.size());
            this.getBaseMapper().updateTeacherId(academicYearSemesterId, gradeId);
        } catch (IOException | InvalidFormatException e) {
            throw new ApiCode.ApiException(-1, "导入数据失败！");
        } catch (ParseException p) {
            throw new ApiCode.ApiException(-1, "时间解析失败");
        }finally {
            file.delete();
        }
    }
}
