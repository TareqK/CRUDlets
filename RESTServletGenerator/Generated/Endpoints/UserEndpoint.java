package Endpoints;


import Entities.User;
import Entities.User;
import com.tna.Endpoints.AuthorisedEndpoint;
import com.tna.Utils.Authorisation;
import javax.servlet.annotation.WebServlet;
import org.json.simple.JSONObject;

@WebServlet("/user/*")
public class userName extends AuthorisedEndpoint{

    @Override
    public JSONObject doList(JSONObject obj) throws Authorisation.UnauthorisedException {
       new User().auth(obj, 2);
       return new User().list();
    }

    @Override
    public JSONObject doCreate(JSONObject json) throws Authorisation.UnauthorisedException {
        new User().auth(json, -1);
	    if(json.containsKey("level")){
            throw new Authorisation.UnauthorisedException();
        }
        json.put("level",1)//the default user level is one
	
        return new User().create(json); 
    }

    @Override
    public JSONObject doUpdate(JSONObject json, int resource) throws Authorisation.UnauthorisedException {
        new User().auth(json, 1);
        return new User().update(json,resource);     }

    @Override
    public JSONObject doRead(JSONObject json, int resource) throws Authorisation.UnauthorisedException {
       new User().auth(json, 2);
       return new User().read(resource);    
    }

    @Override
    public JSONObject doDelete(JSONObject json, int resource) throws Authorisation.UnauthorisedException {
       new User().auth(json, 2);
       return new TestEntity().delete(resource); 
    }
