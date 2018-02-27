/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.test;

import com.tna.RESTServlet.EntityPersistence;
import org.json.simple.JSONObject;
import com.tna.RESTServlet.Entity;
import com.tna.RESTServlet.JSON;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author tareq
 */

    
public class testPersist extends Entity{
    public int one;
    public int two;
    public int three;
    
    public testPersist(){
        this.one= (int )(Math.random() * 50 + 1);
        this.two= (int )(Math.random() * 50 + 1);
        this.three = (int )(Math.random() * 50 + 1);
    }
    
    @Override
    public String toString(){
        return "|"+one+"|"+two+"|"+three+"|";
    }

    @Override
    public JSONObject list() {
        try {
            return EntityPersistence.list(this);
        } catch (Exception ex) {
            Logger.getLogger(testPersist.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void create(){
      try {
            EntityPersistence.create(this);
        } catch (Exception ex) {
            Logger.getLogger(testPersist.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    @Override
    public JSONObject create(JSONObject obj) {
        try {
            return EntityPersistence.create(this);
        } catch (Exception ex) {
            Logger.getLogger(testPersist.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public JSONObject update(JSONObject obj, int resource) {
        JSON.JSONtoObject(this, obj);
        try {
            return EntityPersistence.update(this, resource);
        } catch (Exception ex) {
            Logger.getLogger(testPersist.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public JSONObject read(int resource) {
         try {
            return EntityPersistence.read(this, resource);
        } catch (Exception ex) {
            Logger.getLogger(testPersist.class.getName()).log(Level.SEVERE, null, ex);
        }
         return null;
    }

    @Override
    public JSONObject delete(int resource) {
    try {
            return EntityPersistence.delete(this, resource);
        } catch (Exception ex) {
            Logger.getLogger(testPersist.class.getName()).log(Level.SEVERE, null, ex);
        }    
    return null;
    }
}
