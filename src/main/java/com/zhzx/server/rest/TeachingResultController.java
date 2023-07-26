/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.*;
import com.zhzx.server.dto.annotation.MessageInfo;
import com.zhzx.server.rest.req.TeachingResultParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.*;
import com.zhzx.server.util.StringUtils;
import com.zhzx.server.vo.TeachingResultAuditVo;
import com.zhzx.server.vo.TeachingResultParamVo;
import com.zhzx.server.vo.TeachingResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Api(tags = "TeachingResultController", description = "教学成果表管理")
@RequestMapping("/v1/teaching/teaching-result")
public class TeachingResultController {
    @Resource
    private TeachingResultService teachingResultService;

    @Resource
    private TeachingResultClassifyService teachingResultClassifyService;

    @Resource
    private StaffSubjectService staffSubjectService;

    @Resource
    private StaffLessonTeacherService staffLessonTeacherService;

    @Resource
    private StaffResearchMemberService staffResearchMemberService;

    @Resource
    private StaffResearchLeaderService staffResearchLeaderService;

    @Resource
    private ClazzService clazzService;

    /**
     * 添加
     *
     * @param params 要添加的对象
     * @return 添加后的对象
     */
    @PostMapping("/add")
    @ApiOperation("新增")
    public ApiResponse<TeachingResult> save(@RequestBody Map<String, Object> params) {
        return ApiResponse.ok(this.teachingResultService.add(params));
    }

    @PostMapping("/init")
    @ApiOperation("新增")
    public ApiResponse<Object> init(String fileUrl) {
        return ApiResponse.ok(this.teachingResultService.init(fileUrl));
    }

    /**
     * 更新
     *
     * @param params 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/update")
    @ApiOperation("新增")
    public ApiResponse<TeachingResult> update(@RequestBody Map<String, Object> params) {
        return ApiResponse.ok(this.teachingResultService.update(params));
    }

    /**
     * 更新附件
     *
     * @param urls 附件
     * @param id 主键
     * @return 更新后对象
     */
    @PutMapping("/update-attach")
    @ApiOperation("新增")
    public ApiResponse<TeachingResult> updateAttach(@RequestParam(value = "urls") String urls,
                                              @RequestParam(value = "id") Long id) {
        return ApiResponse.ok(this.teachingResultService.updateAttach(urls, id));
    }

    /**
     * 删除
     *
     * @param id 要删除的对象id
     * @return int
     */
    @DeleteMapping("/delete-together/{id}")
    @ApiOperation("删除")
    public ApiResponse<Integer> deleteTogether(@PathVariable("id") Long id) {
        this.teachingResultService.deleteTogether(id);
        return ApiResponse.ok(null);
    }

