/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.Endpoints;

import com.tna.Utils.URLParser;
import com.tna.Utils.RequestParser;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import javax.servlet.annotation.WebServlet;

/**
 *
 * @author tareq
 */
public abstract class Endpoint extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        JSONObject obj = null;
        try {
            Integer resource = URLParser.parse(request);
            if (resource == null) {
                obj = doList();
            } else {
                obj = doRead(resource);
            }
        } catch (URLParser.URLParseException e) {
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
            obj = doCreate(RequestParser.parse(request));
        } catch (RequestParser.RequestParseException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
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
            if (resource == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
                return;
            } else {
                obj = doUpdate(RequestParser.parse(request), resource);
            }
        } catch (URLParser.URLParseException | RequestParser.RequestParseException e) {
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
            Integer resource = URLParser.parse(request);
            if (resource == null) {
                return;
            } else {
                obj = doDelete(resource);
            }
        } catch (URLParser.URLParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);//send a bad request
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
     * entity details in JSON fomat.
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
