package com.springboot.domain;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Cart {
	
	private String cartId;
	
	private Map<String,CartItem> cartItems;
	private BigDecimal grandTotal;
	
	public Cart() {
		cartItems = new HashMap<String, CartItem>();
		grandTotal = new BigDecimal(0);
	}
	public Cart(String cartId) {
		this();
		this.cartId = cartId;
	}
	
	
	
	
	public void addCartItem(CartItem item) {
	    String bookId = item.getBook().getBookId();
	    if(cartItems.containsKey(bookId)) {
	        CartItem cartItem = cartItems.get(bookId);   
	        cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
	        cartItems.put(bookId, cartItem);
	    } else {
	        cartItems.put(bookId, item);
	    }
	    updateGrandTotal();  
	}

	public void updateGrandTotal() {
	    grandTotal = BigDecimal.ZERO;
	    for (CartItem item : cartItems.values()) {
	        grandTotal = grandTotal.add(item.getTotalPrice()); 
	    }
	}

	public void removeCartItem(CartItem item) {
		String bookId = item.getBook().getBookId();
		cartItems.remove(bookId);
		updateGrandTotal();
	}

}
