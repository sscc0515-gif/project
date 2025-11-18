package com.springboot.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.springboot.domain.*;
import com.springboot.service.BookService;
import com.springboot.service.CartService;
import com.springboot.service.OrderProService;
import com.springboot.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderProService orderProService;
	@Autowired
	private BookService bookService;
    Order order;
    List<Book> listOfBooks;

    @GetMapping("/{cartId}")
    public String requestCartList(@PathVariable(value = "cartId") String cartId, Model model) {
        Cart cart = cartService.validateCart(cartId);  // 이 메서드가 CartService에 선언되어 있어야 함
        order = new Order();
        listOfBooks = new ArrayList<>();

        for (CartItem item : cart.getCartItems().values()) {
            OrderItem orderItem = new OrderItem();
            Book book = item.getBook();
            listOfBooks.add(book);
            orderItem.setBookId(book.getBookId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setTotalPrice(item.getTotalPrice());
            order.getOrderItems().put(book.getBookId(), orderItem);
        }

        order.setCustomer(new Customer());
        order.setShipping(new Shipping());
        order.setGrandTotal(cart.getGrandTotal());

        return "redirect:/order/orderCustomerInfo";
    }

    @GetMapping("/orderCustomerInfo")
    public String requestCustomerInfoForm(Model model) {
        model.addAttribute("customer", order.getCustomer());
        return "orderCustomerInfo";
    }

    @PostMapping("/orderCustomerInfo")
    public String requestCustomerInfo(@ModelAttribute Customer customer, Model model) {
        order.setCustomer(customer);
        return "redirect:/order/orderShippingInfo";
    }

    @GetMapping("/orderShippingInfo")
    public String requestShippingInfoForm(Model model) {
        model.addAttribute("shipping", order.getShipping());
        return "orderShippingInfo";
    }

    @PostMapping("/orderShippingInfo")
    public String requestShippingInfo(@Valid @ModelAttribute Shipping shipping, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "orderShippingInfo";
        }
        order.setShipping(shipping);
        model.addAttribute("order", order);
        return "redirect:/order/orderConfirmation";
    }

    @GetMapping("/orderConfirmation")
    public String requestConfirmation(Model model) {
        model.addAttribute("bookList", listOfBooks);
        model.addAttribute("order", order);
        return "orderConfirmation";
    }

    @PostMapping("/orderConfirmation")
    public String requestConfirmationFinished(Model model) {
        model.addAttribute("order", order);
        orderProService.save(order);  
        return "redirect:/order/orderFinished";
    }

    @GetMapping("/orderFinished")
    public String requestFinished(HttpServletRequest request, Model model) {
        Long orderId = orderService.saveOrder(order);
        order.setOrderId(orderId);
        model.addAttribute("order", order);
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "orderFinished";
    }

    @GetMapping("/orderCancelled")
    public String requestCancelled(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "orderCancelled";
    }
    
    @GetMapping("/list")
    public String viewHomePage(Model model) {
        return viewPage(1, "orderId", "asc", model);
    }

    @GetMapping("/page")
    public String viewPage(@RequestParam("pageNum") int pageNum,
                           @RequestParam("sortField") String sortField,
                           @RequestParam("sortDir") String sortDir,
                           Model model) {
        Page<Order> page = orderProService.listAll(pageNum, sortField, sortDir);
        List<Order> listOrders = page.getContent();

        List<Book> bookList = new ArrayList<>();
        for (Order order : listOrders) {
            for (OrderItem item : order.getOrderItems().values()) {
                String bookId = item.getBookId();
                Book book = bookService.getBookById(bookId);
                bookList.add(book);
            }
        }

        model.addAttribute("bookList", bookList); 
        model.addAttribute("orderList", listOrders);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "orderList";
    }


    @GetMapping("/view/{id}")
    public ModelAndView viewOrder(@PathVariable(name = "id") Long id) {
        Order order = orderProService.get(id);
        List<Book> listOfBooks = new ArrayList<>();
        for (OrderItem item : order.getOrderItems().values()) {
            String bookId = item.getBookId();
            Book book = bookService.getBookById(bookId);
            listOfBooks.add(book);
        }
        ModelAndView mav = new ModelAndView("orderView");
        mav.addObject("order", order);
        mav.addObject("bookList", listOfBooks);
        return mav;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditOrder(@PathVariable(name = "id") Long id) {
        Order order = orderProService.get(id);
        List<Book> listOfBooks = new ArrayList<>();
        for (OrderItem item : order.getOrderItems().values()) {
            String bookId = item.getBookId();
            Book book = bookService.getBookById(bookId);
            listOfBooks.add(book);
        }
        ModelAndView mav = new ModelAndView("orderEdit");
        mav.addObject("order", order);
        mav.addObject("bookList", listOfBooks);
        return mav;
    }

    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable(name = "id") long id) {
        orderProService.delete(id);
        return "redirect:/order/list";
    }

    @GetMapping("/deleteAll")
    public String deleteAllOrder() {
        orderProService.deleteAll();
        return "redirect:/order/list";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Order order) {
        Order save_order = orderProService.get(order.getOrderId());
        save_order.setShipping(order.getShipping());
        orderProService.save(save_order);
        return "redirect:/order/list";
    }

    
}
