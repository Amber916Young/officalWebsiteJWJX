package com.web.service;

import com.web.mapper.KfMapper;
import com.web.mapper.PermissionMapper;
import com.web.model.*;
import com.web.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class PermissionService {
    @Autowired
    PermissionMapper permissionMapper;


    public List<PermissionMenu> queryPermissions() {
        return permissionMapper.queryPermissions();
    }

    public List<PermissionTree> queryChildPermissions(Integer pid) {
        return permissionMapper.queryChildPermissions(pid);
    }

    public List<PermissionTree> queryChildPermissions2(Integer pid) {
        return permissionMapper.queryChildPermissions2(pid);
    }

    public void deleteWebMenu(Integer id) {
        permissionMapper.deleteWebMenu(id);
    }

    public void updateWebMenu(HashMap map) {
        permissionMapper.updateWebMenu(map);

    }

    public void insertRolePermission(HashMap map) {
        permissionMapper.insertRolePermission(map);

    }
    public List<PermissionMenu> queryPermissionsByDefault() {
        return permissionMapper.queryPermissionsByDefault();

    }
    public List<PermissionMenu> queryPermissionsByUser(Member user) {
        return permissionMapper.queryPermissionsByUser(user);

    }

    public void deleteRolePermissions(HashMap map) {
        permissionMapper.deleteRolePermissions(map);
    }
}
