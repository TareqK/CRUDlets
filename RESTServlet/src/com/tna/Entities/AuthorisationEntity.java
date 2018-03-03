/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.Entities;

import com.tna.DataAccess.Access;
import com.tna.DataAccess.Persistence;
import com.tna.DataAccess.AuthorisationPersistence;
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
public abstract class AuthorisationEntity extends Entity {
    
    public int id;
    public String userName;
    public String password;
    public String token;
    public int level;   
    
    public JSONObject login(JSONObject obj) throws Authorisation.UnauthorisedException{
        try {
            PreparedStatement pstmt = Access.connection.prepareStatement(String.format(AuthorisationPersistence.GET_PASSWORD_SQL,this.getClass().getSimpleName()));
            System.out.println(this.getClass().getSimpleName());
            pstmt.setObject(1, obj.get("userName"));
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            if(!rs.getString("password").equals(obj.get("password").toString())){
                throw new Authorisation.UnauthorisedException();
            }
            JSONObject json = new JSONObject();
            PreparedStatement pstmt2 = Access.connection.prepareStatement(String.format(AuthorisationPersistence.SET_TOKEN_SQL,this.getClass().getSimpleName()));
            this.token = UUID.randomUUID().toString();
            this.id = rs.getInt("id");
            pstmt2.setString(1, this.token);
            pstmt2.setInt(2, this.id);
            
            pstmt2.execute();
            json.put("token",this.token);
            json.put("id",this.id);
            return json;
            
        } catch (SQLException ex) {
            System.out.println(ex);
            throw new Authorisation.UnauthorisedException();
    }
    }
        
    public void auth(JSONObject obj, int level) throws Authorisation.UnauthorisedException {
        if(level<=0){
            return;      
        }
        try {
            PreparedStatement pstmt;
            pstmt = Access.connection.prepareStatement(String.format(AuthorisationPersistence.GET_PRIVILEGE_AND_ID_SQL,this.getClass().getSimpleName()));
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
    
    public void auth(JSONObject obj, int level, int resource, Object object) throws Authorisation.UnauthorisedException {
        if(level<=0){
            return;      
        }
        try {
            PreparedStatement pstmt;
            pstmt = Access.connection.prepareStatement(String.format(AuthorisationPersistence.GET_PRIVILEGE_AND_ID_SQL,this.getClass().getSimpleName()));
            pstmt.setObject(1, obj.get("token"));
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            if(level>rs.getInt("level")){
                throw new Authorisation.UnauthorisedException();
            }
            PreparedStatement pstmt2;
            pstmt2 = Access.connection.prepareStatement(String.format(AuthorisationPersistence.READ_OBJECT_USER_SQL,object.getClass().getSimpleName()));
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
