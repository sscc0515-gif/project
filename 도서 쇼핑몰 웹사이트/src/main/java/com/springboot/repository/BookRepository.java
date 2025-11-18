package com.springboot.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.springboot.domain.Book;
@Repository
public interface BookRepository {
	List<Book> getAllBookList();
	Book getBookById(String bookId);
	List<Book> getBookListByCategory(String category);
	
	void setNewBook(Book book);
	 void setUpdateBook(Book book);
	 void setDeleteBook(String bookID); 

}