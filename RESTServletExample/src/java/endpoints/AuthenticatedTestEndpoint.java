package endpoints;


import entities.User;
import entities.TestEntity;
import com.tna.Endpoints.AuthorisedEndpoint;
import com.tna.Utils.Authorisation;
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
       new User().auth(obj, 1);
       return new TestEntity().list();
    }

    @Override
    public JSONObject doCreate(JSONObject json) throws Authorisation.UnauthorisedException {
        new User().auth(json, 1);
        return new TestEntity().create(json); 
    }

    @Override
    public JSONObject doUpdate(JSONObject json, int resource) throws Authorisation.UnauthorisedException {
        new User().auth(json, 1);
        return new TestEntity().update(json,resource);     }

    @Override
    public JSONObject doRead(JSONObject json, int resource) throws Authorisation.UnauthorisedException {
       new User().auth(json, 0);
       return new TestEntity().read(resource);    
    }

    @Override
    public JSONObject doDelete(JSONObject json, int resource) throws Authorisation.UnauthorisedException {
       new User().auth(json, 1);
       return new TestEntity().delete(resource); 
    }
    
}
