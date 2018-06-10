/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.endpoints;

import com.tna.common.AccessError;
import com.tna.common.AccessError.ERROR_TYPE;
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
            case ENTITY_UNAVAILABLE:
                return HttpServletResponse.SC_CONFLICT;
            case USER_NOT_ALLOWED:
                return HttpServletResponse.SC_ACCEPTED;
            default:
                return HttpServletResponse.SC_BAD_REQUEST;
        }
    }

    /**
     * List objects associated with this endpoint.
     * @param token The session token.
     * @return a JSON formatted list of objects.
     * @throws AccessError
     */
    public abstract JSONObject doList(String token) throws AccessError;

    /**
     * Creates an object associated with this endpoint.
     * @param data The JSON formatted data of the object we want to create.
     * @param token The session token of the user.
     * @return a JSON formatted success message.
     * @throws AccessError
     */
    public abstract JSONObject doCreate(JSONObject data, String token) throws AccessError;

    /**
     * Updates an object associated with this endpoint.
     * @param data The JSON formatted update data.
     * @param resource The id of the object we want to update.
     * @param token The session token of the user.
     * @return a JSON formatted success message.
     * @throws AccessError
     */
    public abstract JSONObject doUpdate(JSONObject data, long resource, String token) throws AccessError;

    /**
     * Reads an object associated with this endpoint.
     * @param resource The id of the object we want to read.
     * @param token The session token of the user.
     * @return a JSON formatted object.
     * @throws AccessError
     */
    public abstract JSONObject doRead(long resource, String token) throws AccessError;

    /**
     * Deletes an object associated with this endpoint.
     * @param resource The id of the object we want to delete.
     * @param token The session token of the user.
     * @return a JSON formatted success message.
     * @throws AccessError
     */
    public abstract JSONObject doDelete(long resource, String token) throws AccessError;

}
