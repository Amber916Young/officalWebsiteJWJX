package com.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import com.web.model.MessageWorkorder;
import com.web.model.Wx.UserInfo;
import com.web.service.MemberService;
import com.web.service.NoneMainService;
import com.web.service.WebService;
import com.web.utils.*;
import com.web.utils.database.DataSourceContextHolder;
import com.web.utils.wx.GetNewAccessToken;
import com.web.utils.wx.WxApi;
import javassist.tools.web.Webserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RequestMapping("/web")
@RestController
public class WebController {
    @Resource(name="Delcode")
    private String Delcode ;
    TimeParse timeParse = new TimeParse();
    HTMLSpirit htmlSpirit = new HTMLSpirit();
    @Autowired
    WebService webService;

    ResultMap resultMap = new ResultMap();

    @Autowired
    NoneMainService mainService;

    @Autowired
    MemberService memberService;





    /**
     * @Author
     * @Description 公众号关注人数  jwjxinfo2.userinfo表
     * @Date 2021-07-01
     * @Param
     * @return
     **/
    @ResponseBody
    @RequestMapping(value = "/wx/gzhuser/queryAll", produces = "application/json; charset=utf-8")
    public Object querygzhuser(Page page, @RequestParam("limit") int limit, HttpServletRequest request) {
        ResultMap resultMap = new ResultMap();
        DataSourceContextHolder.setDbType("dataSourceA");
        try {
            int totals = 0;
            page.setRows(limit);
            String keyword = request.getParameter("keyword");
            page.setKeyWord(keyword);
            totals=webService.selectPageCountGzhuser(page);
            List<HashMap<String, Object>> list=webService.selectPageListGzhuser(page);
            page.setTotalRecord(totals);
            resultMap.setCode(0);
            resultMap.setMsg("");
            resultMap.setData(list);
            resultMap.setCount(totals);
        }catch (Exception e){
            e.printStackTrace();
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
        }
        return resultMap;
    }


    /**
     * 校正公众号的正确人数
     * **/
    @ResponseBody
    @RequestMapping(value = "/wx/allUser/check", produces = "application/json; charset=utf-8")
    public Object wxAlluser(){
        GetNewAccessToken getNewAccessToken = new GetNewAccessToken();
        try {
            String accessToken =getNewAccessToken.GetToken("经纬捷讯");
            String urlToken = WxApi.gzhAllUser.replace("ACCESS_TOKEN",accessToken).replace("NEXT_OPENID","");
            JsonNode jsonObject = JsonUtils.stringToJsonNode(HttpRequest.getwx(urlToken));
            String errmsg="校正成功";
            int errcode= 0;
            if(jsonObject.has("errcode")) {
                errcode = Integer.parseInt(jsonObject.get("errcode").toString());
                errmsg = jsonObject.get("errmsg").toString();
            }else {
                int total = jsonObject.get("total").asInt();
                int count = jsonObject.get("count").asInt();
                JsonNode data = jsonObject.get("data");
                JsonNode openids = data.get("openid");
                List<HashMap> list =new ArrayList<>();
                HashMap map = new HashMap();
                for (int i =0;i<openids.size();i++){
                    HashMap map2 = new HashMap();
                    String openid = String.valueOf(openids.get(i)).replaceAll("\"","");
                    map2.put("openid",openid);
                    list.add(map2);
                }
                map.put("user_list",list);
                String obj2 = JsonUtils.objectToJson(map);
                obj2 = obj2.replaceAll("\\\\","");
                urlToken = WxApi.getBatchUserUnionID.replace("ACCESS_TOKEN",accessToken);
                jsonObject = JsonUtils.stringToJsonNode(HttpRequest.postwx(urlToken,obj2));
                JsonNode user_info_list = jsonObject.get("user_info_list");
                for(int i=0;i<user_info_list.size();i++){
                    UserInfo userInfo = JsonUtils.jsonToPojo(user_info_list.get(i).toString(),UserInfo.class);
                    int[] tagids = userInfo.getTagid_list();
                    String strtag ="";
                    for(int id :tagids){
                        strtag = id+",";
                    }
                    userInfo.setTagid_listStr(strtag);
                    HashMap pa = new HashMap();
                    pa.put("openid",userInfo.getOpenid());
                    DataSourceContextHolder.setDbType("dataSourceA");
                    UserInfo isExist = memberService.queryUserInfoByParam(pa);
                    if(isExist==null){
                        memberService.insertUserInfoWhenSubscribe(userInfo);
                    }else{
                        memberService.updateUserInfoWhenSubscribe(userInfo);
                    }
                }



            }
            resultMap.setCode(errcode);
            resultMap.setMsg(errmsg);
        }catch (Exception e){
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
        }
        return resultMap;
    }



