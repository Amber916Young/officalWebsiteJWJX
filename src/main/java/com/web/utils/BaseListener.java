package com.web.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseListener<T> extends AnalysisEventListener<T> {
    /**
     * 每隔3000条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 3000;

    List<T> list = new ArrayList<T>();

    @Override
    public abstract void invoke(T var1, AnalysisContext analysisContext);

    public abstract void saveData();

    @Override
    public abstract void doAfterAllAnalysed(AnalysisContext analysisContext);
}
