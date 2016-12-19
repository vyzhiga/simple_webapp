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

        if (request.getPathInfo().equals("/initdb")) {
            initDb();
            response.sendRedirect(request.getContextPath()+"/index.jsp");
        } else if (request.getPathInfo().equals("/getbooks")) {
            request.setAttribute("bookList", getBooks());
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/ListBooks.jsp");
            rd.forward(request, response);
        }
    }

    private void initDb() {
        Connection con = null;
        Statement stmt = null;

        try {
            con = getConnection();

            con.setAutoCommit(false);
            stmt = con.createStatement();
            stmt.executeUpdate("CREATE TABLE users(id int primary key NOT NULL, name varchar(255) NOT NULL)");
            stmt.executeUpdate("INSERT INTO users(id, name) VALUES(1, 'Иванов')");
            stmt.executeUpdate("INSERT INTO users(id, name) VALUES(2, 'Петров')");
            stmt.executeUpdate("INSERT INTO users(id, name) VALUES(3, 'Сидоров')");

            stmt.executeUpdate("CREATE TABLE books(id int primary key not NULL , isbn varchar(17) NOT NULL, name varchar(50) NOT NULL , takerid int REFERENCES users(id))");
            stmt.executeUpdate("INSERT INTO books(id, ISBN, name, takerid) VALUES(1,'978-3-16-148410-0', 'Евгений Онегин', 1)");
            stmt.executeUpdate("INSERT INTO books(id, ISBN, name) VALUES(2,'5-4-09-148410-0', 'Дубровский')");
            stmt.executeUpdate("INSERT INTO books(id, ISBN, name) VALUES(3,'5-7-22-567348-0', 'Избранное')");
            stmt.executeUpdate("INSERT INTO books(id, ISBN, name, takerid) VALUES(4,'5-3-16-148277-0', 'Собрание сочинений', 2)");

            stmt.close();
            con.commit();
            logger.debug("DB created");
        } catch (Exception e) {
            logger.error("init db error", e);
        } finally {
            closeQuiet(stmt);
            closeQuiet(con);
        }
    }

    private List<LibrarianBook> getBooks() {
        Connection con = null;
        Statement stmt = null;

        List<LibrarianBook> books = new ArrayList<>();
        try {
            con = getConnection();

            con.setAutoCommit(false);
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT B.id AS BookID, B.ISBN AS BookISBN, B.name AS BookName, U.name AS UserName FROM books B JOIN users U ON U.id = B.takerid");

            while (rs.next()) {
                LibrarianBook book = new LibrarianBook();
                book.setIdBook(rs.getInt("BookID"));
                book.setISBNBook(rs.getString("BookISBN"));
                book.setNameBook(rs.getString("BookName"));
                book.setBookTaker(rs.getString("UserName"));
                books.add(book);
            }

            stmt.close();
            con.commit();
        } catch (Exception e) {
            logger.error("get books error", e);
        } finally {
            closeQuiet(stmt);
            closeQuiet(con);
        }
        return books;
    }

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DB_DRIVER);
        return DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
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