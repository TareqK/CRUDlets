/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.RESTServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
public abstract class RESTServletAuthenticatedEndpoint extends HttpServlet {

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        JSONObject obj = null;
        try {
            String resource = RESTServletURLParser.parse(request);
            JSONObject parsed = RESTServletRequestParser.parseRequest(request);
            if (authenticate(parsed)) {
                if (resource == null) {
                    obj = doList(parsed);
                } else {
                    obj = doRead(resource,parsed);
                }
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);//send forbidden
                return;
            }
        } catch (RESTServletURLParser.RESTServletURLParseError e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
            return;
        } catch (RESTServletRequestParser.RESTServletRequestError ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
            return;
        }
        try (PrintWriter printWriter = response.getWriter()) {
            if (obj == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);//send a bad request
                return;
            } else {
                printWriter.print(obj);
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        JSONObject obj = null;
        try {
            JSONObject parsed = RESTServletRequestParser.parseRequest(request);
            if (authenticate(parsed)) {
                obj = doCreate(parsed);
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);//send a bad request
                return;
            }
        } catch (RESTServletRequestParser.RESTServletRequestError ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
        }
        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.print(obj);
        }

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        JSONObject obj = null;
        try {
            String resource = RESTServletURLParser.parse(request);
            JSONObject parsed = RESTServletRequestParser.parseRequest(request);
            if (authenticate(parsed)) {
                if (resource == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
                    return;
                } else {
                    obj = doUpdate(parsed, resource);
                }
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);//send a bad request
                return;
            }
        } catch (RESTServletURLParser.RESTServletURLParseError | RESTServletRequestParser.RESTServletRequestError e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
            return;
        }
        try (PrintWriter printWriter = response.getWriter()) {
            if (obj == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);//send a bad request
                return;
            } else {
                printWriter.print(obj);
            }
        }

    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws
            IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        JSONObject obj;
        try {
            String resource = RESTServletURLParser.parse(request);
            JSONObject parsed = RESTServletRequestParser.parseRequest(request);
            if (authenticate(parsed)) {
                if (resource == null) {
                    return;
                } else {
                    obj = doDelete(resource,parsed);
                }
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);//send a bad request
                return;
            }
        } catch (RESTServletURLParser.RESTServletURLParseError | RESTServletRequestParser.RESTServletRequestError e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
            return;
        }
        //send a bad request
        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.print(obj);
        }
    }

     public abstract JSONObject doList(JSONObject obj);

    /**
     *
     * @param obj
     * @return Creates a new entry in the data source. Should return a success code in JSON format.
     */
    public abstract JSONObject doCreate(JSONObject obj);

    /**
     *
     * @param obj
     * @param resource
     * @return Updates an entity in the data source. Should return a success code in JSON format.
     */
    public abstract JSONObject doUpdate(JSONObject obj, String resource);

    /**
     *
     * @param resource
     * @return Reads/Fetches an entity from the data source. Should return the entity details in JSON fomat.
     */
    public abstract JSONObject doRead(String resource,JSONObject obj);

    /**
     *
     * @param resource
     * @return Deletes an entity from the data source. Should return a success code in JSON format.
     */
    public abstract JSONObject doDelete(String resource, JSONObject obj);

    /**
     * a function that authenticates if the user's key is valid
     *
     * @param request
     * @return returns true/false
     */
    public abstract boolean authenticate(JSONObject request);

}
