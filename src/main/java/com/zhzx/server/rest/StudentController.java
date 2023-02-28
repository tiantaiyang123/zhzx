/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.Student;
import com.zhzx.server.domain.User;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.misc.shiro.ShiroEncrypt;
import com.zhzx.server.rest.req.StudentParam;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.StudentService;
import com.zhzx.server.service.UserService;
import com.zhzx.server.util.StringUtils;
import com.zhzx.server.vo.StudentInfoVo;
import com.zhzx.server.vo.StudentParamVo;
import com.zhzx.server.vo.StudentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Api(tags = "StudentController", description = "学生表管理")
@RequestMapping("/v1/system/student")
public class StudentController {
    @Resource
    private StudentService studentService;
    @Resource
    private UserService userService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<Student> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.studentService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<Student> add(@RequestBody Student entity) {
        return ApiResponse.ok(studentService.saveStudent(entity));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<Student> update(@RequestBody Student entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        return ApiResponse.ok(this.studentService.updateStudent(entity, updateAllFields));

    }

    /**
     * 删除
     *
     * @param id 要删除的对象id
     * @return int
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除")
    public ApiResponse<Integer> delete(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.studentService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<Student> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.studentService.saveBatch(entityList));
    }

    /**
     * 批量更新
     *
     * @param param  更新条件
     * @param entity 要更新的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-update")
    @ApiOperation("批量更新")
    public ApiResponse<Boolean> batchUpdate(StudentParam param, @RequestBody Student entity) {
        return ApiResponse.ok(this.studentService.update(entity, param.toQueryWrapper()));
    }

    /**
     * 批量删除
     *
     * @param idList 要删除的对象id
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-delete")
    @ApiOperation("批量删除")
    public ApiResponse<Boolean> batchDelete(@RequestBody List<Long> idList) {
        return ApiResponse.ok(this.studentService.removeByIds(idList));
    }

    /**
     * 分页查询
     *
     * @param param 查询参数
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return int
     */
    @GetMapping("/search")
    @ApiOperation("分页查询")
    public ApiResponse<IPage<Student>> selectByPage(
        StudentParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<Student> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<Student> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.studentService.page(page, wrapper));
    }

    /**
     * 分页查询
     *
     * @param param 查询参数
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return int
     */
    @GetMapping("/queryStudentByPage")
    @ApiOperation("分页查询")
    public ApiResponse<IPage<Student>> queryStudentByPage(
            StudentParam param,
            @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<Student> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<Student> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.studentService.queryStudentByPage(page, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Long> count(StudentParam param) {
        QueryWrapper<Student> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.studentService.count(wrapper));
    }

    /**
     * 查询班级所有学生
     *
     * @param clazzId 查询参数
     * @return int
     */
    @GetMapping("/list/by/clazz")
    @ApiOperation("查询班级所有学生")
    public ApiResponse<Object> listByClazz(@RequestParam Long clazzId) {
        Map<String,Object> map = new HashMap<>();
        List<Student> studentList = this.studentService.listByClazz(clazzId);
        map.put("studentList",studentList);
        map.put("clazzId",clazzId);
        return ApiResponse.ok(map);
    }

    @GetMapping("/list/by/clazz-student")
    @ApiOperation("查询班级所有学生")
    public ApiResponse<Object> listByClazzStudent(@RequestParam Long clazzId,
                                                  @RequestParam(value = "academicYearSemesterId") Long academicYearSemesterId,
                                                  @RequestParam(value = "studentName", required = false) String studentName) {
        return ApiResponse.ok(this.studentService.listByClazzStudent(clazzId, academicYearSemesterId, studentName));
    }

    @GetMapping("/import-excel")
    @ApiOperation("导入学生表")
    public ApiResponse<Boolean> importExcel(
            @RequestParam(value = "schoolyardId") Long schoolyardId,
            @RequestParam(value = "academicYearSemesterId") Long academicYearSemesterId,
            @RequestParam(value = "gradeId") Long gradeId,
            @RequestParam(value = "fileUrl") String fileUrl) {
        this.studentService.importExcel(schoolyardId, academicYearSemesterId, gradeId, fileUrl);
        return ApiResponse.ok(null);
    }

    /**
     * 分页查询
     *
     * @param param 查询参数
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return int
     */
    @GetMapping("/search/info")
    @ApiOperation("分页查询学生信息")
    public ApiResponse<IPage<StudentInfoVo>> selectInfoByPage(
            StudentParamVo param,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        IPage<StudentInfoVo> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.studentService.selectInfoByPage(page, param));
    }

    @GetMapping("/search/info/tree")
    @ApiOperation("查询学生树信息")
    public ApiResponse<Object> selectInfoTree(@RequestParam(required = false) Long staffId) {
        List<StudentVo> studentVoList = this.studentService.selectListWithClazz(null,staffId);
        List<Map<String,Object>> resultList = new ArrayList<>();
        Map<String,List<StudentVo>> map = studentVoList.stream().collect(Collectors.groupingBy(StudentVo::getGradeName));
        for(String gradeName : map.keySet()){
            Map<String,Object> result = new HashMap<>();
            result.put("name",gradeName);
            result.put("id","grade_"+map.get(gradeName).get(0).getGradeId());
            Map<String,List<StudentVo>> clazzMap = map.get(gradeName).stream().collect(Collectors.groupingBy(StudentVo::getClazzName));
            List<Map<String,Object>> list = new ArrayList<>();
            for (String clazzName : clazzMap.keySet()){
                Map<String,Object> clazz = new HashMap<>();
                clazz.put("name",clazzName);
                clazz.put("id","clazz_"+clazzMap.get(clazzName).get(0).getClazzId());
                clazz.put("children",clazzMap.get(clazzName));
                list.add(clazz);
            }
            result.put("children",list);
            resultList.add(result);
        }
        return ApiResponse.ok(resultList);
    }

    @PostMapping("/rest-password-student")
    @ApiOperation("重置学生密码")
    public ApiResponse<String> resetPassword() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (!"admin".equals(user.getUsername()))
            return ApiResponse.fail(ApiCode.FAILED);
        Page<User> page = this.userService.page(
                new Page<>(1, 9999),
                Wrappers.<User>lambdaQuery()
                        .gt(User::getStudentId, 0L)
                        .eq(User::getIsDelete, YesNoEnum.NO)
                        .eq(User::getPassword, ShiroEncrypt.encrypt("winner!")));
        List<User> userList = page.getRecords();
        userList.forEach(student -> this.userService.update(null,
                Wrappers.<User>lambdaUpdate()
                        .set(User::getPassword, ShiroEncrypt.encrypt(StringUtils.getPassword(student.getStudent().getIdNumber())))));
        return ApiResponse.ok(null);
    }
}
