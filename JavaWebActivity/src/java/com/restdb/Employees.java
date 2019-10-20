/**
 *
 * @author Shriya
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import net.sf.json.JSONObject;


@Path("Employee")
public class Employees {

    @Context
    private UriInfo context;


    public Employees() {
    }


    @GET
    @Produces("application/json")
    @Path("selectAll")
    public String getJson() throws SQLException {

        long time = Instant.now().getEpochSecond();
      
        JSONObject mainObject = new JSONObject();

        try {
            Class.forName("oracle.jdbc.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE","hr", "inf5180");
            String sql = "select * from employees";
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) 
	     {

                String emp_id = rs.getString(1);
                String fname = rs.getString(2);
                String lname = rs.getString(3);
                String email = rs.getString(4);
                String phone = rs.getString(5);
                String hdate = rs.getString(6);
                String jobid = rs.getString(7);
                String salary = rs.getString(8);
                String comission = rs.getString(9);
                String mgid = rs.getString(10);
                String deptid = rs.getString(11);

                mainObject.accumulate("Status:", "OK");
                mainObject.accumulate("Timestamp:", time);
                mainObject.accumulate("id",emp_id);
                mainObject.accumulate("fname",fname);
                mainObject.accumulate("lname",lname);
                mainObject.accumulate("email",email);
                mainObject.accumulate("phone",phone);
                mainObject.accumulate("Hire Date",hdate);
                mainObject.accumulate("job id",jobid);
                mainObject.accumulate("Salary",salary);
                mainObject.accumulate("Comission",comission);
                mainObject.accumulate("manager id",mgid);
                mainObject.accumulate("dept id",deptid);

            }


        } catch (ClassNotFoundException ex) {
           Logger.getLogger(Employees.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
           
            Logger.getLogger(Employees.class.getName()).log(Level.SEVERE, null, ex);
            mainObject.accumulate("Status:", "FAILED");
            mainObject.accumulate("Timestamp:", time);
            mainObject.accumulate("Message:", "Display of all the departments is NOT Successful");
            mainObject.accumulate("ex:", ex);
            System.out.println("catch block");

        }

        return mainObject.toString();
    }

    @GET
    @Produces("application/json")
    @Path("insert&{value1}&{value2}&{value3}&{value4}&{value5}&{value6}&{value7}&{value8}&{value9}&{value10}&{value11}")
    public String empInsert(@PathParam("value1") int id, @PathParam("value2") String fname, @PathParam("value3") String lname, @PathParam("value4") String email,
            @PathParam("value5") String phone,@PathParam("value6") String hdate, @PathParam("value7") String jid, 
            @PathParam("value8") int salary, @PathParam("value9") int comission, 
            @PathParam("value10") int manager, @PathParam("value11") int did) throws SQLException {
        
        long time = Instant.now().getEpochSecond();

        JSONObject mainObject = new JSONObject();

        try {
            Class.forName("oracle.jdbc.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE","hr", "inf5180");
           
            PreparedStatement stm = null;
            String sql = "insert into employees"
                    + " values(?,?,?,?,?,?,?,?,?,?,?) ";
            stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            stm.setString(2, fname);
            stm.setString(3, lname);
            stm.setString(4, email);
            stm.setString(5, phone);
            stm.setString(6, hdate);
            stm.setString(7, jid);
            stm.setInt(8, salary);
            stm.setInt(9, comission);
            stm.setInt(10, manager);
            stm.setInt(11, did);
            
            int rs = stm.executeUpdate();
            
            mainObject.accumulate("Status:", "OK");
            mainObject.accumulate("Timestamp:", time);
            mainObject.accumulate("message", "Succesfully inserted");
            
        }catch (ClassNotFoundException ex) {
            Logger.getLogger(Employees.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Employees.class.getName()).log(Level.SEVERE, null, ex);
            mainObject.accumulate("Status:", "FAILED");
            mainObject.accumulate("Timestamp:", time);
            mainObject.accumulate("Message:", "Insert Unsuccessful!");
            mainObject.accumulate("ex:", ex);
           
        }
    
        return mainObject.toString();
    }

    @GET
    @Produces("application/json")
    @Path("delete&{value1}")
    public String empDelete(@PathParam("value1") int id) throws SQLException {
    
        long time = Instant.now().getEpochSecond();

        JSONObject mainObject = new JSONObject();
        
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE","hr", "inf5180");
           
            PreparedStatement stm = null;
            String sql = "delete from employees where EMPLOYEE_ID=? ";
            stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            int rs = stm.executeUpdate();
            
            mainObject.accumulate("Status:", "OK");
            mainObject.accumulate("Timestamp:", time);
            mainObject.accumulate("message", "Succesfully deleted");
            
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(Employees.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Employees.class.getName()).log(Level.SEVERE, null, ex);
            mainObject.accumulate("Status:", "FAILED");
            mainObject.accumulate("Timestamp:", time);
            mainObject.accumulate("Message:", "delete Unsuccessful!");
            mainObject.accumulate("ex:", ex);
           
        }
    
        return mainObject.toString();
    }
        
    @GET
    @Produces("application/json")
    @Path("update&{id}&{value}")
    public String empUpdate(@PathParam("id") int id,@PathParam("value") String value ) throws SQLException {
    
        long time = Instant.now().getEpochSecond();

        JSONObject mainObject = new JSONObject();
        
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE","hr", "inf5180");
           
            PreparedStatement stm = null;
            String sql = "Update employees set first_name =? where employee_id=?";
            stm = con.prepareStatement(sql);
            stm.setInt(2, id);
            stm.setString(1,value);
            int rs = stm.executeUpdate();
            
            mainObject.accumulate("Status:", "OK");
            mainObject.accumulate("Timestamp:", time);
            mainObject.accumulate("message", "Succesfully Updated");
            
        }
        catch (ClassNotFoundException ex) {
           Logger.getLogger(Employees.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Employees.class.getName()).log(Level.SEVERE, null, ex);
            mainObject.accumulate("Status:", "FAILED");
            mainObject.accumulate("Timestamp:", time);
            mainObject.accumulate("Message:", "Update Unsuccessful!");
            mainObject.accumulate("ex:", ex);
           
        }
    
        return mainObject.toString();
    }


    @GET
    @Produces("application/json")
    @Path("select&{id}")
    public String empSelect(@PathParam("id") int id) throws SQLException {
        
        long time = Instant.now().getEpochSecond();

        JSONObject mainObject = new JSONObject();

        try {
            Class.forName("oracle.jdbc.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE","hr", "inf5180");
            Statement stm = con.createStatement();
            String sql = "select * from employees where employee_id = " + id;
            ResultSet rs = stm.executeQuery(sql);
          
           
            if (rs.next()) {
             
                String emp_id = rs.getString(1);
                String fname = rs.getString(2);
                String lname = rs.getString(3);
                String email = rs.getString(4);
                String phone = rs.getString(5);
                String hdate = rs.getString(6);
                String jobid = rs.getString(7);
                String salary = rs.getString(8);
                String comission = rs.getString(9);
                String mgid = rs.getString(10);
                String deptid = rs.getString(11);

                mainObject.accumulate("Status:", "OK");
                mainObject.accumulate("Timestamp:", time);
                mainObject.accumulate("id",emp_id);
                mainObject.accumulate("fname",fname);
                mainObject.accumulate("lname",lname);
                mainObject.accumulate("email",email);
                mainObject.accumulate("phone",phone);
                mainObject.accumulate("Hire Date",hdate);
                mainObject.accumulate("job id",jobid);
                mainObject.accumulate("Salary",salary);
                mainObject.accumulate("Comission",comission);
                mainObject.accumulate("manager id",mgid);
                mainObject.accumulate("dept id",deptid);

            }


        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Employees.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Employees.class.getName()).log(Level.SEVERE, null, ex);
                mainObject.accumulate("Status:", "FAILED");
                mainObject.accumulate("Timestamp:", time);
                mainObject.accumulate("Message:", "Display of all the employees is NOT Successful");
                mainObject.accumulate("ex:", ex);
                

        }

        return mainObject.toString();
    }
    
    
}