package com.web.utils;

import lombok.Data;

/**
 *需要参数code（要为0，不然数据表格数据显示不出）,
 * msg（返回的消息），
 * data(表格显示的数据)，
 * totals(查询到数据的总记录数)
 *
 * 2021 2 23
 * */

@Data
public class ResultMap<T> {
    private String msg;
    private  T data;
    private  int code;
    private  int count;
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    public ResultMap(String msg, T data, int code, int count) {
        this.msg = msg;
        this.data = data;
        this.code = code;
        this.count = count;
    }
    public ResultMap() {

    }
}
