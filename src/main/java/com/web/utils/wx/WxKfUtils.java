package com.web.utils.wx;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import com.web.service.NoneMainService;
import com.web.utils.AJAXResult;
import com.web.utils.HttpRequest;
import com.web.utils.JsonUtils;
import com.web.utils.ResponseModel;
import com.web.utils.database.DataSourceContextHolder;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.bean.menu.WxMenuRule;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

public class WxKfUtils {

    /**
     * 创建用户和客服的会话
     *
     * @param kf_account
     * @param openid
     * @param access_token
     *
     * @return
     */
    public String createSession(String kf_account, String openid,String access_token){
        String data = "   {\n" +
                "      \"kf_account\" : \""+  kf_account  +"\",\n" +
                "      \"openid\" : \""+ openid +"\"\n" +
                "   }";
        String url = "https://api.weixin.qq.com/customservice/kfsession/create?access_token=" + access_token;
        return HttpRequest.postwx(url,data);
    }

    /**
     * 获取聊天记录
     *
     * @param starttime
     * @param endtime
     * @param access_token
     *
     *  {
     *       "starttime" : 987654321, 起始时间，unix时间戳
     *       "endtime" : 987654321, 	结束时间，unix时间戳，每次查询时段不能超过24小时
     *       "msgid" : 1,	消息id顺序从小到大，从1开始
     *       "number" : 10000 	每次获取条数，最多10000条
     * }
     * @return
     */
    public String Getmsgrecord(String starttime, String endtime,String access_token,String msgid,
                               String number){
        String data = "";
        String url = "https://api.weixin.qq.com/customservice/kfsession/create?access_token=" + access_token;
        return HttpRequest.postwx(url,data);
    }

    @Autowired
    NoneMainService mainService;

    /**
     * 创建菜单
     * */
    AJAXResult ajaxResult = new AJAXResult();

    private WxMpService wxMpService;
    private String menuId = null;
//https://api.weixin.qq.com/cgi-bin/menu/addconditional?access_token=ACCESS_TOKEN

