/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tna.test;

import com.tna.framework.RestServlet;
import javax.servlet.annotation.WebServlet;
import org.json.simple.JSONObject;
/**
 *
 * @author tareq
 */

@WebServlet(urlPatterns = {"/Test/*"})
public class Test extends RestServlet {

    @Override
    public JSONObject doList() {
        JSONObject obj = new JSONObject();//To change body of generated methods, choose Tools | Templates.
        obj.put("Hello", "There");
        return obj;
    }

    @Override
    public JSONObject doCreate(JSONObject obj) {
        JSONObject obj2 = new JSONObject();//To change body of generated methods, choose Tools | Templates.
        obj2.put("General", "Kebobi");
        return obj2;
    }

    @Override
    public JSONObject doUpdate(JSONObject obj, String query) {
        JSONObject obj2 = new JSONObject();//To change body of generated methods, choose Tools | Templates.
        obj2.put("You wanted to Update ", query);
        return obj2;
    }

    @Override
    public JSONObject doRead(String query) {
        JSONObject obj2 = new JSONObject();//To change body of generated methods, choose Tools | Templates.
        obj2.put("You wanted to Read ", query);
        return obj2;
    }

    @Override
    public JSONObject doDelete(String query) {
        JSONObject obj2 = new JSONObject();//To change body of generated methods, choose Tools | Templates.
        obj2.put("You wanted to Delete ", query);
        return obj2;
    }
    
}
