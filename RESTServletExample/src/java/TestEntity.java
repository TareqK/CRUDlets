/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.tna.RESTServlet.Entity;
import java.sql.Connection;
import java.sql.ResultSet;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
public class TestEntity extends Entity {
    
    public TestEntity(){
        
    }
    
    
    @Override
    public JSONObject list() {
        JSONObject obj = new JSONObject();
        obj.put("you tried to ","list");
        return obj;
    }

    
    @Override
    public JSONObject create(JSONObject obj) {
    return null;   
    }

    @Override
    public JSONObject update(JSONObject obj, String query) {
    return null;    
    }

    @Override
    public JSONObject read(String query) {
        JSONObject obj = new JSONObject();
        obj.put("you tried to read",query);
        return obj;    
    }

    @Override
    public JSONObject delete(String query) {
        JSONObject obj = new JSONObject();
        obj.put("you tried to delete",query);
        return obj;     
    }


    
}
