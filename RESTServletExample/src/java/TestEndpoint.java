/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.tna.Endpoints.Endpoint;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        try {
            return new TestEntity().list();
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public JSONObject doCreate(JSONObject obj) {
        try {
            return new TestEntity().create(obj);
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public JSONObject doUpdate(JSONObject obj, int resource) {
        try {
            return new TestEntity().update(obj, resource);
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public JSONObject doRead(int resource) {
        try {
            return new TestEntity().read(resource);
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public JSONObject doDelete(int resource) {
        try {
            return new TestEntity().delete(resource);
        } catch (SQLException ex) {
            return null;
        }
    }

}
