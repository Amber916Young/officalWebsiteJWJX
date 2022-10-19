package com.web.model;

import lombok.Data;



/**
 *工單  消息接收 实体类
 *
 * about页面  用户消息
 *
 *2021 3.11
 * */
@Data
public class MessageWorkorder {
    private int id;
    private String nickName;  //提交者
    private String processor;  //受理人员
    private String phone;
    private String email;
    private String msgContent;  //内容
    private String createTime;
    private String type; //业务性质
    private String title;   //工单标题
    private String process;  //处理进度  0 1 2 3 4
    private String status;   //工单工单状态
    private String finishTime; // 完成时间
    private Integer priority;
//    private Integer category;    //业务性质
    private String attachment;
}
