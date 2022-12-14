package com.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.abel533.echarts.Option;

import com.google.gson.JsonObject;
import com.web.model.Echart.EOption;
import com.web.model.Member;
import com.web.model.WxUserData;
import com.web.model.jwjx.EgtCompany;
import com.web.service.MemberService;
import com.web.service.NoneMainService;
import com.web.utils.*;
import com.web.utils.Sms.SmsUtils;
import com.web.utils.database.DataSourceContextHolder;
import com.web.utils.wx.GetNewAccessToken;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.*;

import static com.web.utils.FileUtil.getFileToByte;

@Controller
@RequestMapping("/wx")
public class WxContoroller {
    private AJAXResult result = new AJAXResult();
    AJAXResult ajaxResult = new AJAXResult();
    WxMpService wxMpService = new WxMpServiceImpl();
    private String menuId = null;

    @Resource(name="Delcode")
    private String Delcode ;
    @Autowired
    NoneMainService mainService;
    @Autowired
    MemberService memberService;


    @Resource(name="grant_type")
    private String grant_type ;
    @Resource(name="appid")
    private String appid;
    @Resource(name="secret")
    private String secret;
    @Resource(name="websocketURL")
    private String websocketURL;
    @Resource(name="fileuploadPath")
    private String theSetDir ;



    /**
     * @Author yyj
     * @Description  ??????????????????  title????????????
     * @Date 2021-07-01
     * @Param
     * @return
     **/

