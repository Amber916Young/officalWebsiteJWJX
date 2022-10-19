package com.web.service;

import com.web.mapper.RoleMapper;
import com.web.mapper.TagsMapper;
import com.web.model.Role;
import com.web.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class RoleService {
    @Autowired
    RoleMapper roleMapper;


    public List<Role> queryRole(Page page) {
        List<Role> list =  roleMapper.queryRole(page);
        return list;
    }

    public int insertRole(HashMap map) {
        return roleMapper.insertRole(map);
    }

    public void deleteRole(Integer id) {
        roleMapper.deleteRole(id);
    }

    public void updateRole(HashMap map) {
        roleMapper.updateRole(map);
    }
}
