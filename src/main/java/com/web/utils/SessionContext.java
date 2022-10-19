package com.web.utils;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

public class SessionContext {
    private static SessionContext instance;
    private HashMap<String,HttpSession> sessionMap;

    private SessionContext() {
        sessionMap = new HashMap<String,HttpSession>();
    }

    public static SessionContext getInstance() {
        if (instance == null) {
            instance = new SessionContext();
        }
        return instance;
    }

    public synchronized void AddSession(HttpSession session) {
        if (session != null) {
            sessionMap.put(session.getId(), session);
        }
    }

    public synchronized void DelSession(HttpSession session) {
        if (session != null) {
            sessionMap.remove(session.getId());
            if(session.getAttribute("userid")!=null){
                sessionMap.remove(session.getAttribute("userid").toString());
                //session.invalidate();
            }
        }
    }

    public synchronized HttpSession getSession(String session_id) {
        if (session_id == null) return null;
        return (HttpSession) sessionMap.get(session_id);
    }

    public HashMap getSessionMap() {
        return sessionMap;
    }

    public void setMymap(HashMap sessionMap) {
        this.sessionMap = sessionMap;
    }

}
