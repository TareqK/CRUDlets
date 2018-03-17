/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.endpoints;

import com.tna.common.UserAccessControl;
import com.tna.utils.Parser;
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

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        JSONObject json = new JSONObject();
        JSONObject obj = new JSONObject();
        try {
            Integer resource = Parser.parseURL(request);
            try {
                if(request.getParameter("token")==null){
                throw new UserAccessControl.UnauthorisedException();
            }
            json.put("token", request.getParameter("token"));
            
                if (resource == null) {
                    obj = doList(json);
                } else {
                    obj = doRead(json, resource);
                }
            } catch (UserAccessControl.UnauthorisedException e) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);//
                return;
            }

        } catch (Parser.URLParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
            return;
        }
        //send a bad request
        try (PrintWriter printWriter = response.getWriter()) {
            if (obj == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);//send a bad request
            } else {
                printWriter.print(obj);
            }
        }

    }

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        JSONObject obj = null;
        try {
            JSONObject json = Parser.parseRequest(request);
            if(request.getParameter("token")==null){
                throw new UserAccessControl.UnauthorisedException();
            }
            json.put("token", request.getParameter("token"));

            obj = doCreate(json);

        } catch (Parser.RequestParseException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
        } catch (UserAccessControl.UnauthorisedException ex) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);//
            return;
        }
        try (PrintWriter printWriter = response.getWriter()) {
            if (obj == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
            } else {
                printWriter.print(obj);
            }
        }

    }

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        JSONObject obj ;
        try {
            Integer resource = Parser.parseURL(request);
            JSONObject json = Parser.parseRequest(request);
            if(request.getParameter("token")==null){
                throw new UserAccessControl.UnauthorisedException();
            }
            json.put("token", request.getParameter("token"));

            if (resource == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
                return;
            } else {
                obj = doUpdate(json, resource);
            }
        } catch (Parser.URLParseException | Parser.RequestParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
            return;
        } catch (UserAccessControl.UnauthorisedException ex) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);//
            return;
        }
        try (PrintWriter printWriter = response.getWriter()) {
            if (obj == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);//send a bad request
            } else {
                printWriter.print(obj);
            }
        }

    }

    /**
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws
            IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        JSONObject obj;
        try {
            Integer resource = Parser.parseURL(request);
            JSONObject json = Parser.parseRequest(request);
            if(request.getParameter("token")==null){
                throw new UserAccessControl.UnauthorisedException();
            }
            json.put("token", request.getParameter("token"));

            if (resource == null) {
                return;
            } else {
                obj = doDelete(json, resource);
            }
        } catch (Parser.URLParseException | Parser.RequestParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
            return;
        } catch (UserAccessControl.UnauthorisedException ex) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);//
            return;
        }
        //send a bad request

        try (PrintWriter printWriter = response.getWriter()) {
            if (obj == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
            } else {
                printWriter.print(obj);
            }

        }
    }

    /**
     *
     * @param obj
     * @return returns a list of all entities
     * @throws com.tna.common.Authorisation.UnauthorisedException
     */
    public abstract JSONObject doList(JSONObject obj) throws UserAccessControl.UnauthorisedException;

    /**
     *
     * @param json
     * @return Creates a new entry in the data source. Should return a success
     * code in JSON format.
     * @throws com.tna.common.Authorisation.UnauthorisedException
     */
    public abstract JSONObject doCreate(JSONObject json) throws UserAccessControl.UnauthorisedException;

    /**
     *
     * @param json
     * @param resource
     * @return Updates an entity in the data source. Should return a success
     * code in JSON format.
     * @throws com.tna.common.Authorisation.UnauthorisedException
     */
    public abstract JSONObject doUpdate(JSONObject json, int resource) throws UserAccessControl.UnauthorisedException;

    /**
     *
     * @param resource
     * @param json
     * @return Reads/Fetches an entity from the data source. Should return the
     * entity details in JSON format.
     * @throws com.tna.common.Authorisation.UnauthorisedException
     */
    public abstract JSONObject doRead(JSONObject json, int resource) throws UserAccessControl.UnauthorisedException;

    /**
     *
     * @param resource
     * @param json
     * @return Deletes an entity from the data source. Should return a success
     * code in JSON format.
     * @throws com.tna.common.Authorisation.UnauthorisedException
     */
    public abstract JSONObject doDelete(JSONObject json, int resource) throws UserAccessControl.UnauthorisedException;

}
