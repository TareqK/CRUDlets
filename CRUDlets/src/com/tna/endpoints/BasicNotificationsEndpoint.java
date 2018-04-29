/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.endpoints;

import com.tna.common.BasicNotificationSessionManager;
import java.io.IOException;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author tareq
 */
public abstract class BasicNotificationsEndpoint {

    @OnOpen
    public void open(Session session) {
        BasicNotificationSessionManager.addSession(session);
    }

    @OnClose
    public void close(Session session) {
        BasicNotificationSessionManager.removeSession(session);
        try {
            session.close();
        } catch (IOException ex) {

        }
    }

    public BasicNotificationsEndpoint() {

    }

}
