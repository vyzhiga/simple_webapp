// Import required java libraries

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

import org.h2.tools.DeleteDbFiles;

// Extend HttpServlet class
public class HelloWorld extends HttpServlet {

    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:./test;DB_CLOSE_DELAY=10";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

    //logging init
    final static Logger logger = LoggerFactory.getLogger(HelloWorld.class);

    public void init() throws ServletException {
        // Do required initialization
        DeleteDbFiles.execute("~", "test", true);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Connection con = null;
        Statement stmt = null;

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName(DB_DRIVER);
            con = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            logger.debug("DB created");

            con.setAutoCommit(false);
            stmt = con.createStatement();
            stmt.execute("CREATE TABLE PERSON(id int primary key, name varchar(255))");
            stmt.execute("INSERT INTO PERSON(id, name) VALUES(1, 'Anju')");
            stmt.execute("INSERT INTO PERSON(id, name) VALUES(2, 'Sonia')");
            stmt.execute("INSERT INTO PERSON(id, name) VALUES(3, 'Asha')");

            ResultSet rs = stmt.executeQuery("select * from PERSON");

            out.println("<h1>H2 Database inserted through Statement</h1><br>");
            while (rs.next()) {
                out.println("Id " + rs.getInt("id") + " Name " + rs.getString("name") + "<br>");
            }
            stmt.close();
            con.commit();
        } catch (SQLException e) {
            out.println("SQLException caught: " + e.getMessage());
        } catch (Exception e) {
            out.println(e);
        } finally {
            closeQuiet(stmt);
            closeQuiet(con);
        }
    }


    public void destroy() {
        // do nothing.
    }

    private void closeQuiet(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
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