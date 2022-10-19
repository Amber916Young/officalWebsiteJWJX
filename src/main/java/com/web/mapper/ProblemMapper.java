package com.web.mapper;

import com.web.model.Problem;
import com.web.utils.Page;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

//@Repository
public interface ProblemMapper {

//    @Select("select * from question where status=1 order by createTime desc")
    List<Problem> queryAll(Page page);
}
