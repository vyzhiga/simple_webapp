package org.duzer.webapp;

/**
 * Created by duzer on 08.01.2017.
 * Класс пользователя библиотеки
 */
public class librarianUser {

    private int userId;
    private String userName;
    private String userPass;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }
}
