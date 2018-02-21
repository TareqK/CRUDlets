/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.RESTServlet;

import java.io.Serializable;
import org.json.simple.JSONObject;
import javax.servlet.annotation.WebServlet;

/**
 *
 * @author tareq
 *
 */

public abstract class Entity {

    /**
     *
     *
     * @return returns a list of all entries
     */
    public abstract JSONObject list();

    /**
     *
     * @param obj
     * @return Creates a new entry in the data source. Should return a success
     * code in JSON format.
     */
    public abstract JSONObject create(JSONObject obj);

    /**
     *
     * @param obj
     * @param resource
     * @return Updates an entity in the data source. Should return a success
     * code in JSON format.
     */
    public abstract JSONObject update(JSONObject obj, String resource);

    /**
     *
     * @param resource
     * @return Reads/Fetches an entity from the data source. Should return the
     * entity details in JSON fomat.
     */
    public abstract JSONObject read(String resource);

    /**
     *
     * @param resource
     * @return Deletes an entity from the data source. Should return a success
     * code in JSON format.
     */
    public abstract JSONObject delete(String resource);
}
