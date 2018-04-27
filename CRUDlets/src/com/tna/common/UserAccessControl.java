/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.common;

import com.tna.common.AccessError.ERROR_TYPE;
import com.tna.data.Access;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
public class UserAccessControl {

    private static final String READ_OBJECT_USER_SQL = "SELECT user FROM %s WHERE id = ?";
    private static final String GET_PRIVILEGE_AND_ID_SQL = "SELECT id,level FROM %s WHERE token = ? ";
    private static final String GET_PASSWORD_SQL = "SELECT password,id FROM %s WHERE userName = ? ";
    private static final String SET_TOKEN_SQL = "UPDATE %s set token = ? where id = ?";
    private static final String CREATE_NEW_USER_SQL = "INSERT INTO %s (userName,password,level) VALUES (?,?,?)";
    private static final String SELECT_OBJECT_USER = "SELECT user from %s where id = ?";

    /**
     * Logs in a user and gives him a session token.
     * @param author the user class to authenticate against.
     * @param credentials the credentials to check.
     * @return returns the token and user id of the user.
     * @throws AccessError on failure.
     */
    public static JSONObject login(Class author, JSONObject credentials) throws AccessError {
        Connection conn = Access.pool.checkOut();
        JSONObject result = new JSONObject();
        try {

            try (PreparedStatement pstmt = conn.prepareStatement(String.format(GET_PASSWORD_SQL, author.getSimpleName()))) {
                pstmt.setObject(1, credentials.get("userName"));
                try (ResultSet rs = pstmt.executeQuery()) {
                    rs.next();
                    if (!rs.getString("password").equals(credentials.get("password").toString())) {
                        throw new AccessError(ERROR_TYPE.USER_NOT_AUTHORISED);
                    }
                    try (PreparedStatement pstmt2 = conn.prepareStatement(String.format(SET_TOKEN_SQL, author.getSimpleName()))) {
                        String token = UUID.randomUUID().toString();
                        long id = rs.getInt("id");
                        pstmt2.setString(1, token);
                        pstmt2.setLong(2, id);
                        pstmt2.execute();
                        result.put("token", token);
                        result.put("id", id);
                    }
                }
            }
        } catch (SQLException ex) {
            throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
        } finally {
            Access.pool.checkIn(conn);
        }
        return result;
    }

    /**
     * Authorises an operation on the system.
     * @param author the class to authorise against.
     * @param token the session token of the user.
     * @param level the user level required for this operation.
     * @throws AccessError on failure.
     */
    public static void authOperation(Class author, String token, int level) throws AccessError {
        Connection conn = Access.pool.checkOut();
        if (level <= 0) {
            return;
        }
        try {
            try (PreparedStatement pstmt = conn.prepareStatement(String.format(GET_PRIVILEGE_AND_ID_SQL, author.getSimpleName()))) {
                pstmt.setObject(1, token);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (!rs.next()) {
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
     * Authorises access to an object in the database.
     * @param object The class of the object to authorised access to.
     * @param author The class of the user object to authorise against.
     * @param token The session token of the user.
     * @param resource The id of the object we want to access.
     * @throws AccessError on failure.
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
            try (PreparedStatement pstmt2 = conn.prepareStatement(String.format(SELECT_OBJECT_USER, className))) {
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

    /**
     * Fetches some user details through his session token.
     * @param author The class of the user object to check against.
     * @param token The session token.
     * @return The user id and privilege level of the user.
     * @throws AccessError on failure.
     */
    public static JSONObject fetchUserByToken(Class author, String token) throws AccessError {
        Connection conn = Access.pool.checkOut();
        JSONObject user = new JSONObject();
        try {
            String authorName = author.getSimpleName();

            try (PreparedStatement pstmt = conn.prepareStatement(String.format(GET_PRIVILEGE_AND_ID_SQL, authorName))) {
                pstmt.setObject(1, token);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new AccessError(AccessError.ERROR_TYPE.USER_NOT_AUTHENTICATED);
                    }
                    user.put("level", rs.getLong("level"));
                    user.put("id", rs.getLong("id"));

                }

            }
        } catch (SQLException ex) {
            throw new AccessError(AccessError.ERROR_TYPE.OPERATION_FAILED);

        } finally {
            Access.pool.checkIn(conn);
        }
        return user;
    }

    /**
     * Checks the validity of a session token.
     * @param author The class of the user object to check against.
     * @param token The session token of the user.
     * @throws AccessError if the token is invalid
     */
    public static void checkToken(Class author, String token) throws AccessError {
        fetchUserByToken(author, token);
    }

    /**
     * Creates a new user.
     * @param author The user class we are going to create a new user object of.
     * @param json The credentials and details of the user.
     * @param level The privilege level of the user.
     * @return The id of the newly created user.
     * @throws AccessError if creation fails.
     */
    public static long createNewUser(Class author, JSONObject json, int level) throws AccessError {

        Connection conn = Access.pool.checkOut();
        long userId = -1;
        try {
            String authorName = author.getSimpleName();
            try (PreparedStatement pstmt = conn.prepareStatement(String.format(CREATE_NEW_USER_SQL, authorName), Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setObject(1, json.get("userName"));
                pstmt.setObject(2, json.get("password"));
                pstmt.setObject(3, level);
                pstmt.execute();
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new AccessError(AccessError.ERROR_TYPE.OPERATION_FAILED);
                    }
                    userId = rs.getLong(1);
                }
            }
        } catch (SQLException ex) {
            throw new AccessError(AccessError.ERROR_TYPE.OPERATION_FAILED);
        } finally {
            Access.pool.checkIn(conn);
        }
        return userId;
    }
}
