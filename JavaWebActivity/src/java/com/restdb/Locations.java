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

/*
 * @author Pavani
 */

/**
 *
 * @author 1895730
 */
public class Locations {
     @Path("Locations")
public class DepartmentTable {
    
    Connection con= null;
    Statement stm= null;
    PreparedStatement ps = null;
    ResultSet rs= null;
    JSONObject mainObject = new JSONObject();
    JSONArray mainArray = new JSONArray();
    
    long now = Instant.now().toEpochMilli()/ 1000L;


//Display all data in table
 @GET
 @Path("Locations")
 @Produces(MediaType.APPLICATION_JSON)
 public String getText(){
        

    try {
        Class.forName("oracle.jdbc.OracleDriver");
         con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "hr", "inf5180");
         stm = con.createStatement();

        String sql = "select * from locations ";

         rs = stm.executeQuery(sql);

        String street_address = null;
        String postal_code,city, state_province;
        int location_id = 0, country_id;

        while (rs.next()) {
            country_id = rs.getInt("country_id");
            location_id = rs.getInt("location_id");
            street_address = rs.getString("street_address");
             postal_code = rs.getString("postal_code");
              city = rs.getString("city");
               state_province = rs.getString("state_province");
            
            mainObject.accumulate("Country ID", country_id);
            mainObject.accumulate("Location ID", location_id);
            mainObject.accumulate("Street address", street_address);
            mainObject.accumulate("Postal code", postal_code);
            mainObject.accumulate("City", city);
            mainObject.accumulate("State Province", state_province);
            mainObject.accumulate("Status","OK");
            mainObject.accumulate("Timestamp",now);
            mainArray.add(mainObject);
            mainObject.clear();
            
        }
    } catch (SQLException | ClassNotFoundException ex) {
        Logger.getLogger(Locations.class.getName()).log(Level.SEVERE, null, ex);
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
//http://localhost:8080/JavaWebActivity/webresources/path/insertDB&1091&MAD&100&1000 
 @GET
 @Path("insertDB&{LID}&{Staddress}&{Pcode}&{City}&{StProvince}&{CID}")
 @Produces(MediaType.APPLICATION_JSON)
 public String getText1(@PathParam("LID") int LID, @PathParam("Staddress") String Staddress, @PathParam("Pcode") String Pcode, @PathParam("City") String City, @PathParam("StProvince") String StProvince,@PathParam("CID") int CID) throws SQLException, ClassNotFoundException{

       try {
            Class.forName("oracle.jdbc.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "hr", "inf5180");
            
            
            String sql = "INSERT INTO locations(location_id,street_address, postal_code, city, state_province, country_id) values (?, ?, ?, ?, ?, ?)";
            ps = con.prepareStatement(sql);
            
            ps.setInt(1,LID);
            ps.setString(2,Staddress);
            ps.setString(3,Pcode);
            ps.setString(4,City);
             ps.setString(5,StProvince);
              ps.setInt(6,CID);
            
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

 
 //Display single List of Departments
 @GET
 @Path("singleDB&{LID}")
 @Produces(MediaType.APPLICATION_JSON)
 public String getText2(@PathParam("LID") int LID){

    try {
        Class.forName("oracle.jdbc.OracleDriver");
        con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "hr", "inf5180");
        
        String singlelist = "select * from locations where location_id= '"+LID+"'";
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
            int location_id,country_id;
            String street_address,postal_code,city,state_province;
            
            location_id = rs.getInt("department_id");
            street_address = rs.getString("street_address");
            postal_code = rs.getString("postal_code");
            city = rs.getString("city");
            state_province = rs.getString("state_province");
            country_id = rs.getInt("country_id");
            
            
            mainObject.accumulate("Location Id", location_id);
            mainObject.accumulate("street_address",street_address);
            mainObject.accumulate("Postal_code", postal_code);
            mainObject.accumulate("City", city);
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

 
 //UPDATE single row of Departments
 //http://localhost:8080/JavaWebActivity/webresources/path/updateDB&1090&CAD
 @GET
 @Path("updateDB&{LID}&{PostalCode}")
 @Produces(MediaType.APPLICATION_JSON)
 public String getText3(@PathParam("LID") int LID,@PathParam("PostalCode") String PostalCode){

    try {
        Class.forName("oracle.jdbc.OracleDriver");
        con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "hr", "inf5180");
        
       
        String updatelist = "update locations set PostalCode= ? where LID=?";
        
        
        ps = con.prepareStatement(updatelist);
        ps.setInt(2, LID);
        ps.setString(1,PostalCode);
        
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
 //http://localhost:8080/JavaWebActivity/webresources/path/deleteDB&1090
 @GET
 @Path("deleteDB&{LID}")
 @Produces(MediaType.APPLICATION_JSON)
 public String getText4(@PathParam("LID") int LID){

    try {
        Class.forName("oracle.jdbc.OracleDriver");
        con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "hr", "inf5180");
        
        String deletelist = "delete from departments where location_id = ?";
       
        
        ps = con.prepareStatement(deletelist);
        ps.setInt(1,LID);
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
}