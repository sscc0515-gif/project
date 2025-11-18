package com.springboot.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springboot.domain.Book;
import com.springboot.exception.BookIdException;
import com.springboot.exception.CategoryException;
import com.springboot.service.BookService;
import com.springboot.validator.BookValidator;
import com.springboot.validator.UnitsInStockValidator;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class BookController {

	@Autowired
	private BookService bookService;

	
	
	@GetMapping("/books")
	public String requestBookList(Model model) {
		List<Book> list = bookService.getAllBookList();
		model.addAttribute("bookList", list);
		return "books";
	}
	
	@GetMapping("/books/book")
	public String requestBookById(@RequestParam("id")String bookId, Model model) {
		Book bookById = bookService.getBookById(bookId);
		model.addAttribute("book", bookById);
		return "book";
	}
	
	
	@GetMapping("/books/{category}")
	public String requestBooksByCategory(
		@PathVariable("category") String bookCategory, Model model) {
		List<Book> booksByCategory = bookService.getBookListByCategory(bookCategory);
		if(booksByCategory == null || booksByCategory.isEmpty()) {
			throw new CategoryException();
		}
		model.addAttribute("bookList", booksByCategory);
		return "books"; 
	}
	
	@Value("${file.uploadDir}")
	private String fileDir;

	
	
	@PostMapping("/books/add")
	public String submitAddNewBook(@Valid @ModelAttribute Book book, BindingResult bindingResult) {
	    if (bindingResult.hasErrors())
	        return "addBook";

	    MultipartFile bookImage = book.getBookImage();

	    if (bookImage != null && !bookImage.isEmpty()) {
	        String saveName = bookImage.getOriginalFilename();

	        if (saveName != null && !saveName.toLowerCase().matches(".*\\.(jpg|jpeg|png)$")) {
	            saveName += ".jpg";
	        }

	        File saveFile = new File(fileDir, saveName);
	        try {
	            bookImage.transferTo(saveFile);
	        } catch (Exception e) {
	            throw new RuntimeException("도서 이미지 업로드 실패하였습니다", e);
	        }

	        book.setFileName(saveName);
	    } 
	    
	    bookService.setNewBook(book);
	    return "redirect:/books";
	}

	@GetMapping("/books/add")
	public String requestAddBookForm(Model model) {
	    model.addAttribute("book", new Book()); 
	    return "addBook";
	}

	
	@GetMapping("/download")
	public void downloadBookImage(@RequestParam("file") String paramKey,
	                              HttpServletResponse response) throws IOException {
	    File imageFile = new File(fileDir + paramKey);  
	    
	    response.setContentType("application/octet-stream"); 
	    response.setContentLength((int) imageFile.length());  
	    response.setHeader("Content-disposition", "attachment;filename=\"" + paramKey + "\"");  

	    ServletOutputStream os = response.getOutputStream(); 
	    FileInputStream fis = new FileInputStream(imageFile);  
	    FileCopyUtils.copy(fis, os);  
	    fis.close();
	    os.close();
	}

	
	@ModelAttribute
	public void addAttributes(Model model) {
		model.addAttribute("addTitle", "신규 도서 등록");
	}
	
	//@Autowired
	//private UnitsInStockValidator unitsInStockValidator;
	
	@Autowired
	private BookValidator bookValidator;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		//binder.addValidators(unitsInStockValidator);
		binder.addValidators(bookValidator);
		
		binder.setAllowedFields("bookId", "name", "unitPrice", "author","description",
				"publisher", "category", "unitsInStock", "totalPages", "releaseDate", "condition", "bookImage");
	}
	
	@ExceptionHandler(value= {BookIdException.class})
	public ModelAndView handleError(HttpServletRequest req, BookIdException exception)
	{
		ModelAndView mav = new ModelAndView();
		mav.addObject("invalidBookId", exception.getBookId());
		mav.addObject("exception", exception);
		mav.addObject("url", req.getRequestURL()+"?"+req.getQueryString());
		mav.setViewName("errorBook");
		return mav;
	}
	
	@GetMapping("/books/update")
	public String getUpdateBookForm(@ModelAttribute("book") Book book,
	                                @RequestParam("id") String bookId,
	                                Model model) {
	    Book bookById = bookService.getBookById(bookId);
	    model.addAttribute("book", bookById);
	    return "updateForm";
	}

	@PostMapping("/books/update")
	public String processUpdateBookForm(@ModelAttribute("book") Book book) {
	    MultipartFile bookImage = book.getBookImage();

	    if (bookImage != null && !bookImage.isEmpty()) {
	        try {
	            String fname = bookImage.getOriginalFilename();
	            bookImage.transferTo(new File(fileDir + fname));
	            book.setFileName(fname);
	        } catch (Exception e) {
	            throw new RuntimeException("Book Image saving failed", e);
	        }
	    }

	    bookService.setUpdateBook(book);
	    return "redirect:/books";
	}

	
	@RequestMapping(value = "/books/delete")
	 public String getDeleteBookForm(Model model, @RequestParam("id") String bookId) {
	 bookService.setDeleteBook(bookId);
	 return "redirect:/books";
	 } 
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	}
	

