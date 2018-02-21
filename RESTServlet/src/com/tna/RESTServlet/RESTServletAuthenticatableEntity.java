/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.RESTServlet;

import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
public abstract class RESTServletAuthenticatableEntity extends RESTServletEntity{
    
    /**
     * Authenticates the user based on a request
     * @param request
     * @return returns true/false depending on if the key is valid.
     */
    public abstract boolean authenticate(JSONObject request);
    
    /**
     * Authorises the current user for an operation based on his user level
     * @param request
     * @param level
     * @return true/false depending if the user has the priveleges
     */
    public abstract boolean authorise(JSONObject request, int level);
}