    public void trymatch(String appid,String appsecret) throws WxErrorException {
        // 1.根据appid和appsecret和回调地址配置微信授权
        WxMpDefaultConfigImpl wxMpDefaultConfig = new WxMpDefaultConfigImpl();
        wxMpDefaultConfig.setAppId(appid);
        wxMpDefaultConfig.setSecret(appsecret);
        WxMpServiceImpl wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpDefaultConfig);
        String Token = wxMpService.getAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=" +Token;
        String dataresturned =HttpRequest.getwx(url);
        System.out.println(dataresturned);
    }
    public void deleteAll(String appid,String appsecret) throws WxErrorException {
        // 1.根据appid和appsecret和回调地址配置微信授权
        WxMpDefaultConfigImpl wxMpDefaultConfig = new WxMpDefaultConfigImpl();
        wxMpDefaultConfig.setAppId(appid);
        wxMpDefaultConfig.setSecret(appsecret);
        WxMpServiceImpl wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpDefaultConfig);
        String Token = wxMpService.getAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token="+Token;
        String dataresturned =HttpRequest.getwx(url);
        System.out.println(dataresturned);
    }


    private String websocketURL="https://www.jwjxinfo.com/";
    public Object addconditionalMenu(String appid,String appsecret){
        try {

            // 1.根据appid和appsecret和回调地址配置微信授权
            WxMpDefaultConfigImpl wxMpDefaultConfig = new WxMpDefaultConfigImpl();
            wxMpDefaultConfig.setAppId(appid);
            wxMpDefaultConfig.setSecret(appsecret);
            WxMpServiceImpl wxMpService = new WxMpServiceImpl();
            wxMpService.setWxMpConfigStorage(wxMpDefaultConfig);
            List<WxMenuButton> subList = new ArrayList<>();
            /*
             *  2. 设置按钮
             *  menu对象是总的按钮,button是具体的按钮
             */
            WxMenu wxMenu = new WxMenu();
//            WxMenuButton button1 = new WxMenuButton();
//            WxMenuButton button1_1 = new WxMenuButton();
//            button1_1.setType(WxConsts.MenuButtonType.VIEW);
//            button1_1.setName("小程序xxx");
//            button1_1.setUrl(websocketURL);
//            WxMenuButton button1_2 = new WxMenuButton();
//            button1_2.setType(WxConsts.MenuButtonType.VIEW);
//            button1_2.setUrl(websocketURL+"#services");
//            button1_2.setName("产品中心");
//            subList.add(button1_1);
//            subList.add(button1_2);

//            button1.setName("公司简介");
//            button1.setSubButtons(subList);
            WxMenuButton button2 = new WxMenuButton();
            button2.setType(WxConsts.MenuButtonType.MINIPROGRAM);
            button2.setPagePath("pages/index/index");
            button2.setName("在线客服");
            button2.setAppId("wx43d95d81bbebafae");
            button2.setUrl("http://mp.weixin.qq.com");

            WxMenuButton button3 = new WxMenuButton();
            button3.setType(WxConsts.MenuButtonType.MINIPROGRAM);
            button3.setName("系统维护");
            button3.setAppId("wxcd810e668dfb5370");
            button3.setPagePath("maintenance/maintenance/maintenance");
            button3.setUrl("http://mp.weixin.qq.com");





            // 3. 添加到menu
//            wxMenu.getButtons().add(button1);
            wxMenu.getButtons().add(button2);
            wxMenu.getButtons().add(button3);
            WxMenuRule matchRule = new WxMenuRule();
            matchRule.setTagId("114");
            wxMenu.setMatchRule(matchRule);

            String result = wxMenu.toJson().toString();
            String Token = wxMpService.getAccessToken();
            String url = "https://api.weixin.qq.com/cgi-bin/menu/addconditional?access_token=" +Token;
            String dataresturned =HttpRequest.postwx(url,result);
            ajaxResult.setErrcode(0);
            ajaxResult.setErrmsg("ok");
        } catch (Exception e) {
            ajaxResult.setErrcode(500);
            ajaxResult.setErrmsg(e.getMessage());
            e.printStackTrace();
        }
        return ajaxResult;
    }
    public Object createMenu(String appid,String appsecret){

        try {
            // 1.根据appid和appsecret和回调地址配置微信授权
            WxMpDefaultConfigImpl wxMpDefaultConfig = new WxMpDefaultConfigImpl();
            wxMpDefaultConfig.setAppId(appid);
            wxMpDefaultConfig.setSecret(appsecret);
            WxMpServiceImpl wxMpService = new WxMpServiceImpl();
            wxMpService.setWxMpConfigStorage(wxMpDefaultConfig);
            List<WxMenuButton> subList = new ArrayList<>();
            /*
             *  2. 设置按钮
             *  menu对象是总的按钮,button是具体的按钮
             */
            WxMenu wxMenu = new WxMenu();
            WxMenuButton button1 = new WxMenuButton();
            WxMenuButton button1_1 = new WxMenuButton();
            button1_1.setType(WxConsts.MenuButtonType.VIEW);
            button1_1.setName("公司主页");
            button1_1.setUrl(websocketURL);

            WxMenuButton button1_2 = new WxMenuButton();
            button1_2.setType(WxConsts.MenuButtonType.VIEW);
            button1_2.setUrl(websocketURL+"#services");
            button1_2.setName("产品中心");

            WxMenuButton button1_3 = new WxMenuButton();
            button1_3.setType(WxConsts.MenuButtonType.VIEW);
            button1_3.setUrl(websocketURL+"aboutus");
            button1_3.setName("公司介绍");
            WxMenuButton button1_4 = new WxMenuButton();
            button1_4.setType(WxConsts.MenuButtonType.VIEW);
            button1_4.setUrl(websocketURL+"problem");
            button1_4.setName("常见问题");
            subList.add(button1_1);
            subList.add(button1_2);
            subList.add(button1_3);
            subList.add(button1_4);
            button1.setName("公司简介");
            button1.setSubButtons(subList);
            WxMenuButton button2 = new WxMenuButton();
            button2.setType(WxConsts.MenuButtonType.CLICK);
            button2.setName("在线客服");
//            button2.setUrl(websocketURL+"kefu/mobile/index");
            button2.setKey("kefu");

            subList = new ArrayList<>();
//            WxMenuButton button3_1 = new WxMenuButton();
//            button3_1.setType(WxConsts.MenuButtonType.MINIPROGRAM);
//            button3_1.setName("系统维护");
//            button3_1.setAppId("wxcd810e668dfb5370");
//            button3_1.setPagePath("maintenance/company/company");
//            button3_1.setUrl("http://mp.weixin.qq.com");

//            WxMenuButton button3_2 = new WxMenuButton();
//            button3_2.setType(WxConsts.MenuButtonType.MINIPROGRAM);
//            button3_2.setPagePath("maintenance/userSystem/userSystem");
//            button3_2.setName("用户管理");
//            button3_2.setAppId("wxcd810e668dfb5370");
//            button3_2.setUrl("http://mp.weixin.qq.com");

//            subList.add(button3_1);
//            subList.add(button3_2);
//            WxMenuButton btn3 = new WxMenuButton();
//            btn3.setName("功能操作");
//            btn3.setSubButtons(subList);
            // 3. 添加到menu
            wxMenu.getButtons().add(button1);
            wxMenu.getButtons().add(button2);

            String result = wxMenu.toJson().toString();

            this.menuId = wxMpService.getMenuService().menuCreate(result);
            ajaxResult.setErrcode(0);
            ajaxResult.setErrmsg("ok");
        } catch (WxErrorException e) {
            ajaxResult.setErrcode(500);
            ajaxResult.setErrmsg(e.getMessage());
            e.printStackTrace();
        }
        return ajaxResult;
    }


    /**
     * 删除菜单
     * */
    public Object deleteMenu(String appid,String appsecret){
        try {
            wxMpService.getMenuService().menuDelete(menuId);
            ajaxResult.setErrcode(0);
            ajaxResult.setErrmsg("ok");
        } catch (Exception e) {
            ajaxResult.setErrcode(500);
            ajaxResult.setErrmsg(e.getMessage());
            e.printStackTrace();
        }
        return ajaxResult;
    }
    /**
     * 获取菜单
     * */
    public void MenuGet_AfterCreateConditionalMenu() throws WxErrorException {
        WxMpMenu wxMenu = this.wxMpService.getMenuService().menuGet();
        assertNotNull(wxMenu);
        System.out.println(wxMenu.toJson());
        assertNotNull(wxMenu.getConditionalMenu().get(0).getRule().getTagId());
    }
    public void msgrecord() throws WxErrorException, ParseException {
        String token = "45_Tt81mb68Y3HrtIxBRkz5E7UospU1F0XLaqJWeL8pfx4xTtQvqS8p6mm6AWyHoVL9KuTlNPq7tLjoLT4-ZmUKEKxmHTu0XPyzk-hdDJ6XmHjZZQM1f5M5VmUkwDapcmpZJnZOI4-7PQ0h1tFUWJTiADAJWG";


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse("2021-06-01 8:45:25");
        long startTime = date.getTime()/1000;
        Date date1 = simpleDateFormat.parse("2021-06-01 17:45:26");
        long endTime = date1.getTime()/1000;
        String url ="https://api.weixin.qq.com/customservice/msgrecord/getmsglist?access_token="+token;
        JsonObject jb = new JsonObject();
        jb.addProperty("starttime",startTime);
        jb.addProperty("endtime",endTime);
        jb.addProperty("msgid",1);
        jb.addProperty("number",1000);
        JsonNode jsonObject = JsonUtils.stringToJsonNode(HttpRequest.postwx(url,jb.toString()));
        System.out.println(jsonObject);



    }
    public static void main(String[] args) throws WxErrorException, ParseException {
        WxKfUtils wxKfUtils = new WxKfUtils();
        String b = "wx66861739dbbd59b1";
        String c = "57ea154cd32a3e0bd81f463c75de869b";
        wxKfUtils.deleteAll(b,c);
        wxKfUtils.createMenu(b,c);
        wxKfUtils.addconditionalMenu(b,c);
        wxKfUtils.trymatch(b,c);
//        wxKfUtils.msgrecord();
        //        String a = "client_credential";

//
//        getAccessToken(a,b,c);
//        try {
//            MenuGet_AfterCreateConditionalMenu();
//        } catch (WxErrorException e) {
//            e.printStackTrace();
//        }

    }
}
