/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.Endpoints;

import com.tna.Entities.AuthorisationEntity;
import com.tna.Utils.Authorisation;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
public abstract class AuthorisationEndpoint extends Endpoint {

    @Override
    public JSONObject doCreate(JSONObject json) {
        try {
            return login(json);
        } catch (Authorisation.UnauthorisedException ex) {
            return null;
        }
    }

    public abstract JSONObject login(JSONObject obj) throws Authorisation.UnauthorisedException ;

  

}