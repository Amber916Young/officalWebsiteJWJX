package com.web.model;

import lombok.Data;

import java.util.Date;


@Data
public class EmpInfo {
    private int id;
    /**
     *消息的来源
     */
    private String fromId;
    private String fromUsername;
    //是否是本人
    private Boolean mine;
    private String fromContent;
    //头像
    private String fromavatar;


    /**
     *消息接收方
     */
    private String toId;
    private String toAvatar;
    private String toType;
    private String remark;
    private String toUsername;
    private String toName;
    //是否已读，未读
    private int status;
    private int statusWeb;
    //发消息的设备  web网页还是移动端
    private String msgDevice;
    //发送消息的时间
    private Date timestamp = new Date();
    //消息转发
    private int tranfernumber;
    //sms消息
    //0没有发送短信  1已发送；一般情况只有未读 status=0 其次isSms为0才发送
    private int isSms;


}
