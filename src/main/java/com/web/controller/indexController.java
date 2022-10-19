package com.web.controller;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.web.model.*;
import com.web.service.*;
import com.web.utils.HttpRequest;
import com.web.utils.JsonUtils;
import com.web.utils.ResultMap;
import com.web.utils.Sms.SmsUtils;
import com.web.utils.TimeParse;
import com.web.utils.database.DataSourceContextHolder;
import com.web.utils.wx.GetNewAccessToken;
import com.web.utils.wx.WxApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class indexController {
    /**
     *
     *     HashMap map = new HashMap();
     *         try {
     *             map.put("errcode",200);
     *             map.put("errmsg","ok");
     *             map.put("data","");
     *
     *         }catch (Exception e){
     *             map.put("errcode",500);
     *             map.put("errmsg",e.getMessage());
     *         }
     * */
    @Autowired
    ArticleService articleService;

    @Autowired
    CertificationService certificationService;
    @Autowired
    ProblemService problemService;
    @Autowired
    OrderService workorderService;
    @Autowired
    IPService ipService;
    @Autowired
    MemberService memberService;

    ResultMap resultMap = new ResultMap();
    TimeParse timeParse = new TimeParse();

    @RequestMapping(value = "/visitor/login")
    public String toLoginView(){
        return "redirect:../layuiAdmin#/user/login";
    }




    @ResponseBody
    @RequestMapping(value = "/index/news/queryAll")
    public Object indexHomePage( int pageno,  int pagesize){
        DataSourceContextHolder.setDbType("dataSourceA");
        HashMap map = new HashMap();
        HashMap params = new HashMap<>(map);
        String type = "news";
        params.put("type",type);
        params.put("num",5);
        params.put("rows",0);
        try {
//            map.put("start", (pageno - 1) * pagesize);
//            map.put("size", pagesize);
            List<Article> articleNews = this.articleService.queryAllByType(params);
            map.put("errcode",200);
            map.put("errmsg","ok");
            map.put("datas", articleNews);
        } catch (Exception var10) {
            var10.printStackTrace();
            map.put("errcode",500);
            map.put("errmsg",var10.getMessage());
        }

        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/index/getCode")
    public Object getCode(@RequestBody String jsonData){
        DataSourceContextHolder.setDbType("dataSourceA");
        JSONObject json = JSONObject.parseObject(jsonData);
        String phone = json.getString("phone");
        String name = json.getString("name");
        HashMap map = new HashMap();
        try {

            String reverification = SmsUtils.acquiReverification(phone);

            HashMap mapParam = new HashMap();
            mapParam.put("phone",phone);
            mapParam.put("name",name);
            mapParam.put("openid",null);
            memberService.insertVisitor(mapParam);
            map.put("errcode",200);
            map.put("errmsg","ok");
            map.put("datas",reverification);
        }catch (Exception e){
            map.put("errcode",500);
            map.put("errmsg",e.getMessage());
            e.printStackTrace();
        }
        return map;
    }
    @ResponseBody
    @RequestMapping(value = "/index/getIP")
    public Object getIPIndex(@RequestBody String jsonData){
        DataSourceContextHolder.setDbType("dataSourceB");
        JSONObject json = JSONObject.parseObject(jsonData);
        String code = json.getString("code");
        String VerifyCode = json.getString("VerifyCode");
        HashMap map = new HashMap();

        try {
            if(code!=null||code.length()>0){
                if(code.equals(VerifyCode)){
                    String regNO = "800001";
                    IP ip = ipService.queryByRegNO(regNO);
                    String networkIP = ip.getNetworkIP().substring(1,ip.getNetworkIP().length());
                    map.put("errcode",200);
                    map.put("errmsg","ok");
                    map.put("datas", networkIP);
                }else{
                    map.put("errcode",404);
                    map.put("errmsg","验证码错误");
                    map.put("datas", "");
                }
            }else{
                map.put("errcode",403);
                map.put("errmsg","未获取验证码");
                map.put("datas", "");
            }

        }catch (Exception e){
            map.put("errcode",500);
            map.put("errmsg",e.getMessage());
            e.printStackTrace();
        }
        return map;
    }

    @RequestMapping(value = "/")
    public String indexHomePage(Model model,@RequestParam(defaultValue = "1") int pageNum){
        DataSourceContextHolder.setDbType("dataSourceA");
        HashMap map = new HashMap();
        try {
            int pageSize = 5;
            PageHelper.startPage(pageNum,pageSize);
            HashMap map1 = new HashMap();
            map1.put("category","index");
            map1.put("status",1);
            map1.put("flag",0);
            Page<Article> products =  articleService.queryProduct(map1);
            int total = articleService.queryProductAllNum(map1);
            int num = total/pageSize;
            int pages = 1;
            if(num<1){
                pages = 1;
            }else {
                pages = (int) (Math.floor(num)+1);
            }
            map.put("errcode",200);
            map.put("errmsg","ok");
            map.put("pages", pages);
            map.put("pageNum", pageNum);
            map.put("datas", products);

            model.addAttribute("list2",map);
            map1 = new HashMap();
            map1.put("category","index");
            map1.put("status",1);
            map1.put("flag",1);
            Page<Article> productsTab =  articleService.queryProduct(map1);
            int totalTab = articleService.queryProductAllNum(map1);
            int numTab = totalTab/pageSize;
            int pagesTab = 1;
            if(numTab<1){
                pagesTab = 1;
            }else {
                pagesTab = (int) (Math.floor(numTab)+1);
            }
            map = new HashMap();
            map.put("errcode",200);
            map.put("errmsg","ok");
            map.put("pages", pagesTab);
            map.put("pageNum", pageNum);
            map.put("datas", productsTab);
            model.addAttribute("listTab",map);
        }catch (Exception e){
            map.put("errcode",500);
            map.put("errmsg",e.getMessage());
            model.addAttribute("list2",map);
            e.printStackTrace();
        }
        return "home";
    }



    @RequestMapping(value = "/index/ip")
    public String ipindex(){
        return "/ip";
    }




    @RequestMapping(value = "/aboutus")
    public String aboutus(){
        return "/aboutus";
    }

    /**
     * @Author yyj
     * @Description 消息推送
     * @Date 2021-07-01
     * @Param
     * @return
     **/
    @Autowired
    NoneMainService mainService;
    public ResultMap sendmsgToWx(List data){
        GetNewAccessToken getNewAccessToken = new GetNewAccessToken();
        ResultMap resultMap = new ResultMap();
        resultMap.setMsg("ok");
        resultMap.setCode(0);
        try {
            String accessToken = getNewAccessToken.GetToken("经纬捷讯");
            String urlToken = "";
            urlToken = WxApi.templateSendMs.replace("ACCESS_TOKEN", accessToken);
            HashMap param = new HashMap();
            param.put("title", "客服审批提醒");
            DataSourceContextHolder.setDbType("dataSourceA");
            HashMap template = mainService.queryTemplate(param);
            String memo = template.get("memo").toString();
            String[] list1= memo.split("#");
            JsonObject keyword = new JsonObject();
            JsonObject datahead = new JsonObject();

            String str = "有新的客户在官网提交表单，请及时查看";
            datahead.addProperty("value",str);
            keyword.add("first",datahead);



            String[] list2= list1[1].split("@");
            for(int i=1;i<=list2.length-1;i++){
                datahead = new JsonObject();
                datahead.addProperty("value",data.get(i-1).toString());
                keyword.add("keyword"+i,datahead);
            }
            datahead = new JsonObject();
            datahead.addProperty("value",data.get(data.size()-1).toString());
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
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.setMsg(e.getMessage());
            resultMap.setCode(500);
        }
        return  resultMap ;
    }



    /**
     * @Author yyj
     * @Description 页面提交联系内容
     * @Date 2021-04-01
     * @Param
     * @return
     **/
    @ResponseBody
    @PostMapping(value = "/index/about/userMsgSend")
    public Object articleUpload(@RequestBody String jsonData,
                                HttpServletRequest httpServletRequest){
        JSONObject json = JSONObject.parseObject(jsonData);
        try {
            String content = json.getString("message");
            String email = json.getString("email");
            String phone = json.getString("phone");
            String nickName = json.getString("name");
            String type = json.getString("type");
            String createTime = timeParse.convertDate2String(new Date(),"yyyy-MM-dd HH:mm:ss");
            MessageWorkorder messageWorkorder = new MessageWorkorder();
            messageWorkorder.setEmail(email);
            messageWorkorder.setMsgContent(content);
            messageWorkorder.setPhone(phone);
            messageWorkorder.setType(type);
            messageWorkorder.setNickName(nickName);
            messageWorkorder.setCreateTime(createTime);
            workorderService.insertWorkorder(messageWorkorder);
            List jb= new ArrayList();
            jb.add(nickName);
            jb.add(phone);
            jb.add(createTime);
            jb.add("主页需求定制表单提交");
            jb.add("内容："+content);
            sendmsgToWx(jb);

            resultMap.setCode(0);
            resultMap.setMsg("ok");
        }catch (Exception e){
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
        }

        return resultMap;

    }





    @RequestMapping(value = "/support")
    public String support(){
        return "/support";
    }
    @RequestMapping(value = "/problem")
    public String problem(){
        return "/problem";
    }

    //在线演示
    @RequestMapping(value = "/display")
    public String display(Model model){
        DataSourceContextHolder.setDbType("dataSourceA");
        HashMap map = new HashMap();
        try {
            Servers servers = certificationService.queryAllservers();
            map.put("errcode",200);
            map.put("errmsg","ok");
            map.put("datas", servers);
            model.addAttribute("list",map);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("errcode",500);
            map.put("errmsg",e.getMessage());
        }
        return "/productDisplay";
    }
    @RequestMapping(value = "/career")
    public String career(){
        return "/career";
    }


    @ResponseBody
    @RequestMapping(value = "/index/product/queryAll")
    public Object APIproduct(int pageno,  int pagesize ){
        DataSourceContextHolder.setDbType("dataSourceA");
        HashMap map = new HashMap();
        HashMap<String, Object> res= new HashMap<>();
        HashMap<String, Object> datas= new HashMap<>();
        try {
            int pageSize = 5;
            PageHelper.startPage(1,pageSize);
            HashMap map1 = new HashMap();
            map1.put("category","index");
            map1.put("status",1);
            map1.put("flag",0);
            Page<Article> products =  articleService.queryProduct(map1);
//            int total = articleService.queryProductAllNum(map1);
            map.put("datas", products);
            datas.put("datas1",products);
            map1 = new HashMap();
            map1.put("category","index");
            map1.put("status",1);
            map1.put("flag",1);
            Page<Article> productsTab =  articleService.queryProduct(map1);
//            int totalTab = articleService.queryProductAllNum(map1);
            map = new HashMap();
            map.put("datas", productsTab);
            datas.put("datas2",productsTab);
            res.put("datas",datas);
        }catch (Exception e){
            map.put("errcode",500);
            map.put("errmsg",e.getMessage());
            e.printStackTrace();
            return "error/500";
        }

        return res;
    }
    @RequestMapping(value = "/product")
    public String product(Model model,@RequestParam(defaultValue = "1") int pageNum){
        DataSourceContextHolder.setDbType("dataSourceA");
        HashMap map = new HashMap();
        HashMap<Object, String> imgUrl= new HashMap<>();
        try {
            int pageSize = 5;
            PageHelper.startPage(pageNum,pageSize);
            HashMap map1 = new HashMap();
            map1.put("category","index");
            map1.put("status",1);
            map1.put("flag",0);
            Page<Article> products =  articleService.queryProduct(map1);
            int total = articleService.queryProductAllNum(map1);
            int num = total/pageSize;
            int pages = 1;
            if(num<1){
                pages = 1;
            }else {
                pages = (int) (Math.floor(num)+1);
            }
            map.put("errcode",200);
            map.put("errmsg","ok");
            map.put("pages", pages);
            map.put("pageNum", pageNum);
            map.put("datas", products);

            model.addAttribute("list2",map);
            map1 = new HashMap();
            map1.put("category","index");
            map1.put("status",1);
            map1.put("flag",1);
            Page<Article> productsTab =  articleService.queryProduct(map1);
            int totalTab = articleService.queryProductAllNum(map1);
            int numTab = totalTab/pageSize;
            int pagesTab = 1;
            if(numTab<1){
                pagesTab = 1;
            }else {
                pagesTab = (int) (Math.floor(numTab)+1);
            }
            map = new HashMap();
            map.put("errcode",200);
            map.put("errmsg","ok");
            map.put("pages", pagesTab);
            map.put("pageNum", pageNum);
            map.put("datas", productsTab);
            model.addAttribute("listTab",map);
        }catch (Exception e){
            map.put("errcode",500);
            map.put("errmsg",e.getMessage());
            model.addAttribute("list",map);
            e.printStackTrace();
            return "error/500";
        }
        return "/product";
    }


    @RequestMapping(value = "/admin")
    public String layuiAdminIndex(){
        return "layuiAdmin/index";
    }





    //aboutus 页面的table 数据获取
    @ResponseBody
    @RequestMapping(value = "/index/aboutus/table/certificationTable", produces = "application/json; charset=utf-8")
    public ResultMap<List<Certification>> Certification_queryAll(com.web.utils.Page page){
        List<Certification> certificationList = new ArrayList<>();
        DataSourceContextHolder.setDbType("dataSourceA");

        int totals = 0;
        try {
            certificationList=certificationService.queryAll(page);
            page.setTotalRecord(totals);
        }catch (Exception e){
        }
        return new ResultMap<List<Certification>>("",certificationList,0,totals);
    }



    //aboutus 页面的table 数据获取
    @ResponseBody
    @RequestMapping(value = "/index/problem/queryAll", produces = "application/json; charset=utf-8")
    public ResultMap<List<Problem>> problem_queryAll(com.web.utils.Page page , @RequestBody String jsonData){
        DataSourceContextHolder.setDbType("dataSourceA");

        List<Problem> problemList = new ArrayList<>();
        int totals = 0;
        try {
            if(!jsonData.equals("null")&&jsonData!=null){
                JSONObject json = JSONObject.parseObject(jsonData);
                if(json.containsKey("keyword")){
                    String txt = json.getString("keyword");
                    page.setKeyWord(txt);
                }
            }
            problemList=problemService.queryAll(page);
            page.setTotalRecord(totals);
        }catch (Exception e){
            e.printStackTrace();

        }
        return new ResultMap<List<Problem>>("",problemList,0,totals);
    }



}
