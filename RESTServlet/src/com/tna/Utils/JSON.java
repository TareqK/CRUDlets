/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.Utils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author tareq
 */



public class JSON {
    
    public static final String OK_CODE = "{\"success\":\"ok\"}";
    public static JSONObject objectToJSON(Object object){
        JSONObject json = null;
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {
            try {
                json.put(field.getName(),field.get(object));
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(JSON.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return json;
    }
    
    public static void JSONtoObject(Object object, JSONObject json){
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {
            try {
                field.set(object,json.get(field.getName()));
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(JSON.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public static JSONObject successResponse(){
     JSONParser parser = new JSONParser();
        try {
            return (JSONObject)parser.parse(OK_CODE);
        } catch (ParseException ex) {
             return null;
        }
    }
   
    }
    

