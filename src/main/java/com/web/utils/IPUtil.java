package com.web.utils;


import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.reflect.Method;

public class IPUtil {
    public static String getIpAddress(HttpServletRequest request){
        String ip=request.getHeader("x-forwarded-for");
        if(ip==null||ip.length()==0||"unknown".equalsIgnoreCase(ip)){
            ip=request.getHeader("Proxy-Client-IP");
        }
        if(ip==null||ip.length()==0||"unknown".equalsIgnoreCase(ip)){
            ip=request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip==null||ip.length()==0||"unknown".equalsIgnoreCase(ip)){
            ip=request.getHeader("HTTP_CLIENT_IP");
        }
        if(ip==null||ip.length()==0||"unknown".equalsIgnoreCase(ip)){
            ip=request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if(ip==null||ip.length()==0||"unknown".equalsIgnoreCase(ip)){
            ip=request.getRemoteAddr();
        }

        if("0:0:0:0:0:0:0:1".equals(ip)){
            return "127.0.0.1";
        }
        return ip;
    }
    public static String getCityInfo(String ip){
        String dbPath = IPUtil.class.getResource("/ip2region.db").getPath();
        System.err.println("dbPath=="+dbPath);
        File file = new File(dbPath);
        if ( file.exists() == false ) {
            System.out.println("不存在这个文件 Error: Invalid ip2region.db file");
            return "不存在这个文件 Error: Invalid ip2region.db file";
        }
        //查询算法
        int algorithm = DbSearcher.BTREE_ALGORITHM; //B-tree
        //DbSearcher.BINARY_ALGORITHM //Binary
        //DbSearcher.MEMORY_ALGORITYM //Memory
        try {
            DbConfig config = new DbConfig();
            DbSearcher searcher = new DbSearcher(config, dbPath);
            //define the method
            Method method = null;
            switch ( algorithm )
            {
                case DbSearcher.BTREE_ALGORITHM:
                    method = searcher.getClass().getMethod("btreeSearch", String.class);
                    break;
                case DbSearcher.BINARY_ALGORITHM:
                    method = searcher.getClass().getMethod("binarySearch", String.class);
                    break;
                case DbSearcher.MEMORY_ALGORITYM:
                    method = searcher.getClass().getMethod("memorySearch", String.class);
                    break;
            }
            DataBlock dataBlock = null;
            if ( Util.isIpAddress(ip) == false ) {
                System.out.println("Error: Invalid ip address");
            }
            dataBlock  = (DataBlock) method.invoke(searcher, ip);
            return dataBlock.getRegion();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
//    public static void main(String[] args)  throws Exception {
//        System.err.println(getCityInfo("163.179.32.107"));
//    }

////        //ip
////        String ip="220.248.12.158";
////
////        // 判断是否为IP地址 (可用)
////        //boolean isIpAddress = Util.isIpAddress(ip);
////
////        //ip和long互转 (可用)
////        //long ipLong = Util.ip2long(ip);
////        //String strIp = Util.long2ip(ipLong);
////
////        //根据ip进行位置信息搜索
////        DbConfig config = new DbConfig();
////
////        //获取ip库的位置（放在src下）（直接通过测试类获取文件Ip2RegionTest为测试类）
////        String dbfile = IPUtil.class.getResource("/resource/static/data/ip2region.db").getPath();
////
////        DbSearcher searcher = new DbSearcher(config, dbfile);
////
////        //采用Btree搜索
////        DataBlock block = searcher.btreeSearch(ip);
////
////        //打印位置信息（格式：国家|大区|省份|城市|运营商）
////        System.out.println(block.getRegion());
//
//
//    }
}