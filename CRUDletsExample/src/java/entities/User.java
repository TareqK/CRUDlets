package entities;


import com.tna.common.AccessError;
import com.tna.common.UserAccessControl;
import com.tna.entities.AuthenticationEntity;
import org.json.simple.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author tareq
 */
public class User extends AuthenticationEntity {

    public static JSONObject login(JSONObject obj)  throws AccessError{
        return UserAccessControl.login(User.class, obj);
    }
    
 
    

}
