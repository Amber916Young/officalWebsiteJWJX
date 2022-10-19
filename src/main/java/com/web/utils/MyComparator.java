package com.web.utils;

import com.web.model.EmpInfo;

import java.util.Comparator;

public class MyComparator  implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        EmpInfo c1 = (EmpInfo) o1;
        EmpInfo c2 = (EmpInfo) o2;
        if (c1.getTimestamp().getTime()< c2.getTimestamp().getTime()) {
            // 根据时间排序，这里根据你的需要来重写
            return 1;
        } else {
            return 0;
        }
    }

}
