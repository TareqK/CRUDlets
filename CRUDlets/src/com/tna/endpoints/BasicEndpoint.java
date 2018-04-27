/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.endpoints;

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
        JSONObject obj;
        try {
            Integer resource = Parser.parseURL(request);
            if (resource == null) {
                obj = doList();
            } else {
                obj = doRead(resource);
            }
        } catch (Parser.URLParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
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
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        JSONObject obj;
        try {
            obj = doCreate(Parser.parseRequest(request));
        } catch (Parser.RequestParseException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
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
        JSONObject obj;
        try {
            Integer resource = Parser.parseURL(request);
            if (resource == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
                return;
            } else {
                obj = doUpdate(Parser.parseRequest(request), resource);
            }
        } catch (Parser.URLParseException | Parser.RequestParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
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
            if (resource == null) {
                return;
            } else {
                obj = doDelete(resource);
            }
        } catch (Parser.URLParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
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
     * Lists all the objects associated with this endpoint.
     * @return a JSON formatted list of objects.
     */
    public abstract JSONObject doList();

    /**
     * Creates an object associated with this endpoint.
     * @param data The JSON formatted object data.
     * @return a JSON formatted success message.
     */
    public abstract JSONObject doCreate(JSONObject data);

    /**
     * Update an object associated with this endpoint.
     * @param data The JSON formatted object data.
     * @param resource The id of the object.
     * @return a JSON formatted succecss message.
     */
    public abstract JSONObject doUpdate(JSONObject data, int resource);

    /**
     * Read an object associated with this endpoint.
     * @param resource The id of the object.
     * @return a JSON formatted object.
     */
    public abstract JSONObject doRead(int resource);

    /**
     * Deletes an object associated with this endpoint.
     * @param resource The id of the object.
     * @return a JSON formatted success message.
     */
    public abstract JSONObject doDelete(int resource);

}
