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
            pstmt.setObject(1, obj.get("userName"));
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            JSONObject json = new JSONObject();
            json.put("token", rs.getString("token"));
            PreparedStatement pstmt2 = Access.connection.prepareStatement(String.format(AuthorisationPersistence.SET_TOKEN_SQL,this.getClass().getSimpleName()));
            this.token = rs.getString("token");
            pstmt.setInt(1, this.id);
            pstmt.setString(2, this.token);
            pstmt2.execute();
            return json;
            
        } catch (SQLException ex) {
            throw new Authorisation.UnauthorisedException();
    }
    }
        
    public void auth(JSONObject obj, int level) throws Authorisation.UnauthorisedException {
        try {
            PreparedStatement pstmt;
            pstmt = Access.connection.prepareStatement(String.format(AuthorisationPersistence.GET_PRIVILEGE_SQL,this.getClass().getSimpleName()));
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
