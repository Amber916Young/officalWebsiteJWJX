package com.web.model.jwjx;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class Gnss_system_ClientTerminaldb {
    private int ID;
    @ExcelProperty(value = "clientCode", index = 0)
    private String clientCode;
    @ExcelProperty(value = "terminalsCode", index = 1)
    private String terminalsCode;//GPS设备号
    @ExcelProperty(value = "carNumber", index = 2)
    private String carNumber;
    private Date validityDate;//有效期
    private String type;//enum '长期','临时','其他'
    private String accredit;
    @ExcelProperty(value = "simNumber", index = 3)
    private String simNumber;//SIM卡号
}
