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
        JSONObject obj = null;
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
        JSONObject obj = null;
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
                return;
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
                return;
            } else {
                printWriter.print(obj);
            }
        }
    }

    @Override
    public String getServletInfo() {
        return null;
    }// </editor-fold>

    /**
     *
     * @return returns a list of all entries
     */
    public abstract JSONObject doList();

    /**
     *
     * @param obj
     * @return Creates a new entry in the data source. Should return a success
     * code in JSON format.
     */
    public abstract JSONObject doCreate(JSONObject obj);

    /**
     *
     * @param obj
     * @param resource
     * @return Updates an entity in the data source. Should return a success
     * code in JSON format.
     */
    public abstract JSONObject doUpdate(JSONObject obj, int resource);

    /**
     *
     * @param resource
     * @return Reads/Fetches an entity from the data source. Should return the
     * entity details in JSON format.
     */
    public abstract JSONObject doRead(int resource);

    /**
     *
     * @param resource
     * @return Deletes an entity from the data source. Should return a success
     * code in JSON format.
     */
    public abstract JSONObject doDelete(int resource);

}
