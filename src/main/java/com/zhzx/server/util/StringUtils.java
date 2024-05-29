/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.util;

import com.zhzx.server.enums.GradeEnum;
import com.zhzx.server.enums.StudentNightDutyTypeEnum;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class StringUtils {

    private static Pattern CamelPattern = Pattern.compile("[A-Z]");

    private static Pattern UnderscorePattern = Pattern.compile("_\\w");

    /**
     * 驼峰转下划线
     *
     * @param str
     * @return
     */
    public static String camelToUnderScore(String str) {
        Matcher matcher = CamelPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 下划线转驼峰
     *
     * @param str
     * @return
     */
    public static String underScoreToCamel(String str) {
        Matcher matcher = UnderscorePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String group = matcher.group(0);
            matcher.appendReplacement(sb, group.substring(0, 1).toUpperCase() + group.substring(1));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * UUID
     *
     * @return
     */
    public static String uuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    /**
     * 获取当前网络ip
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { //"***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    /**
     * 空字符串
     *
     * @param param
     * @return
     */
    public static Boolean isNullOrEmpty(String param) {
        return param == null || "".equals(param) || "".equals(param.trim());
    }

    public static String getRandomNum(Integer num) {
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < num; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static String getPassword(String idNumber) {
        int len;
        if (!isNullOrEmpty(idNumber) && (len = idNumber.length()) > 6) {
            return "zh" + idNumber.substring(len - 6).toLowerCase();
        }
        return "winner!";
    }

    public static String gradeName(Long gradeId){
        if (gradeId==1){
            return GradeEnum.ONE.getName();
        }else if (gradeId==2){
            return GradeEnum.TWO.getName();
        }else if (gradeId==3){
            return GradeEnum.THREE.getName();
        }
        return null;
    }

    public static String schoolyardName(Long schoolyardId){
        String str = "";
        if (schoolyardId==1){
             str="兴隆校区";
        }else if (schoolyardId==2){
             str="雨花校区";
        }
        return str;
    }

}
