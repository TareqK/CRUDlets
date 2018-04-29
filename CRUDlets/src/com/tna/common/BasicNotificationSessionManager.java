/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Session;

/**
 *
 * @author tareq
 */
public class BasicNotificationSessionManager {

    private static BasicNotificationSessionManager sessionManager;
    private static HashMap<Session, Boolean> sessions;

    private static BasicNotificationSessionManager getInstance() {
        if (sessionManager == null) {
            sessionManager = new BasicNotificationSessionManager();
        }
        return sessionManager;

    }

    private BasicNotificationSessionManager() {
        sessions = new HashMap();
    }

    public synchronized static void checkout(Session session) {
        if (BasicNotificationSessionManager.getInstance().sessions.get(session) != null) {
            while (BasicNotificationSessionManager.getInstance().sessions.get(session) == true) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException ex) {
                    return;
                }
            }

            BasicNotificationSessionManager.getInstance().sessions.put(session, Boolean.TRUE);
        }
    }

    public static void checkin(Session session) {
        if (BasicNotificationSessionManager.getInstance().sessions.get(session) != null) {
            BasicNotificationSessionManager.getInstance().sessions.put(session, Boolean.FALSE);
        }

    }

    public static Set sessionsSet() {
        return BasicNotificationSessionManager.getInstance().sessions.keySet();
    }

    public static void addSession(Session session) {

        BasicNotificationSessionManager.getInstance().sessions.put(session, Boolean.FALSE);

    }

    public static void removeSession(Session session) {
        BasicNotificationSessionManager.getInstance().sessions.remove(session);
    }

    public static void closeAllSessions() {
        Set<Session> allSessions = sessionsSet();
        for (Session session : allSessions) {
            removeSession(session);
            try {
                session.close();
            } catch (IOException ex) {
                Logger.getLogger(BasicNotificationSessionManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
