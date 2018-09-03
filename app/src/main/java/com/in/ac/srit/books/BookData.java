package com.in.ac.srit.books;

import java.io.Serializable;

public class BookData implements Serializable {
    private String bookname;
    private String imageUrl;

    public BookData(String bookname, String imageUrl, String bookId) {
        this.bookname = bookname;
        this.imageUrl = imageUrl;
        this.bookId = bookId;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    private String bookId;
}
