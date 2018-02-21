/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.tna.RESTServlet.RESTServletEntity;
import com.tna.RESTServlet.RESTServletEntityDataAccess;
import java.sql.Connection;
import java.sql.ResultSet;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
public class TestEntity extends RESTServletEntity implements RESTServletEntityDataAccess{
    
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

    @Override
    public ResultSet queryList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResultSet queryCreate(JSONObject obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResultSet queryRead(String resource) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResultSet queryUpdate(JSONObject obj, String resource) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResultSet queryDelete(String resource) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Connection connect(String filename) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
