package com.web.model.Wx;

import lombok.Data;

import java.util.Date;

@Data
public class UserInfo {
    private int id;
    private int subscribe;// 用户是否订阅了此公众号
    private String openid;
    private String nickname;
    private String headimgurl;
    private String unionid;
    private String language;

    /**
     * 	返回用户关注的渠道来源，ADD_SCENE_SEARCH
     * 	公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION
     * 	公众号迁移，ADD_SCENE_PROFILE_CARD
     * 	名片分享，ADD_SCENE_QR_CODE
     * 	扫描二维码，ADD_SCENE_PROFILE_LINK
     * 	图文页内名称点击，ADD_SCENE_PROFILE_ITEM
     * 	图文页右上角菜单，ADD_SCENE_PAID
     * 	支付后关注，ADD_SCENE_WECHAT_ADVERTISEMENT
     * 	微信广告，ADD_SCENE_OTHERS 其他
     */
    private String subscribe_scene;
    private int sex;  //1 男 2女 0 未知
    private String city;
    private String country;
    private String province;
    private Date subscribe_time;
    private String remark;
    private int groupid;
    private int[] tagid_list;
    private String tagid_listStr;

    private int qr_scene;
    private String qr_scene_str;
    private int mid=-1;
    private int[] privilege;
    private String userCompany;
    private String userType;
    private String phone;


}
