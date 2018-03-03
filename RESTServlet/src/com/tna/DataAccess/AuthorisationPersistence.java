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
 */
public class AuthorisationPersistence extends Persistence {
    public static final String GET_PRIVILEGE_AND_ID_SQL = "SELECT id,level FROM %s WHERE token = ? ";
    public static final String GET_PASSWORD_SQL = "SELECT password,id FROM %s WHERE userName = ? ";
    public static final String SET_TOKEN_SQL = "UPDATE %s set token = ? where id = ?";
    public static final String READ_OBJECT_USER_SQL = "SELECT user FROM %s WHERE id = ?";
    
    


}
