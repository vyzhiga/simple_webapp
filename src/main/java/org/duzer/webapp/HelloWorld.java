package org.duzer.webapp;// Import required java libraries

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

// Extend HttpServlet class
public class HelloWorld extends HttpServlet {

    //DB driver vars
    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

    //Total number of books
    private static final int numBooks = 20;

    //Total number of records after query executing
    private static int numRecords;

    //logging init
    final static Logger logger = LoggerFactory.getLogger(HelloWorld.class);

    public void init() {
        // Do required initialization
        initDb();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        if (request.getPathInfo().equals("/initdb")) {
            //инициализация БД
            initDb();
            response.sendRedirect(request.getContextPath()+"/index.jsp");

        } else if (request.getPathInfo().equals("/getbooks")) {
            //выводим список книг постранично
            int page = 1;
            int recPerPage = 5;
            if (request.getParameter("page")!=null) {
                page = Integer.parseInt(request.getParameter("page"));
            }
            if (request.getParameter("recPerPage") != null) {
                recPerPage = Integer.parseInt(request.getParameter("recPerPage"));
            }
            request.setAttribute("bookList", getBooks((page-1)*recPerPage, recPerPage));
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/listBooks.jsp");
            rd.forward(request, response);

        } else if (request.getPathInfo().equals("/delbook")) {
            //удаляем книгу
            int idDelBook;
            if (request.getParameter("idDelBook") !=null) {
                idDelBook = Integer.parseInt(request.getParameter("idDelBook"));
                delBooks(idDelBook);
            } else {
                logger.error("!!! Exec /delbook without a parameter!");
            }

        } else if (request.getPathInfo().equals("/getusers")) {
            //вызываем jsp с шаблонами для списка пользователей
            request.setAttribute("userList",getUsers());
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/users.jsp");
            rd.forward(request, response);

        } else if (request.getPathInfo().equals("/deluser")) {
            //удаляем пользователя
            int idDelUser;
            if (request.getParameter("idDelUser") !=null) {
                idDelUser = Integer.parseInt(request.getParameter("idDelUser"));
                delUsers(idDelUser);
            } else {
                logger.error("!!! Exec /deluser without a parameter!");
            }
        }
    }

    private void initDb() {
        //DB connection vars
        Connection con = null;
        Statement stmt = null;
        //Vars for the ISBN pseudorandom generation
        int min = 345;
        int max = 970;
        //Main part of the ISBN string
        String strISBN;
        //Vars for init books' takers
        int initBookTaker;
        String initSQLstBookTaker;
        //Init SQL string
        String strSQLstmt;

        try {
            con = getConnection();
            logger.debug("DB created. Start of filling.");

            con.setAutoCommit(false);
            stmt = con.createStatement();

            //Create and fill Users table
            stmt.executeUpdate("CREATE TABLE users(id INT NOT NULL AUTO_INCREMENT primary key, name varchar(255) NOT NULL UNIQUE, password varchar(255))");
            stmt.executeUpdate("INSERT INTO users(name, password) VALUES('Иванов','xxx')");
            stmt.executeUpdate("INSERT INTO users(name, password) VALUES('Петров','xxx')");
            stmt.executeUpdate("INSERT INTO users(name, password) VALUES('Сидоров','xxx')");
            logger.debug("Finished initial filling of users");

            //Create and fill Book table
            stmt.executeUpdate("CREATE TABLE books(id INT NOT NULL AUTO_INCREMENT primary key, isbn varchar(17) " +
                    "NOT NULL, name varchar(50) NOT NULL, takerid int REFERENCES users(id) ON DELETE SET NULL)");
            //Filling of the Book table
            for (int i=0; i<numBooks; i++) {
                //init vars at the beginning of every iteration
                // Starting ISBN string
                strISBN = "-3-16-148410-0";
                // Starting SQL statement
                strSQLstmt = "INSERT INTO books(ISBN, name";
                //Defining the random part of the ISBN
                int partISBN = ThreadLocalRandom.current().nextInt(min, max + 1);
                //Concat
                strISBN = Integer.toString(partISBN) + strISBN;

                //Defining temporarily owner of a book
                initBookTaker = ThreadLocalRandom.current().nextInt(0, 4);
                //a book is available
                if (initBookTaker == 0) {
                    initSQLstBookTaker = "";
                    strSQLstmt = strSQLstmt + ") VALUES('" + strISBN + "', 'Евгений Онегин')";
                //smb took a book
                } else {
                    initSQLstBookTaker = Integer.toString(initBookTaker);
                    strSQLstmt = strSQLstmt + ", takerid) VALUES('" + strISBN + "', 'Евгений Онегин', " + initSQLstBookTaker + ")";
                }

                //Learn how many records returns query
                ResultSet rs = stmt.executeQuery("SELECT B.id AS BookID, B.ISBN AS BookISBN, B.name AS BookName, " +
                        "U.name AS UserName FROM books B JOIN users U ON U.id = B.takerid ORDER BY BookISBN");
                if (rs.next()) {
                    numRecords = rs.getInt(1);
                }

                stmt.executeUpdate(strSQLstmt);
                logger.debug(strSQLstmt);
            }

            stmt.close();
            con.commit();
            logger.debug("Records were inserted. End of filling");
        } catch (Exception e) {
            logger.error("!!! Init db error", e);
        } finally {
            closeQuiet(stmt);
            closeQuiet(con);
        }
    }