    /**
     * 编辑微信标签
     * **/
    @ResponseBody
    @RequestMapping(value = "/wx/userRemark", produces = "application/json; charset=utf-8")
    public Object userRemark(@RequestBody String jsonData){
        GetNewAccessToken getNewAccessToken = new GetNewAccessToken();
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            String remark =json.getString("remark");
            String openid =json.getString("openid");
            String accessToken =getNewAccessToken.GetToken("经纬捷讯");
            String param="{   \"tag\" : {     \"openid\" : "+openid+",     \"remark\" : \""+remark+"\"} } ";
            String urlToken = WxApi.updateremark.replace("ACCESS_TOKEN",accessToken);
            JsonNode jsonObject = JsonUtils.stringToJsonNode(HttpRequest.postwx(urlToken,param));
            String errmsg="备注成功";
            int errcode= 0;
            if(jsonObject.has("errcode")){
                errcode = Integer.parseInt(jsonObject.get("errcode").toString());
                if(errcode==0){
                    errmsg="备注成功";
                }else if(errcode==-1){
                    errmsg="系统繁忙";
                }else if(errcode==40013){
                    errmsg="AppID无效错误";
                }
            }
            resultMap.setCode(errcode);
            resultMap.setMsg(errmsg);
        }catch (Exception e){
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
        }
        return resultMap;
    }



    /**
     * 查询微信标签下的所有用户
     * **/
    @ResponseBody
    @RequestMapping(value = "/wx/Tags/getuser", produces = "application/json; charset=utf-8")
    public Object Tagsgetuser(@RequestParam("id") int id){
        try {
            GetNewAccessToken getNewAccessToken = new GetNewAccessToken();
            String accessToken =getNewAccessToken.GetToken("经纬捷讯");
            String param="{     \"tagid\" : "+id+" } ";
            String urlToken = WxApi.getTagsFans.replace("ACCESS_TOKEN",accessToken);
            JsonNode jsonObject = JsonUtils.stringToJsonNode(HttpRequest.postwx(urlToken,param));
            String errmsg="ok";
            int errcode= 0;
            if(jsonObject.has("errcode")){
                errcode = Integer.parseInt(jsonObject.get("errcode").toString());
             if(errcode==-1){
                    errmsg="系统繁忙";
                }else if(errcode==40003){
                    errmsg="传入非法的openid";
                }else if(errcode==45159){
                    errmsg="非法的tag_id";
                }
            }else{
                JsonNode obj = jsonObject.get("data");
                JsonNode openids = obj.get("openid");
                List<HashMap> list =new ArrayList<>();
                HashMap map = new HashMap();

                for (int i =0;i<openids.size();i++){
                    HashMap map2 = new HashMap();
                    String openid = String.valueOf(openids.get(i)).replaceAll("\"","");
                    map2.put("openid",openid);
                    list.add(map2);
                }
                map.put("user_list",list);
                String obj2 = JsonUtils.objectToJson(map);
                obj2 = obj2.replaceAll("\\\\","");
                urlToken = WxApi.getBatchUserUnionID.replace("ACCESS_TOKEN",accessToken);
                jsonObject = JsonUtils.stringToJsonNode(HttpRequest.postwx(urlToken,obj2));
                JsonNode data = jsonObject.get("user_info_list");
                resultMap.setData(data);
            }
            resultMap.setCode(0);
            resultMap.setMsg(errmsg);
        }catch (Exception e){
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
        }
        return resultMap;
    }

    /**
     * 编辑微信标签
     * **/
    @ResponseBody
    @RequestMapping(value = "/wx/editTags", produces = "application/json; charset=utf-8")
    public Object editWXTags(@RequestBody String jsonData){
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            int tagid =json.getInteger("id");
            String tagName =json.getString("tags");
            GetNewAccessToken getNewAccessToken = new GetNewAccessToken();
            String accessToken =getNewAccessToken.GetToken("经纬捷讯");
            String param="{   \"tag\" : {     \"id\" : "+tagid+",     \"name\" : \""+tagName+"\"} } ";
            String urlToken = WxApi.editTags.replace("ACCESS_TOKEN",accessToken);
            JsonNode jsonObject = JsonUtils.stringToJsonNode(HttpRequest.postwx(urlToken,param));
            String errmsg="修改标签成功";
            int errcode= 0;
            if(jsonObject.has("errcode")){
                errcode = Integer.parseInt(jsonObject.get("errcode").toString());
                if(errcode==0){
                    errmsg="编辑成功";
                }else if(errcode==-1){
                    errmsg="系统繁忙";
                }else if(errcode==45157){
                    errmsg="标签名非法，请注意不能和其他标签重名";

                }else if(errcode==45158){
                    errmsg="标签名长度超过30个字节";

                }else if(errcode==45058){
                    errmsg="不能修改系统默认保留的标签";
                }
            }
            resultMap.setCode(0);
            resultMap.setMsg(errmsg);
        }catch (Exception e){
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
        }
        return resultMap;
    }
    /**
     * 删除微信标签
     * **/
    @ResponseBody
    @RequestMapping(value = "/wx/delTags", produces = "application/json; charset=utf-8")
    public Object delWXTags(@RequestBody Integer id){
        try {
            GetNewAccessToken getNewAccessToken = new GetNewAccessToken();
            String accessToken =getNewAccessToken.GetToken("经纬捷讯");
            String param="{   \"tag\" : {     \"id\" : "+id+"} } ";
            String urlToken = WxApi.delTags.replace("ACCESS_TOKEN",accessToken);
            JsonNode jsonObject = JsonUtils.stringToJsonNode(HttpRequest.postwx(urlToken,param));
            String errmsg="删除标签成功";
            int errcode= 0;
            if(jsonObject.has("errcode")){
                errcode = Integer.parseInt(jsonObject.get("errcode").toString());
                if(errcode==0){
                    errmsg="删除成功";
                }else if(errcode==-1){
                    errmsg="系统繁忙";
                }else if(errcode==45057){
                    errmsg="该标签下粉丝数超过10w，不允许直接删除";

                }else if(errcode==45058){
                    errmsg="不能删除系统默认保留的标签";
                }
            }
            resultMap.setCode(0);
            resultMap.setMsg(errmsg);
        }catch (Exception e){
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
        }
        return resultMap;
    }
    /**
     * 新增微信标签
     * **/
    @ResponseBody
    @RequestMapping(value = "/wx/addTags", produces = "application/json; charset=utf-8")
    public Object addWXTags(@RequestBody String jsonData){
        try {
            HashMap item = JSONObject.parseObject(jsonData, HashMap.class);
            String tagName= String.valueOf(item.get("tags"));
            GetNewAccessToken getNewAccessToken = new GetNewAccessToken();
            String accessToken =getNewAccessToken.GetToken("经纬捷讯");
            String urlToken = WxApi.createTags.replace("ACCESS_TOKEN",accessToken);
            JsonObject jb = new JsonObject();
            JsonObject jb2 = new JsonObject();
            jb2.addProperty("name",tagName);
            jb.add("tag",jb2);
            JsonNode jsonObject = JsonUtils.stringToJsonNode(HttpRequest.postwx(urlToken,jb.toString()));
            String errmsg="新增标签成功";
            int errcode= 0;
            if(jsonObject.has("errcode")){
                errcode = Integer.parseInt(jsonObject.get("errcode").toString());
                if(errcode==-1){
                    errmsg="系统繁忙";
                }else if(errcode==45157){
                    errmsg="请注意不能和其他标签重名";

                }else if(errcode==45158){
                    errmsg="标签名长度超过30个字节";
                }else if(errcode==45056){
                    errmsg="创建的标签数过多，不能超过100个";
                }
            }else{
            }
            resultMap.setCode(0);
            resultMap.setMsg(errmsg);
        }catch (Exception e){
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
        }
        return resultMap;
    }

    /**
     * 获取微信标签
     * **/
    @ResponseBody
    @RequestMapping(value = "/wx/getTags", produces = "application/json; charset=utf-8")
    public Object getWXTags(){
        try {
            GetNewAccessToken getNewAccessToken = new GetNewAccessToken();
            String accessToken =getNewAccessToken.GetToken("经纬捷讯");
            String urlToken = WxApi.getGroupTags.replace("ACCESS_TOKEN",accessToken);
            JsonNode jsonObject = JsonUtils.stringToJsonNode(HttpRequest.getwx(urlToken));
            JsonNode jsonNode = jsonObject.get("groups");
            resultMap.setData(jsonNode);
            resultMap.setCode(0);
            resultMap.setMsg("ok");
        }catch (Exception e){
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
        }
        return resultMap;
    }


    /**
     * 标签新增
     * **/
    @ResponseBody
    @RequestMapping(value = "/Tags/add", produces = "application/json; charset=utf-8")
    public Object TagseAdd(@RequestBody String jsonData){
        try {
            DataSourceContextHolder.setDbType("dataSourceA");
            HashMap item = JSONObject.parseObject(jsonData, HashMap.class);
            webService.insertTags(item);
            resultMap.setCode(0);
            resultMap.setMsg("ok");
        }catch (Exception e){
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
        }
        return resultMap;
    }
    /**
     * 标签编辑
     * **/
    @ResponseBody
    @RequestMapping(value = "/Tags/edit", produces = "application/json; charset=utf-8")
    public Object Tagsedit(@RequestBody String jsonData){
        try {
            DataSourceContextHolder.setDbType("dataSourceA");
            HashMap item = JSONObject.parseObject(jsonData, HashMap.class);
            webService.updateTags(item);
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
    @RequestMapping(value = "/Tags/deleteByid", produces = "application/json; charset=utf-8")
    public Object deleteByid(@RequestBody Integer id){
        try {
            DataSourceContextHolder.setDbType("dataSourceA");
            HashMap map = new HashMap();
            map.put("id",id);
            webService.deleteByid(map);
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
    @RequestMapping(value = "/Tags/deleteByids", produces = "application/json; charset=utf-8")
    public Object deleteByids(HttpServletRequest request){
        try {
            DataSourceContextHolder.setDbType("dataSourceA");
            String ids = request.getParameter("id");
            String[] idsArry = ids.split(",");
            String type = request.getParameter("type");
            HashMap map = new HashMap();
            map.put("type",type);
            for(int i =0;i<idsArry.length;i++){
                int id = Integer.parseInt(idsArry[i]);
                map.put("id",id);
                if(type.equals("webTags")){
                    webService.deleteByid(map);
                }else {
                    //wx  api
                }
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
    @RequestMapping(value = "/question/upload", produces = "application/json; charset=utf-8")
    public Object webquestion_upload( @RequestBody String jsonData) {
        AJAXResult result = new AJAXResult();
        DataSourceContextHolder.setDbType("dataSourceA");
        String res = "";
        try {
            HashMap item = JSONObject.parseObject(jsonData, HashMap.class);
            String createTime = timeParse.convertDate2String(new Date(),"yyyy-MM-dd HH:mm:ss");
            item.put("createTime",createTime);
            if(item.containsKey("status")){
                String s =  String.valueOf( item.get("status"));
                if(s.equals("true")){
                    item.put("status",1);
                }else{
                    item.put("status",0);
                }
            }
            webService.insertwebQuestion(item);
            res="新增成功";
            result.setErrcode(0);
            result.setErrmsg(res);
        }catch (Exception e){
            e.printStackTrace();
            result.setErrcode(500);
            result.setErrmsg(e.getMessage());
        }
        return result;
    }
    @ResponseBody
    @RequestMapping(value = "/question/edit", produces = "application/json; charset=utf-8")
    public Object webquestion_edit( @RequestBody String jsonData) {
        AJAXResult result = new AJAXResult();
        DataSourceContextHolder.setDbType("dataSourceA");

        String res = "";
        try {
            HashMap item = JSONObject.parseObject(jsonData, HashMap.class);
            if(item.containsKey("status")){
                String s =  String.valueOf( item.get("status"));
                if(s.equals("true")){
                    item.put("status",1);
                }else{
                    item.put("status",0);
                }
            }
            if(item.containsKey("categroy")){
                String s = String.valueOf(item.get("categroy"));
            }
            String createTime = timeParse.convertDate2String(new Date(),"yyyy-MM-dd HH:mm:ss");
            item.put("createTime",createTime);
            webService.updatewebQuestion(item);
            res="更新成功";
            result.setErrcode(0);
            result.setErrmsg(res);
        }catch (Exception e){
            e.printStackTrace();
            result.setErrcode(500);
            result.setErrmsg(e.getMessage());
        }
        return result;
    }
    @ResponseBody
    @RequestMapping(value = "/question/delete", produces = "application/json; charset=utf-8")
    public Object deleteClientTerminaldb( @RequestBody String jsonData) {
        AJAXResult result = new AJAXResult();
        DataSourceContextHolder.setDbType("dataSourceA");

        String res = "";
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            String code = json.getString("code");
            String id = json.getString("id");
            if(code.equals(Delcode)){
                webService.deleteQuestionByid(id);
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
    @PostMapping(value = "/question/deletes")
    public Object deletesClientTerminaldb(@RequestBody String jsonData){
        DataSourceContextHolder.setDbType("dataSourceA");

        AJAXResult result = new AJAXResult();
        String res = "";
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            JSONArray ids =json.getJSONArray("ids");
            String code = json.getString("code");
            if(code.equals(Delcode)){
                for(int i =0;i<ids.size();i++){
                    String  id =ids.get(i).toString();
                    webService.deleteQuestionByid(id);
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
    @RequestMapping(value = "/question/queryAll", produces = "application/json; charset=utf-8")
    public Object queryClientTerminaldb(Page page, @RequestParam("limit") int limit,
                                        HttpServletRequest request) {
        ResultMap resultMap = new ResultMap();
        DataSourceContextHolder.setDbType("dataSourceA");
        try {
            int totals = 0;
            page.setRows(limit);
            String keyword = request.getParameter("keyword");
            if(keyword==null||keyword.length()<=0){
                keyword=null;
            }
            page.setKeyWord(keyword);
            totals=webService.selectPageCountQuestion(page);
            List<HashMap<String, Object>> companyList=webService.selectPageListQuestion(page);
            page.setTotalRecord(totals);
            resultMap.setCode(0);
            resultMap.setMsg("");
            resultMap.setData(companyList);
            resultMap.setCount(totals);
        }catch (Exception e){
            e.printStackTrace();
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
        }
        return resultMap;
    }



}
