package com.web.model;

import lombok.Data;

@Data
public class Certification {
    private int id;
    private String name;
    private String version;
    private String category;
    private String type;
    private String createTime;
    private int status;
}
