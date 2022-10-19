package com.web.service;

import com.web.mapper.OrderMapper;
import com.web.model.FeedbackRecord;
import com.web.model.MessageWorkorder;
import com.web.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderMapper workorderMapper;

    public void insertWorkorder(MessageWorkorder messageWorkorder) {
        workorderMapper.insertWorkorder(messageWorkorder);
    }

    public int selectPageCount(Page page) {
        return  workorderMapper.selectPageCount(page);
    }

    public List<MessageWorkorder> selectPageList(Page page) {
        List<MessageWorkorder> list =  workorderMapper.selectPageList(page);
        return list;
    }

    public MessageWorkorder queryMessageByid(MessageWorkorder workorder) {
        return workorderMapper.queryMessageByid(workorder);
    }

    public void deleteByid( MessageWorkorder workorder ) {
        workorderMapper.deleteByid( workorder );
    }


    public void updateOrder(MessageWorkorder workorder) {
        workorderMapper.updateOrder(workorder);
    }


    //历史  工单或者网页邮件回复记录查询   不想再写一个xml文件，直接写在dao层
    public void insertFeedbackRecord(FeedbackRecord feedbackRecord) {
        workorderMapper.insertFeedbackRecord(feedbackRecord);
    }

    public int selectPageCountRecord(Page page) {
        return workorderMapper.selectPageCountRecord(page);
    }

    public List<FeedbackRecord> selectPageListRecord(Page page) {
        List<FeedbackRecord> list =  workorderMapper.selectPageListRecord(page);
        return list;

    }

    //=============end===================================




}
