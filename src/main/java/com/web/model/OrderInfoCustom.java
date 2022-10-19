package com.web.model;

import lombok.Data;

import java.util.Date;
@Data
public class OrderInfoCustom {
    private int userID;
    private String number;
    private String startTime;
    private String endTime;
    private String keyword;
}
