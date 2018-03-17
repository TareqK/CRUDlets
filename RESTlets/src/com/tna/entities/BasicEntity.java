/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.entities;

import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 *
 */

public abstract class BasicEntity{
    public long id;
    /**
     *
     * Lists all entities of this type in the data source.
     * @return returns a list of all entries.
     */
    public abstract JSONObject list();

    /**
     * Creates a new entry in the data source.
     * @param obj
     * @return returns a JSONObject with a success message if it works, null otherwise.
     */
    public abstract JSONObject create(JSONObject obj);

    /**
     * Updates an entity in the data source.
     * @param obj
     * @param resource
     * @return returns a JSONObject with a success message if it works, null otherwise.
     */
    public abstract JSONObject update(JSONObject obj, int resource);

    /**
     * Reads/Fetches an entity from the data source.
     * @param resource
     * @return returns a JSONObject with the entity details if it works, null otherwise.
     */
    public abstract JSONObject read(int resource);

    /**
     * Deletes an entity from the data source. 
     * @param resource
     * @return returns a JSONObject with a success message if it works, null otherwise.

     */
    public abstract JSONObject delete(int resource);
}
