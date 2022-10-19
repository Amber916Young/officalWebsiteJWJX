package com.web.service;

import com.web.mapper.TagsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class TagsService {
    @Autowired
    TagsMapper tagsMapper;

    public   List<HashMap<String, Object>>  queryAll() {
        return tagsMapper.queryAll();
    }
}
