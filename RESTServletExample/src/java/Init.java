
import com.tna.RESTServlet.DBAccess;
import com.tna.RESTServlet.ServiceInitializer;

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
        DBAccess.host = "stuff";
        DBAccess.database = "tayseer";
        DBAccess.username = "mememe";
        DBAccess.password = "Hello its me";
        DBAccess.connect();
    }

    @Override
    public void onDestroy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
