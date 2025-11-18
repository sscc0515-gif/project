package com.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.springboot.domain.Book;
import com.springboot.domain.Order;
import com.springboot.repository.BookRepository;
import com.springboot.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OrderRepository orderRepository;

    public void confirmOrder(String bookId, long quantity) {
        Book bookById = bookRepository.getBookById(bookId);
        if (bookById.getUnitsInStock() < quantity) {
            throw new IllegalArgumentException("품절입니다. 사용 가능한 재고: " + bookById.getUnitsInStock());
        }

        bookById.setUnitsInStock(bookById.getUnitsInStock() - quantity);
    }

    public Long saveOrder(Order order) {
        Long orderId = orderRepository.saveOrder(order);
        return orderId;
    }
}
