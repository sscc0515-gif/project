package com.springboot.service;

import java.util.List;
import com.springboot.domain.Book;

public interface BookService {

    List<Book> getAllBookList();                         

    Book getBookById(String bookId);                      

    List<Book> getBookListByCategory(String category);   

    void setNewBook(Book book);  
    void setUpdateBook(Book book);
    void setDeleteBook(String bookID);

}
