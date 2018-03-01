package entities;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.tna.Entities.Entity;
import com.tna.DataAccess.Persistence;
import com.tna.Utils.JSON;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */

    
public class TestEntity extends Entity {

    public long id2;

    @Override
    public JSONObject list(){
       return Persistence.list(this);
    }

    @Override
    public JSONObject create(JSONObject obj){
       JSON.JSONtoObject(this,obj);
       return Persistence.create(this);
    }

    @Override
    public JSONObject update(JSONObject obj, int resource){
        JSON.JSONtoObject(this,obj);
        return Persistence.update(this,resource);
    }

    @Override
    public JSONObject read(int resource){
         return Persistence.read(this,resource);
    }

    @Override
    public JSONObject delete(int resource){
       return Persistence.delete(this,resource);
    }
 
    
}
