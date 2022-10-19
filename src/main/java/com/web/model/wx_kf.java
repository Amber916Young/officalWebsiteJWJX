package com.web.model;

import lombok.Data;

@Data
public class wx_kf {
    //完整客服帐号，格式为：帐号前缀@公众号微信号
    /*
    * 完整客服帐号，格式为：帐号前缀@公众号微信号，帐号前缀最多10个字符，
    * 必须是英文、数字字符或者下划线，后缀为公众号微信号，长度不超过30个字符
    * */
    private String kf_account;
    private String kf_headimgurl;
    private String kf_id;
    private String kf_nick;
    //如果客服帐号已绑定了客服人员微信号， 则此处显示微信号
    private String kf_wx;
    //如果客服帐号尚未绑定微信号，但是已经发起了一个绑定邀请， 则此处显示绑定邀请的微信号
    private String invite_wx;
    //如果客服帐号尚未绑定微信号，但是已经发起过一个绑定邀请， 邀请的过期时间，为unix 时间戳
    private String invite_expire_time;
    //邀请的状态，有等待确认“waiting”，被拒绝“rejected”， 过期“expired”
    private String invite_status;
    //客服当前正在接待的会话数
    private int accepted_case;
    //客服在线状态，目前为：1、web 在线
    private int status;
    //客服昵称，最长16个字  add
    private String nickname;


    //上传客服头像
    private String media;

    private String key_member;
}
