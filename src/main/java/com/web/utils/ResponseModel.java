package com.web.utils;

import lombok.Data;

@Data
public class ResponseModel {

	private int code;
	
	private Object data;
	
	private String msg="查询成功";

}
