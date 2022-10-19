//package com.web.filters;
//
//
//import com.web.model.Member;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
///**
// * login  url 拦截
// */
//public class LoginInterceptor implements HandlerInterceptor {
//
//    /**
//     * false不可以进入后台
//     */
//    @Override
//    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
//
//        //判断当前用户是否已经登陆
//        //session有没有用户信息
//        HttpSession session = httpServletRequest.getSession();
//        String path = httpServletRequest.getContextPath();
//
//        String url = httpServletRequest.getRequestURI();
//        System.out.println(httpServletResponse);
//        Member loginUser =(Member)session.getAttribute("user");
//        if(loginUser==null){
////            path="/permission/user/login";
////            if(url.contains("permission/user/login")){
////                return true;
////            }
//            httpServletResponse.sendRedirect(path+"/");
//            return false;
//        }else{
//            sessionHandlerByCacheMap(session);
//            return true;
//        }
//    }
//
//    public void sessionHandlerByCacheMap(HttpSession session){
//        String userid=session.getAttribute("userid").toString();
//        if(SessionListener.sessionContext.getSessionMap().get(userid)!=null){
//            HttpSession userSession=(HttpSession)SessionListener.sessionContext.getSessionMap().get(userid);
//            //注销在线用户
//            userSession.invalidate();
//            SessionListener.sessionContext.getSessionMap().remove(userid);
//            //清除在线用户后，更新map,替换map sessionid
//            SessionListener.sessionContext.getSessionMap().remove(session.getId());
//            SessionListener.sessionContext.getSessionMap().put(userid,session);
//        }
//        else
//        {
//            // 根据当前sessionid 取session对象。 更新map key=用户名 value=session对象 删除map
//            SessionListener.sessionContext.getSessionMap().get(session.getId());
//            SessionListener.sessionContext.getSessionMap().put(userid,SessionListener.sessionContext.getSessionMap().get(session.getId()));
//            SessionListener.sessionContext.getSessionMap().remove(session.getId());
//        }
//    }
//    /**
//     * 控制器执行完毕之后的逻辑操作
//     */
//    @Override
//    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
//
//    }
//    /**
//     * 完成试图渲染之后的方法
//     */
//    @Override
//    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
//
//    }
//}
