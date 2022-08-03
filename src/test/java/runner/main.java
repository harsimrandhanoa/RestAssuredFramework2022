package runner;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class main {
	

	/* This is a class used just for testing to connect to db and run some queries.
	Not used in the project at any place */
	
    public static void main(String args[]) {
    	Connection conn = null;
    	Statement stmt = null;
    	

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = null;
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/retail","root", "pass");
            System.out.println("Database is connected !");
             stmt = conn.createStatement();
            //Retrieving the data
           ResultSet rs = stmt.executeQuery("show tables");

            System.out.println("List of databases: ");
            while(rs.next()) {
               System.out.print(rs.getString(1));
               System.out.println();
            }
            
            
            ResultSet rs1 = stmt.executeQuery("Select * from Category limit 10");
            System.out.println("List of Categories ");

            while(rs1.next())
            {
              System.out.println(rs1.getString(1) +"----------"+rs1.getString(2));
            }
            
            ResultSet rs2 = stmt.executeQuery("Select * from Product limit 10");
            System.out.println("List of Products ");

            while(rs2.next())
            {
              System.out.println(rs2.getString(1) +"--"+rs2.getString(2) +"--"+rs2.getString(3) +"--"+rs2.getString(4) +"--"+rs2.getString(5));
            }
            
            ResultSet rs3 = stmt.executeQuery("Select cat_name as CategoryName from Category  order by cat_id desc limit 2");
            System.out.println("List of Category Names ");

            while(rs3.next())
            {
              String catName = rs3.getString("CategoryName");
              System.out.println("Category name is "+catName);
            }
            
            ResultSet rs4= stmt.executeQuery("Select prod_name as ProductName from Product order by prod_id desc limit 2");
            System.out.println("List of Product Names ");

            while(rs4.next())
            {
              String prodName = rs4.getString("ProductName");
              System.out.println("Product name is " + prodName );
            }
            
            stmt.executeUpdate("delete  from Product order by cat_id desc limit 1");
            stmt.executeUpdate("delete  from Category order by cat_id desc limit 1");




             conn.close();
        }
        catch(Exception e) {
            System.out.print("Do not connect to DB - Error:"+e);
        }
        
        finally {
        try {
          stmt.close();
          conn.close();
          System.out.println("connection closed");
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
}
