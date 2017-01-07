package org.duzer.webapp;

/**
 * Created by duzer on 18.12.2016.
 * Класс библиотечной книги.
 */
public class LibrarianBook {

    private int idBook;
    private String nameBook;
    private String ISBNBook;
    private String bookTaker;

    public int getIdBook() {
        return idBook;
    }

    public void setIdBook(int idBook) {
        this.idBook = idBook;
    }

    public String getNameBook() {
        return nameBook;
    }

    public void setNameBook(String nameBook) {
        this.nameBook = nameBook;
    }

    public String getISBNBook() {
        return ISBNBook;
    }

    public void setISBNBook(String ISBNBook) {
        this.ISBNBook = ISBNBook;
    }

    public String getBookTaker() {
        return bookTaker;
    }

    public void setBookTaker(String bookTaker) {
        this.bookTaker = bookTaker;
    }
}


