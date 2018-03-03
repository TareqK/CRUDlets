package entities;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.tna.Entities.Entity;
import com.tna.DataAccess.Persistence;
import com.tna.Entities.AuthenticatedEntity;
import com.tna.Utils.JSON;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */

    
public class TestEntity extends AuthenticatedEntity {

    public long id2;

    @Override
    public JSONObject list(){
       return Persistence.list(this);
    }

    @Override
    public JSONObject create(JSONObject obj){
       JSON.JSONtoObject(this, obj);
       return Persistence.create(this.getClass(),obj);
    }

    @Override
    public JSONObject update(JSONObject obj, int resource){
        JSON.JSONtoObject(this,obj);
        return Persistence.update(this.getClass(),resource,obj);
    }

    @Override
    public JSONObject read(int resource){
         return Persistence.read(this.getClass(),resource);
    }

    @Override
    public JSONObject delete(int resource){
       return Persistence.delete(this.getClass(),resource);
    }
 
    
}
