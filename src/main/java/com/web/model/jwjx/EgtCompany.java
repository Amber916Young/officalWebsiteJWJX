package com.web.model.jwjx;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * egt_company
 * @author 
 */
@Data
public class EgtCompany  {
    /**
     * 企业编号
     */
    private int companyID;

    private String companypk;
    private String messages;
    /**
     * 企业编号
     */
    private String companyName;

    /**
     * 所属区域编码
     */
    private String adcode;

    /**
     * 0:未知；1:自有；2:思徽 ；9（包含ERP对应全套ERP以及小程序）；12（对接生产线以及使用ERP中的过磅管理、原材料管理功能、使用小程序）；15（只对接生产线，使用小程序）；18（对接旧的ERP数据库，使用小程序功能）
     */
    private String type;

    /**
     * 有效性标识 1:有效 ；0:无效 ; 2:服务器同步;3:客户库同步;
     */
    private Integer validflag;

    /**
     * 企业注册地址
     */
    private String administrative;

    /**
     * 菜单
     */
    private String menu;
    /**
     * 企业地址
     */
    private String address;

    /**
     * 企业联系人
     */
    private String contactname;

    /**
     * 企业联系人（管理员）电话
     */
    private String contactstel;

    /**
     * 授权码
     */
    private String accredit;

    /**
     * 企业数据库
     */
    private String dbname;

    /**
     * 企业数据库
     */
    private String dbnameLan;

    private String sqlkey;

    /**
     * 更新标志：0更新
     */
    private Boolean sync;

    private String ip;

    private Integer port;

    /**
     * IP地址
     */
    private String networkip;

    /**
     * 心跳时间
     */
    private Date heartbeat;

    /**
     * 地址
     */
    private String cpu;

    private String osname;

    /**
     * MAC地址
     */
    private String macaddress;

    private Integer state;

    /**
     * 转发是否登录
     */
    private Byte login;

    /**
     * 更新标志
     */
    private Byte fag;

    /**
     * 经度
     */
    private Float longitude;

    /**
     * 纬度
     */
    private Float latitude;

    private Float accuracy;

    /**
     * 围栏半径
     */
    private Integer radius;

    /**
     * 是否使用
     */
    private Boolean isinterior;

    /**
     * 有效期
     */
    private Date indate;

    /**
     * 推送公众号appID
     */
    private String wxappid;
    private String regNO;
    /**
     * 接收消息公众号openid:多个用逗号分开
     */
    private String wxopenids;

    /**
     * 推送小程序appID
     */
    private String appletappid;

    /**
     * 接收消息小程序openid:多个用逗号分开
     */
    private String appletopenids;



}