package com.web.model;

import lombok.Data;

@Data
public class Article {
    private int id;
    private String title;
    private String content;
    private String detail;
    private String url;
    private String cover_url;
    private int status;
    private String shortTitle;
    private String style;

    private String category;  //分类  产品 product 新闻 news
    private String createTime;
    private String publishTime; //发布时间  显示的是发布时间
    private String author; //作者
    private int orderNum;
    private int flag;//0 普通页面，1tab页面
    private String fatherTitle;
}

