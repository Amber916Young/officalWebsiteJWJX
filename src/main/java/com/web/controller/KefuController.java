package com.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.gson.JsonObject;
import com.web.model.EmpInfo;
import com.web.model.Member;
import com.web.model.NoneMain;
import com.web.model.Wx.UserInfo;
import com.web.service.KfService;
import com.web.service.MemberService;
import com.web.service.NoneMainService;
import com.web.utils.*;
import com.web.utils.database.DataSourceContextHolder;
import com.web.utils.wx.GetNewAccessToken;
import com.web.utils.wx.WxApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

@Controller
@RequestMapping("/kefu")
public class KefuController {
    @Autowired
    MemberService memberService;

    @Autowired
    NoneMainService mainService;
    @Autowired
    KfService kfService;

    @RequestMapping(value = "/index")
    public String kefuIndex(){
        return "kefu/kefu";
    }

    @RequestMapping(value = "/index2")
    public String kefuIndex2(){
        return "kefu/kefu2";
    }


    @Resource(name="grant_type")
    private String grant_type ;
    @Resource(name="appid")
    private String appid;
    @Resource(name="secret")
    private String secret;
    @Resource(name="websocketURL")
    private String websocketURL;

    /**
     * @Author yyj
     * @Description  获取wxchat 记录 在未接入客服前都会记录消息
     * @Date 2021-07-01
     * @Param
     * @return
     **/

    @ResponseBody
    @RequestMapping(value = "/wxchat/queryAll")
    public Object wxchat_queryAll(Page page, @RequestParam("limit") int limit,HttpServletRequest request){
        DataSourceContextHolder.setDbType("dataSourceA");
        ResultMap resultMap = new ResultMap();
        try {
            int totals = 0;
            page.setRows(limit);
            String keyword = request.getParameter("keyword");
            String msgType = request.getParameter("msgType");
            HashMap map = new HashMap();
            if(msgType==null||msgType.length()<=0||msgType.equals("不限")){
                msgType="text";
            }else{
                String event=msgType;
                map.put("event",event);
                msgType="event";
            }
            page.setKeyWord(keyword);
            map.put("msgType",msgType);
            page.setData(map);
            totals=mainService.selectPageCountWxchat(page);
            List<HashMap<String, Object>> list=mainService.selectPageListWxchat(page);
            page.setTotalRecord(totals);
            resultMap.setCode(0);
            resultMap.setMsg("");
            resultMap.setCount(totals);
            resultMap.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
        }
        return resultMap;
    }





    /**
     * @Author yyj
     * @Description
     * @Date 2021-06-28
     * @Param
     * @return
     **/
    @ResponseBody
    @RequestMapping(value = "/wxkefu/queryAll")
    public Object wxkefu_queryAll(Page page, @RequestParam("limit") int limit){
        ResultMap resultMap = new ResultMap();
        try {
            GetNewAccessToken getNewAccessToken = new GetNewAccessToken();
            String token =getNewAccessToken.GetToken("经纬捷讯小程序");
            String url = WxApi.getKfListUrl.replace("ACCESS_TOKEN",token);
            JsonNode jsonObject = JsonUtils.stringToJsonNode(HttpRequest.getwx(url));
            JsonNode kf_list = jsonObject.get("kf_list");
            resultMap.setCode(0);
            resultMap.setData(kf_list);
            resultMap.setCount(kf_list.size());
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
        }
        return resultMap;
    }

    /**
     * @Author yyj
     * @Description
     * @Date 2021-06-28
     * @Param 修改微信小程序客服的昵称
     * @return
     **/
    @ResponseBody
    @RequestMapping(value = "/wxkefu/edit")
    public Object wxkefu_edit(@RequestBody String jsonData){
        DataSourceContextHolder.setDbType("dataSourceA");
        ResultMap resultMap = new ResultMap();
        try {
            GetNewAccessToken getNewAccessToken = new GetNewAccessToken();
            String token =getNewAccessToken.GetToken("经纬捷讯小程序");
            String url = WxApi.kfinfoUpdate.replace("ACCESS_TOKEN",token);
            JsonObject jb = new JsonObject();
            JSONObject json = JSONObject.parseObject(jsonData);
            String nickname=json.getString("nickname");
            String kf_account=json.getString("kf_account");
            jb.addProperty("kf_account",kf_account);
            jb.addProperty("nickname",nickname);
            JsonNode jsonObject = JsonUtils.stringToJsonNode(HttpRequest.postwx(url,jb.toString()));
            int errCode = jsonObject.get("errCode").asInt();
            String errMsg = jsonObject.get("errMsg").textValue();
            resultMap.setCode(errCode);
            resultMap.setMsg(errMsg);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
        }
        return resultMap;
    }


