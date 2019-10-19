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
   
}
