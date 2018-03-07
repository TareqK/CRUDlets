/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.Endpoints;

import com.tna.Utils.Authorisation;
import java.lang.reflect.Field;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
public abstract class AuthorisationEndpoint extends Endpoint {

    @Override
    public JSONObject doList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject doCreate(JSONObject json) {
        try {
            
            
            return login(json);
        } catch (Authorisation.UnauthorisedException ex) {
            return null;
        }
    }
    
    @Override
    public JSONObject doUpdate(JSONObject obj, int resource) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject doRead(int resource) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject doDelete(int resource) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public abstract JSONObject login(JSONObject obj) throws Authorisation.UnauthorisedException ;


  

}