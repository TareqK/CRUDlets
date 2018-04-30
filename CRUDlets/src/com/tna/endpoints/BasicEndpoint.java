/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.endpoints;

import com.tna.common.AccessError;
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
public abstract class BasicEndpoint extends HttpServlet {

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
        JSONObject obj = null;
        try {
            Integer resource = Parser.parseURL(request);
            try {
                if (resource == null) {
                    obj = doList();
                } else {
                    obj = doRead(resource);
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
            try {
                obj = doCreate(json);
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
                if (resource == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
                    return;
                } else {
                    obj = doUpdate(json, resource);
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
        JSONObject obj = null;
        try {
            Integer resource = Parser.parseURL(request);
            try{
                if (resource == null) {
                  response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                } else {
                    obj = doDelete(resource);
                }
            } catch (AccessError e) {
                response.sendError(handleError(e));//
                return;
            }
        }
         catch (Parser.URLParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
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
     * Lists all the objects associated with this endpoint.
     * @return a JSON formatted list of objects.
     * @throws com.tna.common.AccessError
     */
    public abstract JSONObject doList() throws AccessError;

    /**
     * Creates an object associated with this endpoint.
     * @param data The JSON formatted object data.
     * @return a JSON formatted success message.
     * @throws com.tna.common.AccessError
     */
    public abstract JSONObject doCreate(JSONObject data) throws AccessError ;

    /**
     * Update an object associated with this endpoint.
     * @param data The JSON formatted object data.
     * @param resource The id of the object.
     * @return a JSON formatted succecss message.
     * @throws com.tna.common.AccessError
     */
    public abstract JSONObject doUpdate(JSONObject data, int resource) throws AccessError;

    /**
     * Read an object associated with this endpoint.
     * @param resource The id of the object.
     * @return a JSON formatted object.
     * @throws com.tna.common.AccessError
     */
    public abstract JSONObject doRead(int resource) throws AccessError;

    /**
     * Deletes an object associated with this endpoint.
     * @param resource The id of the object.
     * @return a JSON formatted success message.
     * @throws com.tna.common.AccessError
     */
    public abstract JSONObject doDelete(int resource) throws AccessError;

}
