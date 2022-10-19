package com.web.model.jwjx;

import lombok.Data;

@Data
public class Gnss_system_Clientdb {
    private int clientID;
    private String clientCode;
    private String clientName;
    private String pass;
    private String memo;
    private String accredit;
    private int serverFlag;
    private int maxSpeed;
    //所属区域编码
    private String adcode;
    private byte[] password;

}