    @ResponseBody
    @RequestMapping(value = "/pushmessage/template/update", produces = "application/json; charset=utf-8")
    public Object templateUpdate( @RequestBody String jsonData){
        ResponseModel responseModel = new ResponseModel();
        try {
            DataSourceContextHolder.setDbType("dataSourceA");
            HashMap map = JsonUtils.jsonToPojo(jsonData,HashMap.class);
            mainService.updatetemplatemsg(map);
            responseModel.setCode(0);
            responseModel.setMsg("ok");
        }catch (Exception e){
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }

    @ResponseBody
    @RequestMapping(value = "/pushmessage/user/add", produces = "application/json; charset=utf-8")
    public Object addPushmessageUser(@RequestBody String jsonData) {
        DataSourceContextHolder.setDbType("dataSourceA");
        ResponseModel responseModel = new ResponseModel();
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            String dutyList = json.getString("dutyList");
            String[] dlist = dutyList.split(",");
            int id = json.getInteger("id");
            mainService.deleteTemplateUser(id);
            for (int i = 0; i < dlist.length; i++) {
                HashMap map = new HashMap<>();
                int uid = Integer.parseInt(dlist[i]);
                map.put("uid", uid);
                map.put("tid", id);
                mainService.insertTemplateuser(map);
            }
            responseModel.setCode(0);
            responseModel.setMsg("????????????");
        } catch (Exception e) {
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }

    /**
     * ??????id??????
     * **/
    @ResponseBody
    @RequestMapping(value = "/pushmessage/delTemplate", produces = "application/json; charset=utf-8")
    public Object deleteTemplateByid( @RequestBody Integer id){
        ResponseModel responseModel = new ResponseModel();
        try {
            DataSourceContextHolder.setDbType("dataSourceA");
            mainService.deleteTemplateByid(id);
            responseModel.setCode(0);
            responseModel.setMsg("ok");
        }catch (Exception e){
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }




    /**
     * ??????id??????
     * **/
    @ResponseBody
    @RequestMapping(value = "/pushmessage/user/deleteByid", produces = "application/json; charset=utf-8")
    public Object deleteByid( @RequestBody String jsonData){
        ResponseModel responseModel = new ResponseModel();
        try {
            DataSourceContextHolder.setDbType("dataSourceA");
            HashMap map = JsonUtils.jsonToPojo(jsonData,HashMap.class);
            mainService.deleteTemplateUserByid(map);
            responseModel.setCode(0);
            responseModel.setMsg("ok");
        }catch (Exception e){
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }



    @ResponseBody
    @RequestMapping("/pushmessage/user/queryAll")
    public Object pushmessage_user_queryAll( Page page, @RequestParam("limit") int limit, HttpServletRequest request){
        ResultMap resultMap = new ResultMap();
        DataSourceContextHolder.setDbType("dataSourceA");
        try {
            int totals = 0;
            page.setRows(limit);
            String txt = request.getParameter("keyword");
            page.setKeyWord(txt);
            totals=mainService.selectPageCount_templateuser(page);
            List<HashMap<String, Object>> list=mainService.selectPageList_templateuser(page);
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

    @ResponseBody
    @RequestMapping("/pushmessage/template/queryAll")
    public Object pushmessage_template_queryAll( Page page, @RequestParam("limit") int limit, HttpServletRequest request){
        ResultMap resultMap = new ResultMap();
        DataSourceContextHolder.setDbType("dataSourceA");
        try {
            int totals = 0;
            page.setRows(limit);
            String txt = request.getParameter("keyword");
            page.setKeyWord(txt);
            totals=mainService.selectPageCount_template(page);
            List<HashMap<String, Object>> list=mainService.selectPageList_template(page);
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


    @ResponseBody
    @RequestMapping("/wxmenu/upload")
    public Object uploadMenu(@RequestBody String jsonData){
        AJAXResult ajaxResult = new AJAXResult();
        try {
            HashMap<String, String> param = JSONObject.parseObject(jsonData, HashMap.class);
            Iterator i = param.entrySet().iterator();
            //????????????

            List list = new ArrayList();

            for (HashMap.Entry<String, String> entry : param.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

            }


//            String token = GetToken();
//            String url = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=" +token;
//            JsonNode jsonObject = JsonUtils.stringToJsonNode(HttpRequest.getwx(url));

            ajaxResult.setData("");
        } catch (Exception e) {
            ajaxResult.setErrcode(500);
            ajaxResult.setErrmsg(e.getMessage());
            e.printStackTrace();
        }


        return ajaxResult;
    }




    @ResponseBody
    @RequestMapping(value = "/menu/deleteall", produces = "application/json; charset=utf-8")
    public Object deleteall( @RequestBody String jsonData) {
        AJAXResult result = new AJAXResult();
        String res = "";
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            String code = json.getString("code");
            int errcode=0;
            String errmsg="";
            if(code.equals(Delcode)){
                GetNewAccessToken getNewAccessToken = new GetNewAccessToken();
                String token =getNewAccessToken.GetToken("????????????");
                String url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=" +token;
                JsonNode jsonObject = JsonUtils.stringToJsonNode(HttpRequest.getwx(url));
                errcode = Integer.parseInt(jsonObject.get("errcode").toString());
                errmsg = jsonObject.get("errmsg").toString();
                result.setErrcode(errcode);
                if(errcode==0){
                    res = "????????????";
                }else{
                    res=errmsg;
                }
            }else{
                result.setErrcode(500);
                res = "???????????????????????????";
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
    @RequestMapping(value = "/menu/delconditional", produces = "application/json; charset=utf-8")
    public Object deletematch( @RequestBody String jsonData) {
        AJAXResult result = new AJAXResult();
        String res = "";
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            String code = json.getString("code");
            String menuid = json.getString("menuid");
            int errcode=0;
            String errmsg="";
            if(code.equals(Delcode)){
                GetNewAccessToken getNewAccessToken = new GetNewAccessToken();
                String token =getNewAccessToken.GetToken("????????????");
                String url = "https://api.weixin.qq.com/cgi-bin/menu/delconditional?access_token=" +token;
                JsonObject jb = new JsonObject();
                jb.addProperty("menuid",menuid);
                JsonNode jsonObject = JsonUtils.stringToJsonNode(HttpRequest.postwx(url,jb.toString()));
                errcode = Integer.parseInt(jsonObject.get("errcode").toString());
                errmsg = jsonObject.get("errmsg").toString();
                result.setErrcode(errcode);
                if(errcode==0){
                    res = "????????????";
                }else{
                    res=errmsg;
                }
            }else{
                result.setErrcode(500);
                res = "???????????????????????????";
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




    public List handleMenu(  JsonNode button){
        List menu =new ArrayList<>();
        String type="";
        String btnurl="";
        String pagepath="";
        String appid="";
        int id = 0;
        for(int i = 0;i<button.size();i++){
            HashMap  sub = new HashMap();
            String name = button.get(i).get("name").toString().replaceAll("\"", "");
            if(button.get(i).has("sub_button")){
                id = id+1;
                int pid =id;
                sub.put("pid",0);
                sub.put("id", id);
                sub.put("name", name);
                sub.put("appid",appid);
                sub.put("type", type);
                sub.put("pagepath",pagepath);
                sub.put("url", btnurl);
                menu.add(sub);
                JsonNode subm = button.get(i).get("sub_button");
//                    JsonNode subm = object.get("list");
                for(int k = 0;k<subm.size();k++) {
                    id = id+1;
                    HashMap  sub2 = new HashMap();
                    if(subm.get(k).has("type")){
                        type = subm.get(k).get("type").toString().replaceAll("\"", "");
                    }
                    if(subm.get(k).has("url")){
                        btnurl = subm.get(k).get("url").toString().replaceAll("\"", "");
                    }
                    if(subm.get(k).has("appid")){
                        appid = subm.get(k).get("appid").toString().replaceAll("\"", "");
                        pagepath = subm.get(k).get("pagepath").toString().replaceAll("\"", "");
                    }
                    name = subm.get(k).get("name").toString().replaceAll("\"", "");
                    sub2.put("pid", pid);
                    sub2.put("id", id);
                    sub2.put("name", name);
                    sub2.put("appid",appid);
                    sub2.put("type", type);
                    sub2.put("pagepath",pagepath);
                    sub2.put("url", btnurl);
                    menu.add(sub2);
                }
            }else{
                id = id+1;
                btnurl = button.get(i).get("url").toString().replaceAll("\"", "");
                type = button.get(i).get("type").toString().replaceAll("\"", "");
                if(button.get(i).has("appid")) {
                    appid = button.get(i).get("appid").toString().replaceAll("\"", "");
                    pagepath = button.get(i).get("pagepath").toString().replaceAll("\"", "");

                }
                sub.put("pid",0);
                sub.put("id",id);
                sub.put("name",name);
                sub.put("url",btnurl);
                sub.put("type",type);
                sub.put("appid",appid);
                sub.put("pagepath",pagepath);
                menu.add(sub);
            }
        }
        return menu;

    }


    @ResponseBody
    @RequestMapping("/wxmenu/get")
    public Object wxmenuPage(){
        AJAXResult ajaxResult = new AJAXResult();

        try {
            GetNewAccessToken getNewAccessToken = new GetNewAccessToken();
            String token =getNewAccessToken.GetToken("????????????");
            String url = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=" +token;
//            String url = "https://api.weixin.qq.com/cgi-bin/get_current_selfmenu_info?access_token="+token;
            JsonNode jsonObject = JsonUtils.stringToJsonNode(HttpRequest.getwx(url));
            JsonNode jsonArray = jsonObject.get("menu");
            JsonNode button = jsonArray.get("button");
            JsonNode muenId = jsonArray.get("menuid");
            List menu = handleMenu(button);
            JsonNode jsonArray2=null;
            JsonNode muenId2=null;
            JsonNode button2=null;
            List menu2=null;
            if(jsonObject.has("conditionalmenu")){
                jsonArray2 = jsonObject.get("conditionalmenu");
                muenId2 = jsonArray2.get(0).get("menuid");
                button2 = jsonArray2.get(0).get("button");
                menu2 = handleMenu(button2);
            }



            HashMap mm = new HashMap();
            mm.put("tree1",menu);
            mm.put("tree2",menu2);
            mm.put("menuid1",muenId);
            mm.put("menuid2",muenId2);
            ajaxResult.setData(mm);
        } catch (Exception e) {
            ajaxResult.setErrcode(500);
            ajaxResult.setErrmsg(e.getMessage());
            e.printStackTrace();
        }


        return ajaxResult;
    }

    @RequestMapping("/test")
    public String testPage(){
        return "test";
    }
    @RequestMapping("/remote/auth_response")
    public String getOAuth(HttpSession session,HttpServletRequest request, Model model) {
        String code = request.getParameter("code");
        DataSourceContextHolder.setDbType("dataSourceA");
        try {
            String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+secret+"&code=" + code + "&grant_type=authorization_code";
            JsonNode jsonObject = JsonUtils.stringToJsonNode(HttpRequest.getwx(url));
            String openid = jsonObject.get("access_token").toString();
            String access_token = jsonObject.get("access_token").toString();

            //?????????(??????????????????)
            String infoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" +
                    access_token + "&openid=" + openid + "&lang=zh_CN";
            infoUrl = infoUrl.replaceAll("\"", "");
            JsonNode userInfo = JsonUtils.stringToJsonNode(HttpRequest.getwx(infoUrl));
            HashMap map = new HashMap();
            map.put("name",userInfo.get("nickname").toString().replaceAll("\"",""));
            map.put("phone",null);
            map.put("openid",userInfo.get("openid").toString().replaceAll("\"",""));
            map.put("nickname",userInfo.get("nickname").toString().replaceAll("\"",""));
            String gender = userInfo.get("sex").toString().replaceAll("\"","");
            if(gender.equals("1")){
                gender="???";
            }else if(gender.equals("0")){
                gender="???";
            }else{
                gender="??????";
            }
            map.put("sex",gender);
            map.put("language",userInfo.get("language").toString().replaceAll("\"",""));
            map.put("city",userInfo.get("city").toString().replaceAll("\"",""));
            map.put("province",userInfo.get("province").toString().replaceAll("\"",""));
            map.put("country",userInfo.get("country").toString().replaceAll("\"",""));
            map.put("headimgurl",userInfo.get("headimgurl").toString().replaceAll("\"",""));
            String uopenID = userInfo.get("openid").toString().replaceAll("\"","");
            HashMap param = new HashMap();
            param.put("username",userInfo.get("nickname").toString().replaceAll("\"",""));
            Member member = memberService.queryUserisAdmin(param);
            //???????????????  ????????????????????????????????????
            if(member!=null){
                if(member.getAuthority().contains("0")){
                    if(member.getOpenid()==null){
                        Member member2 = new Member();
                        member2.setOpenid(uopenID);
                        member2.setId(member.getId());
                        HashMap membermap = JsonUtils.objectToMap(member2);
                        memberService.updateWorker(membermap);
                    }
                    model.addAttribute("username",userInfo.get("nickname").toString().replaceAll("\"",""));
                    model.addAttribute("openid",uopenID);
                    return "wx/remote";
                }else {
                    return "error/501";
                }
            }else{
                return "error/501";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error/501";
    }


    /**
     * ????????????
     * */
    @RequestMapping(value = "/remote/index")
    public Object remoteindex(){

        String redUrl=websocketURL+"wx/remote/auth_response";
        redUrl = URLEncoder.encode(redUrl);
        String url="https://open.weixin.qq.com/connect/oauth2/authorize?" +
                "appid="+appid+"&redirect_uri="+redUrl+"&response_type=code" +
                "&scope=snsapi_userinfo&state=STATE#wechat_redirect";
        return "redirect:"+url;//????????????????????????????????????
    }

    /**
     * ????????????  ????????????????????????
     * */
    @ResponseBody
    @RequestMapping(value = "/remote/pingNetwork")
    public Object isConnect(@RequestBody String jsonData){
        /**
         * ???Runtime.getRuntime().exec()???????????????????????????????????????
         * ?????????????????????????????????????????????????????????
         * ?????????????????????java.lang.Process?????????
         * ????????????????????????????????????????????????????????????
         * ??????????????????????????????????????????
         */
        AJAXResult ajaxResult = new AJAXResult();
        Runtime runtime = Runtime.getRuntime();
        Process process;
        Boolean connect = false;
        try {
            DataSourceContextHolder.setDbType("dataSourceB");
            JSONObject json = JSONObject.parseObject(jsonData);
            String regNO = json.getString("regNO");
            HashMap param = new HashMap();
            param.put("regNO",regNO);
            HashMap<String, Object> CompaniesList = mainService.queryCompany(param);
            if(CompaniesList!=null){
                if( !CompaniesList.containsKey("networkIP")){
                    ajaxResult.setErrcode(404);
                    ajaxResult.setErrmsg("?????????????????????");
                    return ajaxResult;
                }
                String ipPath = CompaniesList.get("networkIP").toString();

                ipPath = ipPath.substring(1);
                process = runtime.exec("ping " + ipPath);
                InputStream is = process.getInputStream();
                InputStreamReader isr = new InputStreamReader(is,"GBK");
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                StringBuffer sb = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                System.out.println("????????????:"+sb);
                is.close();
                isr.close();
                br.close();
                if (null != sb && !sb.toString().equals("")) {
                    String logString = "";
                    if (sb.toString().indexOf("TTL") > 0) {
                        // ????????????
                        connect = true;
                    } else {
                        connect = false;
                    }
                }
                if(connect==true){
                    ajaxResult.setErrcode(0);
                    ajaxResult.setErrmsg("??????????????????");
                    return ajaxResult;
                }else{
                    // ???????????????
                    ajaxResult.setErrcode(404);
                    ajaxResult.setErrmsg("??????????????????");
                    return ajaxResult;
                }
            }else{
                ajaxResult.setErrcode(404);
                ajaxResult.setErrmsg("??????????????????");
            }
        } catch (IOException e) {
            e.printStackTrace();
            ajaxResult.setErrcode(500);
            ajaxResult.setErrmsg(e.getMessage());
        }
        return ajaxResult;
    }

    /**
     * ????????????   ????????????
     * ?????????
     * ?????????????????????????????????AOP
     * */
    @ResponseBody
    @RequestMapping(value = "/remote/upload")
    public Object remoteUpload(@RequestBody String jsonData){
        DataSourceContextHolder.setDbType("dataSourceA");
        AJAXResult ajaxResult = new AJAXResult();
        try {
            ajaxResult.setErrmsg("????????????");
            ajaxResult.setErrcode(0);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setErrcode(500);
            ajaxResult.setErrmsg(e.getMessage());
        }
        return ajaxResult;
    }

    /**
     * ????????????    ????????????????????????
     * */
    @ResponseBody
    @RequestMapping(value = "/remote/queryAllCompany")
    public Object queryAllCompany(@RequestBody String jsonData){
        AJAXResult ajaxResult = new AJAXResult();
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            String openid = json.getString("openid");
            HashMap<String, Object> map = new HashMap<>();
            map.put("openid",openid);
            DataSourceContextHolder.setDbType("dataSourceA");
            Member member = memberService.queryUserisAdmin(map);
            if (member.getAuthority().contains("0")){
                DataSourceContextHolder.setDbType("dataSourceB");
                List<HashMap<String, Object>> CompaniesList = mainService.queryAllCompany();
                ajaxResult.setData(CompaniesList);
                ajaxResult.setErrmsg("????????????");
                ajaxResult.setErrcode(0);
                return ajaxResult;
            }else{
                ajaxResult.setErrmsg("???????????????????????????");
                ajaxResult.setErrcode(500);
                return ajaxResult;
            }
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setErrcode(500);
            ajaxResult.setErrmsg(e.getMessage());
        }
        return ajaxResult;
    }

    /**
     * ??????????????????????????????
     * https://api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID
     *media_id  ????????????ID
     * ??????
     **/
    @ResponseBody
    @RequestMapping(value = "/upload/record")
    public Object record(@RequestBody String jsonData){
        DataSourceContextHolder.setDbType("dataSourceA");
        AJAXResult ajaxResult = new AJAXResult();
        GetNewAccessToken getNewAccessToken = new GetNewAccessToken();
        String token =getNewAccessToken.GetToken("????????????");
        String errmsg="";
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            String Speaktime = json.getString("Speaktime");
            String serverId = json.getString("serverId");
            String url = "https://api.weixin.qq.com/cgi-bin/media/get?access_token="+token+"&media_id="+serverId;
            String path = HttpRequest.getwx(url);
            if(path==null){
                errmsg="??????????????????????????????????????????";
                ajaxResult.setErrcode(500);
            }else {
                errmsg="ok";
                String upoadurl = theSetDir ;
                String directory ="chat_audio";
                File file = new File(path);
                String result = HttpRequest.upload(upoadurl,file,directory);
                ResponseModel rm = JsonUtils.jsonToPojo(result,ResponseModel.class);
                String AudioUrl,fileName="";
                if(rm.getCode()==0){
                    AudioUrl = rm.getData().toString();
                    fileName = AudioUrl.substring(AudioUrl.lastIndexOf(directory+"/"));
                    fileName = fileName.substring(directory.length()+1);
                    HashMap map = new HashMap();
                    map.put("AudioUrl",AudioUrl);
                    map.put("fileName",fileName);

                    String voiceTime = FileUtil.format2(Double.valueOf(Integer.parseInt(Speaktime)/1000));
                    map.put("voiceTime",voiceTime);
                    ajaxResult.setData(map);
                    ajaxResult.setErrcode(0);
                    if (file.delete()){
                        System.out.println("????????????");
                    }else {
                        System.out.println("????????????");
                    }


                }else{
                    errmsg="??????????????????";
                    ajaxResult.setErrcode(rm.getCode());
                }
            }
            ajaxResult.setErrmsg(errmsg);
        } catch (Exception e) {
            ajaxResult.setErrcode(500);
            ajaxResult.setErrmsg(e.getMessage());
            e.printStackTrace();
        }


        return ajaxResult;
    }
    /**
     * ?????????????????????
     * */
    public  String getNoncestr() {
        String Noncestr = MD5Util.md5Encrypt32Upper(RandomID.genID2());
        return Noncestr;
    }

    public  String SHA1(String decript) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // ????????????????????? ???????????? ???
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * JS-SDK????????????????????????
     **/
    @ResponseBody
    @RequestMapping(value = "/initJSSDK/config")
    public Object initJSSDK(HttpServletRequest request,@RequestBody String jsonData){
        HashMap Tokenandjsapi_ticket = GetTokenandjsapi_ticket();
        String jsapi_ticket = String.valueOf(Tokenandjsapi_ticket.get("jsapi_ticket")).replaceAll("\"","");
        AJAXResult ajaxResult  = new AJAXResult();
        try {
            int errcode=0;
            if(errcode==0){
                ajaxResult.setErrcode(0);
                ajaxResult.setErrmsg("????????????");
                TimeParse timeParse = new TimeParse();
                String timestamp = timeParse.getTimestamp();
                //???????????????????????????
                String noncestr = getNoncestr();
                JSONObject json = JSONObject.parseObject(jsonData);
                String currenturl = json.getString("current_url");
                String str = "jsapi_ticket="+jsapi_ticket+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+currenturl;
                String signature =SHA1(str);
                HashMap map = new HashMap();
                map.put("noncestr",noncestr);
                map.put("timestamp",timestamp);
                map.put("signature",signature);
                ajaxResult.setData(map);
            }else{
                ajaxResult.setErrcode(500);
                ajaxResult.setErrmsg("");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            ajaxResult.setErrcode(500);
            ajaxResult.setErrmsg(e.getMessage());
        }
        return ajaxResult;
    }


    @RequestMapping("/binding/auth_response/phone/{phone}")
    public String getOAuth(@PathVariable("phone") String phone, HttpServletRequest request, Model model) {
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        try {
            String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+secret+"&code=" + code + "&grant_type=authorization_code";
            JsonNode jsonObject = JsonUtils.stringToJsonNode(HttpRequest.getwx(url));
            String openid = jsonObject.get("access_token").toString();
            String access_token = jsonObject.get("access_token").toString();
            String refresh_token = jsonObject.get("refresh_token").toString();
            //??????????????????openid ???access_token????????????????????????????????????????????????????????????????????????????????????:
            //?????????(??????????????????)
            String infoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" +
                    access_token + "&openid=" + openid + "&lang=zh_CN";
            infoUrl = infoUrl.replaceAll("\"", "");
            JsonNode userInfo = JsonUtils.stringToJsonNode(HttpRequest.getwx(infoUrl));
            HashMap map = new HashMap();
            map.put("openid",userInfo.get("openid").toString().replaceAll("\"",""));
            map.put("nickname",userInfo.get("nickname").toString().replaceAll("\"",""));
            String gender = userInfo.get("sex").toString().replaceAll("\"","");
            if(gender.equals("1")){
                gender="???";
            }else if(gender.equals("0")){
                gender="???";
            }else{
                gender="??????";
            }
            map.put("gender",gender);
            map.put("phone",phone);
            memberService.updateUserInfo(map);
            return "error/success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error/500";
    }

    @RequestMapping(value = "/authority/binding/phone/{phone}")
    public String authority(@PathVariable("phone") String phone){
        String redUrl=websocketURL+"wx/binding/auth_response/phone/"+phone;
        redUrl = URLEncoder.encode(redUrl);
        String url="https://open.weixin.qq.com/connect/oauth2/authorize?" +
                "appid="+appid+"&redirect_uri="+redUrl+"&response_type=code" +
                "&scope=snsapi_userinfo&state=STATE#wechat_redirect";
        return "redirect:"+url;//????????????????????????????????????
    }


    @ResponseBody
    @RequestMapping(value = "/binding/login")
    public Object binding(@RequestBody String jsonData){
        JSONObject json = JSONObject.parseObject(jsonData);
        String phone = json.getString("phone");
        String code =json.getString("vercode");
        String VerifyCode = json.getString("VerifyCode");
        String msg = "";

        AJAXResult ajaxResult = new AJAXResult();
        //????????????????????????????????????????????????
        List<HashMap<String, Object>> isVerified = new ArrayList<>();
        DataSourceContextHolder.setDbType("dataSourceA");
        HashMap<String, Object> params = new HashMap();
        params.put("phone",phone);


        isVerified = memberService.queryUserInfo(params);
        if(isVerified.size()>0){
            msg="??????????????????????????????????????????";
            ajaxResult.setErrcode(500);
            ajaxResult.setErrmsg(msg);
            return ajaxResult;
        }
        try {
//            if(VerifyCode.equals(null)){
//                msg="???????????????";
//                ajaxResult.setErrcode(500);
//            }else{
                //?????????phone openid ????????????????????????
                DataSourceContextHolder.setDbType("dataSourceB");
                List<HashMap<String, Object>> users = new ArrayList<>();
                HashMap<String, Object> param = new HashMap<>();
                param.put("phone",phone);
                //??????????????????????????????????????????
                users = memberService.queryUserJwjx(param);
                if(users!=null){
                    msg="????????????";
                    DataSourceContextHolder.setDbType("dataSourceA");
                    //??????userInfo????????????  ????????????????????????????????????
                    for (HashMap map : users){
                        map.put("type","??????");
                        memberService.insertUserInfo(map);
                    }
                    ajaxResult.setErrcode(0);
                    ajaxResult.setData(phone);
                }else{
                    msg="??????????????????";
                    HashMap newMap = new HashMap();
                    newMap.put("phone",phone);
                    newMap.put("type","??????");
                    memberService.insertUserInfo(newMap);
                    ajaxResult.setErrcode(0);
                }
//            }
            ajaxResult.setErrmsg(msg);
        }catch (Exception e){
            ajaxResult.setErrcode(500);
            ajaxResult.setErrmsg(e.getMessage());
            e.printStackTrace();
        }
        return ajaxResult;
    }
    @ResponseBody
    @RequestMapping(value = "/index/getCode")
    public Object getCode(@RequestBody String jsonData){
        DataSourceContextHolder.setDbType("dataSourceA");
        JSONObject json = JSONObject.parseObject(jsonData);
        String phone = json.getString("phone");
        AJAXResult ajaxResult = new AJAXResult();
        try {
            String reverification = SmsUtils.acquiReverification(phone);
            ajaxResult.setErrcode(0);
            ajaxResult.setErrmsg("??????????????????");
            ajaxResult.setData(reverification);
        }catch (Exception e){
            ajaxResult.setErrcode(500);
            ajaxResult.setErrmsg(e.getMessage());
            e.printStackTrace();
        }
        return ajaxResult;
    }


    private HashMap GetTokenandjsapi_ticket(){
        GetNewAccessToken token = new GetNewAccessToken();
        DataSourceContextHolder.setDbType("dataSourceB");
        HashMap<String, Object> map = null;
        String param = "????????????";
        map = mainService.queryWXToken(param);
        String tokenNew = "";
        String jsapi_ticketNew = "";
        ResponseModel res = token.getNewAccessToken(map);
        if(res.getCode()!=0){
            HashMap parans = (HashMap) res.getData();
            tokenNew = parans.get("access_token").toString();
            jsapi_ticketNew = parans.get("jsapi_ticket").toString();
            mainService.updateJwjxWeixin(parans);
        }else{
            HashMap data = (HashMap) res.getData();
            tokenNew = String.valueOf(data.get("access_token"));
            jsapi_ticketNew = String.valueOf(data.get("jsapi_ticket"));
        }
        HashMap hashMap = new HashMap();
        hashMap.put("access_token",tokenNew);
        hashMap.put("jsapi_ticket",jsapi_ticketNew);
        return hashMap;
    }

    /**
     * ???????????????????????????getusersummary???
     * https://api.weixin.qq.com/datacube/getusersummary?access_token=ACCESS_TOKEN
     * ???????????????????????????getusercumulate???
     * https://api.weixin.qq.com/datacube/getusercumulate?access_token=ACCESS_TOKEN
     */
    @PostMapping("/echart/newUserNum")
    @ResponseBody
    public Object newFollower(){
//        GsonOption option = new GsonOption();
        Option option = new Option();
        GetNewAccessToken getNewAccessToken = new GetNewAccessToken();
        String Token =getNewAccessToken.GetToken("????????????");
        AJAXResult result = new AJAXResult();
        try {
            String url = "https://api.weixin.qq.com/datacube/getusersummary?access_token=" +Token ;
            DataSourceContextHolder.setDbType("dataSourceA");
            TimeParse timeParse = new TimeParse();
            Date now = new Date();
            String begin_date=timeParse.days7Before(now);
            String end_date= timeParse.days1Before(now);
            JsonObject jb = new JsonObject();
            jb.addProperty("begin_date",begin_date);
            jb.addProperty("end_date",end_date);
            JsonNode jsonObject = JsonUtils.stringToJsonNode(HttpRequest.postwx(url,jb.toString()));
            int errcode =0;
            String errmsg="";
            if(jsonObject.has("errcode")){
                errcode = Integer.parseInt(jsonObject.get("errcode").toString());
                errmsg = jsonObject.get("errmsg").toString();
            }
            String msg = "";
            if(errcode==61501){
                msg = "??????????????????";
                result.setErrcode(errcode);
                result.setErrmsg(msg);
            }else if(errcode==0){
                msg = "????????????";
                result.setErrcode(errcode);
                result.setErrmsg(msg);
                String list = jsonObject.get("list").toString();
                String title=begin_date+"??????"+end_date+"??????????????????";
                List<WxUserData> list1 =  JsonUtils.jsonToList(list,WxUserData.class);
                for(int k =0;k<list1.size();k++){
                    for(int i=k+1;i<list1.size();i++){
                        if(list1.get(k).getRef_date().equals(list1.get(i).getRef_date())){
                            if(list1.get(k).getUser_source()!=0){
                                list1.remove(i);
                            }else{
                                list1.remove(k);
                            }
                            break;
                        }
                    }
                }

                List datadetail = new ArrayList();
                List newnum = new ArrayList();
                List cancelnum = new ArrayList();
                for(int n = 0;n<list1.size();n++){
                    datadetail.add(list1.get(n).getRef_date());
                    newnum.add(list1.get(n).getNew_user());
                    cancelnum.add(list1.get(n).getCancel_user());
                }

                EOption eOption = new EOption();
                HashMap map = new HashMap();
                HashMap style = new HashMap();
                map.put("text",title);
                map.put("x","center");
                style.put("fontSize",14);
                map.put("textStyle",style);
                eOption.setTitle(map);

//                map = new HashMap();
//                map.put("left","3%");
//                map.put("right","4%");
//                map.put("bottom","3%");
//                map.put("containLabel",true);
//                eOption.setGrid(map);

                //?????????

                map = new HashMap();
                map.put("trigger","axis");
                map.put("formatter","{b}<br>???????????????{c}");
                eOption.setTooltip(map);

                //X???
                List dataXY = new ArrayList();
                map = new HashMap();
                map.put("type","category");
                map.put("boundaryGap",false);
                map.put("data",datadetail);
                dataXY.add(map);
                eOption.setXAxis(dataXY);

                //Y???
                dataXY = new ArrayList();
                map = new HashMap();
                map.put("type","value");
                dataXY.add(map);
                eOption.setYAxis(dataXY);

                //??????
                dataXY = new ArrayList();
//                datadetail = new ArrayList();
//                for(int i=0;i<10;i++){
//                    datadetail.add(i*10);
//                }
                map = new HashMap();
                map.put("type","line");
                map.put("name","????????????");
                map.put("data",newnum);
                dataXY.add(map);
                map = new HashMap();
                map.put("type","line");
                map.put("name","????????????");
                map.put("data",cancelnum);
                dataXY.add(map);
                eOption.setSeries(dataXY);


                result.setData(eOption);
            }




        } catch (Exception e) {
            e.printStackTrace();
        }


        return result;
    }


}
