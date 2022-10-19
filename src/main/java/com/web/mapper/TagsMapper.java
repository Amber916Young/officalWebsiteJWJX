package com.web.mapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

//@Repository
public interface TagsMapper {



    List<HashMap<String, Object>> queryAll();
}
