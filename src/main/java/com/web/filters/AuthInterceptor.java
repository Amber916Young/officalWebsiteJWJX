package com.web.filters;

import com.web.model.PermissionMenu;
import com.web.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//权限拦截器
public class AuthInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private PermissionService permissionService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取用户的请求地址，判断当前的路径是否需要权限验证
        String uri = request.getRequestURI();
        String path = request.getSession().getServletContext().getContextPath();

        //查询所有验证路径的集合
        List<PermissionMenu> permissions =permissionService.queryPermissions();
        Set<String> uriSet = new HashSet<String>();
        for(PermissionMenu permission:permissions){
            if ( permission.getJump() != null && !"".equals(permission.getJump()) ) {
                uriSet.add(path+permission.getJump());
            }
        }
        if(uriSet.contains(uri)){
            //权限验证
            //当前的用户是否拥有对应的权限
            Set<String > authUriSet =(Set<String >)request.getSession().getAttribute("authUriSet");
            if (authUriSet.contains(uri)){
                return true;
            }else {
                response.sendRedirect(path+"/error/501");
                return  false;
            }
        }else {
            //不需要验证
            return true;
        }

    }
}
