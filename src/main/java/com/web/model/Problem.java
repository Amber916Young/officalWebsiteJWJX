package com.web.model;

import lombok.Data;

@Data
public class Problem {
    private int id;
    private String title;
    private String content;
    private String createTime;
    private String categroy;
    private String status;
}
