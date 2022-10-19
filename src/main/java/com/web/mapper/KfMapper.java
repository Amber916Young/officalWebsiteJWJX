package com.web.mapper;

import com.web.model.Article;
import com.web.model.EmpInfo;
import com.web.utils.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.HashMap;
import java.util.List;

public interface KfMapper {


    void insertChatMessage(EmpInfo empInfo);


    void updateChatStatus(String nickname);

    List<EmpInfo> queryChatList2(HashMap hashMap);

    void updateChatStatus2(String toId);

    int selectPageCount(Page page);

    List<EmpInfo> selectPageList(Page page);

    void deleteByid(int id);

    //查询当日此用户的聊天记录
    @Select("select * from ChatInfo where fromId=#{fromId} and toId=#{toId} and " +
            " TO_DAYS(date_format(sendTime,'%Y-%m-%d')) = TO_DAYS(NOW())")
    List<EmpInfo> queryAllChats(HashMap map);

    void updateTranfer(EmpInfo empInfo);

    void insertTransferChat(HashMap<String, Object> map);



    @Select("select * from view_transferChat where tstatus=0 and transferId=#{transferID} order by sendTime asc")
    List<HashMap<String, Object>> queryAllTransferChats(HashMap map);

    @Update("update transferChat set status=1 where id=#{id} ")
    void updateTranferStatus(int id);

    List<EmpInfo> queryAllHistoryChat(HashMap map);

    List<EmpInfo> queryAllHistoryChatTO(HashMap map);

    @Select("SELECT DISTINCT * FROM  ChatInfo  where fromId=#{fromId} or toId=#{fromId}  GROUP BY fromId ")
    List<EmpInfo> queryAllHistoryChatWindows(HashMap map);

    List<EmpInfo> queryAllHistoryChattest(Page page);

    int selectHistoryCount(Page page);
}
