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
            synchronized(AuthenticatedNotificationSessionManager.class){
                if(sessionManager == null){
            sessionManager = new AuthenticatedNotificationSessionManager();
            }
        }    

    }
         return sessionManager;
}
    private AuthenticatedNotificationSessionManager() {
        userSessions = new HashMap();
    }

    public synchronized static UserSession get(Session session){
        return AuthenticatedNotificationSessionManager.getInstance().userSessions.get(session);
    }
    
    public static void checkout(Session session) {
    AuthenticatedNotificationSessionManager.getInstance().userSessions.get(session).lock.lock();      
    }

    public static void checkin(Session session) {
        AuthenticatedNotificationSessionManager.getInstance().userSessions.get(session).lock.unlock();
    }

    public synchronized static Set sessionsSet() {
        return AuthenticatedNotificationSessionManager.getInstance().userSessions.keySet();
    }
    

    public synchronized static void addUserSession(UserSession userSession, Session session) {

        AuthenticatedNotificationSessionManager.getInstance().userSessions.put(session, userSession);

    }

    public synchronized static void removeUserSession(UserSession session) {
        AuthenticatedNotificationSessionManager.getInstance().userSessions.remove(session.getUserSession());
    }

    public synchronized static void closeAllSessions() {
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
