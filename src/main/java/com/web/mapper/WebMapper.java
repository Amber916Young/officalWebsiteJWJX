package com.web.mapper;


import com.web.model.jwjx.*;
import com.web.utils.Page;
import lombok.Data;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;

public interface WebMapper {

    List<HashMap<String, Object>> selectPageListQuestion(Page page);

    int selectPageCountQuestion(Page page);

    @Delete("delete from jwjxinfo2.question where id =#{id}")
    void deleteQuestionByid(String id);

    void insertwebQuestion(HashMap item);

    void updatewebQuestion(HashMap item);

    @Delete("delete from jwjxinfo2.tags where id =#{id}")
    void deleteByid(HashMap map);

    void updateTags(HashMap item);
    void insertTags(HashMap item);

    int selectPageCountGzhuser(Page page);

    List<HashMap<String, Object>> selectPageListGzhuser(Page page);
}