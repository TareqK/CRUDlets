/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.utils;

import java.lang.reflect.Field;
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
    public static final String FAIL_CODE = "{\"faliure\":\"not ok\"}";
    /**
     * Converts an object(with all its fields) to a JSONObject, with the 
     * key being the field name and the value being the field value.
     * @param object
     * @return returns a JSONObject of the object.
     */
    public static JSONObject objectToJSON(Object object){
        JSONObject json = new JSONObject();
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {
            try {
                json.put(field.getName(),field.get(object));
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                return null;
            }
        }
        return json;
    }
    
    /**
     * Writes a JSONObject into an instance of an object, where the key of the JSONObject is 
     * a field in the object and the associated value is the field value.
     * @param object
     * @param json
     */
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
    
    /**
     * Creates a simple success message.
     * @return returns a JSONOBject with a success message.
     */
    public static JSONObject successResponse(){
     JSONParser parser = new JSONParser();
        try {
            return (JSONObject)parser.parse(OK_CODE);
        } catch (ParseException ex) {
             return null;
        }
    }
    
    /**
     * Creats a simple failure message.
     * @return returns a JSONObject with a failure message.
     */
    public static JSONObject failResponse(){
     JSONParser parser = new JSONParser();
        try {
            return (JSONObject)parser.parse(FAIL_CODE);
        } catch (ParseException ex) {
             return null;
        }
    }
   
    }
    

