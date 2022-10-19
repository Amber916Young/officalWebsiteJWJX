package com.web.model;

import lombok.Data;

@Data
public class WxUserData {
    private String ref_date;
    private int user_source;
    private int new_user;
    private int cancel_user;

}
