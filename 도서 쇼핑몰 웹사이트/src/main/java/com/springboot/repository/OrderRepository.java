package com.springboot.repository;

import com.springboot.domain.Order;

public interface OrderRepository {

	Long saveOrder(Order order);
}
