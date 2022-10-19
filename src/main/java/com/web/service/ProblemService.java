package com.web.service;

import com.web.mapper.ProblemMapper;
import com.web.mapper.TagsMapper;
import com.web.model.Problem;
import com.web.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ProblemService {
    @Autowired
    ProblemMapper problemMapper;

    public List<Problem> queryAll(Page page) {
        return problemMapper.queryAll( page);
    }


}
