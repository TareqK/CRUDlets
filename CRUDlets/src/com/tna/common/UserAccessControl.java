/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.common;

import com.tna.common.AccessError.ERROR_TYPE;
import com.tna.data.Access;
import com.tna.data.Persistence;
import static com.tna.data.Persistence.GET_PRIVILEGE_AND_ID_SQL;
import static com.tna.data.Persistence.SELECT_OBJECT_USER;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
public class UserAccessControl {

    /**
     * Thrown when an unauthorised operation is attempted.
     */
    public static class UnauthorisedException extends Exception {

        public UnauthorisedException() {
            System.out.println("Anuthroised Request");
        }
    }

    /**
     * Performs a login operation with a user's username and password, and
     * assigns them a new token on success.
     *
     * @param author
     * @param obj
     * @return a JSONObject with the token and user id
     * @throws UserAccessControl.UnauthorisedException
     */
    public static JSONObject login(Class author, JSONObject obj) throws AccessError {
        Connection conn = Access.pool.checkOut();
        JSONObject result = new JSONObject();
        try {
            PreparedStatement pstmt;
            pstmt = conn.prepareStatement(String.format(Persistence.GET_PASSWORD_SQL, author.getSimpleName()));
            pstmt.setObject(1, obj.get("userName"));
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            if (!rs.getString("password").equals(obj.get("password").toString())) {
                throw new AccessError(ERROR_TYPE.USER_NOT_AUTHORISED);
            }
            PreparedStatement pstmt2 = conn.prepareStatement(String.format(Persistence.SET_TOKEN_SQL, author.getSimpleName()));
            String token = UUID.randomUUID().toString();
            long id = rs.getInt("id");
            pstmt2.setString(1, token);
            pstmt2.setLong(2, id);
            pstmt2.execute();
            result.put("token", token);
            result.put("id", id);
            rs.close();
            pstmt.close();
            pstmt2.close();
        } catch (SQLException ex) {
                throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
        } finally {
            Access.pool.checkIn(conn);
        }
        return result;
    }

    /**
     * Authorises an operation of a certain privilege level.If the privilege
     * level of the operation is higher than that of the user, it throws an
     * exception. Otherwise, it returns nothing. If the user level is 999, then
     * all operations are permitted.
     *
     * @param author
     * @param obj
     * @param level
     * @throws UserAccessControl.UnauthorisedException
     */
    public static void authOperation(Class author, String token, int level) throws AccessError {
        Connection conn = Access.pool.checkOut();
        if (level <= 0) {
            return;
        }
        try {
           try (PreparedStatement pstmt = conn.prepareStatement(String.format(Persistence.GET_PRIVILEGE_AND_ID_SQL, author.getSimpleName()))){
            pstmt.setObject(1,token);
            try(ResultSet rs = pstmt.executeQuery()){
            if(!rs.next()){
                throw new AccessError(ERROR_TYPE.USER_NOT_AUTHENTICATED);
            }
         
            if (rs.getInt("level") == 999) {
                System.out.println("An admin with id : " + rs.getInt("id") + " performed a level-based operation");
            } else if (level >= rs.getInt("level")) {
                throw new AccessError(ERROR_TYPE.USER_NOT_AUTHORISED);
            }
            }
           }

        } catch (SQLException ex) {
                throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
        } finally {
            Access.pool.checkIn(conn);
        }

    }

    /**
     * Authorises a user to do an operation on a certain entity. If the
     * privilege level of the user is 999, then he can do whatever he want to
     * any entity. If the user attempts to access an entity that is not his,
     * then an error is thrown.
     *
     * @param resource
     * @param token
     * @param object
     * @param author
     * @throws com.tna.common.AccessError
     */
    public static void authAccess(Class object, Class author, String token, long resource) throws AccessError {
        Connection conn = Access.pool.checkOut();
        try {
            String authorName = author.getSimpleName();
            String className = object.getSimpleName();
            long user;
            try (PreparedStatement pstmt = conn.prepareStatement(String.format(GET_PRIVILEGE_AND_ID_SQL, authorName))) {
                pstmt.setObject(1, token);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new AccessError(AccessError.ERROR_TYPE.USER_NOT_AUTHENTICATED);
                    }
                    user = rs.getLong("id");
                }
            }
            try (PreparedStatement pstmt2 = conn.prepareStatement(String.format(SELECT_OBJECT_USER, className))){
                pstmt2.setObject(1, resource);
                
                try (ResultSet rs2 = pstmt2.executeQuery()) {
                    if (!rs2.next()) {
                        throw new AccessError(AccessError.ERROR_TYPE.ENTITY_NOT_FOUND);
                    } else if (rs2.getLong("user") != user) {
                        throw new AccessError(AccessError.ERROR_TYPE.USER_NOT_AUTHORISED);
                    }
                }
            }

        } catch (SQLException ex) {
            System.out.println(ex);
            throw new AccessError(AccessError.ERROR_TYPE.OPERATION_FAILED);

        } finally {
            Access.pool.checkIn(conn);
        }
    }

    public static void checkToken(Class author, String token) throws AccessError {
        Connection conn = Access.pool.checkOut();
        try {
            String authorName = author.getSimpleName();

            try (PreparedStatement pstmt = conn.prepareStatement(String.format(GET_PRIVILEGE_AND_ID_SQL, authorName))) {
                pstmt.setObject(1, token);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new AccessError(AccessError.ERROR_TYPE.USER_NOT_AUTHENTICATED);
                    }
                }

            }
        } catch (SQLException ex) {
            throw new AccessError(AccessError.ERROR_TYPE.OPERATION_FAILED);

        } finally {
            Access.pool.checkIn(conn);
        }
    }
}
