package com.web.mapper;

import com.web.model.Certification;
import com.web.model.NoneMain;
import com.web.model.Servers;
import com.web.utils.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author amber
 */
public interface NoneMainMapper {


    @Select("select * from noneMain where tabName=#{tabName}")
    NoneMain queryBytabName(String tabName);

    @Select("select * from jwjx.weixin where memo=#{param}")
    HashMap<String, Object> queryWXToken(String param);


    void updateJwjxWeixin(HashMap hashMap);

    @Select("select * from egt.egt_company")
    List<HashMap<String, Object>> queryAllCompany();

    HashMap<String, Object> queryCompany(HashMap param);

    void insertWxfeedBack(Map map);

    HashMap selectWxfeedBack(Map map);

    void updateWxfeedBack(Map map);

    void insertWxkf(Map map);

    HashMap queryTemplate(HashMap param);

    int selectPageCount_templateuser(Page page);

    List<HashMap<String, Object>> selectPageList_templateuser(Page page);

    List<HashMap<String, Object>> selectPageList_template(Page page);

    int selectPageCount_template(Page page);

    @Delete("delete from jwjxinfo2.tmeplateuser where uid=#{uid} and tid=#{tid}")
    void deleteTemplateUserByid(HashMap map);

    @Delete("delete from jwjxinfo2.templatemsg where id=#{id}")
    void deleteTemplateByid(Integer id);


    @Delete("delete from jwjxinfo2.tmeplateuser where tid=#{id}")
    void deleteTemplateUser(Integer id);


    @Insert("insert into jwjxinfo2.tmeplateuser(uid,tid)  values(#{uid},#{tid})")
    void insertTemplateuser(HashMap map);

    void updatetemplatemsg(HashMap map);

    int selectPageCountWxchat(Page page);

    List<HashMap<String, Object>> selectPageListWxchat(Page page);
}
