/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.endpoints;

import com.tna.common.AccessError;
import com.tna.common.AccessError.ERROR_TYPE;
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
                    throw new AccessError(ERROR_TYPE.USER_NOT_AUTHENTICATED);
                }
                if (resource == null) {
                    obj = doList(token);
                } else {
                    obj = doRead(resource, token);
                }
            } catch (AccessError e) {
                response.sendError(handleError(e));
                return;
            }

        } catch (Parser.URLParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
            return;
        }
        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.print(obj);

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
            try {
                if (token == null) {
                    throw new AccessError(ERROR_TYPE.USER_NOT_AUTHENTICATED);
                }
                obj = doCreate(json, token);
            } catch (AccessError e) {
                response.sendError(handleError(e));
                return;
            }
        } catch (Parser.RequestParseException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
            return;
        }
        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.print(obj);
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
            try {
                String token = request.getParameter("token");
                if (token == null) {
                    throw new AccessError(ERROR_TYPE.USER_NOT_AUTHENTICATED);
                }
                if (resource == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
                    return;
                } else {
                    obj = doUpdate(json, resource, token);
                }
            } catch (AccessError e) {
                response.sendError(handleError(e));
                return;
            }
        } catch (Parser.URLParseException | Parser.RequestParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
            return;
        }
        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.print(obj);
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
            try {
                if (token == null) {
                    throw new AccessError(ERROR_TYPE.USER_NOT_AUTHENTICATED);
                }
                if (resource == null) {
                    return;
                } else {
                    obj = doDelete(resource, token);

                }
            } catch (AccessError e) {
                response.sendError(handleError(e));//
                return;
            }
        } catch (Parser.URLParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
            return;
        }
        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.print(obj);

        }
    }

    private int handleError(AccessError e) {
        switch (e.error) {
            case ENTITY_NOT_FOUND:
                return HttpServletResponse.SC_NOT_FOUND;
            case USER_NOT_AUTHENTICATED:
                return HttpServletResponse.SC_UNAUTHORIZED;
            case USER_NOT_AUTHORISED:
                return HttpServletResponse.SC_FORBIDDEN;
            case OPERATION_FAILED:
                return HttpServletResponse.SC_NOT_IMPLEMENTED;
            case ENTITY_UNAVAILABLE :
                return HttpServletResponse.SC_CONFLICT;
            case USER_NOT_ALLOWED :
                return HttpServletResponse.SC_ACCEPTED;
            default:
                return HttpServletResponse.SC_BAD_REQUEST;
        }
    }

    /**
     *
     * @param token
     * @return returns a list of all entities
     * @throws com.tna.common.AccessError
     */
    public abstract JSONObject doList(String token) throws AccessError;

    /**
     *
     * @param json
     * @return Creates a new entry in the data source. Should return a success
     * code in JSON format.
     * @throws com.tna.common.Authorisation.UnauthorisedException
     */
    public abstract JSONObject doCreate(JSONObject json, String token) throws AccessError;

    /**
     *
     * @param json
     * @param resource
     * @return Updates an entity in the data source. Should return a success
     * code in JSON format.
     * @throws com.tna.common.Authorisation.UnauthorisedException
     */
    public abstract JSONObject doUpdate(JSONObject json, int resource, String token) throws AccessError;

    /**
     *
     * @param resource
     * @param json
     * @return Reads/Fetches an entity from the data source. Should return the
     * entity details in JSON format.
     * @throws com.tna.common.Authorisation.UnauthorisedException
     */
    public abstract JSONObject doRead(int resource, String token) throws AccessError;

    /**
     *
     * @param resource
     * @param json
     * @return Deletes an entity from the data source. Should return a success
     * code in JSON format.
     * @throws com.tna.common.Authorisation.UnauthorisedException
     */
    public abstract JSONObject doDelete(int resource, String token) throws AccessError;

}
