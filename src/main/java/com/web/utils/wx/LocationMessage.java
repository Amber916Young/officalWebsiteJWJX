package com.web.utils.wx;

import lombok.Data;

@Data
public class LocationMessage extends BaseMessage {
    private String Latitude;
    private String Longitude;
    private String Precision;
}
