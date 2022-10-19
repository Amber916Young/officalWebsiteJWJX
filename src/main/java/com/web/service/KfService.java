package com.web.service;

import com.web.mapper.ArticleMapper;
import com.web.mapper.KfMapper;
import com.web.model.Article;
import com.web.model.EmpInfo;
import com.web.model.MessageWorkorder;
import com.web.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class KfService {
    @Autowired
    KfMapper kfMapper;

    //保存聊天信息
    public void insertChatMessage(EmpInfo empInfo) {
        kfMapper.insertChatMessage(empInfo);
    }



    public void updateChatStatus(String nickname) {
        kfMapper.updateChatStatus(nickname);
    }



    public List<EmpInfo> queryChatList2(HashMap hashMap) {
        return kfMapper.queryChatList2(hashMap);
    }

    public void updateChatStatus2(String toId) {
        kfMapper.updateChatStatus2(toId);
    }

    public int selectPageCount(Page page) {
        return  kfMapper.selectPageCount(page);
    }

    public List<EmpInfo> selectPageList(Page page) {
        List<EmpInfo> list =  kfMapper.selectPageList(page);
        return list;
    }

    public void deleteByid(int id) {
        kfMapper.deleteByid(id);
    }

    public List<EmpInfo> queryAllChats(HashMap map) {
        return kfMapper.queryAllChats(map);
    }

    public void updateTranfer(EmpInfo empInfo) {
        kfMapper.updateTranfer(empInfo);
    }

    public void insertTransferChat(HashMap<String, Object> map) {
        kfMapper.insertTransferChat(map);

    }

    public List<HashMap<String, Object>> queryAllTransferChats(HashMap map) {
        return kfMapper.queryAllTransferChats(map);

    }

    public void updateTranferStatus(int id) {
        kfMapper.updateTranferStatus(id);

    }

    public List<EmpInfo> queryAllHistoryChat(HashMap map) {
        return kfMapper.queryAllHistoryChat(map);
    }

    public List<EmpInfo> queryAllHistoryChatTO(HashMap map) {
        return kfMapper.queryAllHistoryChatTO(map);
    }

    public List<EmpInfo> queryAllHistoryChatWindows(HashMap map) {
        return kfMapper.queryAllHistoryChatWindows(map);
    }

    public List<EmpInfo> queryAllHistoryChattest(Page page) {
        return kfMapper.queryAllHistoryChattest(page);

    }

    public int selectHistoryCount(Page page) {
        return kfMapper.selectHistoryCount(page);
    }
}
