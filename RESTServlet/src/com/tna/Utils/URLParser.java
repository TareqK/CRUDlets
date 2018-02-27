/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.Utils;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author tareq
 */
public class URLParser {

    public static Integer parse(HttpServletRequest request) throws URLParseException {//URI parser
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
