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

/**
 * REST Web Service
 *http://localhost:8080/JavaWebActivity/webresources/path/listDB
 * @author Ravilion
 */
@Path("path")
public class pathResource {

  
    @GET
    @Path("listDB")
    @Produces(MediaType.APPLICATION_JSON)
    public String getText() throws ClassNotFoundException, SQLException {
      

      Class.forName("oracle.jdbc.OracleDriver");
      Connection con=DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE","hr", "inf5180");  
      Statement stm= con.createStatement();
   
      String sql="select * from departments ";

      ResultSet rs = stm.executeQuery(sql);
      
      String dep_name = null;
      int dep_id=0, manager_id, location_id;
      String result ="\n";
      
      while(rs.next()){
                dep_id=rs.getInt("department_id");
                manager_id=rs.getInt("manager_id");
                location_id=rs.getInt("location_id");
                dep_name=rs.getString("department_name");
         
        result += dep_id+" "+dep_name+" " +manager_id+" " +location_id + "\n";
       }

     return result;
}
    
    
    
}
