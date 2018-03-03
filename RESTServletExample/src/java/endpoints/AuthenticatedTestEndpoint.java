package endpoints;


import com.tna.DataAccess.AuthorisationPersistence;
import com.tna.DataAccess.Persistence;
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
       return AuthorisationPersistence.list(TestEntity.class,User.class,obj);
    }

    @Override
    public JSONObject doCreate(JSONObject json) throws Authorisation.UnauthorisedException {
        return new TestEntity().create(json); 
    }

    @Override
    public JSONObject doUpdate(JSONObject json, int resource) throws Authorisation.UnauthorisedException {
        TestEntity te = new TestEntity();
        return te.update(json, resource);
    }

    @Override
    public JSONObject doRead(JSONObject json, int resource) throws Authorisation.UnauthorisedException {
        return AuthorisationPersistence.read(TestEntity.class,User.class,json,resource);  
    }

    @Override
    public JSONObject doDelete(JSONObject json, int resource) throws Authorisation.UnauthorisedException {
        TestEntity te = new TestEntity();
        return te.delete(resource); 
    }
    
}
