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
public abstract class RESTServletAuthorisedEndpoint extends HttpServlet {

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        JSONObject obj = null;
        try {
            String resource = RESTServletURLParser.parse(request);
            JSONObject parsed = RESTServletRequestParser.parseRequest(request);
            try{
                if (resource == null) {
                    obj = doList(parsed);
                } else {
                    obj = doRead(resource,parsed);   
                }
            }catch (RESTServletAuthorisableEntity.UnauthorisedError e){
                response.sendError(HttpServletResponse.SC_FORBIDDEN);//
                return;
            }
 
        } catch (RESTServletURLParser.RESTServletURLParseError | RESTServletRequestParser.RESTServletRequestError e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
            return;
        }
        //send a bad request
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
                obj = doCreate(parsed);

        } catch (RESTServletRequestParser.RESTServletRequestError ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
        } catch (RESTServletAuthorisableEntity.UnauthorisedError ex) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);//
                return;        }
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
                if (resource == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
                    return;
                } else {
                    obj = doUpdate(parsed, resource);
                }
        } catch (RESTServletURLParser.RESTServletURLParseError | RESTServletRequestParser.RESTServletRequestError e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
            return;
        } catch (RESTServletAuthorisableEntity.UnauthorisedError ex) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);//
                return;        }
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
                if (resource == null) {
                    return;
                } else {
                    obj = doDelete(resource,parsed);
                }
        } catch (RESTServletURLParser.RESTServletURLParseError | RESTServletRequestParser.RESTServletRequestError e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
            return;
        } catch (RESTServletAuthorisableEntity.UnauthorisedError ex) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);//
                return;        }
        //send a bad request
        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.print(obj);
        }
    }

    /**
     *
     * @param obj
     * @return returns a list of all entities
     * @throws RESTServletAuthorisableEntity.UnauthorisedError
     */
    public abstract JSONObject doList(JSONObject obj) throws RESTServletAuthorisableEntity.UnauthorisedError;

    /**
     *
     * @param obj
     * @return Creates a new entry in the data source. Should return a success code in JSON format.
     * @throws com.tna.RESTServlet.RESTServletAuthorisableEntity.UnauthorisedError
     */
    public abstract JSONObject doCreate(JSONObject obj) throws RESTServletAuthorisableEntity.UnauthorisedError; 

    /**
     *
     * @param obj
     * @param resource
     * @return Updates an entity in the data source. Should return a success code in JSON format.
     * @throws com.tna.RESTServlet.RESTServletAuthorisableEntity.UnauthorisedError
     */
    public abstract JSONObject doUpdate(JSONObject obj, String resource) throws RESTServletAuthorisableEntity.UnauthorisedError; 

    /**
     *
     * @param resource
     * @param obj
     * @return Reads/Fetches an entity from the data source. Should return the entity details in JSON fomat.
     * @throws com.tna.RESTServlet.RESTServletAuthorisableEntity.UnauthorisedError
     */
    public abstract JSONObject doRead(String resource,JSONObject obj) throws RESTServletAuthorisableEntity.UnauthorisedError; 

    /**
     *
     * @param resource
     * @param obj
     * @return Deletes an entity from the data source. Should return a success code in JSON format.
     * @throws com.tna.RESTServlet.RESTServletAuthorisableEntity.UnauthorisedError
     */
    public abstract JSONObject doDelete(String resource, JSONObject obj) throws RESTServletAuthorisableEntity.UnauthorisedError; 

}
