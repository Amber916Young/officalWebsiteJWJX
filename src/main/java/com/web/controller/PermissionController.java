package com.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import com.web.model.Member;
import com.web.model.Permission;
import com.web.model.PermissionMenu;
import com.web.model.PermissionTree;
import com.web.service.MemberService;
import com.web.service.PermissionService;
import com.web.service.RoleService;
import com.web.utils.*;
import com.web.utils.database.DataSourceContextHolder;
import com.web.utils.wx.WxApi;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.*;


@Controller
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    PermissionService permissionService;
    @Autowired
    RoleService roleService;
    @Autowired
    MemberService memberService;
    private String authUriSet ="authUriSet";


    /**
     * @Author yyj
     * @Description 角色删除 批量
     * @Date 2021-06-24 
     * @Param roleids
     * @return 
     **/
    @ResponseBody
    @RequestMapping("/role/deletes")
    public Object RoleDeletes(@RequestBody String jsonData){
        DataSourceContextHolder.setDbType("dataSourceA");
        ResponseModel responseModel = new ResponseModel();
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            JSONArray ids =json.getJSONArray("ids");
            for(int i =0;i<ids.size();i++){
                int id = (int) ids.get(i);
                if (id!=1&&id!=2){
                    HashMap map = new HashMap();
                    map.put("roleid",id);
                    roleService.deleteRole(id);
                    memberService.deleteRoleUser(id);
                    permissionService.deleteRolePermissions(map);
                }
            }
            responseModel.setCode(0);
            responseModel.setMsg("删除成功");
        } catch ( Exception e ) {
            e.printStackTrace();
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }
    @ResponseBody
    @RequestMapping("/role/delete")
    public Object RoleDelete(@RequestBody Integer id){
        DataSourceContextHolder.setDbType("dataSourceA");
        ResponseModel responseModel = new ResponseModel();
        try {
            if (id==null){
                responseModel.setCode(500);
                responseModel.setMsg("id不存在");
                return responseModel;
            }
            if (id==1||id==2){
                responseModel.setCode(500);
                responseModel.setMsg("默认角色不可删除");
                return responseModel;
            }
            HashMap map = new HashMap();
            map.put("roleid",id);
            roleService.deleteRole(id);
            memberService.deleteRoleUser(id);
            permissionService.deleteRolePermissions(map);
            responseModel.setCode(0);
            responseModel.setMsg("删除成功");
        } catch ( Exception e ) {
            e.printStackTrace();
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }



    TimeParse timeParse = new TimeParse();
    @RequestMapping(value = "/user/login", produces = "application/json; charset=utf-8")
    public Object UserLoginPage() {
        return "auth/login";
    }

    private void updateUserlogin(HttpServletRequest request,Member user) throws Exception{
        String ip = IPUtil.getIpAddress(request);
        String ipaddress = IPUtil.getCityInfo(ip);
        String loginTime = timeParse.convertDate2String(new Date(),"yyyy-MM-dd HH:mm:ss");
        user.setLoginIp(ip);
        user.setLoginPosition(ipaddress);
        user.setLoginTime(loginTime);
        memberService.updateMember(user);
    }
    @ResponseBody
    @RequestMapping(value = "/web/login", produces = "application/json; charset=utf-8")
    public Object UserLogin(@RequestBody String jsonData, HttpServletRequest request, HttpServletResponse response,
                            HttpSession session){
        ResponseModel responseModel = new ResponseModel();
        DataSourceContextHolder.setDbType("dataSourceA");
        try {
            jsonData = URLDecoder.decode(jsonData).replaceAll("=","");
            JSONObject json = JSONObject.parseObject(jsonData);
            Map<String, HttpSession> userMap = (Map<String, HttpSession>) request.getServletContext().getAttribute("userMap");
            if (userMap == null) {
                userMap = new HashMap<String, HttpSession>();
            }
            String pass =json.getString("password");
            String username =json.getString("username");
            Member userlogin = (Member) session.getAttribute("user");
            if (userlogin != null) {
                responseModel.setCode(500);
                responseModel.setMsg("您已经登陆了,不可重复登陆");
                return responseModel;
            }
            if (userMap.get(username) == null) {
                //对哈希码进行加密
//                String recodePwd = convertMD5(password);
                HashMap param = new HashMap();
                param.put("username",username);
                param.put("password",pass);
                Member cuser = memberService.authenticate(param);
                if (cuser == null) {
                    responseModel.setCode(500);
                    responseModel.setMsg("用户名或者密码错误，请重试");
                    return responseModel;
                } else {
                    updateUserlogin(request,cuser);
                    userMap.put(cuser.getUsername(), session);
                    request.getServletContext().setAttribute("userMap", userMap);
                    request.changeSessionId();
                    session.setAttribute("user", cuser);
                    int numberOfSessions = userMap.size();
                    session.setAttribute("numberOfSessions", numberOfSessions);
                    responseModel.setCode(0);
                    String asscess_token = UserToken.token(username,pass);
                    responseModel.setData(asscess_token);
                    responseModel.setMsg("登陆成功");
                    responseModel.setCode(0);
                    return responseModel;
                }
            }else {
                HashMap param = new HashMap();
                param.put("username",username);
                param.put("password",pass);
                Member cuser = memberService.authenticate(param);
                if (cuser == null) {
                    responseModel.setCode(500);
                    responseModel.setMsg("用户名或者密码错误，请重试");
                    return responseModel;
                } else {
                    if(session.getAttribute("user")==null){
                        session.setAttribute("user", cuser);
                    }
                    int numberOfSessions = userMap.size();
                    if(session.getAttribute("numberOfSessions")==null){
                        session.setAttribute("numberOfSessions", numberOfSessions);
                    }
                    updateUserlogin(request,cuser);
                    responseModel.setCode(0);
                    String asscess_token = UserToken.token(username,pass);
                    responseModel.setData(asscess_token);
                    responseModel.setMsg("登陆成功");
                    responseModel.setCode(0);
                    return responseModel;
                }
            }

        }catch (Exception e){
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }
    @ResponseBody
    @RequestMapping(value = "/web/userinfo", produces = "application/json; charset=utf-8")
    public Object userinfo(@RequestParam("access_token") String access_token, HttpServletRequest request, HttpServletResponse response,
                           HttpSession session) {
        ResponseModel responseModel = new ResponseModel();
        DataSourceContextHolder.setDbType("dataSourceA");
        try {
            responseModel = authAccess(access_token,session,request);
        } catch (Exception e) {
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }
    /**
     * @Author yyj
     * @Description
     * @Date 2021-06-24
     * @Param 默认进入的页面应该是这个用户信息页面
     * @return
     **/
    @ResponseBody
    @RequestMapping(value = "/userinfo/load", produces = "application/json; charset=utf-8")
    public Object userinfoPage(@RequestParam("access_token") String access_token, HttpServletRequest request, HttpServletResponse response,
                            HttpSession session) {
        ResponseModel responseModel = new ResponseModel();
        DataSourceContextHolder.setDbType("dataSourceA");
        try {
            responseModel = authAccess(access_token,session,request);
            if(responseModel.getCode()==0){
                Member user = (Member)session.getAttribute("user");
                responseModel.setData(user);
            }
        } catch (Exception e) {
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }

    @ResponseBody
    @RequestMapping(value = "/web/user/assgin/add", produces = "application/json; charset=utf-8")
    public Object Userassgin(@RequestBody String jsonData){
        DataSourceContextHolder.setDbType("dataSourceA");
        ResponseModel responseModel = new ResponseModel();

        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            String dutyList = json.getString("dutyName");
            int roleid = json.getInteger("roleid");
            String[] dlist = dutyList.split(",");
            String msg="用户权限分配成功";
            for(int i = 0;i<dlist.length;i++){
                HashMap map = new HashMap();
                map.put("username",dlist[i]);
                map.put("roleid",roleid);
                memberService.insertRoleUser(map);
            }
            responseModel.setMsg(msg);
            responseModel.setData("");
            responseModel.setCode(0);
        } catch (Exception e) {
            e.printStackTrace();
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }
    //
    @ResponseBody
    @RequestMapping(value = "/web/user/load", produces = "application/json; charset=utf-8")
    public Object queryUserList(){
        ResponseModel responseModel = new ResponseModel();
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
            responseModel.setCode(0);
            responseModel.setMsg("查询成功");
            responseModel.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }
    /**
     * @Author yyj
     * @Description  角色信息修改，包括权限修改  先删除再修改
     * @Date 2021-06-24
     * @Param
     * @return
     **/
    @ResponseBody
    @RequestMapping(value = "/web/role/edit", produces = "application/json; charset=utf-8")
    public Object webPermissionRoleedit(@RequestBody String jsonData){
        ResponseModel responseModel = new ResponseModel();
        DataSourceContextHolder.setDbType("dataSourceA");
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            String descr =json.getString("descr");
            int roleId =json.getInteger("roleId");
            String rolename =json.getString("rolename");
            JSONArray permission =json.getJSONArray("permission");
            HashMap map = new HashMap();
            map.put("descr",descr);
            map.put("rolename",rolename);
            map.put("roleid",roleId);
            roleService.updateRole(map);
            permissionService.deleteRolePermissions(map);
            for(int i=0;i<permission.size();i++){
                JSONObject jb =permission.getJSONObject(i);
                int id = jb.getInteger("id");
                int pid = jb.getInteger("pid");
                map.put("id",id);
                map.put("pid",pid);
                permissionService.insertRolePermission(map);
            }

            responseModel.setCode(0);
            responseModel.setMsg("修改成功");
        }catch (Exception e){
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }

    @ResponseBody
    @RequestMapping(value = "/web/role/add", produces = "application/json; charset=utf-8")
    public Object webPermissionRole(@RequestBody String jsonData){
        ResponseModel responseModel = new ResponseModel();
        DataSourceContextHolder.setDbType("dataSourceA");
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            String descr =json.getString("descr");
            String rolename =json.getString("rolename");
            JSONArray permission =json.getJSONArray("permission");
            HashMap map = new HashMap();
            map.put("descr",descr);
            map.put("rolename",rolename);
            int roleId = roleService.insertRole(map);
            roleId= Integer.parseInt(map.get("id").toString());
            map.put("roleid",roleId);
            permissionService.deleteRolePermissions(map);
            for(int i=0;i<permission.size();i++){
                JSONObject jb =permission.getJSONObject(i);
                int id = jb.getInteger("id");
                int pid = jb.getInteger("pid");
                map.put("id",id);
                map.put("pid",pid);
                permissionService.insertRolePermission(map);
            }
            responseModel.setCode(0);
            responseModel.setMsg("新增成功");
        }catch (Exception e){
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }
    @ResponseBody
    @RequestMapping(value = "/web/menu/update", produces = "application/json; charset=utf-8")
    public Object menuUpdate(@RequestBody String jsonData){
        ResponseModel responseModel = new ResponseModel();
        DataSourceContextHolder.setDbType("dataSourceA");
        try {
            JSONObject json = JSONObject.parseObject(jsonData);
            int id =json.getInteger("id");
            String title =json.getString("title");
            HashMap map = new HashMap();
            map.put("id",id);
            map.put("title",title);
            permissionService.updateWebMenu(map);
            responseModel.setCode(0);
            responseModel.setMsg("修改成功");
        }catch (Exception e){
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }

    //递归算法  但是效率比较低，因为操作数据库很多次
    private void deleteChildPermissions(PermissionTree parent){
        DataSourceContextHolder.setDbType("dataSourceA");
        List<PermissionTree> childPermissions = permissionService.queryChildPermissions2(parent.getId());
        for(PermissionTree permission :childPermissions){
            if(parent.getId()==permission.getPid()){
//                permissionService.deleteWebMenu(permission.getId());
                System.out.println(permission);
                deleteChildPermissions(permission);

            }
        }

    }
    @ResponseBody
    @RequestMapping(value = "/web/menu/delete")
    public Object menuDelete(@RequestBody Integer id){
        ResponseModel responseModel = new ResponseModel();
        DataSourceContextHolder.setDbType("dataSourceA");
        try {
            HashMap map = new HashMap();
            map.put("id",id);
            PermissionTree parent = new PermissionTree();
            parent.setId(id);
            deleteChildPermissions(parent);
            responseModel.setCode(0);
            responseModel.setMsg("删除成功");
        }catch (Exception e){
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }

    //递归算法  但是效率比较低，因为操作数据库很多次
    private void queryChildPermissions(PermissionTree parent){
        DataSourceContextHolder.setDbType("dataSourceA");
        List<PermissionTree> childPermissions = permissionService.queryChildPermissions(parent.getId());
        for(PermissionTree permission :childPermissions){
            queryChildPermissions(permission);
        }
        parent.setChildren(childPermissions);

    }

    /**
     * 获取网站菜单
     * **/
    @ResponseBody
    @RequestMapping(value = "/web/menu/load", produces = "application/json; charset=utf-8")
    public Object getWebmenu(){
        ResponseModel responseModel = new ResponseModel();
        List<PermissionTree> permissions = new ArrayList<PermissionTree>();
        try {
            PermissionTree parent = new PermissionTree();
            parent.setId(0);
            queryChildPermissions(parent);
            responseModel.setData(parent.getChildren());
            responseModel.setCode(0);
            responseModel.setMsg("获取成功");
        }catch (Exception e){
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }

    public ResponseModel authAccess( String access_token,HttpSession session, HttpServletRequest request) {
        ResponseModel responseModel = new ResponseModel();
        HashMap map = new HashMap();
        Map<String, HttpSession> userMap = (Map<String, HttpSession>) request.getServletContext().getAttribute("userMap");
        try {
            if (userMap == null) {
                userMap = new HashMap<String, HttpSession>();
            }
            if (StringUtils.isNotBlank(access_token)) {
                DecodedJWT claims = null;
                claims = UserToken.parseJWT(access_token);
                Long expTime =claims.getExpiresAt().getTime()/1000;
                if (expTime < (System.currentTimeMillis()/1000)) {
                    responseModel.setCode(500);
                    responseModel.setMsg("用户登陆过期");
                    session.invalidate();
                    return responseModel;
                } else {
                    map.put("username", claims.getClaim("username").asString());
                    map.put("password", claims.getClaim("password").asString());
                    Member cuser = (Member) session.getAttribute("user");
                    if (cuser == null) {
                        cuser = memberService.authenticate(map);
                        session.setAttribute("user", cuser);
                        userMap.put(cuser.getUsername(), session);
                        session.setAttribute("userMap", userMap);
                        request.changeSessionId();
                        int numberOfSessions = userMap.size();
                        session.setAttribute("numberOfSessions", numberOfSessions);
                    }
                    responseModel.setData(map);
                    responseModel.setCode(0);
                    responseModel.setMsg("");
                    return responseModel;
                }
            } else {
                responseModel.setCode(500);
                responseModel.setMsg("用户登陆过期");
                session.invalidate();
                return responseModel;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }

    @ResponseBody
    @RequestMapping("/web/user/logout")
    public Object logout( @RequestParam("access_token") String access_token,
                          HttpSession session,HttpServletRequest request){
        ResponseModel responseModel = new ResponseModel();
        DataSourceContextHolder.setDbType("dataSourceA");
        try {
            Map<String, HttpSession> userMap = (Map<String, HttpSession>) request.getServletContext().getAttribute("userMap");
            if (userMap != null) {
                DecodedJWT claims = UserToken.parseJWT(access_token);
                String username =claims.getClaim("username").asString().replaceAll("\"","");
                Iterator<Map.Entry<String, HttpSession> > it = userMap.entrySet().iterator();
                while(it.hasNext()){
                    Map.Entry<String, HttpSession> entry=it.next();
                    String key=entry.getKey();
                    if(key.equals(username)){
                        it.remove();
                    }

                }
            }
            session.invalidate();
            responseModel.setCode(0);
            responseModel.setMsg("退出成功");
        }catch (Exception e){
            responseModel.setMsg(e.getMessage());
            responseModel.setCode(500);
        }
        return responseModel;
    }


    /**
     *
     * @param access_token
     * @param session
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/menu/load")
    public Object LoadMenu( @RequestParam("access_token") String access_token,HttpSession session,HttpServletRequest request){
        ResponseModel responseModel = new ResponseModel();
        DataSourceContextHolder.setDbType("dataSourceA");
        try {
            responseModel = authAccess(access_token,session,request);
            if(responseModel.getCode()==0){
                Member user = (Member)session.getAttribute("user");
                List<PermissionMenu> permissions = permissionService.queryPermissionsByUser(user);
                if(permissions.size()==0){
                    permissions = permissionService.queryPermissionsByDefault();
                }
                Map<Integer, PermissionMenu> permissionMap = new HashMap<Integer, PermissionMenu>();
                PermissionMenu root = null;
                Set<String> uriSet = new HashSet<String>();
                for (PermissionMenu permission : permissions) {
                    permissionMap.put(permission.getId(), permission);
                    //不是空字符串
                    if (permission.getJump() != null && !"".equals(permission.getJump())) {
                        //授权
                        uriSet.add(session.getServletContext().getContextPath() + permission.getJump());
                    }
                }
                session.setAttribute(authUriSet, uriSet);
                for (PermissionMenu permission : permissions) {
                    PermissionMenu child = permission;
                    if (child.getPid() == 0) {
                        root = permission;
                    } else {
                        PermissionMenu parent = permissionMap.get(child.getPid());
                        parent.getList().add(child);
                    }
                }
                responseModel.setData(root.getList());
                responseModel.setCode(0);
            }else {
                return responseModel;
            }
        }catch (Exception e){
            responseModel.setMsg(e.getMessage());
            responseModel.setCode(500);
        }
        return responseModel;
    }










}
