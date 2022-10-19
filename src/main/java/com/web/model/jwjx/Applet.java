package com.web.model.jwjx;

import lombok.Data;

import java.util.Date;

@Data
public class Applet {
    private String appID;
    private String nickName;
    private String openid;
    private String adcode;
    private String administrative;//所在省市
    private String latitude;
    private String longitude;

    private String phone;//手机号:用户注册必须用手机验证，同时获取手机号
    private String userName;
    private String userType;
    private int regNO;
    private String plateCode;//车牌号
    private String capacity;//装载容量
    private String dbName;
    private String type;
    private String companyName;
    private boolean isAdmin;
    private String authority;


    private int direction;//方向
    private int altitude; //海拔高度
    private String speed;//速度:km/h
    private Date gpsTime;
    private String site;



    private String concreteTruckNO;
    private int groupID; //组id
    private String avatarUrl;

    private boolean state;//是否连线GPS
    private String stateStr;
    private String wxOpenid;
    private Date indate;
    private String mixStationName;
    private String KH_BM;//客户编码:（搅拌站客户）
    private String GC_BM;//工地编码
    //对于承运人:表示目前正在为那个搅拌站服务，如果没有表示正在闲置，可以为搅拌站提供服务。完成运单后要清空
//    对于搅拌车司机：目前正在为那个搅拌站服务，离开搅拌站后要清空
    private int atRegNO;
    private String regNOList;//多个搅拌站，用’，‘分开
    private String tempAuthority;//对应authority，临时暂存authority的值
    private String companyType;//企业类型
    private String mysqlDb;//当所属单位不对应jwjx数据库时所对应的数据库
}
