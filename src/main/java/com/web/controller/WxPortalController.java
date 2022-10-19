package com.web.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.web.model.Member;
import com.web.model.Wx.UserInfo;
import com.web.service.MemberService;
import com.web.service.NoneMainService;
import com.web.utils.*;
import com.web.utils.database.DataSourceContextHolder;
import com.web.utils.wx.*;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.sql.rowset.spi.SyncResolver;
import java.util.*;

/**
 * 微信认证
 */
@RestController
@RequestMapping("/portal")
public class WxPortalController {

    @Autowired
    MemberService memberService;
    @Autowired
    NoneMainService mainService;
    TimeParse timeParse = new TimeParse();
    private String jwjxMiniAppid="gh_1ddeda6c87d0";
    @GetMapping(produces = "text/plain;charset=utf-8")
    public String authGet(
            @RequestParam(name = "signature", required = false) String signature,
            @RequestParam(name = "timestamp", required = false) String timestamp,
            @RequestParam(name = "nonce", required = false) String nonce,
            @RequestParam(name = "echostr", required = false) String echostr) {


        return echostr;
//        return "非法请求";
    }

    /**
     * FromUserName   	开发者微信号
     * */
    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String post(@RequestBody String requestBody,
                       @RequestParam("signature") String signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce,
                       @RequestParam("openid") String openid,
                       @RequestParam(name = "encrypt_type", required = false) String encType,
                       @RequestParam(name = "msg_signature", required = false) String msgSignature) throws Exception {

        System.out.println(requestBody);
        System.out.println(signature);
        System.out.println(timestamp);
        System.out.println(nonce);
        System.out.println(openid);
        System.out.println(encType);
        System.out.println(msgSignature);

        Map<String,String> param = HTMLSpirit.xmlToMap(requestBody);
        String str = HandleMsgTYPE(param);
        return str;
    }
    private String allKf(String param,String kf_wx){
        GetNewAccessToken getNewAccessToken = new GetNewAccessToken();
        String accessToken =getNewAccessToken.GetToken("经纬捷讯小程序");
        String urlToken = WxApi.getKfListUrl.replace("ACCESS_TOKEN",accessToken);
        JsonNode jsonObject = JsonUtils.stringToJsonNode(HttpRequest.getwx(urlToken));
        String kf_account="";
        JsonNode jsonObject2= jsonObject.get("kf_list");
        for (int i=0;i<jsonObject2.size();i++){
            if(jsonObject2.get(i).get("kf_wx").toString().replaceAll("\"","").equals(kf_wx)){
                kf_account=jsonObject2.get(i).get("kf_account").toString().replaceAll("\"","");
                return kf_account;
            }
        }
        return kf_account;
    }

