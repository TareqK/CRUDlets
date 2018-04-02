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
            String token = request.getParameter("token");
            try {
                if (token == null) {
                    throw new UserAccessControl.UnauthorisedException();
                }
                if (resource == null) {
                    obj = doList(token);
                } else {
                    obj = doRead(resource, token);
                }
            } catch (UserAccessControl.UnauthorisedException e) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);//
                return;
            }

        } catch (Parser.URLParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
            return;
        }
        if (obj == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);//send a bad request
            return;
        } else {
            //send a bad request
            try (PrintWriter printWriter = response.getWriter()) {
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
            String token = request.getParameter("token");
            if (token == null) {
                throw new UserAccessControl.UnauthorisedException();
            }
            obj = doCreate(json, token);

        } catch (Parser.RequestParseException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
            return;
        } catch (UserAccessControl.UnauthorisedException ex) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);//
            return;
        }
        if (obj == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);//send a bad request
            return;
        } else {
            try (PrintWriter printWriter = response.getWriter()) {
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
        JSONObject obj;
        try {
            Integer resource = Parser.parseURL(request);
            JSONObject json = Parser.parseRequest(request);
            String token = request.getParameter("token");
            if (token == null) {
                throw new UserAccessControl.UnauthorisedException();
            }

            if (resource == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
                return;
            } else {
                obj = doUpdate(json, resource, token);
            }
        } catch (Parser.URLParseException | Parser.RequestParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
            return;
        } catch (UserAccessControl.UnauthorisedException ex) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);//
            return;
        }
        if (obj == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);//send a bad request
            return;
        } else {
            try (PrintWriter printWriter = response.getWriter()) {

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
            String token = request.getParameter("token");
            if (token == null) {
                throw new UserAccessControl.UnauthorisedException();
            }
            if (resource == null) {
                return;
            } else {
                obj = doDelete(resource, token);

            }
        } catch (Parser.URLParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
            return;
        } catch (UserAccessControl.UnauthorisedException ex) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);//
            return;
        }
        if (obj == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);//send a bad request
            return;
        } else {
            //send a bad request

            try (PrintWriter printWriter = response.getWriter()) {
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
    public abstract JSONObject doList(String token) throws UserAccessControl.UnauthorisedException;

    /**
     *
     * @param json
     * @return Creates a new entry in the data source. Should return a success
     * code in JSON format.
     * @throws com.tna.common.Authorisation.UnauthorisedException
     */
    public abstract JSONObject doCreate(JSONObject json, String token) throws UserAccessControl.UnauthorisedException;

    /**
     *
     * @param json
     * @param resource
     * @return Updates an entity in the data source. Should return a success
     * code in JSON format.
     * @throws com.tna.common.Authorisation.UnauthorisedException
     */
    public abstract JSONObject doUpdate(JSONObject json, int resource, String token) throws UserAccessControl.UnauthorisedException;

    /**
     *
     * @param resource
     * @param json
     * @return Reads/Fetches an entity from the data source. Should return the
     * entity details in JSON format.
     * @throws com.tna.common.Authorisation.UnauthorisedException
     */
    public abstract JSONObject doRead(int resource, String token) throws UserAccessControl.UnauthorisedException;

    /**
     *
     * @param resource
     * @param json
     * @return Deletes an entity from the data source. Should return a success
     * code in JSON format.
     * @throws com.tna.common.Authorisation.UnauthorisedException
     */
    public abstract JSONObject doDelete(int resource, String token) throws UserAccessControl.UnauthorisedException;

}
