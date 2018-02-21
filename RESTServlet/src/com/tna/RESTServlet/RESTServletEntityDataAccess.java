/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.RESTServlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
public interface RESTServletEntityDataAccess {

    /**
     *
     * @return Returns the Result set of querying the database for a list of all
     * entity instances
     */
    public ResultSet queryList();

    /**
     *
     * @param obj
     * @return Returns the Result set of querying the database to create a new
     * entity
     */
    public ResultSet queryCreate(JSONObject obj);

    /**
     *
     * @param resource
     * @return Returns the Result set of querying the database to read an entity
     * by its id
     */
    public ResultSet queryRead(String resource);

    /**
     *
     * @param obj
     * @param resource
     * @return Returns the Result set of querying the database to update a new
     * entity
     */
    public ResultSet queryUpdate(JSONObject obj, String resource);

    /**
     *
     * @param resource
     * @return Returns the Result set of querying the database to delete an
     * entity by its id
     */
    public ResultSet queryDelete(String resource);

    /**
     *
     * @param filename
     * @return returns a connection to the database we want to read/write to by
     * reading the database details from a file. Parsing the file is left to the
     * user.
     */
    public Connection connect(String filename);

}
