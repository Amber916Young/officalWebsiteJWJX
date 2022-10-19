package com.web.service;

import com.web.mapper.MemberMapper;
import com.web.mapper.TagsMapper;
import com.web.model.Member;
import com.web.model.Role;
import com.web.model.Wx.UserInfo;
import com.web.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MemberService {
    @Autowired
    MemberMapper memberMapper;



    public List<Member> selectPageList(Page page) {
        List<Member> list =  memberMapper.selectPageList(page);
        return list;
    }

    public int selectPageCount(Page page) {
        return memberMapper.selectPageCount(page);
    }

    public int selectPageCountIsAdmin(Page page) {
        return memberMapper.selectPageCountIsAdmin(page);
    }

    public List<Member> selectPageListIsAdmin(Page page) {
        List<Member> list =  memberMapper.selectPageListIsAdmin(page);
        return list;
    }


    public void insertVisitor(HashMap map) {
        memberMapper.insertVisitor(map);
    }

    public List<HashMap<String,Object>> queryKfList() {
        return memberMapper.queryKfList();
    }

    public HashMap queryMineByid(String dutyNumber) {
        return memberMapper.queryMineByid(dutyNumber);

    }

    public List<HashMap<String,Object>> queryKfList2(String dutyNumber) {
        return memberMapper.queryKfList2(dutyNumber);

    }

    public int selectDutyCount(Page page) {
        return memberMapper.selectDutyCount(page);
    }

    public List<HashMap<String, Object>> selectDutyList(Page page) {
        return memberMapper.selectDutyList(page);

    }

    public List<Member> queryAllMembers() {
        return memberMapper.queryAllMembers();
    }

    public void insertDuty(HashMap map) {
        memberMapper.insertDuty(map);
    }

    public Member queryUserLogin(Member member) {
        return  memberMapper.queryUserLogin(member);
    }


    public Member queryUserisAdmin(HashMap param) {
        return memberMapper.queryUserisAdmin(param);
    }

    public void insertWorker(Member member) {
        memberMapper.insertWorker(member);
    }

    public void deleteMemberByid(Integer id) {
        memberMapper.deleteMemberByid(id);
    }

    public void updateWorker(HashMap member) {
        memberMapper.updateWorker(member);
    }

    public Member queryByID(int id) {
        return memberMapper.queryByID(id);
    }

    public void deletDutyById(int id) {
        memberMapper.deletDutyById(id);
    }

    public List<HashMap<String, Object>> queryUserJwjx( HashMap<String, Object> param) {
        return memberMapper.queryUserJwjx(param);
    }

    public void insertUserInfo(HashMap map) {
        memberMapper.insertUserInfo(map);
    }

    public void updateUserInfo(HashMap map) {
        memberMapper.updateUserInfo(map);

    }

    public List<HashMap<String, Object>> queryUserInfoByPhone(String phone) {
        return memberMapper.queryUserInfoByPhone(phone);
    }

    public List<HashMap<String, Object>> queryKfListkehu() {
        return  memberMapper.queryKfListkehu();
    }
    public List<HashMap<String, Object>> queryKfListFangke() {
        return  memberMapper.queryKfListFangke();
    }

    public List<HashMap<String, Object>> queryUserInfo(HashMap<String, Object> params) {
        return memberMapper.queryUserInfo(params);
    }

    public HashMap<String, Object> queryVisitor(HashMap params) {
        return memberMapper.queryVisitor(params);

    }

    public HashMap<String, Object> queryUserVisitor(HashMap map) {
        return memberMapper.queryUserVisitor(map);
    }

    public List<HashMap<String, Object>> queryKfListnowDay() {
        return memberMapper.queryKfListnowDay();
    }

    public List<HashMap<String, Object>> queryKfListDefault() {
        return memberMapper.queryKfListDefault();
    }


    public void insertUserInfoWhenSubscribe(UserInfo userInfo) {
        memberMapper.insertUserInfoWhenSubscribe(userInfo);

    }

    public UserInfo queryUserInfoByParam(HashMap pa) {
        return memberMapper.queryUserInfoByParam(pa);
    }

    public void updateUserInfoWhenSubscribe(UserInfo userInfo) {
         memberMapper.updateUserInfoWhenSubscribe(userInfo);

    }

    public void deleteUserinfo(HashMap map1) {
        memberMapper.deleteUserinfo(map1);

    }

    public List<Member> queryMember(HashMap map) {
        return memberMapper.queryMember(map);

    }

    public Member authenticate(HashMap param) {
        return memberMapper.authenticate(param);
    }

    public void insertRoleUser(HashMap map) {
        memberMapper.insertRoleUser(map);
    }

    public void deleteRoleUser(Integer id) {
        memberMapper.deleteRoleUser(id);
    }

    public void insertWxChat(Map map) {
        memberMapper.insertWxChat(map);
    }

    public List<HashMap> queryKfViewTemUser(HashMap param) {
        return memberMapper.queryKfViewTemUser(param);
    }

    public List<Member> queryMemberWithoutTemplate(HashMap map) {
        return memberMapper.queryMemberWithoutTemplate(map);
    }

    public void updateMember(Member user) {
        memberMapper.updateMember(user);
    }

    public List<Member> queryMemberWithTemplate(HashMap map) {
        return memberMapper.queryMemberWithTemplate(map);
    }

    public int selectPageCountVisitor(Page page) {
        return memberMapper.selectPageCountVisitor(page);

    }

    public List<HashMap<String, Object>> selectPageListVisitor(Page page) {
        return memberMapper.selectPageListVisitor(page);

    }

    public void deleteByid(HashMap map) {
        memberMapper.deleteByid(map);

    }
}
