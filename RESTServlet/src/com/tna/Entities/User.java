/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.Entities;

import com.tna.DataAccess.Access;
import com.tna.DataAccess.Persistence;
import com.tna.Utils.Authorisation;
import com.tna.Utils.JSON;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.simple.JSONObject;
import java.util.UUID;
/**
 *
 * @author tareq
 */
public class User extends Entity {
    
    public int id;
    public String userName;
    public String password;
    public String token;
    public int level;   
    public static final String GET_PRIVILEGE_SQL = "SELECT privelege FROM User WHERE token = ? ";
    public static final String GET_PASSWORD_SQL = "SELECT password FROM User WHERE userName = ? ";
   
    @Override
    public JSONObject list() throws SQLException {
      return Persistence.list(this);
    }

    @Override
    public JSONObject create(JSONObject obj) throws SQLException {
        JSON.JSONtoObject(this, obj);
        token = UUID.randomUUID().toString();
        return Persistence.create(this);
    }

    @Override
    public JSONObject update(JSONObject obj, int resource) throws SQLException {
        JSON.JSONtoObject(this, obj);
        token = UUID.randomUUID().toString();
        return Persistence.update(this,resource);
    }

    @Override
    public JSONObject read(int resource) throws SQLException {
         return Persistence.read(this,resource);
    }

    @Override
    public JSONObject delete(int resource) throws SQLException {
        return Persistence.read(this,resource);
    }
   
    
    public static JSONObject login(JSONObject obj) throws Authorisation.UnauthorisedException{
        try {
            PreparedStatement pstmt = Access.connection.prepareStatement(User.GET_PASSWORD_SQL);
            pstmt.setObject(1, obj.get("userName"));
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            JSONObject json = new JSONObject();
            json.put("token", rs.getString("token"));
            return json;
            
        } catch (SQLException ex) {
            throw new Authorisation.UnauthorisedException();
    }
    }
        
    public static void auth(JSONObject obj, int level) throws Authorisation.UnauthorisedException {
        try {
            PreparedStatement pstmt = Access.connection.prepareStatement(User.GET_PRIVILEGE_SQL);
            pstmt.setObject(1, obj.get("token"));
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            JSONObject json = new JSONObject();
            if(level<rs.getInt("privelege")){
            }else{
                throw new Authorisation.UnauthorisedException();
            }
            
        } catch (SQLException ex) {
            throw new Authorisation.UnauthorisedException();
    }
        
    }

  
}
