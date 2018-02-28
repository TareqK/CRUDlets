package endpoints;


import entities.User;
import com.tna.Endpoints.AuthorisationEndpoint;
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
@WebServlet("/auth/*")
public class Authorisation extends AuthorisationEndpoint{

    @Override
    public JSONObject login(JSONObject obj) throws com.tna.Utils.Authorisation.UnauthorisedException {
     return new User().login(obj);
    }



    
}
