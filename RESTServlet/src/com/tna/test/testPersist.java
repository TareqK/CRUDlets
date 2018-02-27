/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.test;

import com.tna.DataAccess.Persistence;
import org.json.simple.JSONObject;
import com.tna.Entities.Entity;
import com.tna.Utils.JSON;
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
            return Persistence.list(this);
        } catch (Exception ex) {
            Logger.getLogger(testPersist.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void create(){
      try {
            Persistence.create(this);
        } catch (Exception ex) {
            Logger.getLogger(testPersist.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    @Override
    public JSONObject create(JSONObject obj) {
        try {
            return Persistence.create(this);
        } catch (Exception ex) {
            Logger.getLogger(testPersist.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public JSONObject update(JSONObject obj, int resource) {
        JSON.JSONtoObject(this, obj);
        try {
            return Persistence.update(this, resource);
        } catch (Exception ex) {
            Logger.getLogger(testPersist.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public JSONObject read(int resource) {
         try {
            return Persistence.read(this, resource);
        } catch (Exception ex) {
            Logger.getLogger(testPersist.class.getName()).log(Level.SEVERE, null, ex);
        }
         return null;
    }

    @Override
    public JSONObject delete(int resource) {
    try {
            return Persistence.delete(this, resource);
        } catch (Exception ex) {
            Logger.getLogger(testPersist.class.getName()).log(Level.SEVERE, null, ex);
        }    
    return null;
    }
}
