package endpoints;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import entities.TestEntity;
import com.tna.Endpoints.Endpoint;
import javax.servlet.annotation.WebServlet;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
@WebServlet("/test/*")
public class TestEndpoint extends Endpoint {

    @Override
    public JSONObject doList() {
            return new TestEntity().list();

    }

    @Override
    public JSONObject doCreate(JSONObject obj) {
            return new TestEntity().create(obj);
 
    }

    @Override
    public JSONObject doUpdate(JSONObject obj, int resource) {
            return new TestEntity().update(obj, resource);
 
    }

    @Override
    public JSONObject doRead(int resource) {
            return new TestEntity().read(resource);
     
            

    }

    @Override
    public JSONObject doDelete(int resource) {
            return new TestEntity().delete(resource);

    }

}
