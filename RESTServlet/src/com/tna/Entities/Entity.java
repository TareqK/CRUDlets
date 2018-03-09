/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.Entities;

import java.io.Serializable;
import java.sql.SQLException;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 *
 */

public abstract class Entity{
    public long id;
    /**
     *
     *
     * @return returns a list of all entries
     * @throws java.sql.SQLException
     */
    public abstract JSONObject list();

    /**
     *
     * @param obj
     * @return Creates a new entry in the data source. Should return a success
     * code in JSON format.
     * @throws java.sql.SQLException
     */
    public abstract JSONObject create(JSONObject obj);

    /**
     *
     * @param obj
     * @param resource
     * @return Updates an entity in the data source. Should return a success
     * code in JSON format.
     * @throws java.sql.SQLException
     */
    public abstract JSONObject update(JSONObject obj, int resource);

    /**
     *
     * @param resource
     * @return Reads/Fetches an entity from the data source. Should return the
     * entity details in JSON format.
     * @throws java.sql.SQLException
     */
    public abstract JSONObject read(int resource);

    /**
     *
     * @param resource
     * @return Deletes an entity from the data source. Should return a success
     * code in JSON format.
     * @throws java.sql.SQLException
     */
    public abstract JSONObject delete(int resource);
}
