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
public abstract class AuthorisableEntity extends Entity {

    /**
     * Authorises the current user for an operation based on his user level
     *
     * @param request
     * @param level
     * @return true/false depending if the user has the privileges
     * @throws com.tna.RESTServlet.AuthorisableEntity.UnauthorisedException
     */
    public abstract boolean authorise(JSONObject request, int level) throws UnauthorisedException;

    public static class UnauthorisedException extends Exception {

        public UnauthorisedException() {
            System.out.println("oh noes");
        }
    }
}
