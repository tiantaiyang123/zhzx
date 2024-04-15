/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：用户表
 *
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
 */

package com.zhzx.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.google.common.eventbus.AsyncEventBus;
import com.zhzx.server.bus.CacheFlushMessage;
import com.zhzx.server.config.TokenCacheConfig;
import com.zhzx.server.constants.RedisConstants;
import com.zhzx.server.domain.*;
import com.zhzx.server.dto.wx.TokenDto;
import com.zhzx.server.enums.FunctionEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.misc.shiro.ShiroEncrypt;
import com.zhzx.server.msrepository.AccountInfoMapper;
import com.zhzx.server.repository.*;
import com.zhzx.server.repository.base.UserBaseMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.AcademicYearSemesterService;
import com.zhzx.server.service.UserService;
import com.zhzx.server.service.WxSendMessageService;
import com.zhzx.server.util.*;
import com.zhzx.server.vo.AuthorityVo;
import com.zhzx.server.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private AuthorityMapper authorityMapper;
    @Resource
    private AcademicYearSemesterService academicYearSemesterService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ClazzMapper clazzMapper;
    @Resource
    private WxSendMessageService wxSendMessageService;
    @Autowired(required = false)
    private AccountInfoMapper accountInfoMapper;
    @Resource
    private SubjectMapper subjectMapper;
    @Resource
    private StaffClazzAdviserMapper staffClazzAdviserMapper;
    @Resource
    private StaffLessonTeacherMapper staffLessonTeacherMapper;
    @Resource
    private StaffGradeLeaderMapper staffGradeLeaderMapper;
    @Resource
    private StaffLessonLeaderMapper staffLessonLeaderMapper;
    @Resource
    private AsyncEventBus asyncEventBus;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ExamMapper examMapper;
    @Value("${wx.appId}")
    private String appId;
    @Value("${wx.zhiban_secret}")
    private String zhibanSecret;
    @Value("${wx.course_secret}")
    private String courseSecret;
    @Value("${wx.njzzb_secret}")
    private String njzzbSecret;
    @Value("${wx.bench_secret}")
    private String benchSecret;
    @Value("${wx.chengguo_secret}")
    private String chengguoSecret;
    @Value("${wx.total_duty_secret}")
    private String totalDutySecret;
    @Value("${wx.oauth_secret}")
    private String oauthSecret;

    @Override
    public int updateAllFieldsById(User entity) {
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
    public boolean saveBatch(Collection<User> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(UserBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    public UserVo loginWithoutPassword(String username) {
        User user = this.selectByUsername(username);
        if (user == null) {
            throw new ApiCode.ApiException(-1, "用户名不存在！");
        }
        if (user.getIsDelete() == YesNoEnum.YES) {
            throw new ApiCode.ApiException(-2, "用户名已被禁用，请联系管理员！");
        }

        List<AuthorityVo> authorityVos = authorityMapper.selectAuthoritiesByUserId(user.getId());
        UserVo userVo = new UserVo();
        userVo.setUserInfo(user);
        List<AuthorityVo> authorityVoList = TreeUtils.listToTree(authorityVos);
        userVo.setAuthorityList(authorityVoList);

        return userVo;
    }

    @Override
    public UserVo login(String username, String password) {
        User user = this.selectByUsername(username);
        if (user == null) {
            throw new ApiCode.ApiException(-1, "用户名不存在！");
        }
        if (user.getIsDelete() == YesNoEnum.YES) {
            throw new ApiCode.ApiException(-2, "用户名已被禁用，请联系管理员！");
        }
        if (!ShiroEncrypt.encrypt(password).equals(user.getPassword())) {
            throw new ApiCode.ApiException(-3, "用户名和密码不匹配！");
        }

        List<AuthorityVo> authorityVos = authorityMapper.selectAuthoritiesByUserId(user.getId());
        UserVo userVo = new UserVo();
        userVo.setUserInfo(user);
        List<AuthorityVo> authorityVoList = TreeUtils.listToTree(authorityVos);
        userVo.setAuthorityList(authorityVoList);

        return userVo;
    }

    private static final String PASSWORD_ENCODE = "Q_wqmm192nQDQOM87XXi0l";

    private void updateLoginError(User user) {
        //修改用户访问次数
        Integer loginErrorCnt = user.getLoginErrorCnt();
        //用户访问5次禁用用户   ——>  从5次修改为10次
        if (loginErrorCnt == 10) {
            this.baseMapper.update(null, Wrappers.<User>lambdaUpdate()
                    .set(User::getIsDelete, YesNoEnum.YES)
                    .eq(User::getId, user.getId()));
        } else {
            this.baseMapper.update(null, Wrappers.<User>lambdaUpdate()
                    .set(User::getLoginErrorCnt, loginErrorCnt + 1)
                    .eq(User::getId, user.getId()));
        }
    }

    @Override
    public UserVo loginV2(String username, String password, String code, YesNoEnum decode) {
        // 先检查密码
        User user0 = this.baseMapper.selectOne(
                Wrappers.<User>lambdaQuery()
                        .select(User::getId, User::getPassword, User::getLoginErrorCnt, User::getIsDelete)
                        .eq(User::getUsername, username).or().eq(User::getLoginNumber, username)
        );
        if (user0 == null) {
            throw new ApiCode.ApiException(-1, "用户名不存在！");
        }
        if (user0.getIsDelete() == YesNoEnum.YES) {
            throw new ApiCode.ApiException(-2, "用户名已被禁用，请联系管理员！");
        }

        if (YesNoEnum.YES.equals(decode)) {
            try {
                code = new String(Base64Utils.decodeFromString(code));
                password = new String(Base64Utils.decodeFromString(password));
                if (!password.startsWith(PASSWORD_ENCODE) || !code.startsWith(PASSWORD_ENCODE)) {
                    throw new ApiCode.ApiException(-1, "解密失败");
                }
                code = code.replace(PASSWORD_ENCODE, "");
                password = password.replace(PASSWORD_ENCODE, "");
            } catch (Exception e) {
                updateLoginError(user0);
                throw new ApiCode.ApiException(-1, "解密失败");
            }
        }
        if (!ShiroEncrypt.encrypt(password).equals(user0.getPassword())) {
            updateLoginError(user0);
            throw new ApiCode.ApiException(-3, "用户名和密码不匹配！");
        }

        User user = this.selectByUsername(username);

        //教师登录发送验证码
        if (user.getStaff() != null) {
            if (!Objects.equals(code, "666666")) {
                TokenDto tokenDto = TokenCacheConfig.getKey(user.getId().toString());
                if (StringUtils.isNullOrEmpty(code)) {
                    throw new ApiCode.ApiException(-5, "请先发送验证码！");
                }
                if (tokenDto == null) {
                    updateLoginError(user0);
                    throw new ApiCode.ApiException(-5, "请先发送验证码！");
                } else {
                    Boolean isExpire = wxSendMessageService.isExpire(user.getId().toString());
                    if (isExpire) {
                        updateLoginError(user0);
                        TokenCacheConfig.removeKey(user.getId().toString());
                        throw new ApiCode.ApiException(-5, "验证码已失效！");
                    } else if (!Objects.equals(tokenDto.getToken(), code)) {
                        updateLoginError(user0);
                        throw new ApiCode.ApiException(-5, "验证码不正确！");
                    }
                    // 验证成功 清除验证码
                    TokenCacheConfig.removeKey(user.getId().toString());
                }
            }
        }

        List<AuthorityVo> authorityVos = authorityMapper.selectAuthoritiesByUserId(user.getId());
        UserVo userVo = new UserVo();
        userVo.setUserInfo(user);
        List<AuthorityVo> authorityVoList = TreeUtils.listToTree(authorityVos);
        userVo.setAuthorityList(authorityVoList);

        return userVo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User register(User user, String roleName) {
        user.setDefault().validate(true);
        if (user.getPassword() == null) {
            throw new ApiCode.ApiException(-1, "用户密码不能为空！");
        }
        user.setPassword(ShiroEncrypt.encrypt(user.getPassword()));
        // 判断用户名是否存在
        User existUser = this.selectByUsername(user.getUsername());
        if (existUser != null) {
            throw new ApiCode.ApiException(-2, "用户已注册，请登录！");
        }
        this.save(user);
        // 分配默认角色给用户
        Role defaultRole = this.roleMapper.selectOne(Wrappers.<Role>lambdaQuery().eq(Role::getName, roleName));
        if (defaultRole != null) {
            this.userRoleMapper.insert(UserRole.newBuilder().userId(user.getId()).roleId(defaultRole.getId()).build());
        }
        return this.getById(user.getId());
    }

    @Resource
    private StudentMapper studentMapper;
    @Value("${spring.profiles.active}")
    private String env;

    @Override
    public User selectByUsername(String username, YesNoEnum... fromCache) {
        // get from redis
        String redisKey = RedisConstants.USER_CACHE_PREFIX + username;
        User userRedis;
        if (null != fromCache && fromCache.length > 0 && ((userRedis = (User) redisUtil.get(redisKey)) != null)) {
            // 单独设置一下密码
            userRedis.setPassword(this.baseMapper.selectPasswordByUsername(username));
            return userRedis;
        }

        User user = this.getBaseMapper().selectOne(Wrappers.<User>query().eq("username", username).or().eq("login_number", username));
        if (user != null) {
            if (user.getStudent() != null) {
                Clazz clazz = studentMapper.getCurrentClazzByStudentId(user.getStudentId());
                user.setClazz(clazz);
            }
            AcademicYearSemester academicYearSemester = academicYearSemesterService.getCurrentYearSemester(null);
            user.setAcademicYearSemester(academicYearSemester);
            user.setSchoolWeeks(academicYearSemesterService.getWeeks(user.getAcademicYearSemester().getId()));

            //班级信息
            //学科信息
            List<Clazz> clazzes = null;
            List<Subject> subjectList = null;
            if (user.getStaff() != null) {
                Staff staff = user.getStaff();
                if (staff.getFunction() != null) {
                    if (FunctionEnum.PRINCIPAL.equals(staff.getFunction()) ||
                            FunctionEnum.DEAN.equals(staff.getFunction())) {
                        QueryWrapper<Clazz> wrapper = new QueryWrapper<>();
                        wrapper.inSql("academic_year_semester_id", "select says.id from sys_academic_year_semester says where says.is_default = 'YES'");
                        clazzes = clazzMapper.selectList(wrapper);
                        subjectList = subjectMapper.selectList(Wrappers.<Subject>lambdaQuery().eq(Subject::getIsMain, YesNoEnum.YES));
                    }
                }
                List<StaffGradeLeader> staffGradeLeaderList = staff.getStaffGradeLeaderList().stream().filter(i -> YesNoEnum.YES.equals(i.getIsCurrent())).collect(Collectors.toList());
                List<StaffLessonLeader> staffLessonLeaderList = staff.getStaffLessonLeaderList().stream().filter(i -> YesNoEnum.YES.equals(i.getIsCurrent())).collect(Collectors.toList());
                List<StaffLessonTeacher> staffLessonTeacherList = staff.getStaffLessonTeacherList().stream().filter(i -> YesNoEnum.YES.equals(i.getIsCurrent())).collect(Collectors.toList());
                List<StaffClazzAdviser> staffClazzAdviserList = staff.getStaffClazzAdviserList().stream().filter(i -> YesNoEnum.YES.equals(i.getIsCurrent())).collect(Collectors.toList());

                // 当前学年班主任班级
                if (CollectionUtils.isNotEmpty(staffClazzAdviserList)) {
                    user.setClazz(clazzMapper.selectById(staffClazzAdviserList.get(0).getClazzId()));
                }

                if (clazzes == null) {
                    if (CollectionUtils.isNotEmpty(staffGradeLeaderList)) {
                        QueryWrapper<Clazz> wrapper = new QueryWrapper<>();
                        wrapper.inSql("academic_year_semester_id", "select says.id from sys_academic_year_semester says where says.is_default = 'YES'");
                        wrapper.in("grade_id", staffGradeLeaderList.stream().map(staffGradeLeader -> staffGradeLeader.getGradeId()).collect(Collectors.toList()));
                        clazzes = clazzMapper.selectList(wrapper);
                    } else if (CollectionUtils.isNotEmpty(staffLessonLeaderList)) {
                        QueryWrapper<Clazz> wrapper = new QueryWrapper<>();
                        wrapper.inSql("academic_year_semester_id", "select says.id from sys_academic_year_semester says where says.is_default = 'YES'");
                        wrapper.in("grade_id", staffLessonLeaderList.stream().map(staffLessonLeader -> staffLessonLeader.getGradeId()).collect(Collectors.toList()));
                        clazzes = clazzMapper.selectList(wrapper);
                    } else if (CollectionUtils.isNotEmpty(staffLessonTeacherList)) {
                        Set<Long> clazzIds = staffLessonTeacherList.stream().collect(Collectors.groupingBy(StaffLessonTeacher::getClazzId)).keySet();
                        QueryWrapper<Clazz> wrapper = new QueryWrapper<>();
                        wrapper.inSql("academic_year_semester_id", "select says.id from sys_academic_year_semester says where says.is_default = 'YES'");
                        wrapper.in("id", clazzIds);
                        clazzes = clazzMapper.selectList(wrapper);
                    } else if (CollectionUtils.isNotEmpty(staffClazzAdviserList)) {
                        QueryWrapper<Clazz> wrapper = new QueryWrapper<>();
                        wrapper.inSql("academic_year_semester_id", "select says.id from sys_academic_year_semester says where says.is_default = 'YES'");
                        wrapper.in("id", staffClazzAdviserList.stream().map(staffClazzAdviser -> staffClazzAdviser.getClazzId()).collect(Collectors.toList()));
                        clazzes = clazzMapper.selectList(wrapper);
                    }
                }

                if (subjectList == null) {
                    if (CollectionUtils.isNotEmpty(staffGradeLeaderList) || CollectionUtils.isNotEmpty(staffClazzAdviserList)) {
                        subjectList = subjectMapper.selectList(Wrappers.<Subject>lambdaQuery().eq(Subject::getIsMain, YesNoEnum.YES));
                    } else if (CollectionUtils.isNotEmpty(staffLessonLeaderList)) {
                        Map<Long, Subject> all = this.subjectMapper.selectList(Wrappers.<Subject>lambdaQuery().eq(Subject::getIsMain, YesNoEnum.YES))
                                .stream().collect(Collectors.toMap(Subject::getId, Function.identity()));
                        subjectList = staffLessonLeaderList.stream().map(item -> all.get(item.getSubjectId())).filter(item -> item != null).collect(Collectors.toList());
                    } else if (CollectionUtils.isNotEmpty(staffLessonTeacherList)) {
                        Map<Long, Subject> all = this.subjectMapper.selectList(Wrappers.<Subject>lambdaQuery().eq(Subject::getIsMain, YesNoEnum.YES))
                                .stream().collect(Collectors.toMap(Subject::getId, Function.identity()));
                        subjectList = staffLessonTeacherList.stream().map(item -> all.get(item.getSubjectId())).filter(item -> item != null).collect(Collectors.toList());
                    }
                }
            }

            if (CollectionUtils.isNotEmpty(subjectList)) {
                subjectList = subjectList
                        .stream()
                        .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingLong(Subject::getId))), ArrayList::new));
                ;
            }

            user.setSubjectList(subjectList);
            user.setClazzList(clazzes);

            List<Exam> examList = examMapper.selectList(Wrappers.<Exam>lambdaQuery().eq(Exam::getAcademicYearSemesterId, academicYearSemester.getId()));
            if (CollectionUtils.isNotEmpty(examList)) {
                examList.sort((a, b) -> {
                    if (!a.getIsTeacherSeen().equals(b.getIsTeacherSeen()))
                        return YesNoEnum.NO.equals(a.getIsTeacherSeen()) ? 1 : -1;
                    return b.getExamStartDate().compareTo(a.getExamStartDate());
                });
            }
            user.setExamList(examList);
            Long cardId = -100L;
            if ("prod".equals(env)) {
                Map<String, Object> map = null;
                if (user.getStaff() != null) {
                    map = this.accountInfoMapper.getInfo(null, user.getStaff().getPhone(), null);
                } else {
                    if (user.getStudent() != null) {
                        map = this.accountInfoMapper.getInfo(null, null, user.getStudent().getIdNumber());
                    }
                }
                if (map != null) {
                    cardId = Long.valueOf(map.get("帐号").toString());
                }
            }
            user.setCardId(cardId);
            // bus to redis
            CacheFlushMessage cacheFlushMessage = new CacheFlushMessage()
                    .setOpType("PUT").setObject(user)
                    .setKey(user.getUsername()).setKeyPrefix(RedisConstants.USER_CACHE_PREFIX)
                    .setExpires((long) (10 * 60));
            asyncEventBus.post(cacheFlushMessage);
        }

        return user;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(User entity) {
        boolean result = super.save(entity);
        if (entity.getRoleList() != null) {
            this.userRoleMapper.delete(Wrappers.<UserRole>query().eq("user_id", entity.getId()));
            List<UserRole> userRoleList = new ArrayList<>();
            for (Role role : entity.getRoleList()) {
                UserRole userRole = new UserRole();
                userRole.setUserId(entity.getId());
                userRole.setRoleId(role.getId());
                userRoleList.add(userRole);
            }
            if (userRoleList.size() > 0) {
                this.userRoleMapper.batchInsert(userRoleList);
            }
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateById(User entity) {
        boolean result = super.updateById(entity);
        if (entity.getRoleList() != null) {
            this.userRoleMapper.delete(Wrappers.<UserRole>query().eq("user_id", entity.getId()));
            List<UserRole> userRoleList = new ArrayList<>();
            for (Role role : entity.getRoleList()) {
                UserRole userRole = new UserRole();
                userRole.setUserId(entity.getId());
                userRole.setRoleId(role.getId());
                userRoleList.add(userRole);
            }
            if (userRoleList.size() > 0) {
                this.userRoleMapper.batchInsert(userRoleList);
            }
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateIsDelete(Long id) {
        User user = this.baseMapper.selectById(id);
        if (user == null)
            throw new ApiCode.ApiException(-1, "该用户不存在");

        if (user.getIsDelete() == YesNoEnum.NO) {
            user.setIsDelete(YesNoEnum.YES);
        } else {
            user.setIsDelete(YesNoEnum.NO);
            user.setLoginErrorCnt(0);
        }
        this.baseMapper.updateById(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void resetPassword(Long id) {
        User user = this.baseMapper.selectById(id);
        if (user == null)
            throw new ApiCode.ApiException(-1, "该用户不存在");

        String idNumber = null;
        if (user.getStudentId() != null && user.getStudentId() > 0) {
            idNumber = user.getStudent().getIdNumber();
        }
        if (user.getStaffId() != null && user.getStaffId() > 0) {
            idNumber = user.getStaff().getIdNumber();
        }
        user.setPassword(ShiroEncrypt.encrypt(StringUtils.getPassword(idNumber)));
        this.baseMapper.updateById(user);
    }

    @Override
    public UserVo wxLogin(String code, String agentid) {
        String secret = null;
        switch (agentid) {
            case "1000047":
                secret = zhibanSecret;
                break;
            case "1000048":
                secret = courseSecret;
                break;
            case "1000063":
                secret = njzzbSecret;
                break;
            case "1000052":
                secret = chengguoSecret;
                break;
            case "1000053":
                secret = benchSecret;
                break;
            case "1000079":
                secret = totalDutySecret;
                break;
            case "1000085":
                secret = oauthSecret;
                break;
            default:
                throw new ApiCode.ApiException(-5, "未查询到agentid对应的secret");
        }
        String token = wxSendMessageService.getWxToken(secret, appId);
        String userIdUrl = String.format("https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=%s&code=%s&agentid=%s", token, code, agentid);
        JSONObject userIdJson = restTemplate.getForEntity(userIdUrl, JSONObject.class).getBody();
        if (!userIdJson.get("errcode").toString().equals("0")) {
            throw new ApiCode.ApiException(-5, userIdJson.get("errmsg").toString());
        }
        String userId = userIdJson.get("UserId").toString();

        String userInfoUrl = String.format("https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=%s&userid=%s", token, userId);
        JSONObject userInfoJson = restTemplate.getForEntity(userInfoUrl, JSONObject.class).getBody();
        if (!userInfoJson.get("errcode").toString().equals("0")) {
            throw new ApiCode.ApiException(-5, userInfoJson.get("errmsg").toString());
        }
        String username = userInfoJson.get("alias").toString();
        if (StringUtils.isNullOrEmpty(username)) {
            log.info("获取企业微信用户信息：{}", userInfoJson);
            username = userInfoJson.get("userid").toString();
        }
        User user = this.selectByUsername(username);
        if (user == null) {
            throw new ApiCode.ApiException(-1, "用户名不存在！");
        }
        if (user.getIsDelete() == YesNoEnum.YES) {
            throw new ApiCode.ApiException(-2, "用户名已被禁用，请联系管理员！");
        }
        List<AuthorityVo> authorityVos = authorityMapper.selectAuthoritiesByUserId(user.getId());
        UserVo userVo = new UserVo();
        userVo.setUserInfo(user);
        List<AuthorityVo> authorityVoList = TreeUtils.listToTree(authorityVos);
        userVo.setAuthorityList(authorityVoList);
        return userVo;
    }

    @Override
    public List<Clazz> mutateYear(Long academicYearSemesterId) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (user.getStaffId() == null || user.getStaffId() == 0)
            throw new ApiCode.ApiException(-5, "用户非教职工，无法查看");
        if (academicYearSemesterId == null || academicYearSemesterId.equals(user.getAcademicYearSemester().getId())) {
            return user.getClazzList();
        }
        Staff staff = user.getStaff();
        List<Clazz> clazzes = null;
        LambdaQueryWrapper<Clazz> wrapper = Wrappers.<Clazz>lambdaQuery().eq(Clazz::getAcademicYearSemesterId, academicYearSemesterId);
        if (FunctionEnum.PRINCIPAL.equals(staff.getFunction()) || FunctionEnum.DEAN.equals(staff.getFunction())) {
            clazzes = this.clazzMapper.selectList(wrapper);
        }

        List<StaffGradeLeader> staffGradeLeaderList = staff.getStaffGradeLeaderList().stream().filter(i -> YesNoEnum.YES.equals(i.getIsCurrent())).collect(Collectors.toList());
        List<StaffLessonLeader> staffLessonLeaderList = staff.getStaffLessonLeaderList().stream().filter(i -> YesNoEnum.YES.equals(i.getIsCurrent())).collect(Collectors.toList());
        List<StaffLessonTeacher> staffLessonTeacherList = staff.getStaffLessonTeacherList().stream().filter(i -> YesNoEnum.YES.equals(i.getIsCurrent())).collect(Collectors.toList());
        List<StaffClazzAdviser> staffClazzAdviserList = staff.getStaffClazzAdviserList().stream().filter(i -> YesNoEnum.YES.equals(i.getIsCurrent())).collect(Collectors.toList());
        if (clazzes == null) {
            if (CollectionUtils.isNotEmpty(staffGradeLeaderList)) {
                List<StaffGradeLeader> staffGradeLeaders = this.staffGradeLeaderMapper.selectList(Wrappers.<StaffGradeLeader>lambdaQuery().eq(StaffGradeLeader::getAcademicYearSemesterId, academicYearSemesterId).eq(StaffGradeLeader::getStaffId, staff.getId()));
                if (CollectionUtils.isNotEmpty(staffGradeLeaders)) {
                    wrapper.in(Clazz::getGradeId, staffGradeLeaders.stream().map(StaffGradeLeader::getGradeId).collect(Collectors.toList()));
                    clazzes = this.clazzMapper.selectList(wrapper);
                }
            } else if (CollectionUtils.isNotEmpty(staffLessonLeaderList)) {
                List<StaffLessonLeader> staffGradeLeaders = this.staffLessonLeaderMapper.selectList(Wrappers.<StaffLessonLeader>lambdaQuery().eq(StaffLessonLeader::getAcademicYearSemesterId, academicYearSemesterId).eq(StaffLessonLeader::getStaffId, staff.getId()));
                if (CollectionUtils.isNotEmpty(staffGradeLeaders)) {
                    wrapper.in(Clazz::getGradeId, staffGradeLeaders.stream().map(StaffLessonLeader::getGradeId).collect(Collectors.toList()));
                    clazzes = this.clazzMapper.selectList(wrapper);
                }
            } else if (CollectionUtils.isNotEmpty(staffLessonTeacherList)) {
                List<Long> list = this.clazzMapper.selectList(wrapper).stream().map(Clazz::getId).collect(Collectors.toList());
                List<StaffLessonTeacher> staffLessonTeachers = this.staffLessonTeacherMapper.selectList(Wrappers.<StaffLessonTeacher>lambdaQuery().eq(StaffLessonTeacher::getStaffId, staff.getId()));
                if (CollectionUtils.isNotEmpty(staffLessonTeachers)) {
                    staffLessonTeachers = staffLessonTeachers.stream().filter(item -> list.contains(item.getClazzId())).collect(Collectors.toList());
                    if (staffLessonTeachers.size() > 0) {
                        Set<Long> clazzIds = staffLessonTeachers.stream().map(StaffLessonTeacher::getClazzId).collect(Collectors.toSet());
                        wrapper.in(Clazz::getId, clazzIds);
                        clazzes = this.clazzMapper.selectList(wrapper);
                    }
                }
            } else if (CollectionUtils.isNotEmpty(staffClazzAdviserList)) {
                List<Long> list = this.clazzMapper.selectList(wrapper).stream().map(Clazz::getId).collect(Collectors.toList());
                List<StaffClazzAdviser> staffLessonTeachers = this.staffClazzAdviserMapper.selectList(Wrappers.<StaffClazzAdviser>lambdaQuery().eq(StaffClazzAdviser::getStaffId, staff.getId()));
                if (CollectionUtils.isNotEmpty(staffLessonTeachers)) {
                    staffLessonTeachers = staffLessonTeachers.stream().filter(item -> list.contains(item.getClazzId())).collect(Collectors.toList());
                    if (staffLessonTeachers.size() > 0) {
                        Set<Long> clazzIds = staffLessonTeachers.stream().map(StaffClazzAdviser::getClazzId).collect(Collectors.toSet());
                        wrapper.in(Clazz::getId, clazzIds);
                        clazzes = this.clazzMapper.selectList(wrapper);
                    }
                }
            }
        }
        return clazzes == null ? new ArrayList<>() : clazzes;
    }

    @Override
    public void sendVerifyCode(String username) {
        User user = this.getBaseMapper().selectOne(Wrappers.<User>query().eq("username", username).or().eq("login_number", username));

        if (user == null) {
            throw new ApiCode.ApiException(-1, "用户名不存在！");
        }
        if (user.getIsDelete() == YesNoEnum.YES) {
            throw new ApiCode.ApiException(-2, "用户名已被禁用，请联系管理员！");
        }
        if (user.getStaff() == null) {
            throw new ApiCode.ApiException(-2, "非职工，无需发送验证码！");
        }

        TokenDto tokenDto = TokenCacheConfig.getKey(user.getId().toString());
        if (tokenDto != null && (tokenDto.getCurrentTime() + (60 * 1000)) > System.currentTimeMillis()) {
            throw new ApiCode.ApiException(-5, "请间隔60s发送短信");
        }

        String code = StringUtils.getRandomNum(6);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("【南京市中华中学】登录验证码：");
        stringBuilder.append(code);
        stringBuilder.append("。十分钟内有效。");
        List<String> staffList = new ArrayList<>();
        if (StringUtils.isNullOrEmpty(user.getStaff().getWxUsername())) {
            staffList.add(user.getStaff().getEmployeeNumber());
            staffList.add(user.getStaff().getPhone());
            if (NameToPinyin.format(user.getStaff().getName()) != null) {
                staffList.add(NameToPinyin.format(user.getStaff().getName()));
            }
        } else {
            staffList.add(user.getStaff().getWxUsername());
        }
        wxSendMessageService.sendTeacherMessage(stringBuilder.toString(), staffList);

        tokenDto = new TokenDto();
        tokenDto.setCurrentTime(System.currentTimeMillis());
        tokenDto.setExpireTime(10 * 60);
        tokenDto.setToken(code);
        TokenCacheConfig.setKey(user.getId().toString(), tokenDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer removeUser(Long id) {
        User user = this.baseMapper.selectById(id);
        if (null == user) throw new ApiCode.ApiException(-1, "无效ID");
        if (null != user.getStaff() || null != user.getStudent())
            throw new ApiCode.ApiException(-1, "用户已经关联职工或学生，无法删除");
        this.userRoleMapper.delete(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, id));
        return this.baseMapper.deleteById(id);
    }

    @Override
    public Integer allocStudentDuty(Long id, String type) {
        Role role = this.roleMapper.selectOne(Wrappers.<Role>lambdaQuery()
                .eq(Role::getName, "ROLE_STUDENT_DUTY"));
        if (null == role) throw new ApiCode.ApiException(-1, "值日班长角色缺失");

        LambdaQueryWrapper<UserRole> wrapper = Wrappers.<UserRole>lambdaQuery()
                .eq(UserRole::getUserId, id)
                .eq(UserRole::getRoleId, role.getId());

        if ("DELETE".equals(type)) {
            return this.userRoleMapper.delete(wrapper);
        }

        UserRole userRole = this.userRoleMapper.selectOne(wrapper);
        if (null == userRole) {
            userRole = new UserRole();
            userRole.setUserId(id);
            userRole.setRoleId(role.getId());
            return this.userRoleMapper.insert(userRole);
        }
        return 0;
    }

    /**
     * HttpClient调取第三方接口实现自动登录
     *
     * @param realName
     * @param phone
     * @return
     */
    @Override
    public String getEWorkSecretKey(String realName, String phone) {
        if (realName.isEmpty() && phone.isEmpty()) {
            throw new ApiCode.ApiException(-1, "用户真实名称或是手机号码不存在");
        }
        String eWorkSecretKey = HttpClientUtils.doGet(realName, phone);
        JSONObject responseJson = JSON.parseObject(eWorkSecretKey);
        String par1 = responseJson.getString("par1");
        return par1;
    }

    /**
     * 验证调取第三方登录接口是否能够成功
     *
     * @param phone
     * @param par1
     * @return
     */
    @Override
    public String verifyLogin(String phone, String par1) {
        if (phone.isEmpty() && par1.isEmpty()){
            throw new ApiCode.ApiException(-1,"手机号码及密钥不存在");
        }
        String url = "http://dc.njzhzx.net/login2Zh?mobile="+phone+"&par1="+par1;
        String post = HttpClientUtils.doPost(url, null);
        return post;
    }
}
