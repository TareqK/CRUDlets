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
public class ObjectCache<Key,Value>{

    private HashMap<Key,Value> cache;
    private Timestamp timeStamp;
    
    public ObjectCache(){
        this.cache = new HashMap();
        this.timeStamp = new Timestamp(0);
    }
    public synchronized void cache(Key key, Value value){
        this.cache.put(key,value);
    }
    
    public synchronized Value retreive(Key key){
       return (Value)cache.get(key);
    }
    
    public synchronized Timestamp getTimeStamp(){
        return this.timeStamp;
    }
    
    public synchronized void setTimeStamp(Timestamp timeStamp){
        this.timeStamp = timeStamp;
    }

    
    
}
