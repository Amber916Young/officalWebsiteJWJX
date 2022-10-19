package com.web;

import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import com.web.model.jwjx.Gnss_system_ClientTerminaldb;
import com.web.utils.ExcelRead;
import com.web.utils.HttpRequest;
import com.web.utils.JsonUtils;
import com.web.utils.wx.WxApi;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.omg.CORBA.OBJ_ADAPTER;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
public class test {
    /**
     * JXL 读取Excel文件
     */
    public static void main(String[] args) throws Exception {
        //创建workbook
//        File file = new File("C:\\Users\\jwjx\\Desktop\\GPS设备录入.xlsx");
//        EasyExcel.read(file, Gnss_system_ClientTerminaldb.class, new ExcelRead()).sheet().doRead();

//        wxAPi();
//        XSSFWorkbook workbook = new XSSFWorkbook(FileUtils.openInputStream(file));
//        //两种方式读取工作表
//        Sheet sheet = workbook.getSheetAt(0);
//        //获取sheet中最后一行行号
//        List<String> titleList = new ArrayList<>();
//        List<Object> values = new ArrayList<>();
//        int lastRowNum = sheet.getLastRowNum();
//        String valueTitle = "";
//        for (int i = 0; i <= lastRowNum; i++) {
//            Row row = sheet.getRow(i);
//            //获取当前行最后单元格列号
//            int lastCellNum = row.getLastCellNum();
//            for (int j = 0; j < lastCellNum; j++) {
//                Cell cellTitle = row.getCell(j);
//                DataFormatter formatter = new DataFormatter();
//                valueTitle = formatter.formatCellValue(cellTitle);
//                if (i == 0) {
//                    titleList.add(valueTitle);
//                } else {
//                    values.add(valueTitle);
//                }
//                System.out.print(valueTitle + " ");
//            }
//            System.out.println();
//        }
//        int col = titleList.size();
//        int num = 0;
//        int sum=0;
//        for (int i = num; i < values.size(); i++) {
//            HashMap map = new HashMap();
//            if (i % col == 0 && i != num) { //
//                num++;
//                num = col * num;
//                i--;
//                sum = 0;
//                continue;
//            }
//            for (int j = sum; j < titleList.size(); j++) {
//                map.put(titleList.get(j), values.get(i));
//                System.out.println(map);
//                break;
//            }
//            sum++;
//
//        }
        kefumini();
    }
    static void  wxAPi(){

        String accessToken ="46_Mq5WJq6MPrdcvn-EDGsXKGQpRoL-SqKq4Lngo3uwM3Uq71C0uFXRRKK0wvwwSEsFJmfl3WNKpYLkGrogQ6fCG70UdfQ2j2GxkcxxpWKRnCC7k0rx6ZT3HEqweqUy4733BlSbc2-ib1PkoETESIMbABAJZD";
        String urlToken = WxApi.templateSendMs.replace("ACCESS_TOKEN",accessToken);
//        JsonNode jsonObject = JsonUtils.stringToJsonNode(HttpRequest.getwx(urlToken));
        JsonObject jb = new JsonObject();
        jb.addProperty("touser","oTeKzwI4_mJSKovEirIeY2dvzfvA");
        String tid="eG8z0x4TVBSE59dodde5nxD5mzflIWIAYQMQl5i8K-4";
        jb.addProperty("template_id", tid);
        JsonObject miniprogram = new JsonObject();
        miniprogram.addProperty("appid", "wx43d95d81bbebafae");
        miniprogram.addProperty("pagepath", "pages/index/index");
//        jb.add("miniprogram", miniprogram);


        JsonObject data = new JsonObject();

        JsonObject data1= new JsonObject();
        data1.addProperty("value","尊敬的用户，感谢您的咨询。现邀请您对我们的客服服务进行评价。");
        data.add("first", data1);

        data1= new JsonObject();
        data1.addProperty("value","kf2001@gh_1ddeda6c87d0");
        data.add("keyword1", data1);

        data1= new JsonObject();
        data1.addProperty("value","tothemoon");
        data.add("keyword2", data1);


        jb.add("data", data);


        JsonNode jsonObject = JsonUtils.stringToJsonNode(HttpRequest.postwx(urlToken, jb.toString()));
        System.out.println(jsonObject);



    }
    static void kefumini(){
        String accessToken ="46_5zH1YW-7ML1_X-jvtJtd4pqPyrX-KuvQKC17I6LdXFqcOBgWkMkGyzFE2tX8yp3RvwenkU1wCAZJOD_E89HeOYJtf5BlpZ9-5K3i_mcN3Mwt31MWtvZZmIWwMEF0VjFrd8Ab4Bws8_4a1pYqUVUbADABCB";
        String urlToken = WxApi.actSessionUrl.replace("ACCESS_TOKEN",accessToken);
        JsonObject jb = new JsonObject();
        JsonObject jb2 = new JsonObject();
        jb.addProperty("touser", "oeN-a5QTUc2YDguZb4FcgUDUfKXM");
        jb.addProperty("msgtype", "text");
        String content = "正在安排客服";
        jb2.addProperty("content", content);
        jb.add("text", jb2);
        JsonNode tmp = JsonUtils.stringToJsonNode(HttpRequest.postwx(urlToken, jb.toString()));
    }

}
