/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.RESTServlet;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author tareq
 */
 class URLParser {
    
     protected static String parse(HttpServletRequest request) throws URIParseError {//URI parser
         String pathInfo = request.getPathInfo();
          if(pathInfo == null || pathInfo.equals("/")){
              return null;// send null if there is no path
          }else{
              String[] splits = pathInfo.split("/");
              if(splits.length != 2) {
		throw new URIParseError();// send -1 if the path is the wrong format
                }else{
                    return splits[1];
                }
            }
        }

    protected static class URIParseError extends Exception {

        public URIParseError() {
            
            System.out.println("URI Error");
        }
    }
    
}
