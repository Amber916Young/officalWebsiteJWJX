package com.web.utils.wx;

public class WxApi {
    public static final String addKfUrl = "https://api.weixin.qq.com/customservice/kfaccount/add?access_token=ACCESS_TOKEN";
    public static final String getKfListUrl = "https://api.weixin.qq.com/cgi-bin/customservice/getkflist?access_token=ACCESS_TOKEN";
    public static final String getonlinekflist = "https://api.weixin.qq.com/cgi-bin/customservice/getonlinekflist?access_token=ACCESS_TOKEN";
    public static final String delKfUrl = "https://api.weixin.qq.com/customservice/kfaccount/del?access_token=ACCESS_TOKEN&kf_account=KFACCOUNT";
    public static final String bbKfUrl = "https://api.weixin.qq.com/customservice/kfaccount/inviteworker?access_token=ACCESS_TOKEN";
    public static final String kfSessionUrl = "https://api.weixin.qq.com/customservice/kfsession/create?access_token=ACCESS_TOKEN";
    public static final String getsessionlist = "https://api.weixin.qq.com/customservice/kfsession/getsessionlist?access_token=ACCESS_TOKEN&kf_account=KFACCOUNT";
    public static final String getmsgrecord = "https://api.weixin.qq.com/customservice/msgrecord/getmsglist?access_token=ACCESS_TOKEN";
    //微信小程序的客服 不可以发送菜单消息  ，虽然用的是同一个接口
    public static final String kfinfoUpdate = "https://api.weixin.qq.com/customservice/kfaccount/update?access_token=ACCESS_TOKEN";
    public static final String actSessionUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";

    public static final String templateSend = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";


    public static final String createTags = "https://api.weixin.qq.com/cgi-bin/tags/create?access_token=ACCESS_TOKEN";
    public static final String getTags = "https://api.weixin.qq.com/cgi-bin/tags/get?access_token=ACCESS_TOKEN";
    public static final String getGroupTags = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token=ACCESS_TOKEN";
    public static final String editTags = "https://api.weixin.qq.com/cgi-bin/tags/update?access_token=ACCESS_TOKEN";
    public static final String delTags = "https://api.weixin.qq.com/cgi-bin/tags/delete?access_token=ACCESS_TOKEN";
    // 获取标签下粉丝列表
    public static final String getTagsFans = "https://api.weixin.qq.com/cgi-bin/user/tag/get?access_token=ACCESS_TOKEN";
    //批量为用户打标签
    public static final String batchTagsUser = "https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging?access_token=ACCESS_TOKEN";

    //批量为用户取消标签
    public static final String unbatchTagsUser = "https://api.weixin.qq.com/cgi-bin/tags/members/batchuntagging?access_token=ACCESS_TOKEN";
    //获取用户身上的标签列表
    public static final String getListUserTags = "https://api.weixin.qq.com/cgi-bin/tags/getidlist?access_token=ACCESS_TOKEN";
    //设置用户备注名
    public static final String updateremark = "https://api.weixin.qq.com/cgi-bin/user/info/updateremark?access_token=ACCESS_TOKEN";

    //获取用户基本信息(UnionID机制)
    public static final String getUserUnionID = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    //批量获取用户基本信息
    public static final String getBatchUserUnionID = "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=ACCESS_TOKEN";

    //公众号所有 关注用户
    public static final String gzhAllUser ="https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID";



    /**
     * @Author
     * @Description 模板消息
     * @Date 2021-06-29
     * @Param
     * @return
     **/
    public static final String templateSendMs ="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";




}
