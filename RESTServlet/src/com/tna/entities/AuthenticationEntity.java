/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.entities;

import com.tna.data.Access;
import com.tna.data.Persistence;
import com.tna.common.Authorisation;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.simple.JSONObject;
import java.util.UUID;
/**
 *
 * @author tareq
 */
public abstract class AuthenticationEntity extends BasicEntity {
    
    public String userName;
    public String password;
    public String token;
    public int level;   
    
    /**
     * Performs a login operation with a user's username and password, and assigns them a new token on success.
     * @param obj
     * @return a JSONObject with the token and user id
     * @throws Authorisation.UnauthorisedException
     */
    public JSONObject login(JSONObject obj) throws Authorisation.UnauthorisedException{
        try {
            PreparedStatement pstmt = Access.connection.prepareStatement(String.format(Persistence.GET_PASSWORD_SQL,this.getClass().getSimpleName()));
            System.out.println(this.getClass().getSimpleName());
            pstmt.setObject(1, obj.get("userName"));
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            if(!rs.getString("password").equals(obj.get("password").toString())){
                throw new Authorisation.UnauthorisedException();
            }
            JSONObject json = new JSONObject();
            PreparedStatement pstmt2 = Access.connection.prepareStatement(String.format(Persistence.SET_TOKEN_SQL,this.getClass().getSimpleName()));
            this.token = UUID.randomUUID().toString();
            this.id = rs.getInt("id");
            pstmt2.setString(1, this.token);
            pstmt2.setLong(2, this.id);
            
            pstmt2.execute();
            json.put("token",this.token);
            json.put("id",this.id);
            return json;
            
        } catch (SQLException ex) {
            System.out.println(ex);
            throw new Authorisation.UnauthorisedException();
    }
    }
        
    /**
     * Authorises an operation of a certain privilege level.If the privilege level of 
     * the operation is higher than that of the user, it throws an exception. Otherwise,
     * it returns nothing. If the user level is 999, then all operations are permitted.
     * @param obj
     * @param level
     * @throws Authorisation.UnauthorisedException
     */
    public void authOperation(JSONObject obj, int level) throws Authorisation.UnauthorisedException {
        if(level<=0){
            return;      
        }
        try {
            PreparedStatement pstmt;
            pstmt = Access.connection.prepareStatement(String.format(Persistence.GET_PRIVILEGE_AND_ID_SQL,this.getClass().getSimpleName()));
            pstmt.setObject(1, obj.get("token"));
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            JSONObject json = new JSONObject();
            if(rs.getInt("level")==999){
                return;
            }else if(level>rs.getInt("level")){
                throw new Authorisation.UnauthorisedException();
            }
            
        } catch (SQLException ex) {
            throw new Authorisation.UnauthorisedException();
    }
        
    }
    
    /**
     * Authorises a user to do an operation on a certain entity. If the privilege
     * level of the user is 999, then he can do whatever he want to any entity. If 
     * the user attempts to access an entity that is not his, then an error is thrown.
     * @param obj
     * @param resource
     * @param object
     * @throws Authorisation.UnauthorisedException
     */
    public void authAccess(JSONObject obj , int resource, Class object) throws Authorisation.UnauthorisedException {
        try {
            PreparedStatement pstmt;
            pstmt = Access.connection.prepareStatement(String.format(Persistence.GET_PRIVILEGE_AND_ID_SQL,this.getClass().getSimpleName()));
            pstmt.setObject(1, obj.get("token"));
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            if(level>rs.getInt("level")){
                throw new Authorisation.UnauthorisedException();
            }
            if(rs.getInt("level")==999){
                return;
            }
            PreparedStatement pstmt2;
            pstmt2 = Access.connection.prepareStatement(String.format(Persistence.READ_OBJECT_USER_SQL,object.getSimpleName()));
            pstmt2.setObject(1, resource);
            ResultSet rs2 = pstmt2.executeQuery();
            rs2.next();
            if(rs.getLong("id")!=rs2.getLong("user")){
                 throw new Authorisation.UnauthorisedException();
            }
            
        } catch (SQLException ex) {
            throw new Authorisation.UnauthorisedException();
    }


    }
}
