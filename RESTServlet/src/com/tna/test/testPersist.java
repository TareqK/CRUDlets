/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.test;

import com.tna.RESTServlet.PersistedEntity;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */

    
public class testPersist extends PersistedEntity{
    public int one;
    public int two;
    public int three;
    
    public testPersist(){
        this.one= (int )(Math.random() * 50 + 1);
        this.two= (int )(Math.random() * 50 + 1);
        this.three = (int )(Math.random() * 50 + 1);
    }
    
    public JSONObject create() throws Exception{
        return this.create(this);
    }
    
    public JSONObject read(int number) throws Exception{
         return read(this,number);
    }
    public static void test() throws Exception{

    new testPersist().create();
    
}    

    public JSONObject list() throws Exception {
       return this.list(this);
    }

    JSONObject delete(int i) throws Exception {
        return this.delete(this,i);
    }

    JSONObject update(int i) throws Exception {
        return this.update(this,i);
    }
}