    /**
     * 获取字表
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/detail/{id}")
    @ApiOperation("详情")
    public ApiResponse<Map<String, Object>> detail(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.teachingResultService.detail(id));
    }

    /**
     * 审核
     *
     * @param teachingResultAuditVo 更新参数对象
     * @return 单条数据
     */
    @PutMapping("/audit")
    @ApiOperation("审核")
    public ApiResponse<TeachingResult> audit(@RequestBody TeachingResultAuditVo teachingResultAuditVo) {
        return ApiResponse.ok(this.teachingResultService.audit(teachingResultAuditVo));
    }

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<TeachingResult> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.teachingResultService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<TeachingResult> add(@RequestBody TeachingResult entity) {
        entity.setDefault().validate(true);
        this.teachingResultService.save(entity);
        return ApiResponse.ok(this.teachingResultService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<TeachingResult> update(@RequestBody TeachingResult entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.teachingResultService.updateAllFieldsById(entity);
        } else {
            this.teachingResultService.updateById(entity);
        }
        return ApiResponse.ok(this.teachingResultService.getById(entity.getId()));
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
        return ApiResponse.ok(this.teachingResultService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<TeachingResult> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.teachingResultService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(TeachingResultParam param, @RequestBody TeachingResult entity) {
        return ApiResponse.ok(this.teachingResultService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.teachingResultService.removeByIds(idList));
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
    public ApiResponse<IPage<TeachingResult>> selectByPage(
        TeachingResultParamVo param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<TeachingResult> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<TeachingResult> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.teachingResultService.page(page, wrapper));
    }

    /**
     * 分页查询
     *
     * @param param 查询参数
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return int
     */
    @GetMapping("/search-detail")
    @ApiOperation("分页查询(含关联表)")
    public ApiResponse<Map<String, Object>> searchDetail(
            TeachingResultVo param,
            @RequestParam(value = "teacherIds", defaultValue = "", required = false) String teacherIds,
            @RequestParam(value = "resultClassifyIdsParent", defaultValue = "", required = false) String resultClassifyIdsParent,
            @RequestParam(value = "resultClassifyIds", defaultValue = "", required = false) String resultClassifyIds,
            @RequestParam(value = "orderByClause", defaultValue = "result_date,true") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<TeachingResult> wrapper = param.toQueryWrapper();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (user.getStaffId() != 0) {
            wrapper.eq(true, "teacher_id", user.getStaffId());
        } else {
            if (!StringUtils.isNullOrEmpty(teacherIds)) {
                wrapper.in("teacher_id", Arrays.stream(teacherIds.split("[,]")).mapToLong(Long::valueOf).boxed().collect(Collectors.toList()));
            }
        }

        if (!StringUtils.isNullOrEmpty(resultClassifyIds)) {
            wrapper.in("result_classify_id", Arrays.stream(resultClassifyIds.split("[,]")).mapToLong(Long::valueOf).boxed().collect(Collectors.toList()));
        } else {
            if (!StringUtils.isNullOrEmpty(resultClassifyIdsParent)) {
                List<TeachingResultClassify> childLists = this.teachingResultClassifyService.list(Wrappers.<TeachingResultClassify>lambdaQuery()
                        .select(TeachingResultClassify::getId)
                        .inSql(TeachingResultClassify::getParentId, resultClassifyIdsParent));
                if (CollectionUtils.isNotEmpty(childLists)) {
                    wrapper.in("result_classify_id", childLists.stream().mapToLong(TeachingResultClassify::getId).boxed().collect(Collectors.toList()));
                }
            }
        }

        // 必按审核状态排序 待审核 -> 拒绝 -> 通过
        wrapper.orderByAsc("customOrderNum");
        // 这里分隔符配合twx query
        StringTokenizer st = new StringTokenizer(orderByClause, "[|]", false);
        while (st.hasMoreElements()) {
            String next = st.nextElement().toString();
            wrapper.orderBy(true, next.endsWith("true"), next.substring(0, next.indexOf(",")));
        }
        IPage<TeachingResult> page = new Page<>(pageNum, pageSize);
        Map<String, Object> res = new HashMap<>();
        res.put("self", user.getStaffId() != 0);
        res.put("realName", user.getRealName());
        res.put("info", this.teachingResultService.queryPage(page, wrapper));
        return ApiResponse.ok(res);
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Long> count(TeachingResultParam param) {
        QueryWrapper<TeachingResult> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.teachingResultService.count(wrapper));
    }


    /**
     * 分页查询我的成果
     *
     * @param param 查询参数
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return int
     */
    @GetMapping("/search/mine")
    @ApiOperation("分页查询我的成果")
    public ApiResponse<IPage<TeachingResult>> selectMineByPage(
            TeachingResultParamVo param,
            @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Subject userSubject = SecurityUtils.getSubject();
        User user = (User) userSubject.getPrincipal();
        QueryWrapper<TeachingResult> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        wrapper.eq("teacher_id", user.getStaffId());
        IPage<TeachingResult> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.teachingResultService.page(page, wrapper));
    }

    /**
     * 分页查询审核成果
     *
     * @param param 查询参数
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return int
     */
    @GetMapping("/search/audit")
    @ApiOperation("分页查询审核成果")
    public ApiResponse<IPage<TeachingResult>> selectAuditByPage(
            TeachingResultParamVo param,
            @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<TeachingResult> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<TeachingResult> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.teachingResultService.page(page, wrapper));
    }

    /**
     * 分页查询填报成果
     *
     * @param param 查询参数
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return int
     */
    @GetMapping("/search/report")
    @ApiOperation("分页查询填报成果")
    public ApiResponse<IPage<TeachingResult>> selectReportByPage(
            TeachingResultParamVo param,
            @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Subject userSubject = SecurityUtils.getSubject();
        User user = (User) userSubject.getPrincipal();
        QueryWrapper<TeachingResult> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        wrapper.eq("editor_id", user.getStaffId());
        IPage<TeachingResult> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.teachingResultService.page(page, wrapper));
    }

