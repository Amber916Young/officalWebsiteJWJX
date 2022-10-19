package com.web.model.jwjx;

import lombok.Data;

@Data
public class EgtRemoteMode {
    private int id;
    //公司名称
    private String companyName;
    //控制对象。具体到该公司的某一台电脑
    private String controlledMember;
    //远程类型。如向日葵远程、ToDesk远程
    private String remoteType;
    //账号。本机识别码或是设备代码
    private String account;
    //密码。本机验证码或是临时密码
    private String password;
}
