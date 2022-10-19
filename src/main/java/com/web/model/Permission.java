package com.web.model;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class Permission {
    private Integer id;
    private String title;

}

/**
* {
*       title: '江西'
*       ,id: 1
*       ,children: [
*       {
*         title: '南昌'
*         ,id: 1000
*         ,children: [
*         {
*           title: '青山湖区'
*           ,id: 10001
*         },{
*           title: '高新区'
*           ,id: 10002
*         }
* ]
*       },
*       {
*         title: '九江'
*         ,id: 1001
*       }
*       ]
*     }
* */