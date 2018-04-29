/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.utils;

import javax.websocket.Session;

/**
 *
 * @author tareq
 */
public class UserSession {

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPrivilege() {
        return privilege;
    }

    public void setPrivilege(long privilege) {
        this.privilege = privilege;
    }
    boolean lock;
    String token;
    long id;
    long privilege;
    Session userSession;

    public UserSession(String token, long id, long privilege, Session userSession) {
        this.token = token;
        this.id = id;
        this.privilege = privilege;
        this.userSession = userSession;
        this.lock = false;
    }

    public Session getUserSession() {
        return userSession;
    }

    public void setUserSession(Session userSession) {
        this.userSession = userSession;
    }
    
}
