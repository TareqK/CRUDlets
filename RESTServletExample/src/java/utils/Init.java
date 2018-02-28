package utils;


import com.tna.DataAccess.Access;
import com.tna.Utils.ServiceInitializer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author tareq
 */
public class Init extends ServiceInitializer{

    @Override
    public void onInit() {
        Access.access.host = "localhost:3306";
        Access.access.database = "api_test";
        Access.access.username = "api_user";
        Access.access.password = "pass1234";
  
    }

    @Override
    public void onDestroy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
