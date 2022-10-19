package com.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import com.web.model.*;
import com.web.model.Wx.UserInfo;
import com.web.service.*;
import com.web.utils.*;
import com.web.utils.database.DataSourceContextHolder;
import com.web.utils.wx.GetNewAccessToken;
import com.web.utils.wx.WxApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.json.JsonArray;
import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;
import java.lang.reflect.Method;
import java.util.*;

import static java.lang.Integer.parseInt;
import static org.apache.commons.lang.StringUtils.isNumeric;
import static org.apache.commons.lang.StringUtils.replaceEachRepeatedly;

/***
 *
 * app content 页面数据交互
 *
 * */
@Controller
@RequestMapping("/api")
public class ApiController {

    @Autowired
    MemberService memberService;
    @Autowired
    RoleService roleService;
    @Autowired
    ArticleService articleService;
    @Autowired
    TagsService tagsService;
//    @Resource(name="originalID")
    private String originalID="gh_1ddeda6c87d0";  // ="http://119.23.237.197:8888/file/upload";
    @Resource(name="Delcode")
    private String Delcode ;
    TimeParse timeParse = new TimeParse();
    HTMLSpirit htmlSpirit = new HTMLSpirit();
    ResultMap resultMap = new ResultMap();
    AJAXResult result = new AJAXResult();

    /**
     * 根据id删除
     * **/
    @ResponseBody
    @RequestMapping(value = "/web/user/visitor/deleteByid", produces = "application/json; charset=utf-8")
    public Object deleteByid(@RequestBody Integer id){
        try {
            DataSourceContextHolder.setDbType("dataSourceA");
            HashMap map = new HashMap();
            map.put("id",id);
            memberService.deleteByid(map);
            resultMap.setCode(0);
            resultMap.setMsg("ok");
        }catch (Exception e){
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
        }
        return resultMap;
    }
    /**
     * 根据id删除
     * **/
    @ResponseBody
    @RequestMapping(value = "/web/user/visitor/deleteByids", produces = "application/json; charset=utf-8")
    public Object deleteByids(HttpServletRequest request){
        try {
            DataSourceContextHolder.setDbType("dataSourceA");
            String ids = request.getParameter("id");
            String[] idsArry = ids.split(",");
            HashMap map = new HashMap();
            for(int i =0;i<idsArry.length;i++){
                int id = Integer.parseInt(idsArry[i]);
                map.put("id",id);
                memberService.deleteByid(map);
            }
            resultMap.setCode(0);
            resultMap.setMsg("ok");
        }catch (Exception e){
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
        }
        return resultMap;
    }


    @ResponseBody
    @RequestMapping(value = "/web/user/visitor/queryUserList", produces = "application/json; charset=utf-8")
    public Object querySmsVisitor(Page page, @RequestParam("limit") int limit,
                                  HttpServletRequest request){
        DataSourceContextHolder.setDbType("dataSourceA");
        ResultMap resultMap = new ResultMap();
        try {
            int totals = 0;
            page.setRows(limit);
            String keyword = request.getParameter("keyword");
            if(keyword==null||keyword.length()<=0){
                keyword=null;
            }
            page.setKeyWord(keyword);
            totals=memberService.selectPageCountVisitor(page);
            List<HashMap<String, Object>> list=memberService.selectPageListVisitor(page);
            page.setTotalRecord(totals);
            resultMap.setCode(0);
            resultMap.setMsg("");
            resultMap.setData(list);
            resultMap.setCount(totals);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
        }
        return resultMap;
    }

