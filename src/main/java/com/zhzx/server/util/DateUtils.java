/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.util;

import com.zhzx.server.rest.res.ApiCode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtils {

    public static String format(Date date) {
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date parse(String date) {
        try {
            return parse(date, "yyyy-MM-dd HH:mm:ss");
        }catch (ParseException e){
            e.getMessage();
        }
        throw new ApiCode.ApiException(-5,"时间格式化失败");
    }

    public static String format(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Date parse(String date, String format) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(date);
    }

    public static Date parse(String time,Date date){
        String year = format(date,"yyyy-MM-dd");
        return parse(year+" "+ time+":00");
    }

    public static String format(String time,Date date){
        String year = format(date,"yyyy-MM-dd");
        return year + " " + time;
    }

    public static String region(Date timeFrom, Date timeTo){
        String str1 = format(timeFrom,"yyyy-MM-dd HH:mm");
        String str2 = format(timeTo,"HH:mm");
        return str1.concat(" ").concat(str2);
    }

    public static String getWeek(Date time){
        String str1 = format(time,"E");
        return str1.replace("周", "星期");
    }
}
