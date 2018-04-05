package endpoints;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.tna.common.AccessError;
import com.tna.data.Persistence;
import com.tna.endpoints.BasicEndpoint;
import entities.TestEntity;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
@WebServlet("/test/*")
public class TestEndpoint extends BasicEndpoint {

    @Override
    public JSONObject doList() {
        try {
            return Persistence.list(TestEntity.class);
        } catch (AccessError ex) {
            return null;
        }
    }

    @Override
    public JSONObject doCreate(JSONObject obj) {
        try {
            return  Persistence.create(TestEntity.class,obj);
        } catch (AccessError ex) {
            return null;
        }
    }
    
    

    @Override
    public JSONObject doUpdate(JSONObject obj, int resource) {
        try {
            return Persistence.update(TestEntity.class,resource,obj);
        } catch (AccessError ex) {
            return null;
        }
    }

    @Override
    public JSONObject doRead(int resource) {
        try {
            return Persistence.read(TestEntity.class, resource);
        } catch (AccessError ex) {
            return null;
        }
    }

    @Override
    public JSONObject doDelete(int resource) {
        try {
            return Persistence.delete(TestEntity.class, resource);
        } catch (AccessError ex) {
            return null;
        }
    }



}
