package org.duzer.webapp;// Import required java libraries

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import org.duzer.webapp.LibrarianBook;

// Extend HttpServlet class
public class HelloWorld extends HttpServlet {

    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:mem:";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

    //logging init
    final static Logger logger = LoggerFactory.getLogger(HelloWorld.class);

    //public void init() throws ServletException {
    public void init() {
        // Do required initialization
        //DeleteDbFiles.execute("~", "test", true);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Connection con = null;
        Statement stmt = null;

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            Class.forName(DB_DRIVER);
            con = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            logger.debug("DB created");

            con.setAutoCommit(false);
            stmt = con.createStatement();
            stmt.execute("CREATE TABLE users(id int primary key NOT NULL, name varchar(255) NOT NULL)");
            stmt.execute("INSERT INTO users(id, name) VALUES(1, 'Иванов')");
            stmt.execute("INSERT INTO users(id, name) VALUES(2, 'Петров')");
            stmt.execute("INSERT INTO users(id, name) VALUES(3, 'Сидоров')");

            stmt.execute("CREATE TABLE books(id int primary key not NULL , isbn varchar(17) NOT NULL, name varchar(50) NOT NULL , takerid int REFERENCES users(id))");
            stmt.execute("INSERT INTO books(id, ISBN, name, takerid) VALUES(1,'978-3-16-148410-0', 'Евгений Онегин', 1)");
            stmt.execute("INSERT INTO books(id, ISBN, name) VALUES(2,'5-4-09-148410-0', 'Дубровский')");
            stmt.execute("INSERT INTO books(id, ISBN, name) VALUES(3,'5-7-22-567348-0', 'Избранное')");
            stmt.execute("INSERT INTO books(id, ISBN, name, takerid) VALUES(4,'5-3-16-148277-0', 'Собрание сочинений', 2)");

            ResultSet rs = stmt.executeQuery("SELECT B.id AS BookID, B.ISBN AS BookISBN, B.name AS BookName, U.name AS UserName FROM books B JOIN users U ON U.id = B.takerid");
            logger.debug("Result: {}", rs);

            //out.println("<h1>H2 Database inserted through Statement</h1><br>");

            LibrarianBook libBook = new LibrarianBook();
            List<LibrarianBook> bookList = new ArrayList<LibrarianBook>();

            while (rs.next()) {
                //out.println("Id " + rs.getInt("BookID") + " ISBN " + rs.getString("BookISBN") + " Book " + rs.getString("BookName") + "<br>");
                libBook.setIdBook(rs.getInt("BookID"));
                libBook.setISBNBook(rs.getString("BookISBN"));
                libBook.setNameBook(rs.getString("BookName"));
                libBook.setBookTaker(rs.getString("UserName"));
                bookList.add(libBook);
            }
            stmt.close();
            con.commit();
            request.setAttribute("bookList",bookList);
            RequestDispatcher rd = getServletContext().getRequestDispatcher("ListBook.jsp");
            rd.forward(request, response);
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