package org.duzer.webapp;// Import required java libraries

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
        //устанавливаем тип страницы и кодировки
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        // создание объекта сессии, если еще не была создана
        HttpSession session = request.getSession(true);
        // проверяем, есть ли параметр сессии, устанавливаем, если нет
        String param = (String) session.getAttribute("sesCurUser");
        if (param == null) {
            session.setAttribute("sesCurUser", "");
        }

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
            // добавляем атрибут со списком пользователей
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
            String userIdParam = request.getParameter("id");
            if (userIdParam !=null && !userIdParam.isEmpty()) {
                delUsers(Integer.parseInt(userIdParam));
            } else {
                logger.error("!!! Exec /deluser without a parameter!");
            }
            response.sendRedirect(request.getContextPath()+"/hw/getusers");
        } else if (request.getPathInfo().equals("/adduser")) {
            //добавляем пользователя
            String addUser = "";
            String addPass = "";
            if (request.getParameter("addUser")!=null) {
                addUser = request.getParameter("addUser");
            }
            if (request.getParameter("addPass") != null) {
                addPass = request.getParameter("addPass");
            }
            if (addUser != null && !addUser.isEmpty() && addPass!= null) {
                //addUser(addUser, addPass);
                response.setContentType("application/json");
                response.getWriter().write(addUser(addUser, addPass));
            } else {
                logger.error("!!! Error: user or pass is null or user is empty string");
            }
        } else if (request.getPathInfo().equals("/getuserdetails")) {
            //получаем имя пользователя и пароль
            int userId = 0;
            if (request.getParameter("userid")!=null) {
                userId = Integer.parseInt(request.getParameter("userid"));
                logger.debug("/getuserdetails?userid="+Integer.toString(userId));
            }
            if (userId != 0) {
                response.setContentType("application/json");
                response.getWriter().write(getUserDetails(userId));
            } else {
                logger.error("!!! Error: have not received user id (userId=0");
            }
        } else if (request.getPathInfo().equals("/updateuserpass")) {
            //апдейтим пароль пользователя
            int userId = 0;
            String newPass = "";

            if (request.getParameter("userid")!=null) {
                userId = Integer.parseInt(request.getParameter("userid"));
            }
            if (request.getParameter("newpass")!=null) {
                newPass= request.getParameter("newpass");
            }
            logger.debug("Changing password for userid="+userId+". New password is '"+newPass+"'.");

            if (userId != 0 && newPass !="") {
                response.setContentType("application/json");
                response.getWriter().write(updateUserPass(userId,newPass));
            } else {
                logger.error("!!! Error: have not received user id (userId=0");
            }

        } else if (request.getPathInfo().equals("/setuser")) {
            //определяем переменную текущего пользователя
            if (request.getParameter("username")!=null) {
                session.setAttribute("sesCurUser", request.getParameter("username"));
                logger.debug("Set curUser to '" + session.getAttribute("sesCurUser") + "'.");
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/usersdebug.jsp");
                rd.forward(request, response);
            }
        } else if (request.getPathInfo().equals("/changetaker")) {
            int bookid;
            int action;
            String username;
            if (request.getParameter("bookid") != null && request.getParameter("action") != null && request.getParameter("username")!=null) {
                bookid = Integer.parseInt(request.getParameter("bookid"));
                action = Integer.parseInt(request.getParameter("action"));
                username = request.getParameter("username");
                logger.debug("Changing taker, parameters: bookid/action/username"+bookid+"/"+action+"/"+username);
                switch (action) {
                    case 0: changeTaker(bookid, action, username);
                            break;
                    case 1: changeTaker(bookid, action, username);
                            break;
                    default:    logger.error("!!! Error: wrong action in /changetaker. Should be 0 or 1.");
                                break;
                }
            } else {
                logger.error("!!! Error: undefined parameters in /changetaker");
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
            stmt.executeUpdate("INSERT INTO users (name, password) VALUES ('Иванов','xxx')");
            stmt.executeUpdate("INSERT INTO users (name, password) VALUES ('Петров','xxx')");
            stmt.executeUpdate("INSERT INTO users (name, password) VALUES ('Сидоров','xxx')");
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

    private String addUser(String addUser, String addPass) {
    /**
     * Добавляем пользователя
     */
        Connection con = null;
        Statement stmt = null;
        int numUsers = 0;
        String res = "{\"Result\":1}";

        try {
            con = getConnection();
            con.setAutoCommit(false);

            stmt = con.createStatement();

            //проверяем количество пользователей с добавляемым именем
            ResultSet rs = stmt.executeQuery("SELECT COUNT(name) FROM users WHERE name='"+addUser+"'");
            //первый ряд в ResultSet, т.к. COUNT, он должен быть единственный
            rs.first();
            numUsers = rs.getInt(1);
            //пишем в лог
            logger.debug("Number of users '"+addUser+"' in DB: "+String.valueOf(numUsers));

            if (numUsers==0) {
                //пользователи с таким именем отсутствуют, добавляем
                stmt.executeUpdate("INSERT INTO users(name, password) VALUES ('" + addUser + "', '" + addPass + "')");
                logger.debug("Added user with passwd: " + addUser + ":" + addPass);
                stmt.close();
                con.commit();
                res = "{\"Result\":0}";
            } else {
                //пользователи существуют, пропускаем
                logger.debug("User "+addUser+" already exists. Skipping.");
            }
        } catch (Exception e) {
            logger.error("!!! Error adding user", e);
        } finally {
            closeQuiet(stmt);
            closeQuiet(con);
        }
        return res;
    }

    private String getUserDetails(int userId) {
        /**
         *  возвращаем данные по пользователю: имя и пароль
         */
        Connection con = null;
        Statement stmt = null;
        //возвращаем результат. по дефолту - неудача
        String res = "{\"Result\":0}";

        try {
            con = getConnection();
            con.setAutoCommit(false);

            stmt = con.createStatement();
            //выборка
            ResultSet rs = stmt.executeQuery("SELECT name AS username, password AS pass FROM users WHERE id="+userId);
            //одна запись из всей таблицы, т.к. id уникальный
            rs.first();
            //собираем ответ
            res = "{\"user\":\""+rs.getString("username")+"\", \"pass\":\""+rs.getString("pass")+"\", \"Result\":1}";
            logger.debug("JSON user details:"+res);
            stmt.close();
        } catch (Exception e) {
                logger.error("!!! Error getting user details", e);
            } finally {
                closeQuiet(stmt);
                closeQuiet(con);
            }
        return res;
    }

    private String updateUserPass(int userId, String newPass) {
        /**
         * апдейтием пароль пользователя, если он непустой
         */
        Connection con = null;
        Statement stmt = null;
        //возвращаем результат. по дефолту - неудача
        String res = "{\"Result\":0}";

        try {
            con = getConnection();
            con.setAutoCommit(false);

            stmt = con.createStatement();
            //выборка
            stmt.executeUpdate("UPDATE users SET password='"+newPass+"' WHERE id="+userId);
            //возвращаем ответ
            res = "{\"Result\":1}";
            logger.debug("Password for userid="+userId+" has been successfully changed.");

            stmt.close();
            con.commit();

        } catch (Exception e) {
            logger.error("!!! Error updating password", e);
        } finally {
            closeQuiet(stmt);
            closeQuiet(con);
        }

        return res;
    }

    public void changeTaker(int bookId, int action, String username) {
        /**
         *  берем книгу пользователем, либо возвращаем ее
         */
        Connection con = null;
        Statement stmt = null;

        try {
            con = getConnection();
            con.setAutoCommit(false);

            stmt = con.createStatement();

            if (action == 0) {
                stmt.executeUpdate("UPDATE books SET takerid=NULL WHERE id=" + bookId);
            } else if (action == 1) {
                stmt.executeUpdate("UPDATE books SET takerid=(SELECT id FROM users WHERE name = '" + username + "') WHERE id=" + bookId);
            }

            stmt.close();
            con.commit();

        } catch (Exception e) {
            logger.error("!!! Error changing book owner", e);
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