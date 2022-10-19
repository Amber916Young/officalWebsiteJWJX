package com.web.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class PermissionMenu  extends Permission {
    private String name;
    private String jump;
    private Integer pid;
    private boolean spread = false;
    //    private boolean checked = false;
    private String icon;
    private List<PermissionMenu> list = new ArrayList();
}
