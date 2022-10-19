package com.web.utils;

import lombok.Data;

@Data
public class AJAXResult {
    private Object data;
    private int errcode=0;  //0 500 404
    private String errmsg="ok";
}
