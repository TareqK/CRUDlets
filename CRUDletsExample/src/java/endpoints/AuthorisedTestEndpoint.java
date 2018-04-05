package endpoints;

import com.tna.common.AccessError;
import com.tna.common.UserAccessControl;
import com.tna.data.Persistence;
import com.tna.endpoints.AuthorisedEndpoint;
import entities.User;
import entities.TestEntity;
import javax.servlet.annotation.WebServlet;
import org.json.simple.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author tareq
 */
@WebServlet("/testauth/*")
public class AuthorisedTestEndpoint extends AuthorisedEndpoint {

    @Override
    public JSONObject doList(String token) throws AccessError {
        return Persistence.list(TestEntity.class, User.class, token);
    }

    @Override
    public JSONObject doCreate(JSONObject json, String token) throws AccessError {
        return Persistence.create(TestEntity.class, User.class, json, token);
    }

    @Override
    public JSONObject doUpdate(JSONObject json, int resource, String token) throws AccessError {
        return Persistence.update(TestEntity.class, User.class, json, resource, token);
    }

    @Override
    public JSONObject doRead(int resource, String token) throws AccessError {
        return Persistence.read(TestEntity.class, User.class, resource, token);
    }

    @Override
    public JSONObject doDelete(int resource, String token) throws AccessError {
        return Persistence.delete(TestEntity.class, User.class, resource, token);
    }

}
