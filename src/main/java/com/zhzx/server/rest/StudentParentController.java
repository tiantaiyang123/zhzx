/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import javax.annotation.Resource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.Student;
import com.zhzx.server.dto.wx.WxParent;
import com.zhzx.server.dto.wx.WxStudent;
import com.zhzx.server.enums.MessageParentEnum;
import com.zhzx.server.enums.RelationEnum;
import com.zhzx.server.service.WxSendMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.*;
import com.zhzx.server.domain.StudentParent;
import com.zhzx.server.rest.req.StudentParentParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.StudentParentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags = "StudentParentController", description = "学生家长表管理")
@RequestMapping("/v1/system/student-parent")
public class StudentParentController {
    @Resource
    private StudentParentService studentParentService;
    @Resource
    private WxSendMessageService wxSendMessageService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<StudentParent> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.studentParentService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<StudentParent> add(@RequestBody StudentParent entity) {
        entity.setDefault().validate(true);
        this.studentParentService.save(entity);
        return ApiResponse.ok(this.studentParentService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<StudentParent> update(@RequestBody StudentParent entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.studentParentService.updateAllFieldsById(entity);
        } else {
            this.studentParentService.updateById(entity);
        }
        return ApiResponse.ok(this.studentParentService.getById(entity.getId()));
    }

    /**
     * 删除
     *
     * @param id 要删除的对象id
     * @return int
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除")
    public ApiResponse<Boolean> delete(@PathVariable("id") Long id) {
        try {
            StudentParent studentParent = new StudentParent();
            studentParent.setId(id);
            wxSendMessageService.removeWxParent(new ArrayList<StudentParent>(){{this.add(studentParent);}});
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return ApiResponse.ok(this.studentParentService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<StudentParent> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.studentParentService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(StudentParentParam param, @RequestBody StudentParent entity) {
        return ApiResponse.ok(this.studentParentService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.studentParentService.removeByIds(idList));
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
    public ApiResponse<IPage<StudentParent>> selectByPage(
        StudentParentParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<StudentParent> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<StudentParent> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.studentParentService.page(page, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Integer> count(StudentParentParam param) {
        QueryWrapper<StudentParent> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.studentParentService.count(wrapper));
    }

    /**
     * count查询
     *
     * @return int
     */
    @GetMapping("/import/student/parent")
    @ApiOperation("学生家长导入")
    public ApiResponse<Object> importStudentParent(@RequestParam(value = "fileUrl") String fileUrl) {
        this.studentParentService.importStudentParent(fileUrl);
        return ApiResponse.ok(null);
    }

    /**
     * count查询
     *
     * @return int
     */
    @PostMapping("/import/student/parent/wx")
    @ApiOperation("学生家长写入微信")
    public ApiResponse<Object> importStudentParentWx(@RequestBody List<StudentParent> entity) {
        String message = this.wxSendMessageService.createWxParent(entity);
        if(StringUtils.isBlank(message)){
            return ApiResponse.ok(message);
        }else{
            return ApiResponse.fail(-5,message);
        }
    }

    /**
     * count查询
     *
     * @return int
     */
    @PostMapping("/import/student/parent/wx/by/clazz/{clazzId}")
    @ApiOperation("学生家长写入微信")
    public ApiResponse<Object> importStudentParentWx(@PathVariable Long clazzId) {
        this.wxSendMessageService.syncStudentParent(clazzId);
        return ApiResponse.ok(null);
    }

    /**
     * count查询
     *
     * @return int
     */
    @PostMapping("/select/student/parent/wx/by/student")
    @ApiOperation("学生家长写入微信")
    public ApiResponse<Object> selectStudentParentWx(@RequestBody Student student) {
        return ApiResponse.ok( this.wxSendMessageService.syncStudentParent(student));
    }

    /**
     * count查询
     *
     * @return int
     */
    @PostMapping("/import/student/parent/wx/by/student")
    @ApiOperation("学生家长写入微信")
    public ApiResponse<Object> importStudentParentWx(@RequestBody WxStudent wxStudent) {

        if(CollectionUtils.isNotEmpty(wxStudent.getParents())){
            List<StudentParent> entityList = new ArrayList<>();
            for (WxParent wxParent : wxStudent.getParents()){
                StudentParent studentParent = new StudentParent();
                studentParent.setWxParentId(wxParent.getParent_userid());
                studentParent.setStudentId(wxStudent.getId());
                studentParent.setRelation(RelationEnum.O);
                studentParent.setName(wxStudent.getName()+wxParent.getRelation());
                studentParent.setType(MessageParentEnum.CREATED);
                studentParent.setPhone(wxParent.getMobile());
                studentParent.setDefault().validate(true);
                int count = studentParentService.count(Wrappers.<StudentParent>lambdaQuery()
                        .eq(StudentParent::getPhone,wxParent.getMobile())
                );
                if(count == 0){
                    entityList.add(studentParent);
                }
            }
            return ApiResponse.ok(this.studentParentService.saveBatch(entityList));
        }
        return ApiResponse.fail(-5,"没有家长需要绑定");
    }
}
