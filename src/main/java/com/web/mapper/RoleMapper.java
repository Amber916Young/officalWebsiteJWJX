package com.web.mapper;

import com.web.model.Role;
import com.web.utils.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;



public interface RoleMapper {
    //角色
    @Select("select * from role ")
    List<Role> queryRole(Page page);

    int insertRole(HashMap map);

    @Delete("delete from jwjxinfo2.role where id = #{id}")
    void deleteRole(Integer id);

    void updateRole(HashMap map);
}
