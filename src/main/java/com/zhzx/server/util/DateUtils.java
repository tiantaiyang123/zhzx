/**
 * 项目：中华中学流程自动化管理平台
 *
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
 */

package com.zhzx.server.util;

import com.zhzx.server.rest.res.ApiCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public final class DateUtils {

    public static String format(Date date) {
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date parse(String date) {
        try {
            return parse(date, "yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
            e.getMessage();
        }
        throw new ApiCode.ApiException(-5, "时间格式化失败");
    }

    public static String format(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Date parse(String date, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(date);
    }

    public static Date parse(String time, Date date) {
        String year = format(date, "yyyy-MM-dd");
        return parse(year + " " + time + ":00");
    }

    public static String format(String time, Date date) {
        String year = format(date, "yyyy-MM-dd");
        return year + " " + time;
    }

    public static String region(Date timeFrom, Date timeTo) {
        String str1 = format(timeFrom, "yyyy-MM-dd HH:mm");
        String str2 = format(timeTo, "HH:mm");
        return str1.concat(" ").concat(str2);
    }

    public static String getWeek(Date time) {
        String str1 = format(time, "E");
        return str1.replace("周", "星期");
    }

    public static List<Date> getMonthDays(Date timeFrom, Date timeTo) {

        //返回时间集合为空
        if (null == timeFrom || null == timeTo || timeFrom.after(timeTo)) return null;

        List<Date> dateList = new ArrayList<>();
        //获取当前时间
        Calendar calendar = Calendar.getInstance();
        //开始时间设置为当前时间
        calendar.setTime(timeFrom);

        timeTo = parse("23:59", timeTo);

        Date res;
        while (!(res = calendar.getTime()).after(timeTo)) {
            dateList.add(res);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return dateList;
    }

    public static String dayOfWeekInChinese(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        String dayOfWeekInChinese = null;
        switch (dayOfWeek) {
            case MONDAY:
                dayOfWeekInChinese = "星期一";
                break;
            case TUESDAY:
                dayOfWeekInChinese = "星期二";
                break;
            case WEDNESDAY:
                dayOfWeekInChinese = "星期三";
                break;
            case THURSDAY:
                dayOfWeekInChinese = "星期四";
                break;
            case FRIDAY:
                dayOfWeekInChinese = "星期五";
                break;
            case SATURDAY:
                dayOfWeekInChinese = "星期六";
                break;
            case SUNDAY:
                dayOfWeekInChinese = "星期日";
                break;
            default:
                dayOfWeekInChinese = "未知";
                break; // 这实际上不会执行，因为DayOfWeek的枚举值是完整的
        }
        return dayOfWeekInChinese;
    }



}