    private List<librarianUser> getUsers() {
        /**
         * читаем скриптом список всех пользователей из таблицы users, сортируем,
         * добавляем в список и возвращаем список всех пользователей
         */

        Connection con = null;
        Statement stmt = null;

        List<librarianUser> users = new ArrayList<>();

        try {
            con = getConnection();

            con.setAutoCommit(false);
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id AS UserID, name AS UserName, password AS UserPassword FROM users ORDER by UserName");

            while (rs.next()) {
                librarianUser user = new librarianUser();
                user.setUserId(rs.getInt("UserID"));
                user.setUserName(rs.getString("UserName"));
                user.setUserPass(rs.getString("UserPassword"));
                users.add(user);
            }

            stmt.close();
            con.commit();

        } catch (Exception e) {
            logger.error("!!! Get users error", e);
        } finally {
            closeQuiet(stmt);
            closeQuiet(con);
        }

        return users;
    }

    private List<LibrarianBook> getBooks(int offset, int recPerPage) {
        Connection con = null;
        Statement stmt = null;

        List<LibrarianBook> books = new ArrayList<>();
        try {
            con = getConnection();

            con.setAutoCommit(false);
            stmt = con.createStatement();
            /* ResultSet rs = stmt.executeQuery("SELECT B.id AS BookID, B.ISBN AS BookISBN, B.name AS BookName, U.name " +
                    "AS UserName FROM books B JOIN users U ON U.id = B.takerid ORDER BY BookISBN LIMIT " +
                    Integer.toString(recPerPage) + " OFFSET " + Integer.toString(offset)); */

            ResultSet rs = stmt.executeQuery("SELECT B.id AS BookID, B.ISBN AS BookISBN, B.name AS BookName, U.name " +
                    "AS UserName FROM books AS B LEFT JOIN users AS U ON B.takerid = U.id ORDER BY BookISBN LIMIT " +
                    Integer.toString(recPerPage) + " OFFSET " + Integer.toString(offset));

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
            logger.error("!!! Get books error", e);
        } finally {
            closeQuiet(stmt);
            closeQuiet(con);
        }
        return books;
    }

    private void delUsers(int idDelUser) {
        /**
         * удаляем пользователя с id=idDelUser
         */
        Connection con = null;
        Statement stmt = null;

        try {
            con = getConnection();
            con.setAutoCommit(false);

            stmt = con.createStatement();
            stmt.executeUpdate("DELETE FROM users WHERE id= " + Integer.toString(idDelUser));
            logger.debug("Deleted user record with id=" + Integer.toString(idDelUser));
            stmt.close();
            con.commit();
        } catch (Exception e) {
            logger.error("!!! Del users error", e);
        } finally {
            closeQuiet(stmt);
            closeQuiet(con);
        }
    }

    private void delBooks(int idDelBook) {
        Connection con = null;
        Statement stmt = null;

        try {
            con = getConnection();
            con.setAutoCommit(false);

            stmt = con.createStatement();
            stmt.executeUpdate("DELETE FROM books WHERE id= " + Integer.toString(idDelBook));
            logger.debug("Deleted book record with id=" + Integer.toString(idDelBook));
            stmt.close();
            con.commit();
        } catch (Exception e) {
            logger.error("!!! Del books error", e);
        } finally {
            closeQuiet(stmt);
            closeQuiet(con);
        }
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

}