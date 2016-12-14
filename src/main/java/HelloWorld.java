// Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;
import org.h2.tools.DeleteDbFiles;

// Extend HttpServlet class
public class HelloWorld extends HttpServlet {
 
  private static final String DB_DRIVER = "org.h2.Driver";
  private static final String DB_CONNECTION = "jdbc:h2:~/test";
  private static final String DB_USER = "";
  private static final String DB_PASSWORD = "";

  public void init() throws ServletException
  {
	// Do required initialization  
	DeleteDbFiles.execute("~", "test", true);
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    	  
	Connection con = null;
	Statement stmt = null;
    ResultSet rs = null;
	
	response.setContentType("text/html");
	PrintWriter out = response.getWriter();
	  
    try {
		Class.forName(DB_DRIVER);
		con = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
		
        con.setAutoCommit(false);
        stmt = con.createStatement();
        stmt.execute("CREATE TABLE PERSON(id int primary key, name varchar(255))");
        stmt.execute("INSERT INTO PERSON(id, name) VALUES(1, 'Anju')");
        stmt.execute("INSERT INTO PERSON(id, name) VALUES(2, 'Sonia')");
        stmt.execute("INSERT INTO PERSON(id, name) VALUES(3, 'Asha')");

        rs = stmt.executeQuery("select * from PERSON");			
			
		out.println("<h1>H2 Database inserted through Statement</h1><br>");
        while (rs.next()) {
            //System.out.println("Id "+rs.getInt("id")+" Name "+rs.getString("name"));
			out.println("Id "+rs.getInt("id")+" Name "+rs.getString("name")+"<br>");
        }
        stmt.close();
        con.commit();
    } catch (SQLException e) {
			out.println("SQLException caught: " + e.getMessage());
    } catch (Exception e) {
            out.println(e);
    } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ignored) {
                out.println(ignored);
            }
    }
  }
  
  public void destroy()
  {
      // do nothing.
  }
  
/*  private static Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER,
                    DB_PASSWORD);
            return dbConnection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dbConnection;
    }*/
}