/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.utils;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author tareq
 */
public class Parser {

    /**
     * Parses a request to a JSONObject.
     * @param request
     * @return returns the HTTP Request as a JSONObject.
     * @throws IOException
     * @throws RequestParseException
     */
    public static JSONObject parseRequest(HttpServletRequest request) throws IOException, RequestParseException {//parse a request to json array
        Object obj;
        JSONObject array = null;
        JSONParser parser = new JSONParser();
        try {
            obj = parser.parse(request.getReader());
            array = (JSONObject) obj;
        } catch (ParseException pe) {
            throw new RequestParseException();
        }
        return array;
    }

    /**
     * Thrown when there is something wrong with the request.
     */ 
    public static class RequestParseException extends Exception {

        public RequestParseException() {
            System.out.println("Wrong Request");
        }
    }
    
    public static Integer parseURL(HttpServletRequest request) throws URLParseException {//URI parser
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            return null;// send null if there is no path
        } else {
            String[] splits = pathInfo.split("/");
            if (splits.length != 2) {
                throw new URLParseException();// send -1 if the path is the wrong format
            } else {
                return Integer.parseInt(splits[1]);
            }
        }
    }

    public static class URLParseException extends Exception {

        public URLParseException() {
            System.out.println("Wrong URI");
        }
    }

}
