/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.DataAccess;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
public class AuthorisationPersistence {

    public static final String GET_PRIVILEGE_AND_ID_SQL = "SELECT id,level FROM %s WHERE token = ? ";
    public static final String GET_PASSWORD_SQL = "SELECT password,id FROM %s WHERE userName = ? ";
    public static final String SET_TOKEN_SQL = "UPDATE %s set token = ? where id = ?";
    public static final String READ_OBJECT_USER_SQL = "SELECT user FROM %s WHERE id = ?";
    public static final String LIST_USER_OBJECTS_SQL = "SELECT * FROM %s WHERE user = (SELECT id FROM %s WHERE token = ? )";
    public static final String READ_USER_OBJECT_SQL = "SELECT * FROM %s WHERE id = ? AND user = (SELECT id FROM %s WHERE token = ? ) ";
    
    public static JSONObject list(Class object, Class author, JSONObject json) {

        JSONObject response = new JSONObject();
        try {
            String objectName = object.getSimpleName();
            String authorName = author.getSimpleName();
            PreparedStatement pstmt = Access.connection.prepareStatement((String.format(LIST_USER_OBJECTS_SQL, objectName, authorName)), Statement.RETURN_GENERATED_KEYS);
            pstmt.setObject(1, json.get("token"));
            ResultSet rs = pstmt.executeQuery();
            Field[] fields = object.getDeclaredFields();

            while (rs.next()) {
                JSONObject temp = new JSONObject();
                for (Field field : fields) {
                    try {
                        temp.put(field.getName(), rs.getObject(field.getName()));
                    } catch (IllegalArgumentException ex) {
                    }

                }
                response.put(rs.getObject("id"), temp);
            }
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return response;
    }

    public static JSONObject read(Class object, Class author, JSONObject json, long resource) {

        JSONObject response = new JSONObject();
        try {
            String authorName = object.getSimpleName();
            String className2 = author.getSimpleName();
            PreparedStatement pstmt = Access.connection.prepareStatement((String.format(READ_USER_OBJECT_SQL, authorName, className2)), Statement.RETURN_GENERATED_KEYS);
            pstmt.setObject(1, resource);
            pstmt.setObject(2, json.get("token"));
            ResultSet rs = pstmt.executeQuery();
            Field[] fields = object.getDeclaredFields();
            rs.next();
            for (Field field : fields) {
                try {
                    response.put(field.getName(), rs.getObject(field.getName()));
                } catch (IllegalArgumentException ex) {
                }

            }
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return response;
    }
    /*
    public static JSONObject update(Class object, Class author, JSONObject json, long resource) {

        JSONObject response = new JSONObject();
        try {
            String authorName = object.getSimpleName();
            String className2 = author.getSimpleName();
            PreparedStatement pstmt = Access.connection.prepareStatement((String.format(READ_USER_OBJECT_SQL, authorName, className2)), Statement.RETURN_GENERATED_KEYS);
            pstmt.setObject(1, resource);
            pstmt.setObject(2, json.get("token"));
            ResultSet rs = pstmt.executeQuery();
            Field[] fields = object.getDeclaredFields();
            rs.next();
            for (Field field : fields) {
                try {
                    response.put(field.getName(), rs.getObject(field.getName()));
                } catch (IllegalArgumentException ex) {
                }

            }
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return response;
    }
    
    public static JSONObject delete(Class object, Class author, JSONObject json, long resource) {

        JSONObject response = new JSONObject();
        try {
            String authorName = object.getSimpleName();
            String className2 = author.getSimpleName();
            PreparedStatement pstmt = Access.connection.prepareStatement((String.format(READ_USER_OBJECT_SQL, authorName, className2)), Statement.RETURN_GENERATED_KEYS);
            pstmt.setObject(1, resource);
            pstmt.setObject(2, json.get("token"));
            ResultSet rs = pstmt.executeQuery();
            Field[] fields = object.getDeclaredFields();
            rs.next();
            for (Field field : fields) {
                try {
                    response.put(field.getName(), rs.getObject(field.getName()));
                } catch (IllegalArgumentException ex) {
                }

            }
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return response;
    }
    
    public static JSONObject create(Class object, Class author, JSONObject json, long resource) {

        JSONObject response = new JSONObject();
        try {
            String authorName = object.getSimpleName();
            String className2 = author.getSimpleName();
            PreparedStatement pstmt = Access.connection.prepareStatement((String.format(READ_USER_OBJECT_SQL, authorName, className2)), Statement.RETURN_GENERATED_KEYS);
            pstmt.setObject(1, resource);
            pstmt.setObject(2, json.get("token"));
            ResultSet rs = pstmt.executeQuery();
            Field[] fields = object.getDeclaredFields();
            rs.next();
            for (Field field : fields) {
                try {
                    response.put(field.getName(), rs.getObject(field.getName()));
                } catch (IllegalArgumentException ex) {
                }

            }
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return response;
    }
*/
}
