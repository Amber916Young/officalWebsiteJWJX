package com.web.model;

import lombok.Data;

@Data
public class Servers {
    private String username;
    private String password;
    private int state;
    private int type;
}
