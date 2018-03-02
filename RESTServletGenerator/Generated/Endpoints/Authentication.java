package Endpoints;


import Entities.User;
import com.tna.Endpoints.AuthorisationEndpoint;
import javax.servlet.annotation.WebServlet;
import org.json.simple.JSONObject;

@WebServlet("/auth/*")
public class Authentication extends AuthorisationEndpoint{

    @Override
    public JSONObject login(JSONObject obj) throws com.tna.Utils.Authorisation.UnauthorisedException {
     return new User().login(obj);
    }



    
}
