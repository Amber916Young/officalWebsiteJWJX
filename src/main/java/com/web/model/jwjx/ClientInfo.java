package com.web.model.jwjx;

import lombok.Data;

import java.util.Date;

@Data
public class ClientInfo {
    private int clientID;
    private String WeiXinName;
    private String UserWeiXin;
    private int subscribe;
    private String nickname;
    private String openid;
    private String city;
    private int sex;
    private String province;
    private String country;
    private String headimgurl;
    private String subscribe_time;
    private String unionid;
    private String remark;
    private double Latitude;
    private double Longitude;
    private double Distance;
    private Date gpsTime;
    private int attentionState;
    private String CreateTime;
    private String phone;
    private byte[] passWord;
    private String userName;
    private int userType;
    private int regNO;
    private String sqlDbName;
    private String enterpriseName;
    private String isPass;
    private String plateCode;
    private String driverName;
    private int isLocation;
    private String capacity;
    private String dbName;
    private String compayName;
    private int isAdmin; //是否管理员






}
