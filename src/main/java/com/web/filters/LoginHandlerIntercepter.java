package com.web.filters;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.web.mapper.MemberMapper;
import com.web.model.Member;
import com.web.service.MemberService;
import com.web.utils.HttpRequest;
import com.web.utils.JsonUtils;
import com.web.utils.UserToken;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class LoginHandlerIntercepter implements HandlerInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(LoginHandlerIntercepter.class);
    /** token 校验
         *
             * @param httpServletRequest, httpServletResponse, o
     * @return boolean
     * @methodName preHandle
            */

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberMapper memberMapper;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        Map<String, String[]> mapIn = httpServletRequest.getParameterMap();
        JSONObject jsonObject = new JSONObject(mapIn);//转化为json格式
        StringBuffer stringBuffer = httpServletRequest.getRequestURL();
        HttpSession session = httpServletRequest.getSession();
        JSONObject map = new JSONObject();
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = httpServletRequest.getHeader(key);
            map.put(key, value);
        }

        LOG.info("httpServletRequest ,路径:" + stringBuffer + ",入参:" + jsonObject);

        //校验APP的登陆状态，如果token 没有过期
        LOG.info("come in preHandle");
        String oldToken = httpServletRequest.getHeader("access_token");
        LOG.info("token:" + oldToken);
        Member loginUser =(Member)session.getAttribute("user");
        /*刷新token，有效期延长至一个月*/
        if (StringUtils.isNotBlank(oldToken)) {
            DecodedJWT claims = null;
            try {
                claims = UserToken.parseJWT(oldToken);
            } catch (Exception e) {
                e.printStackTrace();
                String str = "{\"code\":801,\"msg\":\"登陆失效,请重新登录\"}";
                dealErrorReturn(httpServletRequest, httpServletResponse, str);
                return false;
            }
//            String username = claims.get("username").toString();
//            String password = claims.get("password").toString();
//            Long.parseLong(claims.get("exp").toString())<System.currentTimeMillis()
            if (1==1) {

                try {
//                    String newToken = UserToken.token(username,password);
//                    LOG.info("new TOKEN:{}", newToken);
//                    httpServletRequest.setAttribute("userId", username);
//                    httpServletResponse.setHeader("token", newToken);
//                    LOG.info("flush token success ,{}", oldToken);
                    httpServletResponse.sendRedirect("/permission/user/login");

                    return false;
                } catch (Exception e) {
                    e.printStackTrace();
                    String str = "{\"code\":801,\"msg\":\"登陆失效,请重新登录\"}";
//                    dealErrorReturn(httpServletRequest, httpServletResponse, str);
                    httpServletResponse.sendRedirect("/permission/user/login");

                    return false;
                }
            }else {
                if (loginUser == null) {
                    HashMap param = new HashMap();
                    param.put("username", "username");
                    param.put("password", "password");
                    Member loginUser2 = memberService.authenticate(param);
                    //7.如果登陆成功将用户保存到session中
                    if (loginUser2 != null) {
                        httpServletRequest.getSession().setAttribute("user", loginUser);
                    }else {
                        httpServletResponse.sendRedirect("/permission/user/login");
                        return false;
                    }
                }
                return true;
            }
        }
        String str = "{\"code\":801,\"msg\":\"登陆失效,请重新登录\"}";
//        dealErrorReturn(httpServletRequest, httpServletResponse, str);
        httpServletResponse.sendRedirect("/permission/user/login");

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    /**
     * 返回错误信息给WEB
     *
     * @param httpServletRequest, httpServletResponse, obj
     * @return void
     * @methodName dealErrorReturn
     * @author fusheng
     * @date 2019/1/3 0003
     */
    public void dealErrorReturn(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object obj) {
        String json = (String) obj;
        PrintWriter writer = null;
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        try {
            writer = httpServletResponse.getWriter();
            writer.print(json);

        } catch (IOException ex) {
            LOG.error("response error", ex);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

}
