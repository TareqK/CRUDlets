/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.endpoints;

import com.tna.common.Authorisation;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
public abstract class AuthenticationEndpoint extends BasicEndpoint {

    @Override
    public JSONObject doList() {
      return null;
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
      return null;
    }

    @Override
    public JSONObject doRead(int resource) {
      return null;
    }

    @Override
    public JSONObject doDelete(int resource) {
      return null;
    }

    /**
     *
     * @param obj
     * @return Returns the token and key on login success, or throws an error on failure.
     * @throws Authorisation.UnauthorisedException
     */
    public abstract JSONObject login(JSONObject obj) throws Authorisation.UnauthorisedException ;


  

}