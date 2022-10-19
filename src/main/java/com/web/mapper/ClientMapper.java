package com.web.mapper;


import com.web.model.jwjx.*;
import com.web.utils.Page;
import org.apache.ibatis.annotations.Delete;

import java.util.HashMap;
import java.util.List;

public interface ClientMapper {

    int selectPageCount(Page page);

    List<HashMap<String, Object>> selectPageList(Page page);

    @Delete("delete from egt.egt_company where companyID=#{id}")
    void deleteCompanyByid(int id);

    List<HashMap<String, Object>> queryjwjxClientInfo(HashMap param);

    void updateEgtCompany(EgtCompany egtCompany);

    void insertEgtCompany(EgtCompany egtCompany);

    int selectPageCountapplet(Page page);

    List<HashMap<String, Object>> selectPageListapplet(Page page);

    List<HashMap<String, Object>> queryEgtcompany(HashMap param);

    @Delete("delete from jwjx.applet where openid=#{openid}")
    void deleteAppletByid(String openid);

    void insertApplet(Applet applet);

    void updateApplet(Applet applet);

    List<HashMap<String, Object>> queryegt_clientInfo(HashMap param);

    int selectPageCountjwjxclientInfo(Page page);

    List<HashMap<String, Object>> selectPageListjwjxclientInfo(Page page);

    int selectPageCountegt_remote_mode(Page page);

    List<HashMap<String, Object>> selectPageListegt_remote_mode(Page page);

    @Delete("delete from egt_remote_mode where id=#{id}")
    void deleteegt_remote_modeByid(String id);

    void updateEgtRemoteMode(EgtRemoteMode egtRemoteMode);

    void insertEgtRemoteMode(EgtRemoteMode egtRemoteMode);

    List<HashMap<String, Object>> selectPageListgnss_system_Clientdb(Page page);

    int selectPageCountgnss_system_Clientdb(Page page);


    @Delete("delete from gnss_system.clientdb where clientID=#{id}")
    void deletegnss_system_ClientdbByid(String id);

    void insertGnss_system_Clientdb(Gnss_system_Clientdb item);

    void updateGnss_system_Clientdb(Gnss_system_Clientdb item);

    int selectPageCountgnss_system_ClientTerminaldb(Page page);

    List<HashMap<String, Object>> selectPageListgnss_system_ClientTerminaldb(Page page);

    @Delete("delete from gnss_system.client_terminals_db where ID=#{id}")
    void deletegnss_system_ClientTerminaldbByid(String id);

    void insertGnss_system_ClientTerminaldb(List list);

    int selectPageCountjwjxweixin(Page page);

    List<HashMap<String, Object>> selectPageListjwjxweixin(Page page);


    @Delete("delete from jwjx.weixin where appID=#{id}")
    void deletejwjx_weixinByid(String id);

    void insertjwjx_weixin(HashMap item);

    void updatejwjx_weixin(HashMap item);
}