    private String sendkefumsg(String type,Map map) {
        String openid = map.get("FromUserName").toString();//	发送方帐号（一个OpenID）
        String fromuser = map.get("ToUserName").toString();
        String content = "";
        if (map.containsKey("Content")) {
            content =" 留言内容："+ map.get("Content").toString();//开发者微信号
        }
        GetNewAccessToken getNewAccessToken = new GetNewAccessToken();
        String accessToken = getNewAccessToken.GetToken("经纬捷讯");
        String urlToken = "";
        String currentTime = timeParse.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss");
        urlToken = WxApi.templateSendMs.replace("ACCESS_TOKEN", accessToken);
        HashMap param = new HashMap();
        if( !map.containsKey("KfAccount")&&!map.containsKey("ToKfAccount")){
            KefuMessage message = new KefuMessage();
            message.setToUserName(openid);
            message.setFromUserName(fromuser);
            message.setCreateTime(new Date().getTime());
            message.setMsgType("transfer_customer_service");
            if (type == "MiniKefuSession") {
                param.put("title", "来访受理提醒");
                DataSourceContextHolder.setDbType("dataSourceA");
                HashMap template = mainService.queryTemplate(param);
                JsonObject jb = new JsonObject();
                param = new HashMap();
                param.put("tid", template.get("id"));
                DataSourceContextHolder.setDbType("dataSourceA");
                List<HashMap> listUser = memberService.queryKfViewTemUser(param);
                for (int i = 0; i < listUser.size(); i++) {
                    String touser = listUser.get(i).get("openid").toString();
                    jb.addProperty("touser", touser);
                    String tid = template.get("templateID").toString();
                    jb.addProperty("template_id", tid);
                    JsonObject data = new JsonObject();
                    JsonObject data1 = new JsonObject();
                    data1.addProperty("value", "可登录网页或者打开客服小助手小程序进行回复");
                    data.add("first", data1);
                    data1 = new JsonObject();
                    data1.addProperty("value", openid);
                    data.add("keyword1", data1);
                    data1 = new JsonObject();
                    data1.addProperty("value", "");
                    data.add("keyword2", data1);
                    data1 = new JsonObject();
                    data1.addProperty("value", currentTime);
                    data.add("keyword3", data1);
                    data1 = new JsonObject();
                    data1.addProperty("value", content);
                    data.add("remark", data1);
                    jb.add("data", data);
                    JsonNode jsonObject = JsonUtils.stringToJsonNode(HttpRequest.postwx(urlToken, jb.toString()));
                    int errcode = 0;
                    String errmsg = jsonObject.get("errmsg").asText();
                    if (jsonObject.has("errcode")) {
                        if (errcode != 0) {
                            return errmsg;
                        }
                    }
                }
            }
            return MessageUtil.textMessageToXml(message);
        }else {
            String KfAccount = "",FromKfAccount="";
            if(map.containsKey("KfAccount")){
                KfAccount =  map.get("KfAccount").toString();
            }
            if(map.containsKey("ToKfAccount")){
                KfAccount =  map.get("ToKfAccount").toString();
                FromKfAccount =  map.get("FromKfAccount").toString();
            }
            if (type == "KF_CREATE_SESSION"||type=="KF_SWITCH_SESSION") {
                param.put("title", "客服受理通知");
                DataSourceContextHolder.setDbType("dataSourceA");
                HashMap template = mainService.queryTemplate(param);
                JsonObject jb = new JsonObject();
                param = new HashMap();
                param.put("tid", template.get("id"));
                DataSourceContextHolder.setDbType("dataSourceA");
                List<HashMap> listUser = memberService.queryKfViewTemUser(param);
                for (int i = 0; i < listUser.size(); i++) {
                    String touser = listUser.get(i).get("openid").toString();
                    jb.addProperty("touser", touser);
                    String tid = template.get("templateID").toString();
                    jb.addProperty("template_id", tid);
                    JsonObject data = new JsonObject();
                    JsonObject data1 = new JsonObject();
                    String value ="客服已接入会话,请及时处理请求";
                    content = "";
                    if(type=="KF_SWITCH_SESSION"){
                        value ="客服已转接会话";
                        content="客服【"+KfAccount+"】已从客服【"+FromKfAccount+"】接入会话";
                    }
                    data1.addProperty("value", value);
                    data.add("first", data1);
                    data1 = new JsonObject();
                    data1.addProperty("value", KfAccount);
                    data.add("keyword1", data1);
                    data1 = new JsonObject();
                    data1.addProperty("value", "");
                    data.add("keyword2", data1);
                    data1 = new JsonObject();
                    data1.addProperty("value", currentTime);
                    data.add("keyword3", data1);
                    data1 = new JsonObject();
                    data1.addProperty("value", "");
                    data.add("keyword4", data1);
                    data1 = new JsonObject();
                    data1.addProperty("value", content);
                    data.add("remark", data1);
                    jb.add("data", data);
                    JsonNode jsonObject = JsonUtils.stringToJsonNode(HttpRequest.postwx(urlToken, jb.toString()));
                    int errcode = 0;
                    String errmsg = jsonObject.get("errmsg").asText();
                    if (jsonObject.has("errcode")) {
                        if (errcode != 0) {
                            return errmsg;
                        }
                    }
                }
            }else if(type=="KF_SWITCH_SESSION"){

            }
        }
        return "";
    }

