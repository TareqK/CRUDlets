/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.Utils;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author tareq
 */
public class RequestParser {

    public static JSONObject parse(HttpServletRequest request) throws IOException, RequestParseException {//parse a request to json array
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

    public static class RequestParseException extends Exception {

        public RequestParseException() {
            System.out.println("Wrong Request");
        }
    }

}
