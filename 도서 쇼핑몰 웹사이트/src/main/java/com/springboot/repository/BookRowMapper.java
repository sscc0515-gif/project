package com.springboot.repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.springboot.domain.Book;

import lombok.Data;

@Data
public class BookRowMapper implements RowMapper<Book> {

    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        Book book = new Book();
        book.setBookId(rs.getString("b_bookId"));
        book.setName(rs.getString("b_name"));
        book.setUnitPrice(rs.getBigDecimal("b_unitPrice"));
        book.setAuthor(rs.getString("b_author"));
        book.setDescription(rs.getString("b_description"));
        book.setPublisher(rs.getString("b_publisher"));
        book.setCategory(rs.getString("b_category"));
        book.setUnitsInStock(rs.getLong("b_unitsInStock"));
        book.setReleaseDate(rs.getString("b_releaseDate"));
        book.setCondition(rs.getString("b_condition"));
        book.setFileName(rs.getString("b_fileName"));  

        return book;
    }
}
