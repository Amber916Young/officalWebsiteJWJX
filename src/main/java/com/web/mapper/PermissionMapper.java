package com.web.mapper;

import com.web.model.*;
import com.web.utils.Page;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.HashMap;
import java.util.List;

public interface PermissionMapper {

    @Select("select * from permission")
    List<PermissionMenu> queryPermissions();

    @Select("select id, title ,pid from permission where pid =#{pid}")
    List<PermissionTree> queryChildPermissions(Integer pid);


    @Select("select * from permission where pid =#{pid}")
    List<PermissionTree> queryChildPermissions2(Integer pid);

    void updateWebMenu(HashMap map);

    void deleteWebMenu(Integer id);

    void insertRolePermission(HashMap map);

    List<PermissionMenu> queryPermissionsByUser(Member user);
    List<PermissionMenu> queryPermissionsByDefault();

    void deleteRolePermissions(HashMap map);
}
