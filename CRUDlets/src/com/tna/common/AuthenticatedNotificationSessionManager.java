/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.common;

import com.tna.utils.UserSession;
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
public class AuthenticatedNotificationSessionManager {

    private static AuthenticatedNotificationSessionManager sessionManager;
    private static HashMap<Session, UserSession> userSessions;

    private static AuthenticatedNotificationSessionManager getInstance() {
        if (sessionManager == null) {
            sessionManager = new AuthenticatedNotificationSessionManager();
        }
        return sessionManager;

    }

    private AuthenticatedNotificationSessionManager() {
        userSessions = new HashMap();
    }

    public synchronized static UserSession get(Session session){
        return AuthenticatedNotificationSessionManager.getInstance().userSessions.get(session);
    }
    
    public synchronized static void checkout(Session session) {
        UserSession userSession = AuthenticatedNotificationSessionManager.getInstance().userSessions.get(session);

        if (userSession != null) {
            while (userSession.isLock() == true) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException ex) {
                    return;
                }
            }
            userSession.setLock(true);
            AuthenticatedNotificationSessionManager.getInstance().userSessions.replace(session, userSession);
        }
    }

    public synchronized static void checkin(Session session) {
        UserSession userSession = AuthenticatedNotificationSessionManager.getInstance().userSessions.get(session);

        if (userSession != null) {
            userSession.setLock(false);
            AuthenticatedNotificationSessionManager.getInstance().userSessions.replace(session, userSession);
        }

    }

    public synchronized static Set sessionsSet() {
        return AuthenticatedNotificationSessionManager.getInstance().userSessions.keySet();
    }
    

    public static void addUserSession(UserSession userSession, Session session) {

        AuthenticatedNotificationSessionManager.getInstance().userSessions.put(session, userSession);

    }

    public static void removeUserSession(UserSession session) {
        AuthenticatedNotificationSessionManager.getInstance().userSessions.remove(session);
    }

    public static void closeAllSessions() {
        Set<Session> allSessions = sessionsSet();
        for (Session session : allSessions) {
            UserSession userSession = AuthenticatedNotificationSessionManager.getInstance().userSessions.get(session);
            removeUserSession(userSession);
            try {
                userSession.getUserSession().close();
            } catch (IOException ex) {
                Logger.getLogger(AuthenticatedNotificationSessionManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
