/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.Endpoints;

import com.tna.Utils.Authorisation;
import com.tna.Utils.URLParser;
import com.tna.Utils.RequestParser;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
public abstract class AuthorisedEndpoint extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        JSONObject obj = null;
        try {
            Integer resource = URLParser.parse(request);
            JSONObject json = RequestParser.parse(request);
            try {
                if (resource == null) {
                    obj = doList(json);
                } else {
                    obj = doRead(json, resource);
                }
            } catch (Authorisation.UnauthorisedException e) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);//
                return;
            }

        } catch (URLParser.URLParseException | RequestParser.RequestParseException e) {
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
            JSONObject json = RequestParser.parse(request);
            obj = doCreate(json);

        } catch (RequestParser.RequestParseException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
        } catch (Authorisation.UnauthorisedException ex) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);//
            return;
        }
        try (PrintWriter printWriter = response.getWriter()) {
             if (obj == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
                return;
            } else {
                printWriter.print(obj);
            }
        }

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        JSONObject obj = null;
        try {
            Integer resource = URLParser.parse(request);
            JSONObject json = RequestParser.parse(request);
            if (resource == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
                return;
            } else {
                obj = doUpdate(json, resource);
            }
        } catch (URLParser.URLParseException | RequestParser.RequestParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
            return;
        } catch (Authorisation.UnauthorisedException ex) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);//
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
            Integer resource = URLParser.parse(request);
            JSONObject json = RequestParser.parse(request);
            if (resource == null) {
                return;
            } else {
                obj = doDelete(json,resource);
            }
        } catch (URLParser.URLParseException | RequestParser.RequestParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
            return;
        } catch (Authorisation.UnauthorisedException ex) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);//
            return;
        }
        //send a bad request

           try (PrintWriter printWriter = response.getWriter()) {
             if (obj == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
                return;
            } else {
                printWriter.print(obj);
            }
        
        }
    }

    /**
     *
     * @param obj
     * @return returns a list of all entities
     * @throws com.tna.RESTServlet.AuthorisableEntity.UnauthorisedException
     */
    public abstract JSONObject doList(JSONObject obj) throws Authorisation.UnauthorisedException;

    /**
     *
     * @param json
     * @return Creates a new entry in the data source. Should return a success
     * code in JSON format.
     * @throws com.tna.RESTServlet.AuthorisableEntity.UnauthorisedException
     */
    public abstract JSONObject doCreate(JSONObject json) throws Authorisation.UnauthorisedException;

    /**
     *
     * @param json
     * @param resource
     * @return Updates an entity in the data source. Should return a success
     * code in JSON format.
     * @throws com.tna.RESTServlet.AuthorisableEntity.UnauthorisedException
     */
    public abstract JSONObject doUpdate(JSONObject json, int resource) throws Authorisation.UnauthorisedException;

    /**
     *
     * @param resource
     * @param json
     * @return Reads/Fetches an entity from the data source. Should return the
     * entity details in JSON fomat.
     * @throws com.tna.RESTServlet.AuthorisableEntity.UnauthorisedException
     */
    public abstract JSONObject doRead(JSONObject json, int resource) throws Authorisation.UnauthorisedException;

    /**
     *
     * @param resource
     * @param json
     * @return Deletes an entity from the data source. Should return a success
     * code in JSON format.
     * @throws com.tna.RESTServlet.AuthorisableEntity.UnauthorisedException
     */
    public abstract JSONObject doDelete(JSONObject json, int resource) throws Authorisation.UnauthorisedException;

}
