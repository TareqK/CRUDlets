/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.tna.Entities.Entity;
import com.tna.DataAccess.ObjectPersistence;
import com.tna.DataAccess.Persistence;
import com.tna.Utils.JSON;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
public class TestEntity extends Entity {

    @Override
    public JSONObject list() throws SQLException {
       return Persistence.list(this);
    }

    @Override
    public JSONObject create(JSONObject obj) throws SQLException {
       JSON.JSONtoObject(this,obj);
       return Persistence.create(this);
    }

    @Override
    public JSONObject update(JSONObject obj, int resource) throws SQLException {
        JSON.JSONtoObject(this,obj);
        return Persistence.update(this,resource);
    }

    @Override
    public JSONObject read(int resource) throws SQLException {
         return Persistence.read(this,resource);
    }

    @Override
    public JSONObject delete(int resource) throws SQLException {
       return Persistence.delete(this,resource);
    }
 
    
}
