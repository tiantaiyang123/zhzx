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
import com.zhzx.server.domain.Staff;
import com.zhzx.server.domain.User;
import com.zhzx.server.dto.PasswordDto;
import com.zhzx.server.dto.annotation.MessageInfo;
import com.zhzx.server.misc.shiro.ShiroEncrypt;
import com.zhzx.server.rest.req.UserParam;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.StaffService;
import com.zhzx.server.service.UserService;
import com.zhzx.server.util.CellUtils;
import com.zhzx.server.util.JWTUtils;
import com.zhzx.server.util.StringUtils;
import com.zhzx.server.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "UserController", description = "用户表管理")
@RequestMapping("/v1/system/user")
public class UserController {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Resource
    private UserService userService;
    @Resource
    private StaffService staffService;


    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<User> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.userService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<User> add(@RequestBody User entity) {
        entity.setDefault().validate(true);
        entity.setPassword(ShiroEncrypt.encrypt("winner!"));
        this.userService.save(entity);
        return ApiResponse.ok(this.userService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<User> update(@RequestBody User entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (!StringUtils.isNullOrEmpty(entity.getPassword())) {
            entity.setPassword(ShiroEncrypt.encrypt(entity.getPassword()));
        }
        if (updateAllFields) {
            this.userService.updateAllFieldsById(entity);
        } else {
            this.userService.updateById(entity);
        }
        return ApiResponse.ok(this.userService.getById(entity.getId()));
    }

    /**
     * 禁用
     *
     * @param id 要删除的对象id
     * @return int
     */
    @GetMapping("/update-isdelete/{id}")
    @ApiOperation("禁用")
    public ApiResponse<Staff> updateDelete(@PathVariable("id") Long id) {
        this.userService.updateIsDelete(id);
        return ApiResponse.ok(null);
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
        return ApiResponse.ok(this.userService.removeUser(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<User> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.userService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(UserParam param, @RequestBody User entity) {
        return ApiResponse.ok(this.userService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.userService.removeByIds(idList));
    }

    /**
     * 分页查询
     *
     * @param param    查询参数
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return int
     */
    @GetMapping("/search")
    @ApiOperation("分页查询")
    public ApiResponse<IPage<User>> selectByPage(
            UserParam param,
            @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<User> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<User> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.userService.page(page, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Long> count(UserParam param) {
        QueryWrapper<User> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.userService.count(wrapper));
    }

    private static final String PASSWORD_ENCODE = "Q_wqmm192nQDQOM87XXi0l";
    /**
     * <pre>
     * 用户登陆接口
     * </pre>
     */
    @ApiOperation("用户登陆接口")
    @MessageInfo(name="普通登陆接口",title = "登录",content = "普通登陆接口")
    @PostMapping("/login")
    public ApiResponse<Map> login(@RequestBody User user) throws Exception {
        Map<String, Object> result = new HashMap<>();
//        UserVo loginUser = this.userService.login(user.getUsername(), user.getPassword());
//        使用登录验证码
        String password = user.getPassword();
        String decodeString = new String(Base64Utils.decodeFromString(password));
        if (!decodeString.startsWith(PASSWORD_ENCODE)) {
            return ApiResponse.fail(40001, "加密验证失败");
        }
        UserVo loginUser = this.userService.loginV2(user.getUsername(), decodeString.replace(PASSWORD_ENCODE, ""), user.getCode());
        user.setId(loginUser.getUserInfo().getId());
        user.setRealName(loginUser.getUserInfo().getRealName());
        result.put("userInfo", loginUser);
        result.put("token", JWTUtils.sign(loginUser.getUserInfo().getUsername(), loginUser.getUserInfo().getPassword()));
        return ApiResponse.ok(result);
    }

    @ApiOperation("用户登陆接口")
    @MessageInfo(name="值班老师验证登陆接口",title = "登录",content = "值班老师验证登陆接口")
    @PostMapping("/login-from-wx-url")
    public ApiResponse<Map> loginFromWxUrl(@RequestBody User user) throws UnsupportedEncodingException {
        Map<String, Object> result = new HashMap<>();
        UserVo loginUser = this.userService.loginWithoutPassword(user.getUsername());
        if (loginUser.getUserInfo().getStaff() == null) {
            return ApiResponse.fail(-5, "该用户不是教职工，无法登录！");
        }
        user.setId(loginUser.getUserInfo().getId());
        user.setRealName(loginUser.getUserInfo().getRealName());
        result.put("userInfo", loginUser);
        result.put("token", JWTUtils.sign(loginUser.getUserInfo().getUsername(), loginUser.getUserInfo().getPassword()));
        return ApiResponse.ok(result);
    }

    /**
     * <pre>
     * 教职工登陆接口
     * </pre>
     */
    @ApiOperation("教职工登陆接口")
    @MessageInfo(name="教职工登陆",title = "登录",content = "教职工登陆接口")
    @PostMapping("/login/staff")
    public ApiResponse<Map> staffLogin(@RequestBody User user) throws UnsupportedEncodingException {
        Map<String, Object> result = new HashMap<>();
//        UserVo loginUser = this.userService.login(user.getUsername(), user.getPassword());
        UserVo loginUser = this.userService.loginV2(user.getUsername(), user.getPassword(),user.getCode());
        Staff staff = staffService.getById(loginUser.getUserInfo().getStaffId());
        if (staff == null) {
            return ApiResponse.fail(-5, "该用户不是教职工，无法登录！");
        }
        user.setId(loginUser.getUserInfo().getId());
        user.setRealName(loginUser.getUserInfo().getRealName());
        result.put("userInfo", loginUser);
        result.put("token", JWTUtils.sign(loginUser.getUserInfo().getUsername(), loginUser.getUserInfo().getPassword()));
        return ApiResponse.ok(result);
    }

    /**
     * <pre>
     * 用户注册接口
     * </pre>
     */
    @ApiOperation("用户注册接口")
    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody User user) {
        return ApiResponse.ok(this.userService.register(user, "ROLE_USER"));
    }

    /**
     * <pre>
     * 查询当前登录的用户信息
     * </pre>
     */
    @ApiOperation("查询当前登录的用户信息")
    @GetMapping("/info")
    public ApiResponse<User> currentUserInfo() {
        Subject subject = SecurityUtils.getSubject();
        return ApiResponse.ok(subject.getPrincipal());
    }

    @PostMapping("/change-password")
    @ApiOperation("修改密码")
    public ApiResponse<User> updatePassword(@RequestBody PasswordDto passwordDto) {
        User user = this.userService.getById(passwordDto.getUserId());
        if (null != user) {
            if (user.getPassword().equals(ShiroEncrypt.encrypt(passwordDto.getOldPassword()))) {
                user.setPassword(ShiroEncrypt.encrypt(passwordDto.getNewPassword()));
                this.userService.updateById(user);
            } else {
                return ApiResponse.fail(-1, "原密码不正确");
            }
        }
        return ApiResponse.ok(user);
    }

    @GetMapping("/rest-password/{id}")
    @ApiOperation("重置密码")
    public ApiResponse<String> resetPassword(@PathVariable("id") Long id) {
        this.userService.resetPassword(id);
        return ApiResponse.ok(null);
    }

    @PostMapping("/wx/login")
    @MessageInfo(name="企业微信登录",title = "登录",content = "企业微信登录")
    @ApiOperation("企业微信获取用户名登录")
    public ApiResponse<User> wxLogin(@RequestParam String code,@RequestParam String agentid) throws UnsupportedEncodingException{
        Map<String, Object> result = new HashMap<>();
        UserVo userVo = userService.wxLogin(code,agentid);
        result.put("userInfo", userVo);
        result.put("token", JWTUtils.sign(userVo.getUserInfo().getUsername(), userVo.getUserInfo().getPassword()));
        return ApiResponse.ok(result);
    }

    @PostMapping("/export-excel")
    @ApiOperation("企业微信用户名同步")
    public ApiResponse<User> test(@RequestParam("file") MultipartFile file) throws UnsupportedEncodingException{
        try {
            // 读取 Excel
            XSSFWorkbook book = new XSSFWorkbook(file.getInputStream());
            XSSFSheet sheet = book.getSheetAt(0);
            //获得总行数
            int rowNum = sheet.getPhysicalNumberOfRows();
            // 读取单元格数据
            for (int rowIndex = 1; rowIndex < rowNum; rowIndex++) {
                String teacherName = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(0));
                String teacherWxName = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(1));
                String nickName = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(2));
                QueryWrapper<Staff> studentQueryWrapper1 = new QueryWrapper<Staff>()
                        .eq("name", teacherName)
                        .eq("employee_number",nickName);
                Staff staff = staffService.getOne(studentQueryWrapper1);
                if(staff != null){
                    staffService.update(Wrappers.<Staff>lambdaUpdate()
                            .set(Staff::getWxUsername,teacherWxName)
                            .eq(Staff::getId,staff.getId())
                    );
                }
            }
        } catch (IOException e) {
            throw new ApiCode.ApiException(-1, "导入数据失败！");
        }
        return ApiResponse.ok(true);
    }

    @GetMapping("/send/verify-Code")
    @ApiOperation("发送验证码")
    public ApiResponse sendVerifyCode(@RequestParam String username){
        this.userService.sendVerifyCode(username);
        return ApiResponse.ok("消息已发送");
    }

    @PostMapping("/alloc-student-duty/{id}")
    @ApiOperation("分配值班班长")
    public ApiResponse<Integer> allocStudentDuty(@PathVariable("id") Long id,
                                                 @RequestParam(name = "type") String type) {
        return ApiResponse.ok(this.userService.allocStudentDuty(id, type));
    }
}
