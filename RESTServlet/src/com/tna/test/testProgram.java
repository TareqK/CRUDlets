/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.test;

import static java.lang.Math.random;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */

public class testProgram{
    
public static void main(String[] args) throws Exception{
   testPersist tp = new testPersist();
   tp.read(105);
   tp.one=1;
   tp.two=2;
   tp.three=3;
   tp.update(105);
   tp.read(105);
    
}


}