    public static List<EmpInfo> sortList(List<EmpInfo> channelSituationList) {
        MyComparator mc = new MyComparator();
        int len = channelSituationList.size();
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                if (mc.compare(channelSituationList.get(i), channelSituationList.get(j)) == 1) {
                    Collections.swap(channelSituationList, i, j);
                }
            }
        }
        return channelSituationList;
    }
    @ResponseBody
    @RequestMapping(value = "/mobile/loadHistoryMsgtest")
    public Object loadHistoryMsgtest(@RequestBody String jsonData){
        DataSourceContextHolder.setDbType("dataSourceA");
        ResultMap resultMap = new ResultMap();
        try {

            HashMap map = new HashMap();
            JSONObject json = JSONObject.parseObject(jsonData);
            //用户id
            int totals = 0;
            Page page = new Page();
            String mid = json.getString("mid");
            String toid = json.getString("toid");
            int limit = 20;
            int curr = json.getInteger("curr");

            page.setRows(limit);
            map.put("fromId",mid);
            map.put("toId",toid);
            page.setData(map);
            page.setPage(curr);
            page.setStart(page.getStart());
            //收集当前用户与所有和此用户产生过记录的用户 的聊天记录
            List<EmpInfo> chatInfos = kfService.queryAllHistoryChattest(page);
            chatInfos.sort(Comparator.comparing(EmpInfo::getTimestamp));

            totals = kfService.selectHistoryCount(page);
            page.setTotalRecord(totals);
            resultMap.setData(chatInfos);
            resultMap.setCode(0);
            resultMap.setMsg("");
            resultMap.setCount(totals);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
        }
        return resultMap;
    }
    @ResponseBody
    @RequestMapping(value = "/mobile/loadHistoryMsg")
    public Object MobileloadHistoryMsg(@RequestBody String jsonData){
        DataSourceContextHolder.setDbType("dataSourceA");
        ResponseModel responseModel = new ResponseModel();
        try {
            HashMap map = new HashMap();
            JSONObject json = JSONObject.parseObject(jsonData);
            //用户id
            String mid = json.getString("mid");
            map.put("fromId",mid);
            //收集当前用户与所有和此用户产生过记录的用户 的聊天记录
            List<EmpInfo> chatInfos = kfService.queryAllHistoryChat(map);
            HashMap<String, Object> data =new HashMap<>();
//            List<EmpInfo> chatInfos2 = kfService.queryAllHistoryChatTO(map);
            List<EmpInfo> chatWindows = kfService.queryAllHistoryChatWindows(map);

            data.put("chatWindows",chatWindows);

//            for(int i = 0;i<chatInfos.size();i++){
//                EmpInfo tmp = chatInfos.get(i);
//                chatInfos2.add(tmp);
//            }

            List<EmpInfo> datas = sortList(chatInfos);
            data.put("historyData",datas);
            responseModel.setData(data);

            responseModel.setCode(0);
            responseModel.setMsg("");
        } catch (Exception e) {
            e.printStackTrace();
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }


    @RequestMapping("/auth_response")
    public String getOAuth(HttpSession session, RedirectAttributes redirectAttributes, HttpServletRequest request, Model model) {
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        DataSourceContextHolder.setDbType("dataSourceA");

        try {
            /**
             * 构造请求链接
             * https://api.weixin.qq.com/sns/oauth2/access_token?
             * appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
             */
            String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+secret+"&code=" + code + "&grant_type=authorization_code";
            JsonNode jsonObject = JsonUtils.stringToJsonNode(HttpRequest.getwx(url));
            String openid = jsonObject.get("openid").toString();
            String access_token = jsonObject.get("access_token").toString();
            String refresh_token = jsonObject.get("refresh_token").toString();
//            String chickUrl = "https://api.weixin.qq.com/sns/auth?access_token=" + access_token + "&openid=" + openid;
//            JsonNode chickuserInfo = JsonUtils.stringToJsonNode(HttpRequest.getwx(chickUrl));
//            if (!"0".equals(chickuserInfo.get("errcode").toString())) {
//                String refreshTokenUrl = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + openid + "&grant_type=refresh_token&refresh_token=" + refresh_token;
//                JsonNode refreshInfo = JsonUtils.stringToJsonNode(HttpRequest.getwx(refreshTokenUrl));
//                access_token = refreshInfo.get("access_token").toString();
//            }
            //获取用户拿到openid 和access_token去获取用户信息，在页面中进行业务处理，获取存储在数据库中:
            //第四步(获取用户接口)
            String infoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" +
                    access_token + "&openid=" + openid + "&lang=zh_CN";
            infoUrl = infoUrl.replaceAll("\"", "");
            String result =HttpRequest.getwx(infoUrl);
            UserInfo userInfo = JsonUtils.jsonToPojo(result,UserInfo.class);
            String gender = String.valueOf(userInfo.getSex());
            if(gender.equals("1")){
                gender="男";
            }else if(gender.equals("2")){
                gender="女";
            }else{
                gender="保密";
            }
            String uopenID = userInfo.getOpenid();
            String mid ="";
            HashMap param = new HashMap();
            param.put("openid",uopenID);
            param.put("status",2);
            param.put("username",userInfo.getNickname());
            Member member = memberService.queryUserisAdmin(param);
            //不是管理员  不同角色进入的场景不一样
            if(member==null){
                UserInfo visitor = memberService.queryUserInfoByParam(param);
                mid= RandomID.genID();
                userInfo.setMid(Integer.parseInt(mid));
                if(visitor==null){
                    memberService.insertUserInfoWhenSubscribe(userInfo);
                }else{
                    if( visitor.getMid()==-1){
                        memberService.updateUserInfoWhenSubscribe(userInfo);
                    }else{
                        mid= String.valueOf(visitor.getMid());
                    }
                }
                model.addAttribute("openid",uopenID);
                model.addAttribute("name",userInfo.getNickname());
                model.addAttribute("mid",mid);
                model.addAttribute("avatar",userInfo.getHeadimgurl());
                return "chat/chat";
            }else{
                Member member1 = new Member();
                member1.setId(member.getId());
                member.setOpenid(uopenID);
                member.setAvatar(userInfo.getHeadimgurl());
                member.setUsername(userInfo.getNickname());
                String ption = userInfo.getCountry()+","+userInfo.getProvince()+","+userInfo.getCity();
                member.setLoginPosition(ption);
                HashMap map = JsonUtils.objectToMap(member);
                memberService.updateWorker(map);
                mid = member.getDutyNumber();
                model.addAttribute("openid",uopenID);
                model.addAttribute("mid",mid);
                model.addAttribute("name",userInfo.getNickname());
                return "chat/chatKefu";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error/500";
    }

    @RequestMapping(value = "/mobile/chat")
    public String mobilechat(Model model,@ModelAttribute("map") HashMap  map){

        model.addAttribute("openid",map.get("openid"));
        model.addAttribute("name",map.get("nickname"));
        model.addAttribute("mid",map.get("mid"));
        model.addAttribute("company",map.get("company"));
        model.addAttribute("userType",map.get("userType"));
        model.addAttribute("avatar",map.get("avatar"));
        return "chat/chat";
    }





    /**
     *
     * name/"+param.name
     *             +"/openid/"+param.openid+"/mid/"+param.mid+"/userCompany/"+userCompany
     *             +"/userType/"+userType+"/avatar/"+avatar
     * **/
    @RequestMapping(value = "/mobile/kehu/index/name/{name}/openid/{openid}/userCompany/{userCompany}/userType/{userType}/appname/{appname}")
    public String mobilKehu(@PathVariable("name") String nickname,
                            @PathVariable("openid") String openid,
                            @PathVariable("userCompany") String company,
                            @PathVariable("userType") String userType,
                            @PathVariable("appname") String appname,
                            RedirectAttributes attributes){
        DataSourceContextHolder.setDbType("dataSourceA");

        HashMap param = new HashMap();
        param.put("appname",appname);
        param.put("userCompany",company);
        param.put("userType",userType);
        HashMap<String, Object> map = memberService.queryVisitor(param);
        HashMap param2 = new HashMap();
        param2.put("openid",openid);
        param2.put("nickname",nickname);
        param2.put("userType",userType);
        param2.put("mid",map.get("id"));
        param2.put("company",company);
        param2.put("avatar",map.get("headImgurl"));
        attributes.addFlashAttribute("map",param2);

        return "redirect:/kefu/mobile/chat";//必须重定向，否则不能成功
    }




    @ResponseBody
    @RequestMapping(value = "/transfer/getMessage")
    public Object transferGetMessage(@RequestBody String jsonData){
        DataSourceContextHolder.setDbType("dataSourceA");
        ResponseModel responseModel = new ResponseModel();
        try {
            HashMap map = new HashMap();
            JSONObject json = JSONObject.parseObject(jsonData);
            //需要转发id
            String transferID = json.getString("transferID");
            //收集客户发言
            map.put("transferID",transferID);
            List<HashMap<String, Object>> chatInfos = kfService.queryAllTransferChats(map);
            for(HashMap<String, Object> obj:chatInfos){
                int id =  Integer.parseInt(obj.get("id").toString());
                kfService.updateTranferStatus(id);
            }
            HashMap data = new HashMap();
            if(chatInfos.size()!=0){
                //存储转发用户信息
                String ismineId=chatInfos.get(0).get("ismineId").toString();
                map = new HashMap();
                map.put("mid",ismineId);
                HashMap<String, Object> visitorInfo = memberService.queryUserVisitor(map);
                data.put("visitorInfo",visitorInfo);
                responseModel.setCode(0);
            }else {
                responseModel.setCode(404);
            }
            data.put("chatInfos",chatInfos);
            responseModel.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }

    /**
     * 获取客个人信息
     * **/
    @ResponseBody
    @RequestMapping(value = "/queryUserInfo")
    public Object queryUserInfo(@RequestBody String jsonData){
        DataSourceContextHolder.setDbType("dataSourceA");
        ResponseModel responseModel = new ResponseModel();
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            String kuhuID = json.getString("id");
            HashMap param = new HashMap();
            param.put("id",kuhuID);
            String data="";
            List<HashMap<String, Object>> map = memberService.queryUserInfo(param);
            if(map.size()==0){
                Member paramMember = new Member();
                paramMember.setDutyNumber(kuhuID);
                Member member = memberService.queryUserLogin(paramMember);
                if(member!=null){
                    data=member.getUsername();
                }else{
                    data="未注册用户";
                }
            }else{
                for(HashMap map1 : map){
                    data = map1.get("userCompany").toString()+"("+map1.get("userType").toString()+")";
                    break;
                }
            }
            responseModel.setData(data);
            responseModel.setCode(0);
        } catch (Exception e) {
            e.printStackTrace();
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }
    /**
     * 获取所有客服列表
     * **/
    @ResponseBody
    @RequestMapping(value = "/transfer/toAnother")
    public Object transferKefus(@RequestBody String jsonData){
        DataSourceContextHolder.setDbType("dataSourceA");
        ResponseModel responseModel = new ResponseModel();
        try {
            HashMap map = new HashMap();
            JSONObject json = JSONObject.parseObject(jsonData);
            //客户id
            String kuhuID = json.getString("kuhuID");
            //需要转发id
            String transferID = json.getString("transferID");
            if(transferID==""||transferID==null){
                responseModel.setCode(404);
                responseModel.setMsg("未选择转发用户");
                return  responseModel;
            }
            //客服id
            String kefuID = json.getString("kefuID");
            map.put("fromId",kuhuID);
            map.put("toId",kefuID);
            //收集客户发言
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("transferId",transferID);
            List<EmpInfo> chatInfos = kfService.queryAllChats(map);
            String ismineId="";
            for (EmpInfo empInfo :chatInfos){
                Date timestamp = new Date();
                kfService.updateTranfer(empInfo);
                hashMap.put("cid",empInfo.getId());
                hashMap.put("create_time",timestamp);
                ismineId = empInfo.getFromId();
                hashMap.put("ismineId",ismineId);
                kfService.insertTransferChat(hashMap);
            }
            map = new HashMap();
            map.put("fromId",kefuID);
            map.put("toId",kuhuID);
            //收集客服发言
            List<EmpInfo> chatInfos2 = kfService.queryAllChats(map);
            for (EmpInfo empInfo :chatInfos2){
                Date timestamp = new Date();
                hashMap.put("cid",empInfo.getId());
                hashMap.put("create_time",timestamp);
                kfService.updateTranfer(empInfo);
                hashMap.put("ismineId",ismineId);
                kfService.insertTransferChat(hashMap);
            }
            responseModel.setCode(0);
            responseModel.setMsg("转发成功");
        } catch (Exception e) {
            e.printStackTrace();
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }

    @RequestMapping(value = "/test/index")
    public String test(){

        return "chat/test";
    }


    @RequestMapping(value = "/mobile/index")
    public String mobileIndex(){
        String redUrl=websocketURL+"kefu/auth_response";
        redUrl = URLEncoder.encode(redUrl);
        String url="https://open.weixin.qq.com/connect/oauth2/authorize?" +
                "appid="+appid+"&redirect_uri="+redUrl+"&response_type=code" +
                "&scope=snsapi_userinfo&state=STATE#wechat_redirect";
        return "redirect:"+url;//必须重定向，否则不能成功
    }

    @ResponseBody
    @PostMapping(value = "/getMessage/unread")
    public Object getMessageUnread(@RequestBody String jsonData) {
        DataSourceContextHolder.setDbType("dataSourceA");
        ResponseModel responseModel = new ResponseModel();
        HashMap map = new HashMap();
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            String nickname = json.getString("nickname");
            String toId = json.getString("toId");
            HashMap hashMap= new HashMap();
            if (nickname != null) {
                hashMap.put("toUsername",nickname);
                List<EmpInfo> empInfoList = kfService.queryChatList2(hashMap);
                kfService.updateChatStatus(nickname);
                map.put("data",empInfoList);
                responseModel.setCode(0);
                responseModel.setMsg("");
                responseModel.setData(map);
            } else if(toId!=null){
                hashMap.put("toId",toId);

                List<EmpInfo> empInfoList = kfService.queryChatList2(hashMap);
                kfService.updateChatStatus2(toId);
                map.put("data",empInfoList);
                responseModel.setCode(0);
                responseModel.setMsg("");
                responseModel.setData(map);
            } else {
                responseModel.setCode(0);
                responseModel.setMsg("暂无此用户");
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }

    @ResponseBody
    @PostMapping(value = "/mobile/getMemberList")
    public Object getListMobile(@RequestBody String jsonData){
        DataSourceContextHolder.setDbType("dataSourceA");
        ResponseModel responseModel = new ResponseModel();
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            String openid = json.getString("openid");
            String loginName = json.getString("name");
            Member member = new Member();
            member.setUsername(loginName);
            member.setOpenid(openid);
            Member user =  memberService.queryUserLogin(member);
            HashMap map = new HashMap();
            if(user!=null){
                String tabName = "webSocket";
                NoneMain noneMain = mainService.queryBytabName(tabName);
                String dutyNumber = user.getDutyNumber();
                HashMap mine = memberService.queryMineByid(dutyNumber);
                List<HashMap<String, Object>> friendList = memberService.queryKfList2(dutyNumber);
                List friendArry = new ArrayList();
                HashMap friend = new HashMap();
                friend.put("groupname","好友列表");
                friend.put("id",1);
                friend.put("list",friendList);
                List<HashMap<String, Object>> friendList2 = memberService.queryKfListkehu();
                HashMap kehu = new HashMap();
                kehu.put("groupname","客户列表");
                kehu.put("id",2);
                kehu.put("list",friendList2);


                friendArry.add(kehu);
                friendArry.add(friend);
                map.put("mine",mine);
                map.put("friend",friendArry);
                if(noneMain!=null){
                    responseModel.setCode(0);
                    responseModel.setMsg("用户列表获取成功");
                    String url = noneMain.getStrvalue()+"?myid="+user.getDutyNumber();
                    map.put("url",url);
                    map.put("user",user);
                    responseModel.setData(map);
                }else{
                    responseModel.setCode(0);
                    responseModel.setMsg("暂无连接信息，请查看后台");
                }
            }else{
                responseModel.setCode(0);
                responseModel.setMsg("账户或密码错误，请重新登陆");
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }

    /**
     * 游客
     **/
    @ResponseBody
    @PostMapping(value = "/connect/websocket/visitor")
    public Object visitorLogin() {
        DataSourceContextHolder.setDbType("dataSourceA");
        ResponseModel responseModel = new ResponseModel();
        try {
            String tabName = "webSocket";
            NoneMain noneMain = mainService.queryBytabName(tabName);
            if (noneMain != null) {
                responseModel.setCode(0);
                responseModel.setMsg("通信连接成功");
                HashMap map = new HashMap();
                String url = noneMain.getStrvalue();
                map.put("url", url);
                responseModel.setData(map);
            } else {
                responseModel.setCode(501);
                responseModel.setMsg("账户或密码错误，请重新登陆");
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }
    /**
     * 获取当天值班的客服，如果没有则获取默认客服
     * */
    @ResponseBody
    @PostMapping(value = "/duty/getKefu/nowDay")
    public Object dutyKeFu() {
        DataSourceContextHolder.setDbType("dataSourceA");
        ResponseModel responseModel = new ResponseModel();
        try {
            List<HashMap<String, Object>> kufuList = memberService.queryKfListnowDay();
            if (kufuList.size() > 0) {
                responseModel.setCode(0);
                responseModel.setMsg("");
                responseModel.setData(kufuList);
            } else {
                List<HashMap<String, Object>> kufudefault = memberService.queryKfListDefault();
                responseModel.setCode(0);
                responseModel.setMsg("");
                responseModel.setData(kufudefault);
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }


    /**
     * 无论是谁，都要登陆才能连接通信
     * */
    @ResponseBody
    @PostMapping(value = "/connect/websocket/login")
    public Object websockeLogin(@RequestBody String jsonData){
        DataSourceContextHolder.setDbType("dataSourceA");
        ResponseModel responseModel = new ResponseModel();
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            String pass = json.getString("password");
            String loginName = json.getString("name");
            Member member = new Member();
            member.setUsername(loginName);
            member.setPassword(pass);
            Member user =  memberService.queryUserLogin(member);
            if(user!=null){
                String tabName = "webSocket";
                NoneMain noneMain = mainService.queryBytabName(tabName);
                if(noneMain!=null){
                    responseModel.setCode(0);
                    responseModel.setMsg("");
                    HashMap map = new HashMap();
                    String url = noneMain.getStrvalue()+"?myid="+user.getDutyNumber();
                    map.put("url",url);
                    map.put("user",user);
                    responseModel.setData(map);
                }else{
                    responseModel.setCode(404);
                    responseModel.setMsg("暂无连接信息，请查看后台");
                }
            }else{
                responseModel.setCode(501);
                responseModel.setMsg("账户或密码错误，请重新登陆");
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }





    @ResponseBody
    @GetMapping(value = "/getList")
    public Object getList(){
        DataSourceContextHolder.setDbType("dataSourceA");
        ResponseModel responseModel = new ResponseModel();
        try {
            responseModel.setCode(0);
            responseModel.setMsg("");
            HashMap mine = new HashMap();
            mine.put("username","访客");
            mine.put("id","100000123");
            mine.put("status","online");
            mine.put("remark","");
            mine.put("avatar","/resources/img/fke.png");
            List<HashMap<String, Object>> friendList = memberService.queryKfList();
            List friendArry = new ArrayList();
            HashMap friend = new HashMap();
            friend.put("groupname","好友列表");
            friend.put("id",1);
            friend.put("list",friendList);



            friendArry.add(friend);
            HashMap data = new HashMap();
            data.put("mine",mine);
            data.put("friend",friendArry);
            responseModel.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }

        return responseModel;
    }




    @ResponseBody
    @GetMapping(value = "/getMemberList")
    public Object getMemberList(){
        ResponseModel responseModel = new ResponseModel();
        try {
            responseModel.setCode(0);
            responseModel.setMsg("");
            String dutyNumber = "100988";
            HashMap mine = memberService.queryMineByid(dutyNumber);
            List<HashMap<String, Object>> friendList = memberService.queryKfList2(dutyNumber);

            List friendArry = new ArrayList();
            HashMap friend = new HashMap();
            friend.put("groupname","好友列表");
            friend.put("id",1);
            friend.put("list",friendList);
            friendArry.add(friend);
            HashMap data = new HashMap();
            data.put("mine",mine);
            data.put("friend",friendArry);
            responseModel.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }


        return responseModel;
    }

}
