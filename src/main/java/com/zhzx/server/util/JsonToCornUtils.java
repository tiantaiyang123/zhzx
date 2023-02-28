package com.zhzx.server.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhzx.server.rest.res.ApiCode;
import org.quartz.CronExpression;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 11345 on 2022/4/13.
 */
public class JsonToCornUtils {

    /**
     * 转换corn
     *
     * @param cronJson
     * @return
     */
    public static String jsonToCron(String cronJson) {
        StringBuffer cronExp = new StringBuffer("");
        JSONObject jsonObject = JSON.parseObject(cronJson);
        if(!jsonObject.containsKey("frequency")){
            throw new ApiCode.ApiException(-5,"请选择类型！");
        }
        if(jsonObject.getIntValue("frequency") == 1 && jsonObject.containsKey("noticeDayTime")){
            String dateFormat = "ss mm HH dd MM ? yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            cronExp.append(sdf.format(jsonObject.getDate("noticeDayTime")));
        }else if(jsonObject.containsKey("noticeTime")){

            String[] arr = jsonObject.getString("noticeTime").split(":");
            String hour = arr[0].trim();
            String minute = arr[1].trim();
            String second = arr[2].trim();
            //秒
            cronExp.append(second).append(" ");
            //分
            cronExp.append(minute).append(" ");
            //小时
            cronExp.append(hour).append(" ");
            if(jsonObject.getIntValue("frequency") == 2){

                cronExp.append("* ");//日
                cronExp.append("* ");//月
                cronExp.append("?");//周

            }else if(jsonObject.getIntValue("frequency") == 3
                    && jsonObject.containsKey("week")){

                //一个月中第几天
                cronExp.append("? ");
                //月份
                cronExp.append("* ");
                //周
                JSONArray weeks = jsonObject.getJSONArray("week");
                for (int i = 0; i < weeks.size(); i++) {
                    if (i == 0) {
                        cronExp.append(weeks.get(i).toString().trim());
                    } else {
                        cronExp.append(",").append(weeks.get(i).toString().trim());
                    }
                }

            }else if(jsonObject.getIntValue("frequency") == 4
                    && jsonObject.containsKey("month")){
                //一个月中的哪几天
                JSONArray days = jsonObject.getJSONArray("month");
                for (int i = 0; i < days.size(); i++) {
                    if (i == 0) {
                        if(days.get(i).toString().equals("32")){
                            cronExp.append("L");
                            break;
                        }
                        cronExp.append(days.get(i).toString().trim());
                    } else {
                        cronExp.append(",").append(days.get(i).toString().trim());
                    }
                }
                //月份
                cronExp.append(" * ");
                //周
                cronExp.append("?");


            }else{
                throw new ApiCode.ApiException(-5,"数据格式错误！");
            }

        }else{
            throw new ApiCode.ApiException(-5,"数据格式错误！");
        }

        Boolean flag = true;

        try {
            CronExpression expression = new CronExpression(cronExp.toString());
        }catch (ParseException e){
            e.getMessage();
            flag = false;
        }
        if(!flag){
            throw new ApiCode.ApiException(-5,"转换出错了");
        }
        return cronExp.toString();
    }

}
