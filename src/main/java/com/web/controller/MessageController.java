package com.web.controller;


import com.alibaba.fastjson.JSONObject;
import com.web.model.EmpInfo;
import com.web.model.FeedbackRecord;
import com.web.model.MessageWorkorder;
import com.web.service.KfService;
import com.web.service.MemberService;
import com.web.service.OrderService;
import com.web.utils.HTMLSpirit;
import com.web.utils.Page;
import com.web.utils.ResultMap;
import com.web.utils.TimeParse;
import com.web.utils.database.DataSourceContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 工单和消息处理
 * */
@RequestMapping("/message")
@Controller
public class MessageController {

    @Autowired
    OrderService workorderService;
    @Autowired
    MemberService memberService;
    @Autowired
    KfService kfService;

    TimeParse timeParse = new TimeParse();
    HTMLSpirit htmlSpirit = new HTMLSpirit();
    ResultMap resultMap = new ResultMap();
    @ResponseBody
    @RequestMapping(value = "/kefu/deleteByid", produces = "application/json; charset=utf-8")
    public Object deletekefuByid(HttpServletRequest request){
        try {
            DataSourceContextHolder.setDbType("dataSourceA");

            String ids = request.getParameter("id");
            String[] idsArry = ids.split(",");
            for(int i =0;i<idsArry.length;i++){
                int id = Integer.parseInt(idsArry[i]);
                kfService.deleteByid(id);
            }
            resultMap.setCode(0);
            resultMap.setMsg("ok");
        }catch (Exception e){
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
        }
        return resultMap;
    }

    /**
     * 客服聊天记录
     * **/
    @ResponseBody
    @RequestMapping(value = "/kefu/chat/queryAll", produces = "application/json; charset=utf-8")
    public Object kefumessage_queryAll(Page page, @RequestParam("limit") int limit, HttpServletRequest request){
        List<EmpInfo> empInfoList = new ArrayList<>();
        DataSourceContextHolder.setDbType("dataSourceA");

        int totals = 0;
        try {
            page.setRows(limit);
            totals=kfService.selectPageCount(page);
            empInfoList=kfService.selectPageList(page);
            page.setTotalRecord(totals);
            resultMap.setData(empInfoList);
            resultMap.setCode(0);
            resultMap.setMsg("查询成功");
            resultMap.setCount(totals);
        }catch (Exception e){
            System.err.println(e.getMessage());
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
        }
        return resultMap;
    }


    /**
     * 消息中心 app页面
     * **/
    @ResponseBody
    @RequestMapping(value = "/message/queryAll", produces = "application/json; charset=utf-8")
    public Object message_queryAll(Page page, @RequestParam("limit") int limit, HttpServletRequest request){
        List<MessageWorkorder> messageWorkorderArrayList = new ArrayList<>();
        DataSourceContextHolder.setDbType("dataSourceA");
        int totals = 0;
        try {
            page.setRows(limit);

            MessageWorkorder workorder = new MessageWorkorder();
            String type = request.getParameter("type");
            String id = request.getParameter("id");
            String processor =request.getParameter("processor");
            String title =request.getParameter("title");
            int ids =-1;
            if(type==null||type.length()<=0){
                type = null;
            }
            if(type.equals("NO")){
                type = null;
            }
            if(processor==null||processor.length()<=0){
                processor = null;
            }
            if(title==null||title.length()<=0){
                title = null;
            }
            if(id==null||id.length()<=0){
                ids = -1;
            }else {
                ids = Integer.parseInt(id);
            }
            workorder.setId(ids);
            workorder.setType(type);
            workorder.setProcessor(processor);
            workorder.setTitle(title);
            page.setData(workorder);
            totals=workorderService.selectPageCount(page);
            messageWorkorderArrayList=workorderService.selectPageList(page);
            page.setTotalRecord(totals);
            resultMap.setData(messageWorkorderArrayList);
            resultMap.setCode(0);
            resultMap.setMsg("ok");
            resultMap.setCount(totals);
        }catch (Exception e){
            System.err.println(e.getMessage());
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
        }
        return resultMap;

    }
    /**
     * 消息中心 app页面  回复内容
     * **/
    @ResponseBody
    @RequestMapping(value = "/detail/queryAll", produces = "application/json; charset=utf-8")
    public Object feedbackmsg_queryAll(Page page, @RequestParam("limit") int limit,
                                       HttpServletRequest request){
        List<FeedbackRecord> feedbackRecordList = new ArrayList<>();
        int totals = 0;
        DataSourceContextHolder.setDbType("dataSourceA");

        try {
            page.setRows(limit);
            totals=workorderService.selectPageCountRecord(page);
            feedbackRecordList=workorderService.selectPageListRecord(page);
            page.setTotalRecord(totals);
            resultMap.setData(feedbackRecordList);
            resultMap.setCode(0);
            resultMap.setMsg("ok");
            resultMap.setCount(totals);
        }catch (Exception e){
            System.err.println(e.getMessage());
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
        }
        return resultMap;

    }

