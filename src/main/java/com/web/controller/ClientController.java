package com.web.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.web.model.jwjx.*;
import com.web.service.ClientService;
import com.web.utils.*;
import com.web.utils.database.DataSourceContextHolder;
import jxl.Sheet;
import jxl.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static java.lang.Integer.parseInt;

@RequestMapping("/client")
@RestController
public class ClientController {
    @Resource(name="Delcode")
    private String Delcode ;
    TimeParse timeParse = new TimeParse();
    HTMLSpirit htmlSpirit = new HTMLSpirit();

    ResultMap resultMap = new ResultMap();
    @Autowired
    ClientService clientService;
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);



    @ResponseBody
    @RequestMapping(value = "/jwjx_weixin/delete", produces = "application/json; charset=utf-8")
    public Object deletjwjx_weixin( @RequestBody String jsonData) {
        AJAXResult result = new AJAXResult();
        DataSourceContextHolder.setDbType("dataSourceB");
        String res = "";
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            String code = json.getString("code");
            String id = json.getString("id");
            if(code.equals(Delcode)){
                clientService.deletejwjx_weixinByid(id);
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
    @PostMapping(value = "/jwjx_weixin/deletes")
    public Object deletesjwjx_weixin(@RequestBody String jsonData){
        DataSourceContextHolder.setDbType("dataSourceB");
        AJAXResult result = new AJAXResult();
        String res = "";
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            JSONArray ids =json.getJSONArray("ids");
            String code = json.getString("code");
            if(code.equals(Delcode)){
                for(int i =0;i<ids.size();i++){
                    String  id =ids.get(i).toString();
                    clientService.deletejwjx_weixinByid(id);
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
    @RequestMapping(value = "/jwjx_weixin/upload", produces = "application/json; charset=utf-8")
    public Object jwjx_weixin_upload( @RequestBody String jsonData) {
        AJAXResult result = new AJAXResult();
        DataSourceContextHolder.setDbType("dataSourceB");
        String res = "";
        try {
            HashMap item = JSONObject.parseObject(jsonData, HashMap.class);
            if(item.containsKey("isPublish")){
                if(item.get("isPublish")!=null){
                    int isPublish = Integer.parseInt(item.get("isPublish").toString());
                    item.put("isPublish",isPublish);
                }
            }
            clientService.insertjwjx_weixin(item);
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
    @RequestMapping(value = "/jwjx_weixin/edit", produces = "application/json; charset=utf-8")
    public Object jwjx_weixin_edit( @RequestBody String jsonData) {
        AJAXResult result = new AJAXResult();
        DataSourceContextHolder.setDbType("dataSourceB");
        String res = "";
        try {
            HashMap item = JSONObject.parseObject(jsonData, HashMap.class);
            if(item.containsKey("isPublish")){
                if(item.get("isPublish")!=null){
                    int isPublish = Integer.parseInt(item.get("isPublish").toString());
                    item.put("isPublish",isPublish);
                }
            }
            clientService.updatejwjx_weixin(item);
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
    /**
     *
     * @param page
     * @param limit
     * @param request jwjx.weixin 表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/jwjx_weixin/queryAll", produces = "application/json; charset=utf-8")
    public Object query_jwjx_weixin( Page page, @RequestParam("limit") int limit, HttpServletRequest request) {
        ResultMap resultMap = new ResultMap();
        DataSourceContextHolder.setDbType("dataSourceB");
        try {
            int totals = 0;
            page.setRows(limit);
            String txt = request.getParameter("keyword");
            page.setKeyWord(txt);
            totals=clientService.selectPageCountjwjxweixin(page);
            List<HashMap<String, Object>> list=clientService.selectPageListjwjxweixin(page);
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
     * 远程连接表
     * gnss_system.ClientTerminaldb
     * */
    @ResponseBody
    @RequestMapping(value = "/gnss_system/ClientTerminaldb/upload")
    public Object ClientTerminaldb_upload( @RequestPart("file") MultipartFile Mutifile) {
        AJAXResult result = new AJAXResult();
        DataSourceContextHolder.setDbType("dataSourceB");
        List<Gnss_system_ClientTerminaldb> list = new ArrayList<Gnss_system_ClientTerminaldb>();
        String res = "";
        try {
            File newFile = FileUtil.multipartFileToFile(Mutifile);
//            File file = new File("C:\\Users\\jwjx\\Desktop\\GPS设备录入.xlsx");
            EasyExcel.read(newFile, Gnss_system_ClientTerminaldb.class, new ExcelRead()).sheet().doRead();
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
    @RequestMapping(value = "/gnss_system/ClientTerminaldb/delete", produces = "application/json; charset=utf-8")
    public Object deleteClientTerminaldb( @RequestBody String jsonData) {
        AJAXResult result = new AJAXResult();
        DataSourceContextHolder.setDbType("dataSourceB");
        String res = "";
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            String code = json.getString("code");
            String id = json.getString("id");
            if(code.equals(Delcode)){
                clientService.deletegnss_system_ClientTerminaldbByid(id);
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
    @PostMapping(value = "/gnss_system/ClientTerminaldb/deletes")
    public Object deletesClientTerminaldb(@RequestBody String jsonData){
        DataSourceContextHolder.setDbType("dataSourceB");
        AJAXResult result = new AJAXResult();
        String res = "";
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            JSONArray ids =json.getJSONArray("ids");
            String code = json.getString("code");
            if(code.equals(Delcode)){
                for(int i =0;i<ids.size();i++){
                    String  id =ids.get(i).toString();
                    clientService.deletegnss_system_ClientTerminaldbByid(id);
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
    @RequestMapping(value = "/gnss_system/ClientTerminaldb/queryAll", produces = "application/json; charset=utf-8")
    public Object queryClientTerminaldb( Page page, @RequestParam("limit") int limit, HttpServletRequest request) {
        ResultMap resultMap = new ResultMap();
        DataSourceContextHolder.setDbType("dataSourceB");
        try {
            int totals = 0;
            page.setRows(limit);
            String carNumber = request.getParameter("carNumber");
            String simNumber = request.getParameter("simNumber");
            if(simNumber==null||simNumber.length()<=0){
                simNumber=null;
            }
            if(carNumber==null||carNumber.length()<=0){
                carNumber =null;
            }
            HashMap param = new HashMap();
            param.put("simNumber", simNumber);
            param.put("carNumber", carNumber);
            page.setData(param);
            totals=clientService.selectPageCountgnss_system_ClientTerminaldb(page);
            List<HashMap<String, Object>> companyList=clientService.selectPageListgnss_system_ClientTerminaldb(page);
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
    /**
     * 远程连接表
     * gnss_system.Clientdb
     * */
    @ResponseBody
    @RequestMapping(value = "/gnss_system_Clientdb/upload", produces = "application/json; charset=utf-8")
    public Object gnss_system_Clientdb_upload( @RequestBody String jsonData) {
        AJAXResult result = new AJAXResult();
        DataSourceContextHolder.setDbType("dataSourceB");
        String res = "";
        try {
            Gnss_system_Clientdb item = JSONObject.parseObject(jsonData, Gnss_system_Clientdb.class);
            clientService.insertGnss_system_Clientdb(item);
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
    @RequestMapping(value = "/gnss_system_Clientdb/edit", produces = "application/json; charset=utf-8")
    public Object gnss_system_Clientdb_edit( @RequestBody String jsonData) {
        AJAXResult result = new AJAXResult();
        DataSourceContextHolder.setDbType("dataSourceB");
        String res = "";
        try {
            Gnss_system_Clientdb item = JSONObject.parseObject(jsonData, Gnss_system_Clientdb.class);
            clientService.updateGnss_system_Clientdb(item);
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
    @RequestMapping(value = "/gnss_system_Clientdb/queryAll", produces = "application/json; charset=utf-8")
    public Object querygnss_system_Clientdb( Page page, @RequestParam("limit") int limit, HttpServletRequest request) {
        ResultMap resultMap = new ResultMap();
        DataSourceContextHolder.setDbType("dataSourceB");
        try {
            int totals = 0;
            page.setRows(limit);
            String clientName = request.getParameter("clientName");
            String clientCode = request.getParameter("clientCode");
            if(clientCode==null||clientCode.length()<=0){
                clientCode=null;
            }
            if(clientName==null||clientName.length()<=0){
                clientName =null;
            }
            HashMap param = new HashMap();
            param.put("clientCode", clientCode);
            param.put("clientName", clientName);
            page.setData(param);
            totals=clientService.selectPageCountgnss_system_Clientdb(page);
            List<HashMap<String, Object>> companyList=clientService.selectPageListgnss_system_Clientdb(page);
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

    @ResponseBody
    @RequestMapping(value = "/gnss_system_Clientdb/delete", produces = "application/json; charset=utf-8")
    public Object deletegnss_system_Clientdb( @RequestBody String jsonData) {
        AJAXResult result = new AJAXResult();
        DataSourceContextHolder.setDbType("dataSourceB");
        String res = "";
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            String code = json.getString("code");
            String id = json.getString("id");
            if(code.equals(Delcode)){
                clientService.deletegnss_system_ClientdbByid(id);
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
    @PostMapping(value = "/gnss_system_Clientdb/deletes")
    public Object deletesgnss_system_Clientdb(@RequestBody String jsonData){
        DataSourceContextHolder.setDbType("dataSourceB");
        AJAXResult result = new AJAXResult();
        String res = "";
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            JSONArray ids =json.getJSONArray("ids");
            String code = json.getString("code");
            if(code.equals(Delcode)){
                for(int i =0;i<ids.size();i++){
                    String  id =ids.get(i).toString();
                    clientService.deletegnss_system_ClientdbByid(id);
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

    /**
     * 远程连接表
     * egt。remote_mode
     * */
    @ResponseBody
    @RequestMapping(value = "/egt_remote_mode/upload", produces = "application/json; charset=utf-8")
    public Object egt_remote_mode_upload( @RequestBody String jsonData) {
        AJAXResult result = new AJAXResult();
        DataSourceContextHolder.setDbType("dataSourceB");
        String res = "";
        try {
            EgtRemoteMode egtRemoteMode = JSONObject.parseObject(jsonData, EgtRemoteMode.class);
            clientService.insertEgtRemoteMode(egtRemoteMode);
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
    @RequestMapping(value = "/egt_remote_mode/edit", produces = "application/json; charset=utf-8")
    public Object egt_remote_mode_edit( @RequestBody String jsonData) {
        AJAXResult result = new AJAXResult();
        DataSourceContextHolder.setDbType("dataSourceB");
        String res = "";
        try {
            EgtRemoteMode egtRemoteMode = JSONObject.parseObject(jsonData, EgtRemoteMode.class);
            clientService.updateEgtRemoteMode(egtRemoteMode);
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
    @RequestMapping(value = "/egt_remote_mode/delete", produces = "application/json; charset=utf-8")
    public Object deleteegt_remote_mode( @RequestBody String jsonData) {
        AJAXResult result = new AJAXResult();
        DataSourceContextHolder.setDbType("dataSourceB");
        String res = "";
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            String code = json.getString("code");
            String id = json.getString("id");
            if(code.equals(Delcode)){
                clientService.deleteegt_remote_modeByid(id);
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
    @PostMapping(value = "/egt_remote_mode/deletes")
    public Object deletesegt_remote_mode(@RequestBody String jsonData){
        DataSourceContextHolder.setDbType("dataSourceB");
        AJAXResult result = new AJAXResult();
        String res = "";
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            JSONArray ids =json.getJSONArray("ids");
            String code = json.getString("code");
            if(code.equals(Delcode)){
                for(int i =0;i<ids.size();i++){
                    String  id =ids.get(i).toString();
                    clientService.deleteegt_remote_modeByid(id);
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
    @RequestMapping(value = "/egt_remote_mode/queryAll", produces = "application/json; charset=utf-8")
    public Object queryegt_remote_mode( Page page, @RequestParam("limit") int limit, HttpServletRequest request) {
        ResultMap resultMap = new ResultMap();
        DataSourceContextHolder.setDbType("dataSourceB");
        String res = "";
        try {
            int totals = 0;
            page.setRows(limit);
            String remoteType = request.getParameter("remoteType");
            String companyName = request.getParameter("companyName");
            if(remoteType==null||remoteType.length()<=0){
                remoteType=null;
            }
            if(companyName==null||companyName.length()<=0){
                companyName =null;
            }
            HashMap param = new HashMap();
            param.put("companyName", companyName);
            param.put("remoteType", remoteType);
            page.setData(param);
            totals=clientService.selectPageCountegt_remote_mode(page);
            List<HashMap<String, Object>> companyList=clientService.selectPageListegt_remote_mode(page);
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






    /**
     * 用户维护
     * egt。clientInfo
     * */
    @ResponseBody
    @RequestMapping(value = "/egt_clientInfo/queryAll", produces = "application/json; charset=utf-8")
    public Object queryegt_client( Page page, @RequestParam("limit") int limit, HttpServletRequest request) {
        ResultMap resultMap = new ResultMap();
        DataSourceContextHolder.setDbType("dataSourceB");
        String res = "";
        try {
            int totals = 0;
            page.setRows(limit);
            String nickname = request.getParameter("nickname");
            String strsex = request.getParameter("sex");
            int sex =-1;
            if(nickname==null||nickname.length()<=0){
                nickname=null;
            }
            if(strsex==null||strsex.length()<=0){
                sex =-1;
            }else{
                sex = Integer.parseInt(strsex);
            }

            HashMap param = new HashMap();
            param.put("sex", sex);
            param.put("nickname", nickname);
            page.setData(param);
            totals=clientService.selectPageCountjwjxclientInfo(page);
            List<HashMap<String, Object>> companyList=clientService.selectPageListjwjxclientInfo(page);
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

    /**
     * 用户维护
     * jwjx。applet
     * */
    @ResponseBody
    @RequestMapping(value = "/applet/upload", produces = "application/json; charset=utf-8")
    public Object applet_upload( @RequestBody String jsonData) {
        AJAXResult result = new AJAXResult();
        DataSourceContextHolder.setDbType("dataSourceB");
        String res = "";
        try {
            Applet applet = JSONObject.parseObject(jsonData, Applet.class);
            clientService.insertApplet(applet);
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
    @RequestMapping(value = "/applet/edit", produces = "application/json; charset=utf-8")
    public Object applet_edit( @RequestBody String jsonData) {
        AJAXResult result = new AJAXResult();
        DataSourceContextHolder.setDbType("dataSourceB");
        String res = "";
        try {
            Applet applet = JSONObject.parseObject(jsonData, Applet.class);
            clientService.updateApplet(applet);
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
    @RequestMapping(value = "/applet/delete", produces = "application/json; charset=utf-8")
    public Object deleteapplet( @RequestBody String jsonData) {
        AJAXResult result = new AJAXResult();
        DataSourceContextHolder.setDbType("dataSourceB");
        String res = "";
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            String code = json.getString("code");
            String id = json.getString("id");
            if(code.equals(Delcode)){
                clientService.deleteAppletByid(id);
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
    @PostMapping(value = "/applet/deletes")
    public Object deletesapplet(@RequestBody String jsonData){
        DataSourceContextHolder.setDbType("dataSourceB");
        AJAXResult result = new AJAXResult();
        String res = "";
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            JSONArray ids =json.getJSONArray("ids");
            String code = json.getString("code");
            if(code.equals(Delcode)){
                for(int i =0;i<ids.size();i++){
                    String  id =ids.get(i).toString();
                    clientService.deleteAppletByid(id);
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
    @RequestMapping(value = "/applet/egt_company/queryegt_company", produces = "application/json; charset=utf-8")
    public Object queryegt_company( @RequestBody String jsonData) {
        AJAXResult result = new AJAXResult();
        DataSourceContextHolder.setDbType("dataSourceB");
        String res = "";
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            String  regNOList = json.getString("regNOList");
            String[] regNOListName = regNOList.split(",");
            List datamsg=new ArrayList();
            for(String str:regNOListName) {
                HashMap param = new HashMap();
                param.put("regNO", str);
                List<HashMap<String, Object>> ClientInfo = clientService.queryEgtcompany(param);
                for(HashMap<String, Object> map :ClientInfo){
                    String  companyName =  map.get("companyName").toString();
                    datamsg.add(str+"("+companyName+")");
                }
            }
            result.setErrcode(0);
            result.setData(datamsg);
            result.setErrmsg(res);
        }catch (Exception e){
            e.printStackTrace();
            result.setErrcode(500);
            result.setErrmsg(e.getMessage());
        }
        return result;
    }
    @ResponseBody
    @RequestMapping(value = "/applet/queryAll", produces = "application/json; charset=utf-8")
    public Object applet_queryAll(Page page, @RequestParam("limit") int limit
            , HttpServletRequest request) {
        DataSourceContextHolder.setDbType("dataSourceB");
        try {
            List<HashMap<String, Object>> companyList = new ArrayList<>();
            HashMap<String, Object> map = new HashMap();
            int totals = 0;
            page.setRows(limit);
//            HashMap map2 = (HashMap) JsonUtils.getRequestConvertBean(request,Applet.class);
            String nickName = request.getParameter("nickName");
            String userName = request.getParameter("userName");
            String phone = request.getParameter("phone");
            String userType = request.getParameter("userType");
            String regNO = request.getParameter("regNO");
            String companyName = request.getParameter("companyName");
//            String appID = request.getParameter("appID");
//            if(appID==null||appID.length()<=0){
//                appID=null;
//            }
            if(companyName==null||companyName.length()<=0){
                companyName=null;
            }
            if(regNO==null||regNO.length()<=0){
                regNO=null;
            }
            if(nickName==null||nickName.length()<=0){
                nickName=null;
            }
            if(userName==null||userName.length()<=0){
                userName=null;
            }
            if(phone==null||phone.length()<=0){
                phone=null;
            }
            if(userType==null||userType.length()<=0){
                userType=null;
            }
            if(userType==null||userType.length()<=0){
                userType=null;
            }
//            map.put("appID",appID);
            map.put("companyName",companyName);
            map.put("regNO",regNO);
            map.put("nickName",nickName);
            map.put("userName",userName);
            map.put("phone",phone);
            map.put("userType",userType);
            page.setData(map);
            totals=clientService.selectPageCountapplet(page);
            companyList=clientService.selectPageListapplet(page);
            page.setTotalRecord(totals);
            resultMap.setCode(0);
            resultMap.setMsg("");
            resultMap.setData(companyList);
            resultMap.setCount(totals);
        } catch (Exception e) {
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return resultMap;
    }
    /**
     * 客户维护
     * egt.company
     * */


    @ResponseBody
    @RequestMapping(value = "/company/jwjx_clientInfo/queryWxopenids", produces = "application/json; charset=utf-8")
    public Object queryWxopenids( @RequestBody String jsonData) {
        AJAXResult result = new AJAXResult();
        DataSourceContextHolder.setDbType("dataSourceB");
        String res = "";
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            String  wxOpenids = json.getString("wxOpenids");
            String[] wxopendidName = wxOpenids.split(",");
            List datamsg=new ArrayList();
            for(String str:wxopendidName) {
                HashMap param = new HashMap();
                param.put("openid", str);
                List<HashMap<String, Object>> ClientInfo = clientService.queryjwjxClientInfo(param);
                for(HashMap<String, Object> map :ClientInfo){
//                    String  username = (String) map.get("userName");
                    String  nickname =  map.get("nickname").toString();
                    datamsg.add(str+"("+nickname+")");
                }
            }
            result.setErrcode(0);
            result.setData(datamsg);
            result.setErrmsg(res);
        }catch (Exception e){
            e.printStackTrace();
            result.setErrcode(500);
            result.setErrmsg(e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/company/queryAll", produces = "application/json; charset=utf-8")
    public Object company_queryAll(Page page, @RequestParam("limit") int limit
            , HttpServletRequest request) {
        DataSourceContextHolder.setDbType("dataSourceB");
        try {
            List<HashMap<String, Object>> companyList = new ArrayList<>();
            HashMap<String, Object> map = new HashMap();
            int totals = 0;
            page.setRows(limit);
            String companyname = request.getParameter("companyname");
            String companyPK = request.getParameter("companyPK");
            if(companyname==null||companyname.length()<=0){
                companyname=null;
            }
            if(companyPK==null||companyPK.length()<=0){
                companyPK=null;
            }
            map.put("companyName",companyname);
            map.put("companyPK",companyPK);
            page.setData(map);
            totals=clientService.selectPageCount(page);
            companyList=clientService.selectPageList(page);

            page.setTotalRecord(totals);
            resultMap.setCode(0);
            resultMap.setMsg("");
            resultMap.setData(companyList);
            resultMap.setCount(totals);
        } catch (Exception e) {
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return resultMap;
    }
    @ResponseBody
    @RequestMapping(value = "/newCompany/upload", produces = "application/json; charset=utf-8")
    public Object newCompany_upload( @RequestBody String jsonData) {
        AJAXResult result = new AJAXResult();
        DataSourceContextHolder.setDbType("dataSourceB");
        String res = "";
        try {
            EgtCompany egtCompany = JSONObject.parseObject(jsonData, EgtCompany.class);
            egtCompany.setCompanypk(egtCompany.getAccredit());
            clientService.insertEgtCompany(egtCompany);
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
    @RequestMapping(value = "/company/edit", produces = "application/json; charset=utf-8")
    public Object editEgt_company_table( @RequestBody String jsonData) {
        AJAXResult result = new AJAXResult();
        DataSourceContextHolder.setDbType("dataSourceB");
        String res = "";
        try {
            EgtCompany egtCompany = JSONObject.parseObject(jsonData, EgtCompany.class);
            clientService.updateEgtCompany(egtCompany);
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
    @RequestMapping(value = "/company/delete", produces = "application/json; charset=utf-8")
    public Object deleteUserWorker( @RequestBody String jsonData) {
        AJAXResult result = new AJAXResult();
        DataSourceContextHolder.setDbType("dataSourceB");
        String res = "";
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            String code = json.getString("code");
            int id = Integer.parseInt(json.getString("id"));
            if(code.equals(Delcode)){
                clientService.deleteCompanyByid(id);
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
    @PostMapping(value = "/company/deletes")
    public Object Wokerdeletes(@RequestBody String jsonData){
        DataSourceContextHolder.setDbType("dataSourceB");
        AJAXResult result = new AJAXResult();
        String res = "";
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            JSONArray ids =json.getJSONArray("ids");
            String code = json.getString("code");
            if(code.equals(Delcode)){
                for(int i =0;i<ids.size();i++){
                    int id = (int) ids.get(i);
                    clientService.deleteCompanyByid(id);
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








}
