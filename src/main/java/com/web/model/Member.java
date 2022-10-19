package com.web.model;

import lombok.Data;

@Data
public class Member {
    private int id;
    private String avatar;
    private String email;
    private String phone;
    private String username;
    private String nickname;
    private String gender;
    private String loginTime;
    private String loginPosition;
    private String loginMark;
    private String loginIp;
    private String password;
    private String dutyNumber;
    private String jobPosition;
    private String authority;
    private int isAdmin;
    private String adminName;
    private String openid;
    //0不是微信客服，1添加为微信客服 2已经绑定微信客服
    private int status;
    private String kf_account;
    private String wxID;
}
