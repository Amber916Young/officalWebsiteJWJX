package com.web.service;

import com.web.mapper.IPMapper;
import com.web.mapper.TagsMapper;
import com.web.model.IP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class IPService {
    @Autowired
    IPMapper ipMapper;

    public IP queryByRegNO(String regNO) {
        return ipMapper.queryByRegNO(regNO);
    }
}
