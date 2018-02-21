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
public abstract class RESTServletAuthorisableEntity extends RESTServletEntity{
    
    /**
     * Authorises the current user for an operation based on his user level
     * @param request
     * @param level
     * @return true/false depending if the user has the privileges
     * @throws com.tna.RESTServlet.RESTServletAuthorisableEntity.UnauthorisedError
     */
    public abstract boolean authorise(JSONObject request, int level) throws UnauthorisedError;

    public static class UnauthorisedError extends Exception {

        public UnauthorisedError() {
            System.out.println("oh noes");
        }
    }
}
