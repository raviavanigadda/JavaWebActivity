/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *http://localhost:8080/JavaWebActivity/webresources/path/listDB
 * @author Ravilion
 */
@Path("path")
public class DepartmentTable {
long now = Instant.now().toEpochMilli()/ 1000L;

 @GET
 @Path("listDB")
 @Produces(MediaType.APPLICATION_JSON)
 public String getText(){

        Connection con= null;
        Statement stm= null;
        ResultSet rs= null;
        JSONObject mainObject = new JSONObject();
        JSONArray mainArray = new JSONArray();

    try {
        Class.forName("oracle.jdbc.OracleDriver");
         con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "hr", "inf5180");
         stm = con.createStatement();

        String sql = "select * from departments ";

         rs = stm.executeQuery(sql);

        String dep_name = null;
        int dep_id = 0, manager_id, location_id;

        while (rs.next()) {
            dep_id = rs.getInt("department_id");
            manager_id = rs.getInt("manager_id");
            location_id = rs.getInt("location_id");
            dep_name = rs.getString("department_name");
            
            mainObject.accumulate("Department ID", dep_id);
            mainObject.accumulate("Manager ID", manager_id);
            mainObject.accumulate("Location ID", location_id);
            mainObject.accumulate("Department name", dep_name);
            mainObject.accumulate("Status","OK");
            mainObject.accumulate("Timestamp",now);
            mainArray.add(mainObject);
            mainObject.clear();
            
        }
    } catch (SQLException | ClassNotFoundException ex) {
        Logger.getLogger(DepartmentTable.class.getName()).log(Level.SEVERE, null, ex);
    }
    finally {
            closeDBConnection(rs, stm, con);
            }
        return mainArray.toString();
    }

    private void closeDBConnection(ResultSet rs, Statement stm, Connection con) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
            }
        }
        if (stm != null) {
            try {
                stm.close();
            } catch (SQLException e) {
                
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                
            }
        }
    }

//http://localhost:8080/JavaWebActivity/webresources/path/insertDB&1000&MAD&1001&1002 
 @GET
 @Path("insertDB&{dID}&{dName}&{mID}&{lID}")
 @Produces(MediaType.APPLICATION_JSON)
 public String getText1(@PathParam("dID") int depID, @PathParam("dName") String depName, @PathParam("mID") int magID, @PathParam("lID") int locID){

        Connection con= null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        JSONObject mainObject = new JSONObject();
   
    try {
        Class.forName("oracle.jdbc.OracleDriver");
        con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "hr", "inf5180");
        
      /*  String checksql = "select * from departments where department_id= '"+depID+"'";
        ps = con.prepareStatement(checksql);
        rs = ps.executeQuery();
        
      if(!rs.next()){
            mainObject.accumulate("Message","Insertion Failed!");
            mainObject.accumulate("Status","ERROR");
            mainObject.accumulate("Timestamp",now);
        }
      else {
          do{*/
            String sql = "insert into departments values(?,?,?,?)";
            ps = con.prepareStatement(sql);
            
            ps.setInt(1,depID);
            ps.setString(2,depName);
            ps.setInt(3, magID);
            ps.setInt(4, locID);
            
            ps.executeUpdate();
        
            mainObject.accumulate("Message","Data Insertion Sucessful");
            mainObject.accumulate("Status","OK");
            mainObject.accumulate("Timestamp",now);
           
     } //while(rs.next());
          
        /* else
         {
                    mainObject.accumulate("Message","Insertion Failed!");
                    mainObject.accumulate("Status","ERROR");
                    mainObject.accumulate("Timestamp",now);
                    mainArray.add(mainObject);
                    mainObject.clear();
         }
        */
 
   
    catch (SQLException | ClassNotFoundException ex) {
        Logger.getLogger(DepartmentTable.class.getName()).log(Level.SEVERE, null, ex);
    }
    finally {
            closeDBConnection(rs, ps, con);
            }
        return mainObject.toString();
    }

 
 //Single List
 //
 @GET
 @Path("singleDB&{dID}")
 @Produces(MediaType.APPLICATION_JSON)
 public String getText2(@PathParam("dID") int depID){

        Connection con= null;
        PreparedStatement stm= null;
        ResultSet rs= null;
        JSONObject mainObject = new JSONObject();
        JSONArray mainArray = new JSONArray();

    try {
        Class.forName("oracle.jdbc.OracleDriver");
        con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "hr", "inf5180");
        
        String singlelist = "select * from departments where department_id= '"+depID+"'";
        stm = con.prepareStatement(singlelist);
        rs = stm.executeQuery();
        
        if(!rs.next()){
            mainObject.accumulate("Message","Data not Found!");
            mainObject.accumulate("Status","ERROR");
            mainObject.accumulate("Timestamp",now);
        }
        else
        {
        
    do
        {
            int dep_id, manager_id, location_id;
            String dep_name;
            
            dep_id = rs.getInt("department_id");
            manager_id = rs.getInt("manager_id");
            location_id = rs.getInt("location_id");
            dep_name = rs.getString("department_name");
            
            mainObject.accumulate("Department ID", dep_id);
            mainObject.accumulate("Manager ID", manager_id);
            mainObject.accumulate("Location ID", location_id);
            mainObject.accumulate("Department name", dep_name);
            mainObject.accumulate("Status","OK");
            mainObject.accumulate("Timestamp",now);
        
            } while (rs.next());
        }
  
    } 
    
    catch (SQLException | ClassNotFoundException ex) {
        Logger.getLogger(DepartmentTable.class.getName()).log(Level.SEVERE, null, ex);
    }
    finally {
            closeDBConnection(rs, stm, con);
            }
        return mainObject.toString();
    }

}