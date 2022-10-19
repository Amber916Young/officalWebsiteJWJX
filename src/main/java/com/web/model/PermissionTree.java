package com.web.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class PermissionTree  extends Permission{
    private List<PermissionTree> children = new ArrayList();
    private Boolean spread=true;
    private int pid;
}
