/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.DataAccess;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
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
public abstract class ObjectPersistence implements Serializable {

    static final String CREATE_OBJECT_SQL = "INSERT INTO %s (object_value) VALUES (?)";
    static final String READ_OBJECT_SQL = "SELECT object_value FROM %s WHERE id = ?";
    static final String UPDATE_OBJECT_SQL = "UPDATE %s SET object_value = ? where id = ?";
    static final String DELETE_OBJECT_SQL = "DELETE FROM %s WHERE id = ?";
    static final String LIST_OBJECT_SQL = "SELECT object_value FROM %s";

    public JSONObject create(Object object) throws Exception {
        String className = object.getClass().getSimpleName();
        PreparedStatement pstmt = Access.connection.prepareStatement((String.format(CREATE_OBJECT_SQL, className)), Statement.RETURN_GENERATED_KEYS);
        pstmt.setObject(1, object);
        try {
            pstmt.execute();
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    public JSONObject read(Object object, int id) throws Exception {
        String className = object.getClass().getSimpleName();
        PreparedStatement pstmt = Access.connection.prepareStatement((String.format(READ_OBJECT_SQL, className)), Statement.RETURN_GENERATED_KEYS);

        pstmt.setLong(1, id);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        byte[] buf = rs.getBytes(1);
        ObjectInputStream objectIn = null;
        if (buf != null) {
            objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
        }
        Object object2 = objectIn.readObject();
        rs.close();
        pstmt.close();
        Field[] fields = object2.getClass().getDeclaredFields();
         for (Field field : fields) {
                System.out.print("|"+field.get(object2)+"|");
            }
            System.out.println("");
        return null;
    }

    public JSONObject update(Object object, int id) throws Exception {
        String className = object.getClass().getSimpleName();
        PreparedStatement pstmt = Access.connection.prepareStatement((String.format(UPDATE_OBJECT_SQL, className)), Statement.RETURN_GENERATED_KEYS);
        pstmt.setObject(1, object);
        pstmt.setInt(2, id);
        try {
            pstmt.execute();
        } catch (SQLException e) {
            return null;
        }
        
        return null;
    }

    public JSONObject delete(Object object, int id) throws Exception {
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
        return null;

    }

    public JSONObject list(Object object) throws Exception {
        String className = object.getClass().getSimpleName();
        PreparedStatement pstmt = Access.connection.prepareStatement((String.format(LIST_OBJECT_SQL, className)), Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = pstmt.executeQuery();
        System.out.println(rs);
        try{while (rs.next()) {
            byte[] buf = rs.getBytes(1);
            ObjectInputStream objectIn = null;
            if (buf != null) {
                objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
            }
            Object object2 = objectIn.readObject();
    
           
            Field[] fields = object2.getClass().getDeclaredFields();
            for (Field field : fields) {
                System.out.print("|"+field.get(object2)+"|");
            }
            System.out.println("");
        }
        }catch(SQLException e){
        rs.close();
        pstmt.close();
        }
        return null;

    }

}
