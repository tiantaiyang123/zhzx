package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zhzx.server.domain.Student;
import com.zhzx.server.domain.User;
import com.zhzx.server.dto.card.ConsumeRecordDto;
import com.zhzx.server.msdomain.AccountInfo;
import com.zhzx.server.msrepository.AccountInfoMapper;
import com.zhzx.server.repository.StudentMapper;
import com.zhzx.server.repository.UserMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.CardService;
import com.zhzx.server.service.UserService;
import com.zhzx.server.util.HttpUtils;
import com.zhzx.server.util.StringUtils;
import com.zhzx.server.vo.ConsumeRecordVo;
import com.zhzx.server.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CardServiceImpl implements CardService {

    @Value("${card.consume-record}")
    private String consumeRecord;

    private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    @Autowired(required = false)
    public AccountInfoMapper accountInfoMapper;

    @Resource
    public UserService userService;

    @Resource
    public StudentMapper studentMapper;

    @Resource
    public UserMapper userMapper;

    @Override
    public Map<String, Object> cardLogin(String username, String password) {
        if (StringUtils.isNullOrEmpty(username) && StringUtils.isNullOrEmpty(password)) {
            throw new ApiCode.ApiException(-3, "参数为空！");
        }
        UserVo userVo;
        Map<String, Object> map = new HashMap<>();
        if (username.toUpperCase().equals(username) && username.toLowerCase().equals(username)) {
            // 卡号都能登
            AccountInfo accountInfo = this.getInfo(Long.valueOf(username), null, null);
            if (accountInfo != null) {
                if ("2".equals(accountInfo.getFlag())) {
                    throw new ApiCode.ApiException(-1, "无效卡");
                }
                User user;
                if ("学生".equals(accountInfo.getType())) {
                    Student curr = this.studentMapper.selectOne(Wrappers.<Student>lambdaQuery().select(Student::getId).eq(Student::getIdNumber, accountInfo.getIdNumber()));
                    if (curr == null) {
                        throw new ApiCode.ApiException(-2, "无法根据卡号寻找学生！");
                    }
                    user = this.userMapper.selectOne(Wrappers.<User>lambdaQuery().select(User::getUsername, User::getPassword, User::getLoginNumber)
                            .eq(User::getStudentId, curr.getId()));
                } else {
                    user = this.userMapper.selectOne(Wrappers.<User>lambdaQuery().select(User::getUsername, User::getPassword, User::getLoginNumber).eq(User::getUsername, accountInfo.getMobile()));
                }
                if (user == null) {
                    throw new ApiCode.ApiException(-2, "无法获取对应用户");
                }
                userVo = userService.login(user.getUsername(), password);
                map.put("accountInfo", accountInfo);
                map.put("userVo", userVo);
                return map;
            }
        }
        //  username login_number 均能登录
        userVo = userService.login(username, password);
        AccountInfo accountInfo1;
        if (userVo.getUserInfo().getStudentId() != 0) {
            accountInfo1 = this.getInfo(null, null, userVo.getUserInfo().getLoginNumber());
        } else {
            accountInfo1 = this.getInfo(null, userVo.getUserInfo().getUsername(), null);
        }
        if (accountInfo1 == null) {
            throw new ApiCode.ApiException(-4, "非内部用户！");
        }
        map.put("accountInfo", accountInfo1);
        map.put("userVo", userVo);
        return map;
    }

    @Override
    public AccountInfo getInfo(Long cPhysicalNo, String mobile, String idNumber) {
        if (cPhysicalNo == null && StringUtils.isNullOrEmpty(mobile) && StringUtils.isNullOrEmpty(idNumber)) {
            return null;
        }
        Map<String, Object> map = this.accountInfoMapper.getInfo(cPhysicalNo, mobile, idNumber);
        if (map == null || map.isEmpty()) return null;
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setCPhysicalNo(map.get("C_PhysicalNo").toString());
        accountInfo.setMobile(map.get("Mobile").toString());
        accountInfo.setIdNumber(map.get("PIN").toString());
        accountInfo.setId(Long.valueOf(map.get("帐号").toString()));
        accountInfo.setOrderNumber(map.get("编号").toString());
        accountInfo.setName(map.get("姓名").toString());
        accountInfo.setType(map.get("身份").toString());
        accountInfo.setSex(map.get("性别").toString());
        accountInfo.setFlag(map.get("标志").toString());
        accountInfo.setYch(map.get("预撤户").toString());
        return accountInfo;
    }

    @Override
    public IPage<ConsumeRecordDto> selectConsumePage(ConsumeRecordVo consumeRecordVo, Integer pageNum, Integer pageSize){
        // 内部api必传开始结束时间 默认前推一个月
//        if (consumeRecordVo.getA_Accounts() == null) {
//            User user = (User) SecurityUtils.getSubject().getPrincipal();
//            AccountInfo info;
//            if (user.getStaff() != null) {
//                info = this.getInfo(null, user.getStaff().getPhone(), null);
//            } else {
//                info = this.getInfo(null, null, user.getStudent().getIdNumber());
//            }
//            if (info == null) {
//                throw new ApiCode.ApiException(-5, "无法获取默认卡号！");
//            }
//            consumeRecordVo.setA_Accounts(info.getId());
//        }
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (StringUtils.isNullOrEmpty(consumeRecordVo.getBeginDate())) {
            consumeRecordVo.setBeginDate(now.minusMonths(1).format(formatter));
        }
        if (StringUtils.isNullOrEmpty(consumeRecordVo.getEndDate())) {
            consumeRecordVo.setEndDate(now.format(formatter));
        }
        consumeRecordVo.setBeginDate(consumeRecordVo.getBeginDate().concat(" 00:00:00"));
        consumeRecordVo.setEndDate(consumeRecordVo.getEndDate().concat(" 23:59:59"));

        IPage<ConsumeRecordDto> iPage = new Page<>();
        List<ConsumeRecordDto> records = iPage.getRecords();
        Map<String, String> map;
        try {
            map = BeanUtils.describe(consumeRecordVo);
            map.put("page", pageNum.toString());
            map.put("pageSize", pageSize.toString());
        } catch (Exception e) {
            throw new ApiCode.ApiException(-1, "bean转换失败");
        }
        String str;
        try {
            str = HttpUtils.doGet(consumeRecord, map);
        } catch (Exception e1) {
            log.error(">>>>发送请求失败>>>>{}", e1.getMessage());
            throw new ApiCode.ApiException(-2, "发送请求失败：" + e1.getMessage());
        }
        if (str.startsWith("{") && str.endsWith("}")) {
            throw new ApiCode.ApiException(-4, "内部服务错误：" + str);
        }
        if (str.length() > 0) {
            records = gson.fromJson(str, new TypeToken<List<ConsumeRecordDto>>() {}.getType());
        }
        iPage.setRecords(records);
        iPage.setTotal(records.size());
        iPage.setSize(pageSize);
        iPage.setCurrent(pageNum);
        iPage.setPages(iPage.getPages());
        return iPage;
    }
}
