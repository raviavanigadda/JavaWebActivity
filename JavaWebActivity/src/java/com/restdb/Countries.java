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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * @author 1895126
 */
@Path("countries")
public class Countries {
    
    Connection con= null;
    Statement stm= null;
    PreparedStatement ps = null;
    ResultSet rs= null;
    JSONObject mainObject = new JSONObject();
    JSONArray mainArray = new JSONArray();
    
        long now = Instant.now().toEpochMilli()/ 1000L;


//Display all data in table
//http://localhost:8080/JavaWebActivity/webresources/countries/showcountries
 @GET
 @Path("showcountries")
 @Produces(MediaType.APPLICATION_JSON)
 public String getText(){
        

    try {
        Class.forName("oracle.jdbc.OracleDriver");
         con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "hr", "inf5180");
         stm = con.createStatement();

        String sql = "select * from countries";

         rs = stm.executeQuery(sql);

        String country_name;
        int c_id, r_id;

        while (rs.next()) {
            c_id = rs.getInt("country_id");
            r_id = rs.getInt("region_id");
            country_name = rs.getString("country_name");
            
            mainObject.accumulate("Country ID", c_id);
            mainObject.accumulate("Region ID", r_id);
            mainObject.accumulate("Country name", country_name);
            mainObject.accumulate("Status","OK");
            mainObject.accumulate("Timestamp",now);
            mainArray.add(mainObject);
          //  mainObject.clear();
            
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
   
    //Insert into database
 @GET
 @Path("insertDB&{cID}&{cName}&{rID}")
 @Produces(MediaType.APPLICATION_JSON)
 public String getText1(@PathParam("cID") int CID, @PathParam("cName") String CName, @PathParam("rID") int rID) throws SQLException, ClassNotFoundException{

       try {
            Class.forName("oracle.jdbc.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "hr", "inf5180");
            
            
            String sql = "INSERT INTO countries(country_id,country_name,region_id) values (?, ?, ?)";
            ps = con.prepareStatement(sql);
            
            ps.setInt(1,CID);
            ps.setString(2,CName);
            ps.setInt(3,rID);
            
            int flag = ps.executeUpdate();

           
            if(flag==1)
            {
                
                mainObject.accumulate("Message","Data Insertion Sucessful");
                mainObject.accumulate("Status","OK");
                mainObject.accumulate("Timestamp",now);
                
            }
   
        } catch (SQLException ex) {
            mainObject.accumulate("Message","Data not Inserted");
            mainObject.accumulate("Status","ERROR");
            mainObject.accumulate("Timestamp",now);
                
            Logger.getLogger(DepartmentTable.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (ClassNotFoundException ex) { 
            Logger.getLogger(DepartmentTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return mainObject.toString();
    
 }

 //Display single List of countries
 @GET
 @Path("singleDB&{cID}")
 @Produces(MediaType.APPLICATION_JSON)
 public String getText2(@PathParam("cID") int cID){

    try {
        Class.forName("oracle.jdbc.OracleDriver");
        con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "hr", "inf5180");
        
        String singlelist = "select * from countries where country_id= '"+cID+"'";
        ps = con.prepareStatement(singlelist);
        rs = ps.executeQuery();
        
        if(!rs.next()){
            mainObject.accumulate("Message","Data not Found!");
            mainObject.accumulate("Status","ERROR");
            mainObject.accumulate("Timestamp",now);
        }
        else
        {
        
    do
        {
            int c_id, r_id;
            String c_name;
            
            c_id = rs.getInt("country_id");
            r_id = rs.getInt("region_id");
            c_name = rs.getString("country_name");
            
            mainObject.accumulate("Country ID", c_id);
            mainObject.accumulate("Region ID", r_id);
            mainObject.accumulate("Country name", c_name);
            mainObject.accumulate("Status","OK");
            mainObject.accumulate("Timestamp",now);
        
            } while (rs.next());
        }
  
    } 
    
    catch (SQLException | ClassNotFoundException ex) {
        Logger.getLogger(DepartmentTable.class.getName()).log(Level.SEVERE, null, ex);
    }
    finally {
            closeDBConnection(rs, ps, con);
            }
        return mainObject.toString();
    }

    //UPDATE single row of countries
 @GET
 @Path("updateDB&{cID}&{cName}")
 @Produces(MediaType.APPLICATION_JSON)
 public String getText3(@PathParam("cID") int cID,@PathParam("cName") String cName){

    try {
        Class.forName("oracle.jdbc.OracleDriver");
        con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "hr", "inf5180");
        
       
        String updatelist = "update countries set country_name= ? where country_id=?";
        
        
        ps = con.prepareStatement(updatelist);
        ps.setInt(2, cID);
        ps.setString(1,cName);
        
        int flag = ps.executeUpdate();
    
        if(flag!=1){
            mainObject.accumulate("Message","Data not Found!");
            mainObject.accumulate("Status","ERROR");
            mainObject.accumulate("Timestamp",now);
        }
        else
        {
       
            mainObject.accumulate("Message", "Data updated Sucessfully!");
            mainObject.accumulate("Status","OK");
            mainObject.accumulate("Timestamp",now);
  
        }
  
    } 
    
    catch (SQLException | ClassNotFoundException ex) {
            mainObject.accumulate("Message","Data not Found!");
            mainObject.accumulate("Status","ERROR");
            mainObject.accumulate("Timestamp",now);
        Logger.getLogger(DepartmentTable.class.getName()).log(Level.SEVERE, null, ex);
    }
    finally {
            closeDBConnection(rs, ps, con);
            }
        return mainObject.toString();
    }
 
  //Delete single List of Departments
 @GET
 @Path("deleteDB&{cID}")
 @Produces(MediaType.APPLICATION_JSON)
 public String getText4(@PathParam("cID") int cID){

    try {
        Class.forName("oracle.jdbc.OracleDriver");
        con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "hr", "inf5180");
        
        String deletelist = "delete from countries where country_id = ?";
       
        
        ps = con.prepareStatement(deletelist);
        ps.setInt(1,cID);
        int flag = ps.executeUpdate();
        
        
        if(flag==1){
            mainObject.accumulate("Message","Data Deleted Sucessfully!");
            mainObject.accumulate("Status","ERROR");
            mainObject.accumulate("Timestamp",now);
        }
       
  
    } 
    
    catch (SQLException | ClassNotFoundException ex) {
            mainObject.accumulate("Message", "Data not Deleted!");
            mainObject.accumulate("Status","Error!");
            mainObject.accumulate("Timestamp",now);
        Logger.getLogger(DepartmentTable.class.getName()).log(Level.SEVERE, null, ex);
    }
    finally {
            closeDBConnection(rs, ps, con);
            }
        return mainObject.toString();
    }
}
