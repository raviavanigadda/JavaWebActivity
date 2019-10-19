/**
 *
 * @author 1894475
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

@Path("jobs")
public class JobsTable {
    
      
    Connection con= null;
    Statement stm= null;
    PreparedStatement ps = null;
    ResultSet rs= null;
    JSONObject mainObject = new JSONObject();
    JSONArray mainArray = new JSONArray();
    
    long now = Instant.now().toEpochMilli()/ 1000L;

    
    //Display all job data in table
 @GET
 @Path("jobslistDB")
 @Produces(MediaType.APPLICATION_JSON)
 public String getText(){
        

    try {
        Class.forName("oracle.jdbc.OracleDriver");
         con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "hr", "inf5180");
         stm = con.createStatement();

        String sql = "select * from jobs ";

         rs = stm.executeQuery(sql);

        String job_id = null,job_title=null;
        int min_salary , max_salary;

        while (rs.next()) {
           job_id = rs.getString("job_id");
           job_title= rs.getString("job_title");
           min_salary = rs.getInt("min_salary");
           max_salary = rs.getInt("max_salary");
            
            mainObject.accumulate("Job ID", job_id);
            mainObject.accumulate("Job Title", job_title);
            mainObject.accumulate("Min Salary", min_salary);
            mainObject.accumulate("Max Salary", max_salary);
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
    
    //Insert into database
//http://localhost:8080/JavaWebActivity/webresources/path/insertDB&1091&MAD&100&1000 
 @GET
 @Path("insertJobDB&{jobid}&{jobtitle}&{minsalary}&{maxsalary}")
 @Produces(MediaType.APPLICATION_JSON)
 public String getText1(@PathParam("jobid") String job_Id, @PathParam("jobtitle") String job_Title, @PathParam("minsalary") int min_Salary, @PathParam("maxsalary") int max_Salary) throws SQLException, ClassNotFoundException{

       try {
            Class.forName("oracle.jdbc.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "hr", "inf5180");
            
            
            String sql = "INSERT INTO jobs(JOB_ID,JOB_TITLE,MIN_SALARY,MAX_SALARY) values (?, ?, ?, ?)";
            ps = con.prepareStatement(sql);
            
            ps.setString(1,job_Id);
            ps.setString(2,job_Title);
            ps.setInt(3,min_Salary);
            ps.setInt(4,max_Salary);
            
            int flag = ps.executeUpdate();

           
            if(flag==1)
            {
                
                mainObject.accumulate("Message","Jobdata Insertion Sucessful");
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
//Display single List of Jobs
 @GET
 @Path("singleDB&{JobID}")
 @Produces(MediaType.APPLICATION_JSON)
 public String getText2(@PathParam("JobID") int jobID){

    try {
        Class.forName("oracle.jdbc.OracleDriver");
        con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "hr", "inf5180");
        
        String singlelist = "select * from jobs where job_id= '"+jobID+"'";
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
            int  min_salary,max_salary;
            String job_id, job_title;
            
            job_id = rs.getString("job_id");
            job_title = rs.getString("job_title");
            min_salary = rs.getInt("min_salary");
            max_salary = rs.getInt("max_salary");
            
            mainObject.accumulate("Job ID", job_id);
            mainObject.accumulate("Job Title", job_title);
            mainObject.accumulate("Min Salary", min_salary);
            mainObject.accumulate("Max Salary", max_salary);
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

 //UPDATE single row of Jobs
 //http://localhost:8080/JavaWebActivity/webresources/path/updateDB&1090&CAD
 @GET
 @Path("updateDB&{JID}&{JTitle}")
 @Produces(MediaType.APPLICATION_JSON)
 public String getText3(@PathParam("JID") String jobID,@PathParam("JTitle") String jobTitle){

    try {
        Class.forName("oracle.jdbc.OracleDriver");
        con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "hr", "inf5180");
        
       
        String updatelist = "update jobs set job_id= ? where job_title=?";
        
        
        ps = con.prepareStatement(updatelist);
        ps.setString(1, jobID);
        ps.setString(2,jobTitle);
        
        int flag = ps.executeUpdate();
    
        if(flag!=1){
            mainObject.accumulate("Message","data not Found!");
            mainObject.accumulate("Status","ERROR");
            mainObject.accumulate("Timestamp",now);
        }
        else
        {
       
            mainObject.accumulate("Message", "Job list updated Sucessfully!");
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
 
 //Delete single List of Jobs
 //http://localhost:8080/JavaWebActivity/webresources/path/deleteDB&1090
 @GET
 @Path("deleteDB&{jID}")
 @Produces(MediaType.APPLICATION_JSON)
 public String getText4(@PathParam("jID") int jobID){

    try {
        Class.forName("oracle.jdbc.OracleDriver");
        con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "hr", "inf5180");
        
        String deletelist = "delete from jobs where job_id = ?";
       
        
        ps = con.prepareStatement(deletelist);
        ps.setInt(1,jobID);
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
    

