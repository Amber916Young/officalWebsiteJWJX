package com.web.model.Echart;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class EOption {
    private Object title;
    private Object tooltip;
    private Object xAxis;
    private Object yAxis;
    private Object series;
    private Object legend;
    private Object color;
    @JSONField(name="xAxis")
    public Object getxAxis() {
        return xAxis;
    }
    @JSONField(name="yAxis")
    public Object getyAxis() {
        return yAxis;
    }


}
