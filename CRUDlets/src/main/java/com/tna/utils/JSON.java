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

    private static final String OK_CODE = "{\"success\":\"ok\"}";
    private static final String FAIL_CODE = "{\"faliure\":\"not ok\"}";

    /**
     * Converts an object to JSON format.
     * @param object The object to be converted.
     * @return The JSON formatted object.
     */
    public static JSONObject objectToJSON(Object object) {
        JSONObject json = new JSONObject();
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {
            try {
                json.put(field.getName(), field.get(object));
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                return null;
            }
        }
        return json;
    }

    /**
     * Writes a JSON object into an object.
     * @param object The object to be written into.
     * @param json The JSON object we are going to write.
     */
    public static void JSONtoObject(Object object, JSONObject json) {
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {
            try {
                field.set(object, json.get(field.getName()));
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(JSON.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Returns a JSON formatted success response.
     * @return a JSON formatted success response.
     */
    public static JSONObject successResponse() {
        JSONParser parser = new JSONParser();
        try {
            return (JSONObject) parser.parse(OK_CODE);
        } catch (ParseException ex) {
            return null;
        }
    }

    /**
     * Returns a JSON formatted failure response.
     * @return a JSON formatted failure response.
     */
    public static JSONObject failResponse() {
        JSONParser parser = new JSONParser();
        try {
            return (JSONObject) parser.parse(FAIL_CODE);
        } catch (ParseException ex) {
            return null;
        }
    }

}
