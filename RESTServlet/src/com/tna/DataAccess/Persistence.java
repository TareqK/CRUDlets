/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.DataAccess;

import com.tna.Utils.JSON;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 *
 */
public abstract class Persistence {

    static final String CREATE_OBJECT_SQL = "INSERT INTO %s (%s) VALUES (%s) ";
    static final String READ_OBJECT_SQL = "SELECT * FROM %s WHERE id = ?";
    static final String UPDATE_OBJECT_SQL = "UPDATE %s SET %s where id = ?";
    static final String DELETE_OBJECT_SQL = "DELETE FROM %s WHERE id = ?";
    static final String LIST_OBJECT_SQL = "SELECT * FROM %s";

    public static JSONObject create(Object object) throws SQLException {
        String className = object.getClass().getSimpleName();
        Field[] fields = object.getClass().getDeclaredFields();
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        for (Field field : fields) {
            columns.append(field.getName());
            values.append("?,");
            columns.append(",");
        }
        columns.deleteCharAt((columns.length() - 1));
        values.deleteCharAt((values.length() - 1));
        PreparedStatement pstmt = Access.connection.prepareStatement((String.format(CREATE_OBJECT_SQL, className, columns.toString(), values.toString())), Statement.RETURN_GENERATED_KEYS);
        int i = 1;
        for (Field field : fields) {
            try {
                pstmt.setObject(i, field.get(object));
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                return null;
            }
            i++;
        }
        try {
            pstmt.execute();
        } catch (SQLException e) {
            return null;
        }
        return JSON.successResponse();
    }

    public static JSONObject read(Object object, int id) throws SQLException {
        String className = object.getClass().getSimpleName();
        PreparedStatement pstmt;
        pstmt = Access.connection.prepareStatement((String.format(READ_OBJECT_SQL, className)), Statement.RETURN_GENERATED_KEYS);
        Field[] fields = object.getClass().getDeclaredFields();
        pstmt.setLong(1, id);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        for (Field field : fields) {
            try {
                field.set(object, rs.getObject(field.getName()));
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                return null;
            }
        }
        return JSON.objectToJSON(object);
    }

    public static JSONObject update(Object object, int id) throws SQLException {
        String className = object.getClass().getSimpleName();
        Field[] fields = object.getClass().getDeclaredFields();
        StringBuilder values = new StringBuilder();
        for (Field field : fields) {
            values.append(field.getName()).append(" = ?,");
        }
        values.deleteCharAt((values.length() - 1));
        PreparedStatement pstmt = Access.connection.prepareStatement((String.format(UPDATE_OBJECT_SQL, className, values.toString())), Statement.RETURN_GENERATED_KEYS);
        int i = 1;
        for (Field field : fields) {
            try {
                pstmt.setObject(i, field.get(object));
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                return null;
            }
            i++;

        }
        pstmt.setObject(i, id);
        try {
            pstmt.execute();
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }

        return JSON.successResponse();
    }

    public static JSONObject delete(Object object, int id) throws SQLException {
        String className = object.getClass().getSimpleName();
        PreparedStatement pstmt = Access.connection.prepareStatement((String.format(DELETE_OBJECT_SQL, className)), Statement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, id);
        try {
            pstmt.execute();
        } catch (SQLException e) {
            return null;
        }
        ResultSet rs = pstmt.getGeneratedKeys();
        rs.next();
        return JSON.successResponse();

    }

    public static JSONObject list(Object object) throws SQLException {
        String className = object.getClass().getSimpleName();
        PreparedStatement pstmt = Access.connection.prepareStatement((String.format(LIST_OBJECT_SQL, className)), Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = pstmt.executeQuery();
        Field[] fields = object.getClass().getDeclaredFields();

        JSONObject obj = new JSONObject();
        try {
            int i = 1;
            while (rs.next()) {

                for (Field field : fields) {
                    try {
                        field.set(object, rs.getObject(field.getName()));
                    } catch (IllegalArgumentException | IllegalAccessException ex) {
                        return null;
                    }

                }
                obj.put(+i, JSON.objectToJSON(object));
                i++;
            }
        } catch (SQLException e) {
            rs.close();
            pstmt.close();
            return null;
        }
        for (Field field : fields) {
            try {
                field.set(object, null);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
            }

        }
        return obj;

    }

}
