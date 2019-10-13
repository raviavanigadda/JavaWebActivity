/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ResourcePackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
/**
 *
 * @author ravilion
 */

//Identify the class
@Path("DepController")
public class Department {
    
    //http://localhost:8080/JavaWebActivity/webresources/DepController/department
    
 @GET
 @Path("department") //this path is for method
 @Produces(MediaType.APPLICATION_JSON)
 public ArrayList<DepartmentModel> getDatainJSON() throws ClassNotFoundException, SQLException
 {
     ArrayList<DepartmentModel> dm = new ArrayList<>();
     Connection con = null;
     Statement stm = null;
     
     String query = "select *from departments";
     Class.forName("oracle.jdbc.OracleDriver");
     con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521/XE", "hr", "inf5180");
     
     Statement st = con.createStatement();
     ResultSet rs = st.executeQuery(query);
     while(rs.next()){
         DepartmentModel depm = new DepartmentModel();
         depm.setDept_id(rs.getInt("department_id"));
         depm.setDept_name(rs.getString("department_name"));
         depm.setMan_id(rs.getInt("manager_id"));
         depm.setLoc_id(rs.getInt("location_id"));
         dm.add(depm);
         
     }
     return dm;
    
 }
 
 
}
