package com.web.mapper;

import com.web.model.Member;
import com.web.model.Role;
import com.web.model.Wx.UserInfo;
import com.web.utils.Page;
import lombok.Data;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author amber
 */
//@Repository
public interface MemberMapper {


    int selectPageCount(Page page);


    List<Member> selectPageList(Page page);


    //管理员
    @Select("    select * from member where isAdmin=1")
    List<Member> selectPageListIsAdmin(Page page);

    @Select("    select count(1) from member where isAdmin=1")
    int selectPageCountIsAdmin(Page page);


    void insertVisitor(HashMap map);

    @Select("select nickname as username,dutyNumber as id,avatar as avatar, username as remark ,status as status  from view_duty ")
    List<HashMap<String,Object>> queryKfList();


    @Select("select distinct nickname as username,dutyNumber as id,avatar as avatar, username as remark ,status as status  from view_duty where dutyNumber=#{dutyNumber}")
    HashMap queryMineByid(String dutyNumber);

    @Select("select distinct  dutyNumber as id,nickname as username,avatar as avatar, username as remark ,status as status  from view_duty where dutyNumber!=#{dutyNumber} and statusID=2")
    List<HashMap<String,Object>> queryKfList2(String dutyNumber);

    int selectDutyCount(Page page);

    List<HashMap<String, Object>> selectDutyList(Page page);

    @Select(" select * from member")
    List<Member> queryAllMembers();

    @Insert("insert into duty(mid,dutyTime,wxAccount) values(#{mid},#{dateTime},#{wxAccount})")
    void insertDuty(HashMap map);

    Member queryUserLogin(Member member);



//    @Select("select * from member where username=#{username} and status=#{status}")
    Member queryUserisAdmin(HashMap param);

    void insertWorker(Member member);

    @Delete("delete from member where id=#{id}")
    void deleteMemberByid(Integer id);

    @Select("select * from member  where id=#{id}")
    Member queryByID(int id);

    void updateWorker(HashMap member);

    @Delete("delete from duty where id=#{id}")
    void deletDutyById(int id);

    List<HashMap<String, Object>> queryUserJwjx(HashMap<String, Object> param);

    void insertUserInfo(HashMap map);

    void updateUserInfo(HashMap map);

    @Select("select * from userInfo where phone=#{phone}")
    List<HashMap<String, Object>> queryUserInfoByPhone(String phone);

    @Select("select distinct  mid as id,nickname as username, headImgurl as avatar, CONCAT(userCompany,'(',userType,')') as remark ,'online' as status from userInfo where openid not in (select openid from member where status=2)")
    List<HashMap<String, Object>> queryKfListkehu();



    @Select("select distinct  id as id, CONCAT('访客',id) as username, headImgurl as avatar," +
            " '' as remark ,'online' as status from view_userInfo where type='访客'")
    List<HashMap<String, Object>> queryKfListFangke();

    List<HashMap<String, Object>> queryUserInfo(HashMap<String, Object> params);

    HashMap<String, Object> queryVisitor(HashMap params);

    HashMap<String, Object> queryUserVisitor(HashMap map);

    @Select("select * from view_duty where statusID=2  and  TO_DAYS(dutyTime) = TO_DAYS(NOW())")
    List<HashMap<String, Object>> queryKfListnowDay();

    @Select("select  *  from view_duty where mid=1 limit 1")
    List<HashMap<String, Object>> queryKfListDefault();


    void insertUserInfoWhenSubscribe(UserInfo userInfo);

    UserInfo queryUserInfoByParam(HashMap pa);

    void updateUserInfoWhenSubscribe(UserInfo userInfo);

    void deleteUserinfo(HashMap map1);

    List<Member> queryMember(HashMap map);

    Member authenticate(HashMap param);

    @Insert("insert into jwjxinfo2.role_user(username,roleid) values(#{username},#{roleid})")
    void insertRoleUser(HashMap map);


    @Delete("delete from jwjxinfo2.role_user where roleid=#{id}")
    void deleteRoleUser(Integer id);

    void insertWxChat(Map map);

    List<HashMap> queryKfViewTemUser(HashMap param);

    List<Member> queryMemberWithoutTemplate(HashMap map);

    void updateMember(Member user);

    List<Member> queryMemberWithTemplate(HashMap map);

    int selectPageCountVisitor(Page page);

    List<HashMap<String, Object>> selectPageListVisitor(Page page);

    @Delete("delete from jwjxinfo2.visitor where id =#{id}")
    void deleteByid(HashMap map);
}
