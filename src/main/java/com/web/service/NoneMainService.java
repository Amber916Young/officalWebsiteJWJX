package com.web.service;

import com.web.mapper.NoneMainMapper;
import com.web.model.NoneMain;
import com.web.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NoneMainService {
    @Autowired
    NoneMainMapper mainMapper;


    public NoneMain queryBytabName(String tabName) {
        return mainMapper.queryBytabName(tabName);
    }


    public HashMap<String, Object> queryWXToken(String param) {
        return mainMapper.queryWXToken(param);
    }

    public void updateJwjxWeixin(HashMap hashMap) {
        mainMapper.updateJwjxWeixin(hashMap);

    }


    public List<HashMap<String, Object>> queryAllCompany() {
        return  mainMapper.queryAllCompany();

    }

    public HashMap<String, Object> queryCompany(HashMap param) {
        return mainMapper.queryCompany(param);
    }

    public void insertWxfeedBack(Map map) {
        mainMapper.insertWxfeedBack(map);
    }

    public HashMap selectWxfeedBack(Map map) {
        return  mainMapper.selectWxfeedBack(map);

    }

    public void updateWxfeedBack(Map map) {
        mainMapper.updateWxfeedBack(map);
    }

    public void insertWxkf(Map map) {
        mainMapper.insertWxkf(map);
    }

    public HashMap queryTemplate(HashMap param) {
        return  mainMapper.queryTemplate(param);

    }

    public int selectPageCount_templateuser(Page page) {
        return  mainMapper.selectPageCount_templateuser(page);
    }

    public List<HashMap<String, Object>> selectPageList_templateuser(Page page) {
        return  mainMapper.selectPageList_templateuser(page);

    }

    public List<HashMap<String, Object>> selectPageList_template(Page page) {
        return  mainMapper.selectPageList_template(page);
    }

    public int selectPageCount_template(Page page) {
        return  mainMapper.selectPageCount_template(page);
    }

    public void deleteTemplateUserByid(HashMap map) {
        mainMapper.deleteTemplateUserByid(map);
    }

    public void deleteTemplateByid(Integer id) {
        mainMapper.deleteTemplateByid(id);

    }

    public void insertTemplateuser(HashMap map) {
        mainMapper.insertTemplateuser(map);

    }

    public void updatetemplatemsg(HashMap map) {
        mainMapper.updatetemplatemsg(map);
    }

    public int selectPageCountWxchat(Page page) {
        return  mainMapper.selectPageCountWxchat(page);
    }

    public List<HashMap<String, Object>> selectPageListWxchat(Page page) {
        return  mainMapper.selectPageListWxchat(page);
    }

    public void deleteTemplateUser(int id) {
        mainMapper.deleteTemplateUser(id);
    }
}
