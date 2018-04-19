package endpoints;


import com.tna.common.AccessError;
import entities.User;
import com.tna.endpoints.AuthenticationEndpoint;
import com.tna.common.UserAccessControl;
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
public class Authentication extends AuthenticationEndpoint{

    @Override
    public JSONObject login(JSONObject obj) throws AccessError {
        return UserAccessControl.login(User.class,obj);
    }




    
}
