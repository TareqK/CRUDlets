package Endpoints;


import Entities.User;
import Entities.Test;
import com.tna.Endpoints.AuthorisedEndpoint;
import com.tna.Utils.Authorisation;
import javax.servlet.annotation.WebServlet;
import org.json.simple.JSONObject;

@WebServlet("/test/*")
public class testName extends AuthorisedEndpoint{

    @Override
    public JSONObject doList(JSONObject obj) throws Authorisation.UnauthorisedException {
       new User().auth(obj, 1);
       return new Test().list();
    }

    @Override
    public JSONObject doCreate(JSONObject json) throws Authorisation.UnauthorisedException {
        new User().auth(json, 2);
        return new Test().create(json); 
    }

    @Override
    public JSONObject doUpdate(JSONObject json, int resource) throws Authorisation.UnauthorisedException {
        new User().auth(json, 2);
        return new Test().update(json,resource);     }

    @Override
    public JSONObject doRead(JSONObject json, int resource) throws Authorisation.UnauthorisedException {
       new User().auth(json, 1);
       return new Test().read(resource);    
    }

    @Override
    public JSONObject doDelete(JSONObject json, int resource) throws Authorisation.UnauthorisedException {
       new User().auth(json, 2);
       return new TestEntity().delete(resource); 
    }
