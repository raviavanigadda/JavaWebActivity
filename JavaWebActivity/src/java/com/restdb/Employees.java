/**
 *
 * @author 1895198
 * Shriya vulupala
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

@Path("Employeepath")
public class Employees {
    Connection con= null;
    Statement stm= null;
    PreparedStatement ps = null;
    ResultSet rs= null;
    JSONObject mainObject = new JSONObject();
    JSONArray mainArray = new JSONArray();
    
    long now = Instant.now().toEpochMilli()/ 1000L;

//Display all data in table
 @GET
 @Path("listDB")
 @Produces(MediaType.APPLICATION_JSON)
 public String getText(){
        

    try {
        Class.forName("oracle.jdbc.OracleDriver");
         con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "hr", "inf5180");
         stm = con.createStatement();

        String sql = "select * from employees ";

         rs = stm.executeQuery(sql);

        String dep_name = null;
        int emp_id ,job_id,salary,manager_id,department_id,commision_pct;
        String first_name,last_name,hire_date,email,phone_number;
        
        while (rs.next()) {
            
            emp_id = rs.getInt("emp_id");
            job_id= rs. getInt("job_id");
            salary = rs.getInt("salary");
            manager_id = rs.getInt("manager_id");
            department_id = rs.getInt("department_id");
            commision_pct= rs. getInt("commision_pct");
            
           first_name = rs.getString("first_name");
           last_name = rs.getString("last_name");
           hire_date = rs.getString("hire_date");
           email = rs.getString("email");
           phone_number = rs.getString("phone_number");
            
            mainObject.accumulate("Employee ID", dep_id);
            mainObject.accumulate("JOb ID", manager_id);
            mainObject.accumulate("Salary", location_id);
            mainObject.accumulate("Manager ID", dep_name);
            mainObject.accumulate("Department ID", dep_id);
            mainObject.accumulate("Commision ID", manager_id);
            mainObject.accumulate("Firstname", location_id);
            mainObject.accumulate("Manager ID", dep_name);
            
            
            
            
            
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
    
    
    
    
}
