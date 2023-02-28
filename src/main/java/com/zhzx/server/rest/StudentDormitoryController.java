/**
* 项目：中华中学管理平台
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.StudentDormitory;
import com.zhzx.server.rest.req.StudentDormitoryParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.StudentDormitoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;

@Slf4j
@RestController
@Api(tags = "StudentDormitoryController", description = "宿舍学生表管理")
@RequestMapping("/v1/system/student-dormitory")
public class StudentDormitoryController {
    @Resource
    private StudentDormitoryService studentDormitoryService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<StudentDormitory> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.studentDormitoryService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<StudentDormitory> add(@RequestBody StudentDormitory entity) {

        return ApiResponse.ok(this.studentDormitoryService.saveEntity(entity));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<StudentDormitory> update(@RequestBody StudentDormitory entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.studentDormitoryService.updateAllFieldsById(entity);
        } else {
            this.studentDormitoryService.updateById(entity);
        }
        return ApiResponse.ok(this.studentDormitoryService.getById(entity.getId()));
    }

    /**
     * 删除
     *
     * @param id 要删除的对象id
     * @return int
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除")
    public ApiResponse<Integer> update(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.studentDormitoryService.removeById(id));
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
    public ApiResponse<IPage<StudentDormitory>> selectByPage(
        StudentDormitoryParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<StudentDormitory> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<StudentDormitory> authorityPage = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.studentDormitoryService.page(authorityPage, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Long> count(StudentDormitoryParam param) {
        QueryWrapper<StudentDormitory> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.studentDormitoryService.count(wrapper));
    }

    @GetMapping("/import/student/dormitory")
    @ApiOperation("学生宿舍导入")
    public ApiResponse<Object> importStudentParent(@RequestParam(value = "fileUrl") String fileUrl) {
        this.studentDormitoryService.importStudentDormitory(fileUrl);
        return ApiResponse.ok(null);
    }

}
