/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * REST Web Service
 *http://localhost:8080/restfulDatabase/webresources/path/mainDB
 * @author Ravilion
 */
@Path("path")
public class pathResource {

  
    @GET
    @Path("mainDB")
    @Produces(MediaType.TEXT_PLAIN)
    public String getText() throws ClassNotFoundException, SQLException {
      

      Class.forName("oracle.jdbc.OracleDriver");
      Connection con=DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE","hr", "inf5180");  
      Statement stm= con.createStatement();
   
      String sql="select departments.department_id, "
                + "departments.department_name, count(*) numberEmployee "
                + "from employees join departments "
                + "on employees.department_id=departments.department_id "
                + "group by departments.department_id, "
                + "departments.department_name "
                + "order by numberEmployee ";

      ResultSet rs = stm.executeQuery(sql);
      
      String name = null;
      int id = 0,number = 0;
      
      while(rs.next()){
                id=rs.getInt("department_id");
                name=rs.getString("department_name");
                number=rs.getInt("numberEmployee");
        
       }

     return id+" "+name+" "+ number;
}
    
}
