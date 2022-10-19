package com.web.service;

import com.web.mapper.CertificationMapper;
import com.web.model.Certification;
import com.web.model.Role;
import com.web.model.Servers;
import com.web.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificationService {
    @Autowired
    CertificationMapper certificationMapper;


    public List<Certification> queryAll(Page page) {
        List<Certification> list =  certificationMapper.queryAll(page);
        return list;
    }

    public Servers queryAllservers() {
        return certificationMapper.queryAllservers();
    }
}
