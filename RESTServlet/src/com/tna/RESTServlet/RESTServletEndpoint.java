/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.RESTServlet;

import java.io.IOException;
import java.io.PrintWriter;
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
public abstract class RESTServletEndpoint extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        JSONObject obj;
        try {
            String query = URLParser.parse(request);
            if (query == null) {
                obj = doList();
            } else {
                obj = doRead(query);
            }
        } catch (URLParser.URIParseError e) {
            return;
        }
        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.print(obj);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        JSONObject obj = doCreate(RequestParser.parseRequest(request));
        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.print(obj);
        }

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        JSONObject obj;
        try {
            String query = URLParser.parse(request);
            if (query == null) {
                return;
            } else {
                obj = doUpdate(RequestParser.parseRequest(request), query);
            }
        } catch (URLParser.URIParseError e) {
            return;
        }
        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.print(obj);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws
            IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        JSONObject obj;
        try {
            String query = URLParser.parse(request);
            if (query == null) {
                return;
            } else {
                obj = doDelete(query);
            }
        } catch (URLParser.URIParseError e) {
            return;
        }
        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.print(obj);
        }
    }

    @Override
    public String getServletInfo() {
        return "A simple API framework";
    }// </editor-fold>

    /**
     *
     * @return returns a list of all entries
     */
    public abstract JSONObject doList();

    /**
     *
     * @param obj
     * @return Creates a new entry in the data source. Should return a success code in JSON format.
     */
    public abstract JSONObject doCreate(JSONObject obj);

    /**
     *
     * @param obj
     * @param query
     * @return Updates an entity in the data source. Should return a success code in JSON format.
     */
    public abstract JSONObject doUpdate(JSONObject obj, String query);

    /**
     *
     * @param query
     * @return Reads/Fetches an entity from the data source. Should return the entity details in JSON fomat.
     */
    public abstract JSONObject doRead(String query);

    /**
     *
     * @param query
     * @return Deletes an entity from the data source. Should return a success code in JSON format.
     */
    public abstract JSONObject doDelete(String query);
 
  

}