    private  String MiniKefuSession(Map map) {
        String res = "";
        try {
            String openid = map.get("FromUserName").toString();//	发送方帐号（一个OpenID）
            String fromuser = map.get("ToUserName").toString();//开发者微信号
            DataSourceContextHolder.setDbType("dataSourceA");
            String createTime = TimeParse.stampToDate(map.get("CreateTime").toString());
            map.put("CreateTime", createTime);
            String currentTime = timeParse.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss");
            map.put("currtime", currentTime);
            DataSourceContextHolder.setDbType("dataSourceA");
            memberService.insertWxChat(map);
            if (fromuser.equals(jwjxMiniAppid)) {
//                if (map.containsKey("SessionFrom")) {
                    return sendkefumsg("MiniKefuSession",map);
                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }




    private String HandleMsgTYPE(Map map) {
        //响应消息
        String responseMessage = "";
        //得到消息类型
        String msgType = map.get("MsgType").toString();
        String Event="";
        MessageType eventType=null;
        if (map.containsKey("Event")){
            Event = map.get("Event").toString();
            eventType = MessageType.valueOf(MessageType.class, Event.toUpperCase());
        }

        MessageType messageEnumType = MessageType.valueOf(MessageType.class, msgType.toUpperCase());
        switch (messageEnumType) {
            case TEXT:
                responseMessage = MiniKefuSession(map);
                break;
            case IMAGE:
            case VOICE:
            case VIDEO:
            case SHORTVIDEO:
                responseMessage = HandleMessage(map);
                break;
            case LOCATION:
                recordLoction(map);
                break;
            case LINK:
                break;
            case EVENT:
                switch (eventType){
                    case USER_ENTER_TEMPSESSION:
//                        responseMessage = USER_ENTER_TEMPSESSION(map);
                        break;
                    case SUBSCRIBE:
                        responseMessage = HandleSubscribe(map);
                        break;
                    case SCAN:
                        break;
                    case VIEW:
                        break;
                    case CLICK:
                        responseMessage = HandleMessage(map);
                        break;
                    case UNSUBSCRIBE:
                        responseMessage = HandleSubscribe(map);
                        break;
                    case KF_CLOSE_SESSION:
                        responseMessage = KF_CLOSE_SESSION(map);
                        break;
                    case KF_CREATE_SESSION:
                        responseMessage = KF_CREATE_SESSION(map);
                        break;
                    case KF_SWITCH_SESSION:
                        responseMessage = KF_SWITCH_SESSION(map);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;

        }



        return responseMessage;
    }

    private String KF_CREATE_SESSION(Map map) {
        String str="";
        String openid = map.get("FromUserName").toString();//	发送方帐号（一个OpenID）
        String timestamp = map.get("CreateTime").toString();//消息创建时间 （整型）
        String fromuser = map.get("ToUserName").toString();//开发者微信号
        String KfAccount = map.get("KfAccount").toString();
        GetNewAccessToken getNewAccessToken = new GetNewAccessToken();
        String accessToken =getNewAccessToken.GetToken("经纬捷讯小程序");
        String urlToken = WxApi.actSessionUrl.replace("ACCESS_TOKEN",accessToken);
        JsonObject jb = new JsonObject();
        JsonObject jb2 = new JsonObject();
        jb.addProperty("touser", openid);
        jb.addProperty("msgtype", "text");
        String content = "客服【"+KfAccount+"】接入会话";
        jb2.addProperty("content", content);
        jb.add("text", jb2);
        JsonNode tmp = JsonUtils.stringToJsonNode(HttpRequest.postwx(urlToken, jb.toString()));
        int errCode = tmp.get("errcode").asInt();
        String errMsg = tmp.get("errmsg").textValue();
        if(errCode==0){
            String createTime = TimeParse.stampToDate(map.get("CreateTime").toString());
            map.put("CreateTime",createTime);
            map.put("currtime",timeParse.convertDate2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
            DataSourceContextHolder.setDbType("dataSourceA");
            memberService.insertWxChat(map);
            return sendkefumsg("KF_CREATE_SESSION",map);
        }else {
            return errMsg;
        }
    }

    private String KF_SWITCH_SESSION(Map map) {
        String str="";
        String openid = map.get("FromUserName").toString();//	发送方帐号（一个OpenID）
        String fromuser = map.get("ToUserName").toString();//开发者微信号
        String ToKfAccount = map.get("ToKfAccount").toString();//开发者微信号
        String FromKfAccount = map.get("FromKfAccount").toString();//开发者微信号
        GetNewAccessToken getNewAccessToken = new GetNewAccessToken();
        String accessToken =getNewAccessToken.GetToken("经纬捷讯小程序");
        String urlToken = WxApi.actSessionUrl.replace("ACCESS_TOKEN",accessToken);
        JsonObject jb = new JsonObject();
        JsonObject jb2 = new JsonObject();
        jb.addProperty("touser", openid);
        jb.addProperty("msgtype", "text");
        String content = "消息已转接给客服【"+ToKfAccount+"】";
        jb2.addProperty("content", content);
        jb.add("text", jb2);
        JsonNode tmp = JsonUtils.stringToJsonNode(HttpRequest.postwx(urlToken, jb.toString()));
        int errCode = tmp.get("errcode").asInt();
        String errMsg = tmp.get("errmsg").textValue();
        if(errCode==0){
            String createTime = TimeParse.stampToDate(map.get("CreateTime").toString());
            map.put("CreateTime",createTime);
            map.put("currtime",timeParse.convertDate2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
            DataSourceContextHolder.setDbType("dataSourceA");
            memberService.insertWxChat(map);
            return sendkefumsg("KF_SWITCH_SESSION",map);
        }else {
            return errMsg;
        }
    }

    private String recordLoction  (Map map) {
        String openid = map.get("FromUserName").toString();//	发送方帐号（一个OpenID）
        String timestamp = map.get("CreateTime").toString();//消息创建时间 （整型）
        String fromuser = map.get("ToUserName").toString();//开发者微信号


        String str = "";
        return str;

    }
    private String KF_CLOSE_SESSION(Map map) {
        String openid = map.get("FromUserName").toString();
        String fromuser = map.get("ToUserName").toString();
        String str="";
        if(map.containsKey("CloseType")) {
            if(map.get("CloseType").equals("KF")){
                String KfAccoount = map.get("KfAccoount").toString();

//TODO  微信客服写法

//                HashMap hashMap = new HashMap();
//                hashMap.put("touser",openid);
//                hashMap.put("msgtype", "msgmenu");
//                HashMap hashMap2 = new HashMap();
//
//                hashMap2.put("head_content", "您对本次会话是否满意呢? (五分钟有效)\n");
//                hashMap2.put("tail_content", "如需再次对话，请再点击【在线客服】按钮");
//                List list = new ArrayList();
//                HashMap hashMap3 = new HashMap();
//                hashMap3.put("id","101");
//                hashMap3.put("content", "满意");
//                list.add(hashMap3);
//
//                hashMap3 = new HashMap();
//                hashMap3.put("id","102");
//                hashMap3.put("content", "不满意");
//                list.add(hashMap3);
//                hashMap2.put("list",list);
//                hashMap.put("msgmenu", hashMap2);
//                String obj = JsonUtils.objectToJson(hashMap);
//                GetNewAccessToken getNewAccessToken = new GetNewAccessToken();
//                String accessToken =getNewAccessToken.GetToken("经纬捷讯");
//                String urlToken = WxApi.actSessionUrl.replace("ACCESS_TOKEN",accessToken);
//                String tmp = HttpRequest.postwx(urlToken, obj);
//                CRUDDb((HashMap) map,"insert");


//                小程序客服写法
                GetNewAccessToken getNewAccessToken = new GetNewAccessToken();
                String accessToken =getNewAccessToken.GetToken("经纬捷讯");
                String urlToken = WxApi.templateSendMs.replace("ACCESS_TOKEN",accessToken);
                //满意度调查
                HashMap param = new HashMap();
                param.put("title","客服满意度评价提醒");
                HashMap template = mainService.queryTemplate(param);
                JsonObject jb = new JsonObject();
                jb.addProperty("touser","oTeKzwI4_mJSKovEirIeY2dvzfvA");
                String tid=template.get("templateID").toString();
                jb.addProperty("template_id", tid);
                JsonObject miniprogram = new JsonObject();
                miniprogram.addProperty("appid", "wx43d95d81bbebafae");
                miniprogram.addProperty("pagepath", "pages/index/index");
//        jb.add("miniprogram", miniprogram);
                JsonObject data = new JsonObject();
                JsonObject data1= new JsonObject();
                data1.addProperty("value","尊敬的用户，感谢您的咨询。现邀请您对我们的客服服务进行评价。");
                data.add("first", data1);
                data1= new JsonObject();
                data1.addProperty("value","kf2001@gh_1ddeda6c87d0");
                data.add("keyword1", data1);
                data1= new JsonObject();
                data1.addProperty("value","tothemoon");
                data.add("keyword2", data1);
                jb.add("data", data);
                JsonNode jsonObject = JsonUtils.stringToJsonNode(HttpRequest.postwx(urlToken, jb.toString()));
                return str;
            }
        }
        return str;
    }

    private static final int leafMsg1  = 201;
    private static final int leafMsg2  = 202;
    private String HandleMessage(Map map) {
        String openid = map.get("FromUserName").toString();//	发送方帐号（一个OpenID）
        String fromuser = map.get("ToUserName").toString();//开发者微信号
        String str = "";
        if(map.containsKey("EventKey")) {
            String key = map.get("EventKey").toString();
            if (key.equals("kefu")) {
                String kf = kfseesion(openid);
                TextMessage text = new TextMessage();
                text.setContent(kf);
                text.setToUserName(openid);
                text.setFromUserName(fromuser);
                text.setCreateTime(new Date().getTime());
                text.setMsgType("text");
                str = MessageUtil.textMessageToXml(text);
                return str;
            }
        }else {
            String content="";
            if (map.containsKey("bizmsgmenuid")){
                int bizmid = Integer.parseInt(map.get("bizmsgmenuid").toString());
                DataSourceContextHolder.setDbType("dataSourceA");
                map.put("type","KF");
                HashMap hashMap = mainService.selectWxfeedBack(map);
                if(hashMap!=null) {
                    long datetime2 = Long.parseLong(map.get("CreateTime").toString());
                    long datetime = Long.parseLong(hashMap.get("CreateTime").toString());
                    long diff = datetime2 - datetime;
                    long days = diff / (60 * 60 * 24);
                    long hours = (diff - days * (60 * 60 * 24)) / (60 * 60);
                    long minutes = (diff - days * (60 * 60 * 24) - hours * (60 * 60)) / (60);
                    if (minutes <= 5 && days == 0) {
                        content = "感谢您的反馈";
                        map.put("KfAccount", hashMap.get("KfAccount"));
                        map.put("id", hashMap.get("id"));
                        map.put("finishTime", map.get("CreateTime").toString());
                        CRUDDb((HashMap) map,"update");
                    } else {
                        return str;
                    }
                }else {
                    return str;
                }

            }else {
                content = "请问您是否需要咨询？是 请点击下面【在线客服】开始会话";
            }
            TextMessage text = new TextMessage();
            text.setContent(content);
            text.setToUserName(openid);
            text.setFromUserName(fromuser);
            text.setCreateTime(new Date().getTime());
            text.setMsgType("text");
            str = MessageUtil.textMessageToXml(text);
            return str;

        }
        return "";
    }

    /**
     * 功能描述:获取在线客服列表
     * @param:
     * @return:
     */
    public JsonNode getonlinekflist() {
        GetNewAccessToken getNewAccessToken = new GetNewAccessToken();
        String accessToken =getNewAccessToken.GetToken("经纬捷讯");
        String urlToken = WxApi.getonlinekflist.replace("ACCESS_TOKEN",accessToken);
        JsonNode jsonObject = JsonUtils.stringToJsonNode(HttpRequest.getwx(urlToken));
        return jsonObject;
    }
//    public void leaveMessage(String openid) {
//        HashMap hashMap = new HashMap();
//        hashMap.put("touser", openid);
//        hashMap.put("msgtype", "text");
//        HashMap hashMap2 = new HashMap();
//        String content = "如需留言，请点击下面【留言】按钮开始会话";
//        hashMap2.put("content", content);
//        hashMap.put("text", hashMap2);
//
//        String obj = JsonUtils.objectToJson(hashMap);
//        GetNewAccessToken getNewAccessToken = new GetNewAccessToken();
//        String accessToken =getNewAccessToken.GetToken("经纬捷讯");
//        String urlToken = WxApi.actSessionUrl.replace("ACCESS_TOKEN", accessToken);
//        String tmp = HttpRequest.postwx(urlToken, obj);
//        HashMap map = new HashMap<>();
//        map.put("FromUserName", openid);
//        map.put("Event", "userLY");
//        map.put("CloseType", "LY");  //leave msg 留言
//        map.put("CreateTime", new Date().getTime() / 1000);
//        CRUDDb(map,"insert");
//    }
    private void CRUDDb(HashMap map,String type){
        DataSourceContextHolder.setDbType("dataSourceA");
        if(type=="insert"){
            mainService.insertWxfeedBack(map);
        }else{
            mainService.updateWxfeedBack(map);
        }

    }
    public void sendMsgToKF(){
        GetNewAccessToken getNewAccessToken = new GetNewAccessToken();
        String accessToken =getNewAccessToken.GetToken("经纬捷讯");
        HashMap map = new HashMap();
        map.put("status",2);
        DataSourceContextHolder.setDbType("dataSourceA");
        List<Member> kf_list = memberService.queryMember(map);
        for(int i=0;i<kf_list.size();i++) {
            JsonObject jb = new JsonObject();
            JsonObject jb2 = new JsonObject();
            jb.addProperty("touser", kf_list.get(i).getOpenid());
            jb.addProperty("msgtype", "text");
            String content = "当前没有客服在线，请各位客服登陆查看用户消息，并尽快回复";
            jb2.addProperty("content", content);
            jb.add("text", jb2);
            String urlToken2 = WxApi.actSessionUrl.replace("ACCESS_TOKEN", accessToken);
            String tmp = HttpRequest.postwx(urlToken2, jb.toString());
            System.err.println(tmp);
        }
    }


    /**
     * 功能描述:创建会话
     * @param:
     * @return:
     */
    public String kfseesion(String openId) {
        GetNewAccessToken getNewAccessToken = new GetNewAccessToken();
        String accessToken =getNewAccessToken.GetToken("经纬捷讯");
        String urlToken = WxApi.kfSessionUrl.replace("ACCESS_TOKEN",accessToken);
        JsonNode kfInfo=getonlinekflist();
        if (kfInfo.has("kf_online_list")){
            JsonNode kf_online_list = kfInfo.get("kf_online_list");
            if(kf_online_list.size()==0){
                sendMsgToKF();
                return "你好，当前没有客服在线，我们看到消息后会立刻回复您";
            }else {
                DataSourceContextHolder.setDbType("dataSourceA");
                /**
                 * 首先 判断当前客服是否有客服，再判断当前存在的客服是否是今日值班客服
                 * 值班客服不存在  转接给现有客服
                 * 值班客服存在  转接给值班客服
                 * 值班客服今日没有  转接给默认客服
                 * */
                List<HashMap<String, Object>> kufuList = memberService.queryKfListnowDay();
                String zbkefu = "";
                if (kufuList.size() > 0) {
                    for (int i = 0; i < kf_online_list.size(); i++) {
                        for (int j = 0; j < kufuList.size(); j++) {
                            String online_kefu = String.valueOf(kf_online_list.get(i).get("kf_account")).replaceAll("\"","");
                            String kefu_kefu = String.valueOf(kufuList.get(j).get("kf_account")).replaceAll("\"","");
                            if (online_kefu.equals(kefu_kefu) ) {
                                zbkefu = "客服" + kufuList.get(j).get("username") + "准备为您服务";
                                JsonObject jb = new JsonObject();
                                jb.addProperty("kf_account",kefu_kefu);
                                jb.addProperty("openid", openId);
                                JsonNode jsonObject = JsonUtils.stringToJsonNode(HttpRequest.postwx(urlToken, jb.toString()));
                                int errcode = Integer.parseInt(jsonObject.get("errcode").toString());
                                String errmsg = jsonObject.get("errmsg").toString();
                                if (errcode == 0) {
                                    return zbkefu + "\n\n客服连接成功，请问有什么可以帮助您";
                                } else if (errcode == 65415) {
                                    sendMsgToKF();
                                    return "你好，当前没有客服在线，我们看到消息后会立刻回复您";
                                } else {
                                    return errmsg;
                                }

                            }
                        }
                    }
                } else {
                    List<HashMap<String, Object>> kufudefault = memberService.queryKfListDefault();
                    for(int i =0;i<kf_online_list.size();i++) {
                        for (int j = 0; j < kufudefault.size(); j++) {
                            String online_kefu = String.valueOf(kf_online_list.get(i).get("kf_account")).replaceAll("\"","");
                            String kefu_kefu = String.valueOf(kufudefault.get(j).get("kf_account")).replaceAll("\"","");
                            if (online_kefu.equals(kefu_kefu) ) {
                                zbkefu = "客服" + kufudefault.get(j).get("username") + "准备为您服务";
                                JsonObject jb = new JsonObject();
                                jb.addProperty("kf_account", kefu_kefu);
                                jb.addProperty("openid", openId);
                                JsonNode jsonObject = JsonUtils.stringToJsonNode(HttpRequest.postwx(urlToken, jb.toString()));
                                int errcode = Integer.parseInt(jsonObject.get("errcode").toString());
                                String errmsg = jsonObject.get("errmsg").toString();
                                if (errcode == 0) {
                                    return zbkefu + "\n\n客服连接成功，请问有什么可以帮助您";
                                } else if (errcode == 65415) {
                                    sendMsgToKF();
                                    return "你好，当前没有客服在线，我们看到消息后会立刻回复您";
                                } else {
                                    return errmsg;
                                }
                            }
                        }
                    }
                }
                return zbkefu;
            }
        }
        sendMsgToKF();
        return "你好，当前没有客服在线，我们看到消息后会立刻回复您";
    }


    private String HandleSubscribe(Map map){
        String str="";
        AJAXResult ajaxResult = new AJAXResult();
        String openid =  map.get("FromUserName").toString();//	发送方帐号（一个OpenID）
        String timestamp = map.get("CreateTime").toString();//消息创建时间 （整型）
        String fromuser =  map.get("ToUserName").toString();//开发者微信号
        String msgType = String.valueOf(map.get("MsgType"));

        String Event =  map.get("Event").toString();//事件类型，subscribe(订阅)、unsubscribe(取消订阅)
        if(Event.equals("subscribe")){
            //获取用户信息
            GetNewAccessToken getNewAccessToken = new GetNewAccessToken();
            String accessToken =getNewAccessToken.GetToken("经纬捷讯");
            String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" +accessToken+"&openid="+openid+"&lang=zh_CN";
            String result =HttpRequest.getwx(url);
            if(result.contains("errmsg")){
                JsonNode jsonObject =JsonUtils.stringToJsonNode(result);
                int errcode = Integer.parseInt(jsonObject.get("errcode").toString());
                String errmsg = jsonObject.get("errmsg").toString();
                ajaxResult.setErrmsg(errmsg);
                ajaxResult.setErrcode(errcode);
            }else{
                DataSourceContextHolder.setDbType("dataSourceA");

                ajaxResult.setErrmsg("关注成功");
                ajaxResult.setErrcode(0);
                UserInfo userInfo = JsonUtils.jsonToPojo(result,UserInfo.class);
                int[] tagids = userInfo.getTagid_list();
                String strtag ="";
                for(int id :tagids){
                    strtag = id+",";
                }
                userInfo.setTagid_listStr(strtag);
                HashMap pa = new HashMap();
                pa.put("openid",userInfo.getOpenid());
                UserInfo isExist = memberService.queryUserInfoByParam(pa);
                if(isExist==null){
                    memberService.insertUserInfoWhenSubscribe(userInfo);
                }else{
                    memberService.updateUserInfoWhenSubscribe(userInfo);
                }
            }
            TextMessage text = new TextMessage();
            text.setContent("感谢关注经纬捷讯！\n\n" +
                    "深圳经纬捷讯信息技术有限公司主要从事工业互联网软件、物联网和通讯技术的开发、升级，为企业提供高效的企业信息化管理系统。" +
                    "\n" +
                    "工业互联网平台是指以云服务、人机互联、数据采集等模块为核心， 为客户提供数据采集、传输、控制、显示和数据分析等服务的系统性平台。");
            text.setToUserName(openid);
            text.setFromUserName(fromuser);
            text.setCreateTime(new Date().getTime());
            text.setMsgType("text");
            str = MessageUtil.textMessageToXml(text);
        }else {
            HashMap map1 = new HashMap();
            map1.put("openid",openid);
            memberService.deleteUserinfo(map1);
        }



        return str;
    }


    private String HandleView(Map map) {
        DataSourceContextHolder.setDbType("dataSourceA");

        String str = "";
        String openid = map.get("FromUserName").toString();
        String fromuser = map.get("ToUserName").toString();
        KefuMessage message = new KefuMessage();
        KKTransInfo kkTransInfo = new KKTransInfo();
        List<HashMap<String, Object>> kufuList = memberService.queryKfListnowDay();
        String touser="";
        if (kufuList.size() > 0) {
            touser= String.valueOf(kufuList.get(0).get("kf_account"));
        } else {
            List<HashMap<String, Object>> kufudefault = memberService.queryKfListDefault();
            touser = String.valueOf(kufudefault.get(0).get("kf_account"));
        }
        if(map.containsKey("Content")){
            String content = map.get("Content").toString();
        }

        if (touser==null){
            kkTransInfo.setKfAccount(touser);
            message.setTransInfo(kkTransInfo);
        }
        message.setToUserName(openid);
        message.setFromUserName(fromuser);
        message.setCreateTime(new Date().getTime());
        message.setMsgType("transfer_customer_service");
        str = MessageUtil.textMessageToXml(message);
        return str;
    }
}