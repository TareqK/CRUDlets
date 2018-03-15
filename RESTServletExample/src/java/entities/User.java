package entities;


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

    public static JSONObject login(JSONObject obj) throws UserAccessControl.UnauthorisedException{
        return UserAccessControl.login(User.class, obj);
    }
    
    @Override
    public JSONObject list() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject create(JSONObject obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject update(JSONObject obj, int resource) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject read(int resource) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject delete(int resource) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   
    

}
