package endpoints;


import com.tna.common.Authorisation;
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
public class AuthenticatedTestEndpoint extends AuthorisedEndpoint{

    @Override
    public JSONObject doList(JSONObject obj) throws Authorisation.UnauthorisedException {
        return Persistence.list(TestEntity.class,User.class,obj);
    }

    @Override
    public JSONObject doCreate(JSONObject json) throws Authorisation.UnauthorisedException {
        return Persistence.create(TestEntity.class,User.class,json);
    }

    @Override
    public JSONObject doUpdate(JSONObject json, int resource) throws Authorisation.UnauthorisedException {
        return Persistence.update(TestEntity.class,User.class,json,resource);
    }

    @Override
    public JSONObject doRead(JSONObject json, int resource) throws Authorisation.UnauthorisedException {
        return Persistence.read(TestEntity.class,User.class,json,resource);
    }

    @Override
    public JSONObject doDelete(JSONObject json, int resource) throws Authorisation.UnauthorisedException {
        return Persistence.delete(TestEntity.class,User.class,json,resource);
    }

}
