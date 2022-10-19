package com.web.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.model.EmpInfo;
import com.web.service.KfService;
import com.web.utils.database.DataSourceContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.json.JSONObject;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

//消息处理类
@Component
public class myHandler extends TextWebSocketHandler {

    // 在线用户列表
    public static final Map<Integer, WebSocketSession> userSocketSessionMap = new HashMap<Integer, WebSocketSession>();


    // 用户连接成功 就被调用
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //获取传来的登录用户的id
        String idstr = session.getUri().toString().split("=")[1];
        int id = Integer.parseInt(idstr);
        System.err.println("用户连接成功");
        //保存对应的WebSocketSession
        userSocketSessionMap.put(id, session);
        System.err.println("userSocketSessionMap===>"+userSocketSessionMap);
    }


    @Autowired
    KfService kfService;


    // 消息处理方法
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JSONObject jsonObject = new JSONObject(message.getPayload());
        DataSourceContextHolder.setDbType("dataSourceA");
        String type = jsonObject.getString("type");
        if (type.equals("heart") ) {
            onMessage(message,session);
        } else {
            JSONObject data = jsonObject.getJSONObject("data");
            JSONObject mineJson = data.getJSONObject("mine");
            JSONObject toJson = data.getJSONObject("to");
            EmpInfo empInfo = new EmpInfo();
            //发送方信息
            String v_username = mineJson.getString("username");
            String v_avatar = mineJson.getString("avatar");
            String v_id = mineJson.getString("id");
            Boolean v_mine = mineJson.getBoolean("mine");
            String v_content = mineJson.getString("content");
            empInfo.setFromId(v_id);
            empInfo.setFromavatar(v_avatar);
            empInfo.setFromContent(v_content);
            empInfo.setMine(v_mine);
            empInfo.setFromUsername(v_username);
            //接收方信息
            String c_name = toJson.getString("name");
            String c_type = toJson.getString("type");
            String c_remark = "";
            if (toJson.has("remark")) {
                c_remark = toJson.getString("remark");
            } else if (toJson.has("sign")) {
                c_remark = toJson.getString("sign");
            }
            String c_username = toJson.getString("username");
            String c_avatar = toJson.getString("avatar");
            String c_id = String.valueOf(toJson.getInt("id"));
            empInfo.setToId(c_id);
            empInfo.setToName(c_name);
            empInfo.setToType(c_type);
            empInfo.setToUsername(c_username);
            empInfo.setRemark(c_remark);
            empInfo.setToAvatar(c_avatar);
            if (type.equals("kefu")) {
                kfService.insertChatMessage(empInfo);
                sendMessageToUser(message, empInfo);
            }
        }

    }
    public void onMessage(TextMessage message, WebSocketSession session) {
        Set<Integer> clientIds  = userSocketSessionMap.keySet();
        JSONObject jsonObject = new JSONObject(message.getPayload());
        String toId = jsonObject.getString("toId");
        DataSourceContextHolder.setDbType("dataSourceA");

//        List<EmpInfo> list = kfService.queryChatList2(toId);
//        if (list.size()!=0){
//
//        }

        for (Integer clientId : clientIds) {
            session = userSocketSessionMap.get(clientId);
            String clientIdStr = String.valueOf(clientId);
            try {
                if(session.isOpen()&&toId.equals(clientIdStr)){
                    session.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

        /**
         * 给所有的用户发送消息
         */
    public void sendMessageToUser(TextMessage message,EmpInfo UserInfo) throws IOException {

        Set<Integer> clientIds  = userSocketSessionMap.keySet();
        WebSocketSession session = null;
        int flag = 0;
        for (Integer clientId : clientIds) {
            session = userSocketSessionMap.get(clientId);
            System.out.println(session);
            String clientIdStr = String.valueOf(clientId);
            try {
                System.out.println(session.isOpen());
                if(session.isOpen()&&UserInfo.getToId().equals(clientIdStr)){
                    System.out.println("回复消息");
                    session.sendMessage(message);
                    flag = 1;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String nickname = UserInfo.getToUsername();
        if(flag==1){
            kfService.updateChatStatus(nickname);
        }
    }


    //用户退出后的处理，退出之后，要将用户信息从websocket的session中remove掉，这样用户就处于离线状态了，也不会占用系统资源
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        try {
            if (session.isOpen()) {
                session.close();
            }
             userSocketSessionMap.remove(session.getId());
            System.out.println("退出系统");
        } catch (Exception e) {
            System.out.println("用户非正常关闭");
        }
    }

}
