/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.tna.RESTServlet.RESTServletEndpoint;
import javax.servlet.annotation.WebServlet;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
@WebServlet("/test/*")
public class TestEndpoint extends RESTServletEndpoint {

    @Override
    public JSONObject doList() {
      return new TestEntity().list();
    }

    @Override
    public JSONObject doCreate(JSONObject obj) {
      return new TestEntity().create(obj);
    }

    @Override
    public JSONObject doUpdate(JSONObject obj, String query) {
        return new TestEntity().update(obj,query);    
    }

    @Override
    public JSONObject doRead(String query) {
        return new TestEntity().read(query);    
    }

    @Override
    public JSONObject doDelete(String query) {
       return new TestEntity().delete(query);    
    }
    
}
