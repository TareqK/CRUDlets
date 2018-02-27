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
public class Authorisation {
    
    public static String authorisedClass = null;
    public static String tokenField = null;
    public static String privelegeField = null;
    public static final String GET_TOKEN_SQL = "SELECT %s FROM %s WHERE %s = ? ";
    public static boolean authorise(String token, int level) throws UnauthorisedException{
        return false;
    }

    public static class UnauthorisedException extends Exception {

        public UnauthorisedException() {
            System.out.println("oh noes");
        }
    }
}
