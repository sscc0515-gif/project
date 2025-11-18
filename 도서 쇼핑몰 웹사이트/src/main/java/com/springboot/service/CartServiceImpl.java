package com.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.springboot.domain.Cart;
import com.springboot.repository.CartRepository;

@Service
public class CartServiceImpl implements CartService {
    
	@Autowired
	private CartRepository cartRepository;
	public Cart create(Cart cart) {
		return cartRepository.create(cart);
	}
	public Cart read(String cartId) {
		return cartRepository.read(cartId);
	}
	public void update(String cartId, Cart cart) {
		cartRepository.update(cartId, cart);
	}
	public void delete(String cartId) {
		cartRepository.delete(cartId);
		}
	@Override
	public Cart validateCart(String cartId) {
	    Cart cart = read(cartId);  
	    if (cart == null) {
	        throw new IllegalArgumentException("해당 cartId로 장바구니를 찾을 수 없습니다: " + cartId);
	    }
	    return cart;
	}

}
