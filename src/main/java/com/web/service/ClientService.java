package com.web.service;

import com.web.mapper.ClientMapper;
import com.web.model.jwjx.*;
import com.web.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ClientService {
    @Autowired
    ClientMapper clientMapper;

    public int selectPageCount(Page page) {
        return clientMapper.selectPageCount(page);
    }

    public List<HashMap<String, Object>> selectPageList(Page page) {
        return clientMapper.selectPageList(page);

    }

    public void deleteCompanyByid(int id) {
        clientMapper.deleteCompanyByid(id);
    }

    public List<HashMap<String, Object>> queryjwjxClientInfo(HashMap param) {
        return clientMapper.queryjwjxClientInfo(param);

    }

    public void updateEgtCompany(EgtCompany egtCompany) {
        clientMapper.updateEgtCompany(egtCompany);
    }

    public void insertEgtCompany(EgtCompany egtCompany) {
        clientMapper.insertEgtCompany(egtCompany);

    }

    public int selectPageCountapplet(Page page) {
        return clientMapper.selectPageCountapplet(page);
    }

    public List<HashMap<String, Object>> selectPageListapplet(Page page) {
        return clientMapper.selectPageListapplet(page);
    }

    public List<HashMap<String, Object>> queryEgtcompany(HashMap param) {
        return clientMapper.queryEgtcompany(param);

    }

    public void deleteAppletByid(String openid) {
        clientMapper.deleteAppletByid(openid);
    }

    public void insertApplet(Applet applet) {
        clientMapper.insertApplet(applet);

    }

    public void updateApplet(Applet applet) {
        clientMapper.updateApplet(applet);
    }

    public List<HashMap<String, Object>> queryegt_clientInfo(HashMap param) {
        return clientMapper.queryegt_clientInfo(param);

    }

    public int selectPageCountjwjxclientInfo(Page page) {
        return clientMapper.selectPageCountjwjxclientInfo(page);

    }

    public List<HashMap<String, Object>> selectPageListjwjxclientInfo(Page page) {
        return clientMapper.selectPageListjwjxclientInfo(page);

    }

    public int selectPageCountegt_remote_mode(Page page) {
        return clientMapper.selectPageCountegt_remote_mode(page);
    }

    public List<HashMap<String, Object>> selectPageListegt_remote_mode(Page page) {
        return clientMapper.selectPageListegt_remote_mode(page);
    }

    public void deleteegt_remote_modeByid(String id) {
        clientMapper.deleteegt_remote_modeByid(id);
    }

    public void updateEgtRemoteMode(EgtRemoteMode egtRemoteMode) {
        clientMapper.updateEgtRemoteMode(egtRemoteMode);

    }

    public void insertEgtRemoteMode(EgtRemoteMode egtRemoteMode) {
        clientMapper.insertEgtRemoteMode(egtRemoteMode);
    }

    public List<HashMap<String, Object>> selectPageListgnss_system_Clientdb(Page page) {
        return clientMapper.selectPageListgnss_system_Clientdb(page);

    }

    public int selectPageCountgnss_system_Clientdb(Page page) {
        return clientMapper.selectPageCountgnss_system_Clientdb(page);

    }

    public void deletegnss_system_ClientdbByid(String id) {
        clientMapper.deletegnss_system_ClientdbByid(id);

    }

    public void insertGnss_system_Clientdb(Gnss_system_Clientdb item) {
        clientMapper.insertGnss_system_Clientdb(item);
    }

    public void updateGnss_system_Clientdb(Gnss_system_Clientdb item) {
        clientMapper.updateGnss_system_Clientdb(item);
    }

    public int selectPageCountgnss_system_ClientTerminaldb(Page page) {
        return clientMapper.selectPageCountgnss_system_ClientTerminaldb(page);
    }

    public List<HashMap<String, Object>> selectPageListgnss_system_ClientTerminaldb(Page page) {
        return clientMapper.selectPageListgnss_system_ClientTerminaldb(page);
    }

    public void deletegnss_system_ClientTerminaldbByid(String id) {
        clientMapper.deletegnss_system_ClientTerminaldbByid(id);

    }

    public void insertGnss_system_ClientTerminaldb(List list) {
        clientMapper.insertGnss_system_ClientTerminaldb(list);


    }

    public int selectPageCountjwjxweixin(Page page) {
        return clientMapper.selectPageCountjwjxweixin(page);

    }

    public List<HashMap<String, Object>> selectPageListjwjxweixin(Page page) {
        return clientMapper.selectPageListjwjxweixin(page);
    }

    public void deletejwjx_weixinByid(String id) {
        clientMapper.deletejwjx_weixinByid(id);
    }

    public void insertjwjx_weixin(HashMap item) {
        clientMapper.insertjwjx_weixin(item);

    }

    public void updatejwjx_weixin(HashMap item) {
        clientMapper.updatejwjx_weixin(item);
    }
}
