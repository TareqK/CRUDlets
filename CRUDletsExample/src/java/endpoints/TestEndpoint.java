package endpoints;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.tna.data.Persistence;
import com.tna.endpoints.BasicEndpoint;
import entities.TestEntity;
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
       return Persistence.list(TestEntity.class);
    }

    @Override
    public JSONObject doCreate(JSONObject obj) {
       return  Persistence.create(TestEntity.class,obj);
    }
    
    

    @Override
    public JSONObject doUpdate(JSONObject obj, int resource) {
       return Persistence.update(TestEntity.class,resource,obj);
    }

    @Override
    public JSONObject doRead(int resource) {
        return Persistence.read(TestEntity.class, resource);
    }

    @Override
    public JSONObject doDelete(int resource) {
        return Persistence.delete(TestEntity.class, resource);
    }



}
