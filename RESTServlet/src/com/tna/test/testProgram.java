/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.test;

import com.tna.RESTServlet.Authorisation;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */

public class testProgram{
    
public static void main(String[] args) throws Exception{
    Authorisation.authoriedClass = com.tna.testPersist;
    System.out.println(Authorisation.authoriedClass);
   Authorisation.authorise("11", 0);
}


}