package com.web.utils;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.web.model.jwjx.Gnss_system_ClientTerminaldb;
import com.web.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.ws.Action;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 **/
@Slf4j  //日志框架
public class ExcelRead extends BaseListener<Gnss_system_ClientTerminaldb> {

    /**
     * 每隔100条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;

    //存放数据信息的集合
    List<Object> list = new ArrayList<>(100);

    ClientService clientService = (ClientService)SpringContextUtil.getBean("clientService");


    /**
     * 读取表头的方法,默认设置的第一行为表头,多行可自行设置
     *
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        log.info("进入校验表头方法");
        //把表头全部读取拼接成字符串进行比对
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<Integer, String> map : headMap.entrySet()) {
            stringBuilder.append(map.getValue().trim());
        }
        log.info("表头="+stringBuilder.toString());
        //校验表头,不是则抛出异常 (这里应该是一个常量,或者动态生成的表头: 建议动态生成后传入)
        if (!stringBuilder.toString().equals("clientCodeterminalsCodecarNumbersimNumber")) {
            log.error("表头对比错误 ,表头数据为:{}", headMap);
            //easyExcel 内业务异常必须抛 ExcelAnalysisStopException 才能被接收到
            throw new ExcelAnalysisException("表头数据对比错误,请使用标准的Excel模板上传");
        }
        //todo 所有在读取数据之前的逻辑都可以写在这里
    }

    /**
     * Excel 读取器,一行一行读取
     * @param var1    每一行的数据
     * @param analysisContext
     */
    @Override
    public void invoke(Gnss_system_ClientTerminaldb var1, AnalysisContext analysisContext) {
        log.info("获取到数据为:"+var1.toString());
        list.add(var1);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            list.clear();
        }
    }

    /**
     * 存储数据到数据库
     */
    @Override
    public void saveData() {
        //todo 循环存储 , 因为 Listener是new出来的没有交给spring管理,所以要用构造方法把mapper传进来,具体请百度
        //todo 循环插入数据库的方法
        log.info("执行数据入库方法");
        clientService.insertGnss_system_ClientTerminaldb(list);
    }

    /**
     * 收尾方法,把不足 BATCH_COUNT 的存入数据库
     * 因为我们是100条存一次数据库,如果最后剩余50条则在此方法处理
     *
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        //todo 不足100条的也存进数据库
        //todo insert 方法
        saveData();

        log.info("执行数据入库方法");
    }
}

