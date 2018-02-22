/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.RESTServlet;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author tareq
 * @param <T>
 */
public class EntityPersistence<T> {

    static final String WRITE_OBJECT_SQL = "INSERT INTO java_objects(name, object_value) VALUES (?, ?)";

    static final String READ_OBJECT_SQL = "SELECT object_value FROM java_objects WHERE id = ?";

    static final String DELETE_OBJECT_SQL = "DELETE FROM java_objects WHERE id = ?";

    public static long writeJavaObject(Object object) throws Exception {
        String className = object.getClass().getName();
        int id;
        // set input parameters
        try (PreparedStatement pstmt = DBAccess.connection.prepareStatement(WRITE_OBJECT_SQL)) {
            // set input parameters
            pstmt.setString(1, className);
            pstmt.setObject(2, object);
            pstmt.executeUpdate();
            try ( // get the generated key for the id
                    ResultSet rs = pstmt.getGeneratedKeys()) {
                id = -1;
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            }
        }
        return id;
    }

    public static Object readJavaObject(long id) throws Exception {
        Object object;
        String className;
        try (PreparedStatement pstmt = DBAccess.connection.prepareStatement(READ_OBJECT_SQL)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                object = rs.getObject(1);
                className = object.getClass().getName();
            }
        }
        return object;
    }

    public static void deleteJavaObject(long id) throws Exception {
        Object object;
        String className;
        try (PreparedStatement pstmt = DBAccess.connection.prepareStatement(DELETE_OBJECT_SQL)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {

            }
        }
    }
}
