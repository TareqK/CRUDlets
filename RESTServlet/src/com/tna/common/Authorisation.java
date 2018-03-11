/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.common;

/**
 *
 * @author tareq
 */
public class Authorisation {
    
    public static class UnauthorisedException extends Exception {

        public UnauthorisedException() {
            System.out.println("Anuthroised Request");
        }
    }
}