    /**
     * 分页查询成果
     *
     * @param param 查询参数
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return int
     */
    @GetMapping("/search/query")
    @ApiOperation("分页查询成果")
    public ApiResponse<IPage<TeachingResult>> selectQueryByPage(
            TeachingResultParamVo param,
            @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<TeachingResult> wrapper = param.toQueryWrapper();
        if (CollectionUtils.isEmpty(param.getTeacherIdList())) {
            Subject userSubject = SecurityUtils.getSubject();
            User user = (User) userSubject.getPrincipal();
            List<StaffResearchLeader> staffResearchLeaders = null;
            List<StaffGradeLeader> staffGradeLeaders = null;
            if (user.getStaffId() > 0) {
                staffResearchLeaders = user.getStaff().getStaffResearchLeaderList();
                staffGradeLeaders = user.getStaff().getStaffGradeLeaderList();
            }

            Set<Long> teacherIds = new HashSet<>();

            // 教研组长
            if (CollectionUtils.isNotEmpty(staffResearchLeaders)) {
                List<Long> subjectIds = staffResearchLeaders.stream().map(StaffResearchLeader::getSubjectId).collect(Collectors.toList());
                List<StaffSubject> staffSubjects = this.staffSubjectService.list(Wrappers.<StaffSubject>lambdaQuery()
                        .in(StaffSubject::getSubjectId, subjectIds));
                if (CollectionUtils.isNotEmpty(staffSubjects)) {
                    teacherIds.addAll(staffSubjects.stream().map(StaffSubject::getStaffId).collect(Collectors.toList()));
                }
            }
            // 年级组长
            if (CollectionUtils.isNotEmpty(staffGradeLeaders)) {
                List<Long> gradeIds = staffGradeLeaders.stream().map(StaffGradeLeader::getGradeId).collect(Collectors.toList());
                List<Clazz> clazzList = clazzService.list(Wrappers.<Clazz>lambdaQuery()
                        .select(Clazz::getId)
                        .in(Clazz::getGradeId, gradeIds));
                if (CollectionUtils.isNotEmpty(clazzList)) {
                    List<StaffLessonTeacher> staffLessonTeachers = this.staffLessonTeacherService.list(Wrappers.<StaffLessonTeacher>lambdaQuery()
                            .in(StaffLessonTeacher::getClazzId, clazzList.stream().map(Clazz::getId).collect(Collectors.toList())));
                    if (CollectionUtils.isNotEmpty(staffLessonTeachers)) {
                        teacherIds.addAll(staffLessonTeachers.stream().map(StaffLessonTeacher::getStaffId).collect(Collectors.toList()));
                    }
                }
            }

            wrapper.in(!teacherIds.isEmpty(), "teacher_id", new ArrayList<>(teacherIds));
        }

        if (null != param.getSubjectId()) {
            Set<Long> teacherIdsRange = new HashSet<>();
            List<StaffResearchMember> staffResearchMembers = this.staffResearchMemberService.list(Wrappers.<StaffResearchMember>lambdaQuery()
                    .eq(StaffResearchMember::getSubjectId, param.getSubjectId()));
            List<StaffResearchLeader> staffResearchLeaders1 = this.staffResearchLeaderService.list(Wrappers.<StaffResearchLeader>lambdaQuery()
                    .eq(StaffResearchLeader::getSubjectId, param.getSubjectId()));
            if (CollectionUtils.isNotEmpty(staffResearchMembers)) {
                staffResearchMembers.forEach(t -> teacherIdsRange.add(t.getStaffId()));
            }
            if (CollectionUtils.isNotEmpty(staffResearchLeaders1)) {
                staffResearchLeaders1.forEach(t -> teacherIdsRange.add(t.getStaffId()));
            }
            wrapper.in(!teacherIdsRange.isEmpty(), "teacher_id", teacherIdsRange);
        }
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<TeachingResult> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.teachingResultService.page(page, wrapper));
    }

    /**
     * 添加新的成果
     */
    @PostMapping("/add-result")
    @MessageInfo(name="添加成果",title = "添加",content = "您有新的成果需要审核")
    @ApiOperation("添加新的成果")
    public ApiResponse<String> addResult(@RequestBody Map<String, Object> params) throws ClassNotFoundException, InvocationTargetException, InstantiationException, NoSuchMethodException, IllegalAccessException {
        this.teachingResultService.addResult(params);
        return ApiResponse.ok(null);
    }
    /**
     * 修改成果
     */
    @PostMapping("/update-result")
    @MessageInfo(name="修改成果",title = "修改",content = "您有新的成果需要审核")
    @ApiOperation("修改成果")
    public ApiResponse<String> updateResult(
            @RequestBody Map<String, Object> params,
            @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) throws ClassNotFoundException, InvocationTargetException, InstantiationException, NoSuchMethodException, IllegalAccessException {
        this.teachingResultService.updateResult(params, updateAllFields);
        return ApiResponse.ok(null);
    }
    /**
     * 删除成果
     */
    @GetMapping("/delete-result")
    @ApiOperation("删除成果")
    public ApiResponse<String> deleteResult(@RequestParam(value = "id") Long id, @RequestParam(value = "schema") String schema) throws ClassNotFoundException, InvocationTargetException, InstantiationException, NoSuchMethodException, IllegalAccessException {
        this.teachingResultService.deleteResult(id, schema);
        return ApiResponse.ok(null);
    }
    /**
     * 审核成果
     */
    @PostMapping("/audit-result")
    @MessageInfo(name="审核成果",title = "审核",content = "您的成果审核")
    @ApiOperation("审核成果")
    public ApiResponse<String> auditResult(@RequestBody TeachingResult entity) {
        this.teachingResultService.auditResult(entity);
        return ApiResponse.ok(null);
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出成果")
    public void exportExcel(HttpServletResponse response, HttpServletRequest request,
                            TeachingResultParamVo param) throws IOException, InvalidFormatException {
        QueryWrapper<TeachingResult> wrapper = param.toQueryWrapper();
        XSSFWorkbook book = this.teachingResultService.exportExcel(wrapper);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = "成果表(" + sdf.format(new Date()) + ").xlsx";
        //获取浏览器使用的编码
        String encoding = request.getCharacterEncoding();
        if (encoding != null && encoding.length() > 0) {
            fileName = URLEncoder.encode(fileName, encoding);
        } else {
            //默认编码是utf-8
            fileName = URLEncoder.encode(fileName, "UTF-8");
        }
//        response.reset();
        response.setCharacterEncoding(encoding);
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        OutputStream out = response.getOutputStream();
        book.write(out);
        out.flush();
        out.close();
        book.close();
    }

    /**
     * 待处理事务成果
     */
    @GetMapping("/detail-result")
    @ApiOperation("带处理事务成果详情")
    public ApiResponse<String> detailResult(@RequestParam Long teacherResultId) {
        return ApiResponse.ok(this.teachingResultService.detailResult(teacherResultId));
    }
}
