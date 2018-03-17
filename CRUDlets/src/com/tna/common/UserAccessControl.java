/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.common;

import com.tna.data.Access;
import com.tna.data.Persistence;
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
     * Performs a login operation with a user's username and password, and assigns them a new token on success.
     * @param author
     * @param obj
     * @return a JSONObject with the token and user id
     * @throws UserAccessControl.UnauthorisedException
     */
    public static JSONObject login(Class author, JSONObject obj) throws UserAccessControl.UnauthorisedException{
        try {
            PreparedStatement pstmt;
            pstmt = Access.connection.prepareStatement(String.format(Persistence.GET_PASSWORD_SQL,author.getSimpleName()));
            pstmt.setObject(1, obj.get("userName"));
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            if(!rs.getString("password").equals(obj.get("password").toString())){
                throw new UserAccessControl.UnauthorisedException();
            }
            JSONObject json = new JSONObject();
            PreparedStatement pstmt2 = Access.connection.prepareStatement(String.format(Persistence.SET_TOKEN_SQL,author.getSimpleName()));
            String token = UUID.randomUUID().toString();
            long id = rs.getInt("id");
            pstmt2.setString(1, token);
            pstmt2.setLong(2, id);
            
            pstmt2.execute();
            json.put("token",token);
            json.put("id",id);
            return json;
            
        } catch (SQLException ex) {
            System.out.println(ex);
            throw new UserAccessControl.UnauthorisedException();
    }
    }
        
    /**
     * Authorises an operation of a certain privilege level.If the privilege level of 
     * the operation is higher than that of the user, it throws an exception. Otherwise,
     * it returns nothing. If the user level is 999, then all operations are permitted.
     * @param author
     * @param obj
     * @param level
     * @throws UserAccessControl.UnauthorisedException
     */
    public static void authOperation(Class author, JSONObject obj, int level) throws UserAccessControl.UnauthorisedException {
        if(level<=0){
            return;      
        }
        try {
            PreparedStatement pstmt;
            pstmt = Access.connection.prepareStatement(String.format(Persistence.GET_PRIVILEGE_AND_ID_SQL,author.getSimpleName()));
            pstmt.setObject(1, obj.get("token"));
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            JSONObject json = new JSONObject();
            if(rs.getInt("level")==999){
                return;
            }else if(level>rs.getInt("level")){
                throw new UserAccessControl.UnauthorisedException();
            }
            
        } catch (SQLException ex) {
            throw new UserAccessControl.UnauthorisedException();
    }
        
    }
    
    /**
     * Authorises a user to do an operation on a certain entity. If the privilege
     * level of the user is 999, then he can do whatever he want to any entity. If 
     * the user attempts to access an entity that is not his, then an error is thrown.
     * @param obj
     * @param resource
     * @param object
     * @param author
     * @throws UserAccessControl.UnauthorisedException
     */
    public static void authAccess(Class author, JSONObject obj , int resource, Class object) throws UserAccessControl.UnauthorisedException {
        try {
            PreparedStatement pstmt;
            pstmt = Access.connection.prepareStatement(String.format(Persistence.GET_PRIVILEGE_AND_ID_SQL,author.getSimpleName()));
            pstmt.setObject(1, obj.get("token"));
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            if(rs.getInt("level")==999){
                return;
            }
            PreparedStatement pstmt2;
            pstmt2 = Access.connection.prepareStatement(String.format(Persistence.READ_OBJECT_USER_SQL,object.getSimpleName()));
            pstmt2.setObject(1, resource);
            ResultSet rs2 = pstmt2.executeQuery();
            rs2.next();
            if(rs.getLong("id")!=rs2.getLong("user")){
                 throw new UserAccessControl.UnauthorisedException();
            }
            
        } catch (SQLException ex) {
            throw new UserAccessControl.UnauthorisedException();
    }


    }
}
