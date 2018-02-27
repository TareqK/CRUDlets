/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.Utils;

import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
public interface Authorisation {
    
    public static class UnauthorisedException extends Exception {

        public UnauthorisedException() {
            System.out.println("oh noes");
        }
    }
}
