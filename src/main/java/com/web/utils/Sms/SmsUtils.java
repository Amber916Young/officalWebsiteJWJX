package com.web.utils.Sms;

import com.aliyuncs.exceptions.ClientException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class SmsUtils {


    /**
     * 产生验证码
     *
     * @param phone  手机号码
     * @param type 验证类型：0:注册验证；1：登录验证；2：操作验证......
     * @return 验证码
     */
    public synchronized static  String acquiReverification(String phone){
        String reverification="";
        String msg = "";
        try {
            reverification=(60000+(int)(Math.random()*10000))+"";
            try {
                SmsDemo.sendSms(phone, "{\"code\":\""+reverification+"\"}", "SMS_150736215");
                msg=reverification;
            } catch (ClientException e) {
                e.printStackTrace();
                msg=e.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;

    }

}