    /**
     * 根据id查找
     * **/
    @ResponseBody
    @RequestMapping(value = "/message/queryById", produces = "application/json; charset=utf-8")
    public Object message_queryById(HttpServletRequest request){
        try {
            DataSourceContextHolder.setDbType("dataSourceA");

            MessageWorkorder workorder = new MessageWorkorder();
            String type = request.getParameter("type");
            int id = Integer.parseInt(request.getParameter("id"));
            workorder.setType(type);
            workorder.setId(id);
            MessageWorkorder messageWorkorder=workorderService.queryMessageByid(workorder);
            resultMap.setData(messageWorkorder);
            resultMap.setCode(0);
            resultMap.setMsg("ok");
        }catch (Exception e){
            System.err.println(e.getMessage());
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
        }
        return resultMap;

    }

    /**
     * 根据id删除
     * **/
    @ResponseBody
    @RequestMapping(value = "/message/deleteByid", produces = "application/json; charset=utf-8")
    public Object deleteByid(HttpServletRequest request){
        try {
            DataSourceContextHolder.setDbType("dataSourceA");

            String ids = request.getParameter("id");
            String[] idsArry = ids.split(",");
            String type = request.getParameter("type");
            MessageWorkorder workorder = new MessageWorkorder();
            workorder.setType(type);
            for(int i =0;i<idsArry.length;i++){
                int id = Integer.parseInt(idsArry[i]);
                workorder.setId(id);
                workorderService.deleteByid(workorder);
            }
            resultMap.setCode(0);
            resultMap.setMsg("ok");
        }catch (Exception e){
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
        }
        return resultMap;
    }

    /**
     * 回复邮件
     * **/
    @ResponseBody
    @RequestMapping(value = "/message/detailUpload", produces = "application/json; charset=utf-8")
    public Object detailUpload(@RequestBody String jsonData,HttpServletRequest request){
        try {
            DataSourceContextHolder.setDbType("dataSourceA");

            JSONObject json = JSONObject.parseObject(jsonData);
            String content = json.getString("content");
            String title = json.getString("title");
            String sendName = json.getString("sendName");
            String mid = json.getString("mid");
            int id = Integer.parseInt(mid);


            String receiveName = json.getString("receiveName");
            FeedbackRecord feedbackRecord = new FeedbackRecord();
            feedbackRecord.setContact(sendName);
            feedbackRecord.setMsgContent(content);
            feedbackRecord.setProcessor(sendName);
            feedbackRecord.setCreateTime(timeParse.convertDate2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
            feedbackRecord.setMid(id);
            feedbackRecord.setTitle(title);
            workorderService.insertFeedbackRecord(feedbackRecord);

            MessageWorkorder workorder = new MessageWorkorder();
            workorder.setProcessor(sendName);
            workorder.setProcess("25%");
            workorder.setStatus("回复邮件");
            workorder.setId(id);
            workorderService.updateOrder(workorder);

            /*
            * 发送邮件
            * **/






            resultMap.setCode(0);
            resultMap.setMsg("ok");
        }catch (Exception e){
            System.err.println(e.getMessage());
            resultMap.setCode(500);
            resultMap.setMsg(e.getMessage());
        }
        return resultMap;

    }
}
