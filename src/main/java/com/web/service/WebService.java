package com.web.service;

import com.web.mapper.ClientMapper;
import com.web.mapper.WebMapper;
import com.web.model.jwjx.*;
import com.web.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class WebService {
    @Autowired
    WebMapper webMapper;


    public int selectPageCountQuestion(Page page) {
        return webMapper.selectPageCountQuestion(page);
    }

    public List<HashMap<String, Object>> selectPageListQuestion(Page page) {
        return webMapper.selectPageListQuestion(page);

    }

    public void deleteQuestionByid(String id) {
        webMapper.deleteQuestionByid(id);
    }

    public void insertwebQuestion(HashMap item) {
        webMapper.insertwebQuestion(item);

    }

    public void updatewebQuestion(HashMap item) {
        webMapper.updatewebQuestion(item);
    }

    public void deleteByid(HashMap map) {
        webMapper.deleteByid(map);
    }

    public void updateTags(HashMap item) {
        webMapper.updateTags(item);
    }
    public void insertTags(HashMap item) {
        webMapper.insertTags(item);
    }

    public int selectPageCountGzhuser(Page page) {
        return webMapper.selectPageCountGzhuser(page);
    }

    public List<HashMap<String, Object>> selectPageListGzhuser(Page page) {
        return webMapper.selectPageListGzhuser(page);
    }
}
