/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：权限表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.config.TokenCacheConfig;
import com.zhzx.server.domain.*;
import com.zhzx.server.dto.wx.TokenDto;
import com.zhzx.server.dto.wx.WxParent;
import com.zhzx.server.dto.wx.WxStudent;
import com.zhzx.server.enums.MessageParentEnum;
import com.zhzx.server.enums.RelationEnum;
import com.zhzx.server.repository.*;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.WxSendMessageService;
import com.zhzx.server.util.JsonUtils;
import com.zhzx.server.util.wxutil.AesException;
import com.zhzx.server.util.wxutil.StringToDocument;
import com.zhzx.server.util.wxutil.WXBizMsgCrypt;
import com.zhzx.server.vo.StudentInfoVo;
import com.zhzx.server.vo.StudentParamVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WxSendMessageServiceImpl implements WxSendMessageService {
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private SettingsMapper settingsMapper;
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private StudentParentMapper studentParentMapper;
    @Value("${wx.message_appId}")
    private String messageAppId;
    @Value("${wx.mail_list_secret}")
    private String mailListSecret;
    @Value("${wx.message_secret}")
    private String messageSecret;
    @Value("${wx.agentId}")
    private String agentId;
    @Value("${wx.resp.token}")
    private String respToken;
    @Value("${wx.resp.encodingAESKey}")
    private String respEncodingAESKey;

    @Override
    public Boolean sendMessage(String content,List<String> userList){
        String token = this.getWxToken(messageSecret,messageAppId);
        Map<String, Object> param = new HashMap<>();
        param.put("to_parent_userid",userList);
        param.put("msgtype","text");
        param.put("agentid",agentId);
        Map<String,Object> map = new HashMap<>();
        map.put("content",content);
        param.put("text",map);
        String sendMessageUrl = String.format("https://qyapi.weixin.qq.com/cgi-bin/externalcontact/message/send?access_token=%s",token);
        JSONObject sendJson = restTemplate.postForEntity(sendMessageUrl, param,JSONObject.class).getBody();
        log.info("发送家长微信返回："+userList.stream().collect(Collectors.joining("|"))+"--"+sendJson.toJSONString());
        if(!sendJson.get("errcode").toString().equals("0")){
            throw new ApiCode.ApiException(-5,sendJson.get("errmsg").toString());
        }
        return true;
    }

    @Override
    public Boolean sendTeacherMessage(String content,List<String> teacherList){
        String token = this.getWxToken(messageSecret,messageAppId);
        Map<String, Object> param = new HashMap<>();
        param.put("touser",teacherList.stream().collect(Collectors.joining("|")));
        param.put("msgtype","text");
        param.put("agentid",agentId);
        Map<String,Object> map = new HashMap<>();
        map.put("content",content);
        param.put("text",map);
        String sendMessageUrl = String.format("https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s",token);
        JSONObject sendJson = restTemplate.postForEntity(sendMessageUrl, param,JSONObject.class).getBody();
        log.info("发送值班微信返回："+teacherList.stream().collect(Collectors.joining("|"))+"--"+sendJson.toJSONString());
        if(!sendJson.get("errcode").toString().equals("0")){

            throw new ApiCode.ApiException(-5,sendJson.get("errmsg").toString());
        }
        return true;
    }

    /***
     * 发送图文消息(类似公众号文章推送)
     * @Author: yu
     * @Date: 2022/11/4 11:40
     */
    @Override
    @Async( "threadPoolExecutor" )
    public void sendTeacherMessageNews(List<Map<String, Object>> articles, List<String> teacherList){
        String token = this.getWxToken(messageSecret,messageAppId);
        Map<String, Object> param = new HashMap<>();
        String userinf = String.join("|", teacherList);
        param.put("touser",userinf);
        param.put("msgtype","news");
        param.put("agentid",agentId);

        Map<String, Object> map1 = new HashMap<>();
        map1.put("articles", articles);

        param.put("news",map1);
        String sendMessageUrl = String.format("https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s",token);
        JSONObject sendJson = restTemplate.postForEntity(sendMessageUrl, param,JSONObject.class).getBody();
        log.info("发送值班微信返回："+userinf+"--"+sendJson.toJSONString());
        if(!sendJson.get("errcode").toString().equals("0")){
            throw new ApiCode.ApiException(-5,sendJson.get("errmsg").toString());
        }
    }

    @Override
    public String getWxToken(String secret,String appId){
        if(!isExpire(secret)){
            TokenDto tokenDto = TokenCacheConfig.getKey(secret);
            return tokenDto.getToken();
        }else{
            String tokenUrl = String.format("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s",appId,secret);
            JSONObject tokenJson = restTemplate.getForEntity(tokenUrl, JSONObject.class).getBody();
            if(!tokenJson.get("errcode").toString().equals("0")){
                throw new ApiCode.ApiException(-5,tokenJson.get("errmsg").toString());
            }
            String token = tokenJson.get("access_token").toString();
            Long expireTime = Long.valueOf(tokenJson.get("expires_in").toString());
            TokenDto tokenDto = new TokenDto();
            tokenDto.setCurrentTime(System.currentTimeMillis());
            tokenDto.setExpireTime(expireTime - 1800);
            tokenDto.setToken(token);
            TokenCacheConfig.setKey(appId,tokenDto);
            return token;
        }
    }

    @Override
    public Boolean isExpire(String key){
        TokenDto tokenDto = TokenCacheConfig.getKey(key);
        if(tokenDto != null){
            long oldTime = tokenDto.getCurrentTime();
            long newTime = System.currentTimeMillis();
            long cnt = (newTime - oldTime)/1000;
            if(cnt <= tokenDto.getExpireTime()){
                return false;
            }else{
                return true;
            }
        }else{
            return true;
        }
    }

    @Override
    public String createWxDepartmentId() {
        Settings settings = settingsMapper.selectOne(Wrappers.<Settings>lambdaQuery()
                .eq(Settings::getCode,"zhzx_wx_departmment")
        );
        if(settings == null){
            String token = this.getWxToken(mailListSecret,messageAppId);
            String departmentUrl = String.format("https://qyapi.weixin.qq.com/cgi-bin/school/department/create?access_token=%s",token);
            Map<String, Object> param = new HashMap<>();
            param.put("name","家长管理");
            param.put("parentid","1");
            param.put("id","888");
            param.put("type",2);
            param.put("standard_grade",0);
            Map<String,Object> departmentAdmin = new HashMap<>();
            departmentAdmin.put("type","2");
            departmentAdmin.put("userid","MaWenLin");
            param.put("department_admins",departmentAdmin);
            param.put("parentid","888");
            param.put("id","8888");
            param.put("type",1);
            departmentAdmin.put("type","3");
            param.put("department_admins",departmentAdmin);
            JSONObject department = restTemplate.postForEntity(departmentUrl, param,JSONObject.class).getBody();
            settings = new Settings();
            settings.setCode("zhzx_wx_departmment");
            Map<String, Object> json = new HashMap<>();
            json.put("departmentId",department.getString("id"));
            settings.setParams(JsonUtils.toJson(json));
            settings.setRemark("微信家长部门");
            settings.setDefault().validate(true);
            settingsMapper.insert(settings);
        }
        JSONObject jsonObject = JSON.parseObject(settings.getParams());
        return jsonObject.getString("departmentId");
    }

    @Override
    public void syncStudentParent(Long clazzId) {
        //获取微信所有用户
        String token = this.getWxToken(mailListSecret,messageAppId);
        String student = String.format("https://qyapi.weixin.qq.com/cgi-bin/school/user/list?access_token=%s&department_id=%s&fetch_child=1",token,1);
        JSONObject studentJson = restTemplate.getForEntity(student, JSONObject.class).getBody();
        if(!studentJson.get("errcode").toString().equals("0")){
            throw new ApiCode.ApiException(-5,studentJson.get("errmsg").toString());
        }
        JSONArray jsonArray = studentJson.getJSONArray("students");
        if(jsonArray != null && jsonArray.size() > 0){
            List<WxStudent> wxStudentList = jsonArray.toJavaList(WxStudent.class);
            Map<String,List<WxStudent>> mapList = wxStudentList.stream().collect(Collectors.groupingBy(WxStudent::getName));
            IPage<StudentInfoVo> page = new Page(1,10000);
            StudentParamVo param = new StudentParamVo();
            param.setClazzId(clazzId);
            IPage<StudentInfoVo> studentInfoVoIPage = studentMapper.selectInfoByPage(page,param);

            //本地用户新增
            for(StudentInfoVo studentInfoVo : studentInfoVoIPage.getRecords()){
                List<WxStudent> wxStudents = mapList.get(studentInfoVo.getName());
                if(wxStudents != null && wxStudents.size() == 1){
                    if(CollectionUtils.isNotEmpty(wxStudents.get(0).getParents())){
                        //循环判断是否已有家长手机号，有就替换，没有就新增
                        for(WxParent wxParent : wxStudents.get(0).getParents()){
                            try {
                                Boolean flag = false ;
                                if(CollectionUtils.isNotEmpty(studentInfoVo.getStudentParentList())){
                                    for (StudentParent studentParent : studentInfoVo.getStudentParentList()){
                                        if(Objects.equals(studentParent.getPhone(),wxParent.getMobile())){
                                            studentParent.setWxParentId(wxParent.getParent_userid());
                                            studentParentMapper.updateById(studentParent);
                                            flag = true;
                                            break;
                                        }
                                    }
                                }
                                //没有家长新增
                                if(!flag){
                                    StudentParent studentParent = new StudentParent();
                                    studentParent.setWxParentId(wxParent.getParent_userid());
                                    studentParent.setStudentId(studentInfoVo.getId());
                                    studentParent.setRelation(RelationEnum.O);
                                    studentParent.setName(studentInfoVo.getName()+wxParent.getRelation());
                                    studentParent.setType(MessageParentEnum.CREATED);
                                    studentParent.setPhone(wxParent.getMobile());
                                    studentParent.setDefault().validate(true);
                                    studentParentMapper.insert(studentParent);
                                }
                            }catch (Exception e){
                                log.error(e.getMessage());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public String createWxParent(List<StudentParent> studentParents) {
        String departmentId = createWxDepartmentId();
        StringBuilder message = new StringBuilder();
        for(StudentParent studentParent : studentParents){
            try{
                String token = this.getWxToken(mailListSecret,messageAppId);
                String parentUrl = String.format("https://qyapi.weixin.qq.com/cgi-bin/school/user/get?access_token=%s&userid=%s",token,"parent_"+studentParent.getId());
                JSONObject parentJson = restTemplate.getForEntity(parentUrl, JSONObject.class).getBody();
                if(!parentJson.containsKey("parent")){
                    String studentUrl = String.format("https://qyapi.weixin.qq.com/cgi-bin/school/user/get?access_token=%s&userid=%s",token,"student_"+studentParent.getStudentId());
                    JSONObject studentJson = restTemplate.getForEntity(studentUrl, JSONObject.class).getBody();
                    if(!studentJson.containsKey("student")){
                        String createStudentUrl = String.format("https://qyapi.weixin.qq.com/cgi-bin/school/user/create_student?access_token=%s",token);
                        Student student = studentMapper.selectById(studentParent.getStudentId());
                        Map<String,Object> studentMap = new HashMap<>();
                        studentMap.put("student_userid","student_"+studentParent.getStudentId());
                        studentMap.put("name",student.getName());
                        studentMap.put("department",new ArrayList<String>(){{this.add(departmentId);}});
                        JSONObject createStudentJson = restTemplate.postForEntity(createStudentUrl,studentMap ,JSONObject.class).getBody();
                        if(!createStudentJson.get("errcode").toString().equals("0")){
                            message.append(studentParent.getId()+",");
                            throw new ApiCode.ApiException(-5,createStudentJson.get("errmsg").toString());
                        }
                    }
                    String createParentUrl = String.format("https://qyapi.weixin.qq.com/cgi-bin/school/user/create_parent?access_token=%s",token);
                    Map<String,Object> parentMap = new HashMap<>();
                    parentMap.put("parent_userid","parent_"+studentParent.getId());
                    parentMap.put("mobile",studentParent.getPhone());
                    Map<String,Object> studentMap = new HashMap<>();
                    studentMap.put("student_userid","student_"+studentParent.getStudentId());
                    studentMap.put("relation",studentParent.getRelation().getName());
                    parentMap.put("children",new ArrayList<Map<String,Object> >(){{this.add(studentMap);}});
                    JSONObject createParentJson = restTemplate.postForEntity(createParentUrl, parentMap,JSONObject.class).getBody();
                    if(!createParentJson.get("errcode").toString().equals("0")){
                        message.append(studentParent.getId()+",");
                        throw new ApiCode.ApiException(-5,createParentJson.get("errmsg").toString());
                    }
                    studentParent.setType(MessageParentEnum.CREATED);
                    studentParentMapper.updateById(studentParent);
                }
            }catch (Exception e){
                log.error(e.getMessage());
            }
        }

        return message.toString();
    }

    @Override
    public void removeWxParent(List<StudentParent> studentParents) {
        String token = this.getWxToken(mailListSecret,messageAppId);
        Map<String,Object> param = new HashMap<>();
        param.put("useridlist",studentParents.stream().map(i->"parent_"+i.getId()).collect(Collectors.toList()));
        String deleteParent = String.format("https://qyapi.weixin.qq.com/cgi-bin/school/user/batch_delete_parent?access_token=%s",token);
        JSONObject deleteJson = restTemplate.postForEntity(deleteParent, param ,JSONObject.class).getBody();
        if(!deleteJson.get("errcode").toString().equals("0")){
            throw new ApiCode.ApiException(-5,deleteJson.get("errmsg").toString());
        }
    }


    @Override
    public List<WxStudent> syncStudentParent(Student student) {
        //获取微信所有用户
        String token = this.getWxToken(mailListSecret,messageAppId);
        String studentUrl = String.format("https://qyapi.weixin.qq.com/cgi-bin/school/user/list?access_token=%s&department_id=%s&fetch_child=1",token,1);
        JSONObject studentJson = restTemplate.getForEntity(studentUrl, JSONObject.class).getBody();
        if(!studentJson.get("errcode").toString().equals("0")){
            throw new ApiCode.ApiException(-5,studentJson.get("errmsg").toString());
        }
        JSONArray jsonArray = studentJson.getJSONArray("students");
        if(jsonArray != null && jsonArray.size() > 0){
            List<WxStudent> wxStudentList = jsonArray.toJavaList(WxStudent.class);
            Map<String,List<WxStudent>> mapList = wxStudentList.stream().collect(Collectors.groupingBy(WxStudent::getName));
            List<WxStudent> wxStudents = mapList.get(student.getName());
            if(CollectionUtils.isNotEmpty(wxStudents)){
                for (int i = 0; i < wxStudents.size(); i++) {
                    Long departmenId = wxStudents.get(i).getDepartment().get(0);
                    String departmentUrl = String.format("https://qyapi.weixin.qq.com/cgi-bin/school/department/list?access_token=%s&id=%s",token,departmenId);
                    JSONObject departmentClazz = restTemplate.getForEntity(departmentUrl, JSONObject.class).getBody();
                    String clazzName = departmentClazz.getJSONArray("departments").getJSONObject(0).getString("name");
                    String wxClazzId = departmentClazz.getJSONArray("departments").getJSONObject(0).getString("parentid");
                    String departmentGradeUrl = String.format("https://qyapi.weixin.qq.com/cgi-bin/school/department/list?access_token=%s&id=%s",token,wxClazzId);
                    JSONObject departmentGrade = restTemplate.getForEntity(departmentGradeUrl, JSONObject.class).getBody();
                    String gradeName = departmentGrade.getJSONArray("departments").getJSONObject(0).getString("name");
                    wxStudents.get(i).setClazzName(clazzName);
                    wxStudents.get(i).setGradeName(gradeName);
                }
                return wxStudents;
            }
        }
        return null;
    }

    @Override
    public void applicationMessageGet(HttpServletResponse response, HttpServletRequest request) throws IOException{
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        // 微信加密签名
        String msg_signature = request.getParameter("msg_signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");

        PrintWriter out = response.getWriter();
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        String result = null;
        try {
            WXBizMsgCrypt crypt = new WXBizMsgCrypt(respToken, respEncodingAESKey, messageAppId);
            result = crypt.VerifyURL(msg_signature, timestamp, nonce, echostr);
            if (result == null) {
                result = respToken;
            }
            out.write(result);
        } catch (AesException e) {
            e.printStackTrace();
        }finally {
            out.close();
            out = null;
        }
    }

    @Resource
    private MessageReplyMapper messageReplyMapper;
    @Resource
    private StaffMapper staffMapper;


    @Override
    public void applicationMessagePost(HttpServletResponse response, HttpServletRequest request) throws IOException{
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        // 微信加密签名
        String msg_signature = request.getParameter("msg_signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");

        String fromUserName = null;
        String createTime = null;
        String msgType = null;
        String content = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(request.getInputStream());
            Element root = document.getDocumentElement();
            String encrypt = root.getElementsByTagName("Encrypt").item(0).getTextContent();//用户发送的内容
            WXBizMsgCrypt crypt = new WXBizMsgCrypt(respToken, respEncodingAESKey, messageAppId);
            String result = crypt.VerifyURL(msg_signature, timestamp, nonce, encrypt);
            Document parse = StringToDocument.stringToDoc(result);
            fromUserName = parse.getElementsByTagName("FromUserName").item(0).getTextContent();
            createTime = parse.getElementsByTagName("CreateTime").item(0).getTextContent();
            msgType = parse.getElementsByTagName("MsgType").item(0).getTextContent();
            content = parse.getElementsByTagName("Content").item(0).getTextContent();
            log.info(msg_signature+"--"+timestamp+"--"+nonce+"--"+result);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        Staff staff = staffMapper.selectOne(Wrappers.<Staff>lambdaQuery()
                .eq(Staff::getWxUsername,fromUserName).or().eq(Staff::getEmployeeNumber,fromUserName)
        );

        MessageReply messageReply = new MessageReply();
        if(staff != null){
            messageReply.setReceiverId(staff.getId());
            messageReply.setReceiverName(staff.getName());
        }
        messageReply.setReceiverWxName(fromUserName);
        messageReply.setReplyContent(content);
        messageReply.setReplyTime(new Date(Long.valueOf(createTime+"000")));
        messageReply.setReplyType(msgType);
        messageReply.setDefault().validate(true);
        messageReplyMapper.insert(messageReply);
    }
}
