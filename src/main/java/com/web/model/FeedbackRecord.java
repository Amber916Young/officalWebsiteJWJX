package com.web.model;


import lombok.Data;

@Data
public class FeedbackRecord {
    private int id;
    private int mid;
    private String processor;  //受理人员
    private String contact;   //郵箱  或者  其他聯係方式回復訪客
    private String msgContent;  //内容
    private String createTime;
    private String title;
}