    /**
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/user/kefu/queryUserList", produces = "application/json; charset=utf-8")
    public Object queryKefuList(@RequestBody Integer id){
        DataSourceContextHolder.setDbType("dataSourceA");
        try {
            HashMap map = new HashMap();
            map.put("status",2);
            map.put("tid",id);
            DataSourceContextHolder.setDbType("dataSourceA");
            //查询没有此模版的客服
            List<Member> list2 = memberService.queryMemberWithoutTemplate(map);
            List<Member> list3 = memberService.queryMemberWithTemplate(map);
            List list = new ArrayList();
            for(Member member:list2){
                map = new HashMap();
                map.put("disabled","");
                map.put("checked","");
                map.put("value",member.getId());
                map.put("title",member.getUsername());
                list.add(map);
            }
            for(Member member:list3){
                map = new HashMap();
                map.put("disabled","");
                map.put("checked","checked");
                map.put("value",member.getId());
                map.put("title",member.getUsername());
                list.add(map);
            }
            result.setErrcode(0);
            result.setErrmsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            result.setErrcode(500);
            result.setErrmsg(e.getMessage());
        }
        return result;
    }



    //修改状态
    private String updateStatusWXAccount(String id,int status,String kf_account){
        String res = "";
        try {
            HashMap member = new HashMap();
            member.put("id",Integer.parseInt(id));
            member.put("status",status);
            member.put("kf_account",kf_account);
            memberService.updateWorker(member);
            res="ok";
        } catch (NumberFormatException e) {
            e.printStackTrace();
            res=e.getMessage();
        }
        return res;
    }


    //取消绑定客服
    @ResponseBody
    @PostMapping(value = "/user/duty/cancel_bindingWX", produces = "application/json; charset=utf-8")
    public Object cancel_binding(@RequestBody String jsonData) {
        try {
            GetNewAccessToken getNewAccessToken = new GetNewAccessToken();

            JSONObject json = JSONObject.parseObject(jsonData);
            String KFACCOUNT = json.getString("dutyNumber");
            KFACCOUNT = "kf"+KFACCOUNT + "@" + originalID;

            //TODO
            String Token =  getNewAccessToken.GetToken("经纬捷讯小程序");
            String url = WxApi.delKfUrl.replace("ACCESS_TOKEN",Token).replace("KFACCOUNT",KFACCOUNT);

            DataSourceContextHolder.setDbType("dataSourceA");
            JsonNode jsonObject = JsonUtils.stringToJsonNode(HttpRequest.getwx(url));
            int errcode = Integer.parseInt(jsonObject.get("errcode").toString());
            String errmsg = jsonObject.get("errmsg").toString();
            result.setErrcode(errcode);
            String msg = "";
            if(errcode==0){
                String id = json.getString("id");
                int status =0;
                String res = updateStatusWXAccount(id,status,"");
                if(!res.equals("ok")){
                    msg = res;
                    result.setErrcode(500);
                }else{
                    msg = "取消客服帐号成功";
                    sendWxmsg("cancelbinding",id);

                }
            }else{
                if(errcode==65400){
                    msg="API不可用，即没有开通/升级到新客服功能";
                }else if(errcode==65401){
                    msg="无效客服帐号";
                }
            }
            result.setErrmsg(msg);
        } catch (Exception e) {
            e.printStackTrace();
            result.setErrcode(500);
            result.setErrmsg(e.getMessage());
        }
        return result;
    }

    @Autowired
    NoneMainService mainService;
    private ResultMap sendWxmsg(String type,String id){
        Member member = new Member();
        member.setId(Integer.parseInt(id));
        ResultMap resultMap = new ResultMap();
        resultMap.setMsg("ok");
        resultMap.setCode(0);
        try {
            Member user = memberService.queryUserLogin(member);

            if (user != null) {
                List data= new ArrayList();
                String first="";
                String keyword1="";
                String keyword2=timeParse.convertDate2String(new Date(),"yyyy-MM-dd HH:mm:ss");
                String remark="";
                if(type.equals("binding")){
                    first="申请添加为小程序客服";
                    keyword1=user.getUsername()+"已被授权添加为小程序客服，请微信搜索【客服小助手】进行登陆，实时获取客户消息";
                    remark="微信公众号及时推送最新客户消息，第一个接入的客服，其他客服不会提醒此客户消息，如果需要转接，请登陆微信客服平台";
                }else{
                    first="取消申请小程序客服";
                    keyword1=user.getUsername()+"不再接收客户消息";
                    remark="微信公众号不在推送客户访问消息";
                }
                data.add(keyword1);
                data.add(keyword2);
                GetNewAccessToken getNewAccessToken = new GetNewAccessToken();
                String accessToken = getNewAccessToken.GetToken("经纬捷讯");
                String urlToken = "";
                urlToken = WxApi.templateSendMs.replace("ACCESS_TOKEN", accessToken);
                HashMap param = new HashMap();
                param.put("title", "受理成功通知");
                DataSourceContextHolder.setDbType("dataSourceA");
                HashMap template = mainService.queryTemplate(param);
                String memo = template.get("memo").toString();
                String[] list1= memo.split("#");
                JsonObject keyword = new JsonObject();
                JsonObject datahead = new JsonObject();
                datahead.addProperty("value",first);
                keyword.add("first",datahead);
                String[] list2= list1[1].split("@");
                for(int i=1;i<=list2.length-1;i++){
                    datahead = new JsonObject();
                    datahead.addProperty("value",data.get(i-1).toString());
                    keyword.add("keyword"+i,datahead);
                }
                datahead = new JsonObject();
                datahead.addProperty("value",remark);
                keyword.add("remark",datahead);

                JsonObject jb = new JsonObject();
                jb.add("data", keyword);
                String tid = template.get("templateID").toString();
                jb.addProperty("template_id", tid);
                param = new HashMap();
                param.put("tid", template.get("id"));
                List<HashMap> listUser = memberService.queryKfViewTemUser(param);
                for(int i=0;i<listUser.size();i++){
                    String touser = listUser.get(i).get("openid").toString();
                    jb.addProperty("touser", touser);
                    JsonNode jsonObject = JsonUtils.stringToJsonNode(HttpRequest.postwx(urlToken, jb.toString()));
                    int errcode = 0;
                    String errmsg = jsonObject.get("errmsg").asText();
                    if (jsonObject.has("errcode")) {
                        if (errcode != 0) {
                            resultMap.setMsg(errmsg);
                            resultMap.setCode(errcode);
                            return resultMap;
                        }
                    }
                }
            }else{
                resultMap.setMsg("无法推送消息，没有此人");
                resultMap.setCode(404);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.setMsg(e.getMessage());
            resultMap.setCode(500);
        }
        return  resultMap ;
    }
    //发送邀请
    @ResponseBody
    @PostMapping(value = "/user/duty/invited_bindingWX", produces = "application/json; charset=utf-8")
    public Object invited_bindingWX(@RequestBody String jsonData) {
        try {
            GetNewAccessToken getNewAccessToken = new GetNewAccessToken();

            JSONObject json = JSONObject.parseObject(jsonData);
            String dutyNumber = json.getString("dutyNumber");
            String invite_wx = json.getString("invite_wx");
            String Token =  getNewAccessToken.GetToken("经纬捷讯");
            String url = "https://api.weixin.qq.com/customservice/kfaccount/inviteworker?access_token=" +Token;
            DataSourceContextHolder.setDbType("dataSourceA");

            HashMap<String, Object> mapwx = new HashMap<>();
            String wxAccount = dutyNumber + "@" + originalID;
            mapwx.put("kf_account", wxAccount);
            mapwx.put("invite_wx", invite_wx);
            String data = JsonUtils.objectToJson(mapwx);
            String jsonWx = HttpRequest.postwx(url, data);
            JsonNode jsonObject = JsonUtils.stringToJsonNode(jsonWx);
            int errcode = Integer.parseInt(jsonObject.get("errcode").toString());
            String errmsg = jsonObject.get("errmsg").toString();
            result.setErrcode(errcode);
            result.setErrmsg(errmsg);
            if(errcode==0){
                String id = json.getString("id");
                int status =2;
                String res = updateStatusWXAccount(id,status,wxAccount);
                if(!res.equals("ok")){
                    result.setErrmsg(res);
                    result.setErrcode(500);
                }else{
                    result.setErrmsg("邀请客服成功");
                    ResultMap resultMap = sendWxmsg("binding",id);
                }
            }else{
                String msg = "";
                if(errcode==65409){
                    msg="无效的微信号";
                }else if(errcode==65401){
                    msg="无效客服帐号";
                }else if(errcode==65407){
                    msg="邀请对象已经是本公众号客服";
                }else if(errcode==65408){
                    msg="本公众号已发送邀请给该微信号";
                }else if(errcode==65410){
                    msg="邀请对象绑定公众号客服数量达到上限（目前每个微信号最多可以绑定5个公众号客服帐号）";
                }else if(errcode==65411){
                    msg="该帐号已经有一个等待确认的邀请，不能重复邀请";
                }else if(errcode==65412){
                    msg="该帐号已经绑定微信号，不能进行邀请";
                }
                result.setErrmsg(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setErrcode(500);
            result.setErrmsg(e.getMessage());
        }
        return result;
    }

    /**
     * @Author yyj
     * @Description 现在改为小程序客服，这种方式可以用手机查看客服消息
     * 此外，不能使用微信客服【添加客服帐号】的接口addKfUrl
     * 直接使用【邀请绑定客服帐号】接口
     * 与微信公众号客服不同！！！
     * @Date 2021-06-25
     * @Param
     * @return
     **/
    @ResponseBody
    @PostMapping(value = "/user/duty/bindingWX", produces = "application/json; charset=utf-8")
    public Object bindingWX(@RequestBody String jsonData) {
        try {
            GetNewAccessToken getNewAccessToken = new GetNewAccessToken();

            JSONObject json = JSONObject.parseObject(jsonData);
            String dutyNumber = json.getString("dutyNumber");
            String wxID = json.getString("wxID");
            String Token = getNewAccessToken.GetToken("经纬捷讯小程序");
            String url = WxApi.bbKfUrl.replace("ACCESS_TOKEN",Token);
            HashMap<String, Object> mapwx = new HashMap<>();
            DataSourceContextHolder.setDbType("dataSourceA");
            String wxAccount = "kf"+dutyNumber + "@" + originalID;
            mapwx.put("kf_account", wxAccount);
            mapwx.put("invite_wx", wxID);
            String data = JsonUtils.objectToJson(mapwx);
            String jsonWx = HttpRequest.postwx(url, data);
            JsonNode jsonObject = JsonUtils.stringToJsonNode(jsonWx);
            int errcode = Integer.parseInt(jsonObject.get("errcode").toString());
            String errmsg = jsonObject.get("errmsg").toString();
            result.setErrcode(errcode);
            result.setErrmsg(errmsg);
            if(errcode==0){
                String id = json.getString("id");
                int status =2;
                String res = updateStatusWXAccount(id,status,wxAccount);
                if(!res.equals("ok")){
                    result.setErrcode(500);
                }else{
                    result.setErrmsg("添加客服帐号成功");
                }
            }else{
                String msg = "";
                if(errcode==65400){
                    msg="API不可用，即没有开通/升级到新客服功能";
                }else if(errcode==65403){
                    msg="客服昵称不合法";
                }else if(errcode==65404){
                    msg="客服帐号不合法";
                }else if(errcode==65405){
                    msg="帐号数目已达到上限，不能继续添加";
                }else if(errcode==65406){
                    msg="已经存在的客服帐号";
                }
                result.setErrmsg(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setErrcode(500);
            result.setErrmsg(e.getMessage());
        }
        return result;
    }





    @ResponseBody
    @PostMapping(value = "/user/duty/deletes")
    public Object dutydeletes(@RequestBody String jsonData){
        DataSourceContextHolder.setDbType("dataSourceA");
        JSONObject json = JSONObject.parseObject(jsonData);
        String res = "";
        try {
            JSONArray ids =json.getJSONArray("ids");
            String code = json.getString("code");
            if(code.equals(Delcode)){
                for(int i =0;i<ids.size();i++){
                    int id = (int) ids.get(i);
                    memberService.deletDutyById(id);
                }
                result.setErrcode(0);
                res = "删除成功";
            }else{
                result.setErrcode(500);
                res = "指令错误，删除失败";
            }
            result.setData("");
            result.setErrmsg(res);
        }catch (Exception e){
            e.printStackTrace();
            result.setErrcode(500);
            result.setErrmsg(e.getMessage());
        }
        return result;
    }
    @ResponseBody
    @RequestMapping(value = "/user/duty/delete", produces = "application/json; charset=utf-8")
    public Object deleteDutyUser(@RequestBody String jsonData){
        DataSourceContextHolder.setDbType("dataSourceA");
        try {
            String res = "";
            JSONObject json = JSONObject.parseObject(jsonData);
            String id = json.getString("id");
            String code = json.getString("code");
            if(code.equals(Delcode)){
                memberService.deletDutyById(Integer.parseInt(id));
                res = "删除成功";
                result.setErrcode(0);
            }else{
                result.setErrcode(500);
                res = "指令错误，删除失败";
            }
            result.setData("");
            result.setErrmsg(res);
        } catch (Exception e) {
            e.printStackTrace();
            result.setErrcode(500);
            result.setErrmsg(e.getMessage());
        }
        return result;
    }
    @ResponseBody
    @RequestMapping(value = "/user/duty/addDutyUser", produces = "application/json; charset=utf-8")
    public Object addDutyUser(@RequestBody String jsonData){
        DataSourceContextHolder.setDbType("dataSourceA");
        GetNewAccessToken getNewAccessToken = new GetNewAccessToken();

        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            String dateTime = json.getString("dateTime");
            String dutyList = json.getString("dutyList");
            String[] dlist = dutyList.split(",");
            dateTime = dateTime.replaceAll("年","-");
            dateTime = dateTime.replaceAll("月","-");
            dateTime = dateTime.replaceAll("日","");

            int flag = 0;
            String msg="ok";
            for(int i = 0;i<dlist.length;i++){
                Member member = memberService.queryByID(Integer.parseInt(dlist[i]));
                if(member.getStatus()==0){
                    msg += "用户："+member.getUsername()+"未绑定客服";
                }else if(member.getStatus()==1){
                    msg += "用户："+member.getUsername()+"未通过邀请绑定客服";
                }else{
                    String wxAccount = member.getDutyNumber() + "@" + originalID;
                    HashMap map = new HashMap();
                    map.put("mid",dlist[i]);
                    map.put("dateTime",dateTime);
                    map.put("wxAccount",wxAccount);
                    memberService.insertDuty(map);
                    result.setErrcode(0);
                    flag=1;
                    String token = getNewAccessToken.GetToken("经纬捷讯");
                    JsonObject jb = new JsonObject();
                    JsonObject jb2 = new JsonObject();
                    jb.addProperty("touser",member.getOpenid());
                    jb.addProperty("msgtype","text");
                    String content=dateTime+" :由客服"+member.getUsername()+"值班";
                    jb2.addProperty("content",content);
                    jb.add("text",jb2);
                    String url="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+token;
                    String jsonObject = HttpRequest.postwx(url,jb.toString());
                }
            }
            if(flag==0){
                result.setErrcode(500);
            }
            if(msg.contains("用户")){
                msg.replaceAll("ok","");
                result.setErrcode(500);
            }
            result.setErrmsg(msg);
            result.setData("");
        } catch (Exception e) {
            e.printStackTrace();
            result.setErrcode(500);
            result.setErrmsg(e.getMessage());
        }
        return result;
    }

    //值班系统人员列表
    @ResponseBody
    @RequestMapping(value = "/user/duty/queryUserList", produces = "application/json; charset=utf-8")
    public Object queryUserList(){
        DataSourceContextHolder.setDbType("dataSourceA");
        try {
            List<Member> memberList = memberService.queryAllMembers();
            List list = new ArrayList();
            for(Member member:memberList){
                HashMap map = new HashMap();
                map.put("disabled","");
                map.put("checked","");
                map.put("value",member.getId());
                map.put("title",member.getUsername());
                list.add(map);
            }

            result.setErrcode(0);
            result.setErrmsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            result.setErrcode(500);
            result.setErrmsg(e.getMessage());
        }
        return result;
    }


    //值班系统
    @ResponseBody
    @RequestMapping(value = "/duty/queryAll", produces = "application/json; charset=utf-8")
    public Object duty_queryAll(Page page, @RequestParam("limit") int limit
            ,HttpServletRequest request) {
        DataSourceContextHolder.setDbType("dataSourceA");

        try {
            List<HashMap<String, Object>> dutyList = new ArrayList<>();
            HashMap<String, Object> map = new HashMap();
            int totals = 0;
            page.setRows(limit);
            String username = request.getParameter("username");
            String gender = request.getParameter("gender");
            String email = request.getParameter("email");
            String dutyNumber = request.getParameter("dutyNumber");
            if(username==null||username.length()<=0){
                username=null;
            }
            if(gender==null||gender.length()<=0){
                gender=null;
            }else{
                if(gender.equals("0")){
                    gender="女";
                }else if(gender.equals("1")){
                    gender="男";
                }else{
                    gender=null;
                }
            }
            if(email==null||email.length()<=0){
                email=null;
            }
            if(dutyNumber==null||dutyNumber.length()<=0){
                dutyNumber=null;
            }
            map.put("username",username);
            map.put("gender",gender);
            map.put("email",email);
            map.put("dutyNumber",dutyNumber);
            page.setData(map);
            totals=memberService.selectDutyCount(page);
            dutyList=memberService.selectDutyList(page);
//            if(dutyList.size()>0){
//                for(int i =0;i<dutyList.size();i++){
//                    Date d = (Date)(dutyList.get(i).get("dutyTime"));
//                    String date = timeParse.convertDate2String(d,"yyyy-MM-dd");
//                    dutyList.get(i).put("dutyTime",date);
//                }
//            }
            page.setTotalRecord(totals);
            resultMap.setCode(0);
            resultMap.setMsg("");
            resultMap.setData(dutyList);
            resultMap.setCount(totals);
        } catch (Exception e) {
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return resultMap;
    }


    @ResponseBody
    @RequestMapping(value = "/app/content/article/product/queryAll", produces = "application/json; charset=utf-8")
    public ResultMap<List<Article>> Article_queryAll(Page page, @RequestParam("limit") int limit
            ,HttpServletRequest request){
        DataSourceContextHolder.setDbType("dataSourceA");

        List<Article> articleList = new ArrayList<>();
        int totals = 0;
        try {
            page.setRows(limit);
            String id = request.getParameter("id");
            int Id=-1;
            String author = request.getParameter("author");
            String title = request.getParameter("title");
            String category = request.getParameter("articleType");

            Article articleParam = new Article();
            if(category=="-1"){
                category=null;
            }
            articleParam.setCategory(category);
            if(id==null||id.length()<=0){
                id=null;
            }else{
                if(isNumeric(id)){
                    Id = parseInt(id);
                }
            }
            articleParam.setId(Id);
            if(author==null||author.length()<=0){
                author=null;
            }
            if(title==null||title.length()<=0){
                title=null;
            }
            articleParam.setAuthor(author);
            articleParam.setTitle(title);
            page.setData(articleParam);

            totals=articleService.selectPageCount(page);
            articleList=articleService.selectPageList(page);
            for(Article article :articleList){
                article.setCreateTime(timeParse.getFullTime(article.getCreateTime()));
                article.setPublishTime(timeParse.getFullTime(article.getPublishTime()));
//                article.setContent(htmlSpirit.delHTMLTag(article.getContent()));
                    String DeTitle = htmlSpirit.delHTMLTag(article.getTitle()).substring(0,10)+"....";
                article.setTitle(DeTitle);
            }
            page.setTotalRecord(totals);
        }catch (Exception e){
            System.err.println(e);
        }
        System.out.println(new ResultMap<List<Article>>("",articleList,0,totals));
        return new ResultMap<List<Article>>("",articleList,0,totals);
    }





    @ResponseBody
    @RequestMapping(value = "/app/content/tags", produces = "application/json; charset=utf-8")
    public String TagsQurry(){
        DataSourceContextHolder.setDbType("dataSourceA");

        String result = "";
         try {
             List<HashMap<String, Object>> tagsList = tagsService.queryAll();

             String data = JsonUtils.listToJson(tagsList);
             int count = tagsList.size();
             result ="{  \"code\": 0" +
                     "  ,\"msg\": \"ok\"" +
                     "  ,\"count\": \""+count+"\"" +
                     "  ,\"data\":"+data+"}";



         }catch (Exception e){
             result ="{  \"code\": 500" +
                     "  ,\"msg\": \""+e.getMessage()+"\"}";
         }
        System.err.println(result);

         return result;
    }



    @ResponseBody
    @PostMapping(value = "/app/content/articleUpload")
    public Object articleUpload(@RequestBody String jsonData,
                                HttpServletRequest httpServletRequest){
        DataSourceContextHolder.setDbType("dataSourceA");

        JSONObject json = JSONObject.parseObject(jsonData);
        try {
            String content = json.getString("content");
            String detail = json.getString("detail");
            String author = json.getString("author");
            String shortTitle = json.getString("shortTitle");
            String publish_time = json.getString("date");
            String articleType = json.getString("articleType");
            String fatherTitle = json.getString("fatherTitle");
            int flag = json.getInteger("flag");
//            if(flag==0){
//                articleType="index";
//            }
            String cover_url = json.getString("cover_url");
            String createTime = timeParse.convertDate2String(new Date(),"yyyy-MM-dd HH:mm:ss");
            String url = "/view/product/id/";
            if(articleType=="news"){
                url = "/view/news/id/";
            }
            Article article = new Article();
            article.setPublishTime(publish_time);
            article.setCreateTime(createTime);
            article.setContent(content);
            article.setAuthor(author);
            article.setCategory(articleType);
            article.setTitle(shortTitle);
            article.setDetail(detail);
            article.setShortTitle(shortTitle);
            article.setStatus(1);
            article.setUrl(url);
            article.setCover_url(cover_url);
            article.setFlag(flag);
            article.setFatherTitle(fatherTitle);
            articleService.insertArticle(article);
            resultMap.setCode(0);
            resultMap.setMsg("ok");
        }catch (Exception e){
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
        }

        return resultMap;

    }

    @ResponseBody
    @PostMapping(value = "/app/content/articleDelete")
    public Object articleDelete(@RequestBody String jsonData){
        DataSourceContextHolder.setDbType("dataSourceA");
        JSONObject json = JSONObject.parseObject(jsonData);
        try {
            JSONArray ids =json.getJSONArray("ids");
            for(int i =0;i<ids.size();i++){
                int id = (int) ids.get(i);
                articleService.deleteArticleByid(id);
            }
            resultMap.setCode(0);
            resultMap.setMsg("ok");
        }catch (Exception e){
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
        }
        return resultMap;
    }

    /**
     * 修改文章 回显数据
     * /api/app/content/articleEdit/id/"+id
     * */
    @ResponseBody
    @RequestMapping("/app/content/articleEdit/id/{id}")
    public Object GotoEditArticle(@PathVariable("id") int id){
        DataSourceContextHolder.setDbType("dataSourceA");
        try {
            Article article = articleService.queryArticleByid(id);
//            String DeTitle = htmlSpirit.delHTMLTag(article.getTitle());
//            article.setTitle(DeTitle);
            resultMap.setCode(0);
            resultMap.setMsg("ok");
            resultMap.setData(article);
        }catch (Exception e){
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
        }
        return resultMap;
    }

    /**
     * 修改文章 上传数据
     * /api/app/content/articleUpdate/id/"+id
     * */
    @ResponseBody
    @PostMapping(value = "/app/content/articleUpdate")
    public Object articleUpdate(@RequestBody String jsonData,
                                HttpServletRequest httpServletRequest){
        DataSourceContextHolder.setDbType("dataSourceA");
        JSONObject json = JSONObject.parseObject(jsonData);
        try {
            String content = json.getString("content");
            int id = json.getInteger("id");
            String detail = json.getString("detail");
            String author = json.getString("author");
            String shortTitle = json.getString("shortTitle");
            String publish_time = json.getString("date");
            String cover_url = json.getString("cover_url");
            String articleType = json.getString("articleType");
            String fatherTitle = json.getString("fatherTitle");
            int flag = json.getInteger("flag");
            String createTime = timeParse.convertDate2String(new Date(),"yyyy-MM-dd HH:mm:ss");
            Article article = new Article();
            article.setPublishTime(publish_time);
            article.setCreateTime(createTime);   //再次更新编辑时间
            article.setContent(content);
            article.setAuthor(author);
            article.setCategory(articleType);
            article.setDetail(detail);
            article.setTitle(shortTitle);
            article.setShortTitle(shortTitle);
            article.setStatus(1);
            article.setCover_url(cover_url);
            article.setFlag(flag);
            article.setFatherTitle(fatherTitle);
            article.setId(id);
            articleService.updateArticle(article);
            resultMap.setCode(0);
            resultMap.setMsg("ok");
        }catch (Exception e){
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
        }

        return resultMap;

    }



/**
 * =============================================公司员工==============================================================
 * */

@ResponseBody
@RequestMapping(value = "/user/worker/newUser/edit", produces = "application/json; charset=utf-8")
public Object editUserWorker( @RequestBody String jsonData) {
    ResponseModel responseModel = new ResponseModel();
    DataSourceContextHolder.setDbType("dataSourceA");
    try {
        HashMap member = JsonUtils.jsonToPojo(jsonData,HashMap.class);
        if(member.containsKey("oldPassword")){
            String pass = member.get("password").toString();
            String oldPassword = member.get("oldPassword").toString();
            member.put("password",oldPassword);
            Member queryuser = memberService.authenticate(member);
            if(queryuser==null){
                responseModel.setCode(404);
                responseModel.setMsg("原密码输入不正确，请检查");
                return responseModel;
            }
            member.put("password",pass);
        }
        memberService.updateWorker(member);
        responseModel.setCode(0);
        responseModel.setMsg("用户信息更新成功，请重新登录");
    } catch (Exception e) {
        e.printStackTrace();
        responseModel.setCode(500);
        responseModel.setMsg(e.getMessage());
    }
    return responseModel;
}


    @ResponseBody
    @PostMapping(value = "/user/worker/newUser/deletes")
    public Object Wokerdeletes(@RequestBody String jsonData){
        DataSourceContextHolder.setDbType("dataSourceA");
        JSONObject json = JSONObject.parseObject(jsonData);
        String res = "";
        try {
            JSONArray ids =json.getJSONArray("ids");
            String code = json.getString("code");
            if(code.equals(Delcode)){
                for(int i =0;i<ids.size();i++){
                    int id = (int) ids.get(i);
                    memberService.deleteMemberByid(id);
                }
                res = "删除成功";
            }else{
                result.setErrcode(500);
                res = "指令错误，删除失败";
            }
            result.setData("");
            result.setErrmsg(res);
        }catch (Exception e){
            e.printStackTrace();
            result.setErrcode(500);
            result.setErrmsg(e.getMessage());
        }
        return result;
    }



    @ResponseBody
    @RequestMapping(value = "/user/worker/newUser/delete", produces = "application/json; charset=utf-8")
    public Object deleteUserWorker( @RequestBody Integer id) {
        ResponseModel responseModel = new ResponseModel();
        DataSourceContextHolder.setDbType("dataSourceA");
        try {
            memberService.deleteMemberByid(id);
            responseModel.setCode(0);
            responseModel.setMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }

    @ResponseBody
    @RequestMapping(value = "/user/worker/newUser/upload", produces = "application/json; charset=utf-8")
    public Object newUserWorker( @RequestBody String jsonData) {
        ResponseModel responseModel = new ResponseModel();
        DataSourceContextHolder.setDbType("dataSourceA");
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            String avatar = json.getString("avatar");
            String email = json.getString("email");
            String file = json.getString("file");
            String password = json.getString("password");
            String phone = json.getString("phone");
            String sex = json.getString("sex");
            String username = json.getString("username");
            String jobPosition = json.getString("jobPosition");
            String nickname = json.getString("nickname");
            String wxID = json.getString("wxID");
            Member member = new Member();
            member.setUsername(username);
            member.setAvatar(avatar);
            member.setWxID(wxID);
            member.setNickname(nickname);
            member.setPassword(password);
            member.setGender(sex);
            member.setPhone(phone);
            member.setJobPosition(jobPosition);
            member.setEmail(email);
            member.setDutyNumber(RandomID.genIDWorker());
            //username 验证是否userinfo是否存在  不存在调用api查询 如果还没有返回不插入数据
            //必须关注经纬捷讯公众号才可以
            HashMap pa = new HashMap();
            pa.put("nickname",username);
            UserInfo userInfo = memberService.queryUserInfoByParam(pa);
            if(userInfo==null){
                responseModel.setCode(500);
                responseModel.setMsg("此用户未关注公众号");
                return responseModel;
            }
            member.setOpenid(userInfo.getOpenid());
            memberService.insertWorker(member);
            responseModel.setCode(0);
            responseModel.setMsg("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }



    @ResponseBody
    @RequestMapping(value = "/user/worker/worker/queryAll", produces = "application/json; charset=utf-8")
    public ResultMap<List<Member>> addresslist_queryAll(Page page, @RequestParam("limit") int limit,HttpServletRequest request){
        List<Member> memberList = new ArrayList<>();
        DataSourceContextHolder.setDbType("dataSourceA");
        int totals = 0;
        try {
            String keyword = request.getParameter("keyword");
            page.setKeyWord(keyword);
            page.setRows(limit);
            totals=memberService.selectPageCount(page);
            memberList=memberService.selectPageList(page);
            page.setTotalRecord(totals);
        }catch (Exception e){
            System.err.println(e.getMessage());

        }
        return new ResultMap<List<Member>>("",memberList,0,totals);
    }


    //后台管理员
    @ResponseBody
    @RequestMapping(value = "/useradmin/mangadmin/queryAll", produces = "application/json; charset=utf-8")
    public ResultMap<List<Member>> mangadmin_queryAll(Page page, @RequestParam("limit") int limit){
        DataSourceContextHolder.setDbType("dataSourceA");
        List<Member> memberList = new ArrayList<>();
        int totals = 0;
        try {
            page.setRows(limit);
            totals=memberService.selectPageCountIsAdmin(page);
            memberList=memberService.selectPageListIsAdmin(page);
            page.setTotalRecord(totals);
        }catch (Exception e){

        }
        return new ResultMap<List<Member>>("",memberList,0,totals);
    }


    //管理员角色 role表
    @ResponseBody
    @RequestMapping(value = "/useradmin/role/queryAll", produces = "application/json; charset=utf-8")
    public ResultMap<List<Role>> role_queryAll(Page page){
        DataSourceContextHolder.setDbType("dataSourceA");
        List<Role> roleList = new ArrayList<>();
        int totals = 0;
        try {
            roleList=roleService.queryRole(page);
            page.setTotalRecord(totals);
        }catch (Exception e){

        }
        return new ResultMap<List<Role>>("",roleList,0,totals);
    }



}
