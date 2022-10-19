package com.web.mapper;

import com.web.model.Article;
import com.web.model.FeedbackRecord;
import com.web.model.MessageWorkorder;
import com.web.utils.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

//@Repository
public interface OrderMapper {

    void insertWorkorder(MessageWorkorder messageWorkorder);

    int selectPageCount(Page page);

    List<MessageWorkorder> selectPageList(Page page);

    MessageWorkorder queryMessageByid(MessageWorkorder workorder);


    void deleteByid( MessageWorkorder workorder );


    @Insert("insert into feedbackrecord(mid,processor,createTime,msgContent,contact) values(" +
            "#{mid}" +
            ",#{processor}" +
            ",#{createTime}" +
            ",#{msgContent}" +
            ",#{contact})")
    void insertFeedbackRecord(FeedbackRecord feedbackRecord);

    void updateOrder(MessageWorkorder workorder);

    @Select("select count(1) from feedbackrecord")
    int selectPageCountRecord(Page page);


    @Select("select * from feedbackrecord  order by id DESC limit #{start},#{rows}")
    List<FeedbackRecord> selectPageListRecord(Page page);
}
