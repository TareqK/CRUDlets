/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.common;

import java.sql.Timestamp;
import java.util.HashMap;

/**
 * A simple object cache based on a hashmap.
 * @author tareq
 * @param <O> The Type of object we want to cache.
 */
public class ObjectCache<O>{

    private HashMap<Object,O> cache;
    private Timestamp timeStamp;
    
    public synchronized void cache(Object key, O cached){
        cache.put(key,cached);
    }
    
    public synchronized O retreive(Object key){
       return (O)cache.get(key);
    }
    
    public synchronized Timestamp getTimeStamp(){
        return this.timeStamp;
    }
    
    public synchronized void setTimeStamp(Timestamp timeStamp){
        this.timeStamp = timeStamp;
    }

    
    
}
