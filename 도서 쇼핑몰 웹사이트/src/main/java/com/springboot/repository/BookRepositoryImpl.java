package com.springboot.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.springboot.domain.Book;
import com.springboot.exception.BookIdException;

@Repository
public class BookRepositoryImpl implements BookRepository {
	
	@Autowired
	 private JdbcTemplate jdbcTemplate;

	public List<Book> getAllBookList() {
		 String sql = "SELECT * FROM book";
		 List<Book> listOfBooks = new ArrayList<>();
		 List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(sql);
		 for (Map<String, Object> row : rows) {
		 Book book = new Book();
		 book.setBookId((String)row.get("b_bookId"));
		 book.setName((String)row.get("b_name"));
		 book.setUnitPrice(new BigDecimal((Integer)row.get("b_unitPrice")));
		 book.setAuthor((String)row.get("b_author"));
		 book.setDescription((String)row.get("b_description"));
		 book.setPublisher((String)row.get("b_publisher"));
		 book.setCategory((String)row.get("b_category"));
		 book.setUnitsInStock(new Long((String)row.get("b_unitsInStock")));
		 book.setReleaseDate((String)row.get("b_releaseDate"));
		 book.setCondition((String)row.get("b_condition"));
		 book.setFileName((String)row.get("b_fileName"));
				 listOfBooks.add(book);
		 }
		 return listOfBooks;
		}
	
private List<Book> listOfBooks = new ArrayList<Book>();
	
	
	public BookRepositoryImpl() {
		Book book1 = new Book();
		book1.setBookId("ISBN1234");
		book1.setName("자바스크립트 입문");
		book1.setUnitPrice(new BigDecimal(30000));
		book1.setAuthor("조현영");
		book1.setDescription("자바스크립트의 기초부터 심화까지 핵심 문법을 학습한 후 12가지 프로그램을 만들며 "
				+ "학습한 내용을 확인 할 수 있습니다. 문법 학습과 실습이 적절히 섞여 있어 프로그램을 만드는 방법을 재미있게 익힐 수 있습니다.");
		book1.setPublisher("길벗");
		book1.setCategory("IT전문서");
		book1.setUnitsInStock(1000);
		book1.setReleaseDate("2024/02/20");
		book1.setFileName("ISBN1234.jpg");
		
		Book book2 = new Book();
		book2.setBookId("ISBN1235");
		book2.setName("파이썬의 정석");
		book2.setUnitPrice(new BigDecimal(29800));
		book2.setAuthor("조용주, 임좌상");
		book2.setDescription("4차 산업혁명의 핵심인 머신러닝, 사물인터넷, 데이터분석 등 다양한 분야에"
				+ "활용되는 직관적이고 간결한 문법의 파이썬 프로그래밍 언어를 최신 트렌드에 맞게 예제 중심으로 학습할 수 있습니다.");
		book2.setPublisher("길벗");
		book2.setCategory("IT교육교재");
		book2.setUnitsInStock(1000);
		book2.setReleaseDate("2023/01/10");
		book2.setFileName("ISBN1235.jpg");

		
		Book book3 = new Book();
		book3.setBookId("ISBN1236");
		book3.setName("안드로이드 프로그래밍");
		book3.setUnitPrice(new BigDecimal(36000));
		book3.setAuthor("송미영");
		book3.setDescription("안드로이드의 기본 개념을 체계적으로 익히고, 이를 실습 예제를 통해 익힙니다."
				+ "기본 개념과 사용법을 스스로 실전에 적용하는 방법을 학습한 다음 실습 예제와 응용 예제를 통해 실전 프로젝트 응용력을 키웁니다,");
		book3.setPublisher("길벗");
		book3.setCategory("IT교육교재");
		book3.setUnitsInStock(1000);
		book3.setReleaseDate("2023/06/30");
		book3.setFileName("ISBN1236.jpg");

		
		listOfBooks.add(book1);
		listOfBooks.add(book2);
		listOfBooks.add(book3);

		
	}

	public Book getBookById(String bookId) {
			 Book bookInfo = null;
			 String sql = "SELECT count(*) FROM book where b_bookId=?"; 
			 int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, bookId); 
			 if (rowCount != 0) {
				 sql = "SELECT * FROM book where b_bookId=?"; 
				 bookInfo = jdbcTemplate.queryForObject(sql, new BookRowMapper(),bookId); 
			 }
			 
			 if (bookInfo == null)
				 throw new BookIdException(bookId);
			 return bookInfo;
			 } 
	
	
	
	public List<Book> getBookListByCategory(String category){
		List<Book> booksByCategory = new ArrayList<Book>();
		
		String sql = "SELECT * FROM book where b_category LIKE '%" + category + "%'"; 		
		 List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(sql);
		 for (Map<String, Object> row : rows) {
			 Book book = new Book();
			 book.setBookId((String)row.get("b_bookId"));
			 book.setName((String)row.get("b_name"));
			 book.setUnitPrice(new BigDecimal((Integer)row.get("b_unitPrice")));
			 book.setAuthor((String)row.get("b_author"));
			 book.setDescription((String)row.get("b_description"));
			 book.setPublisher((String)row.get("b_publisher"));
			 book.setCategory((String)row.get("b_category"));
			 book.setUnitsInStock(new Long((String)row.get("b_unitsInStock")));
			 book.setReleaseDate((String)row.get("b_releaseDate"));
			 book.setCondition((String)row.get("b_condition"));
			 book.setFileName((String)row.get("b_fileName"));
			 booksByCategory.add(book);
			 } 
		 return booksByCategory;
	}
	
	public void setNewBook(Book book) {
		String SQL = "INSERT INTO book (b_bookId, b_name, b_unitPrice, b_author, b_description, b_publisher, b_category, b_unitsInStock, b_releaseDate,b_condition, b_fileName)" 
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		        jdbcTemplate.update(SQL, book.getBookId(), book.getName(), book.getUnitPrice(),
				book.getAuthor(),book.getDescription(), book.getPublisher(),
				book.getCategory(), book.getUnitsInStock(), book.
				getReleaseDate(), book.getCondition(), book.getFileName());
				 
	}
	public void setUpdateBook(Book book) {
	    if (book.getFileName() != null) {
	        String SQL = "UPDATE Book SET b_name = ?, b_unitPrice = ?, b_author = ?, " +
	                     "b_description = ?, b_publisher = ?, b_category = ?, " +
	                     "b_unitsInStock = ?, b_releaseDate = ?, b_condition = ?, " +
	                     "b_fileName = ? WHERE b_bookId = ?";
	        jdbcTemplate.update(SQL,
	                book.getName(),
	                book.getUnitPrice(),
	                book.getAuthor(),
	                book.getDescription(),
	                book.getPublisher(),
	                book.getCategory(),
	                book.getUnitsInStock(),
	                book.getReleaseDate(),
	                book.getCondition(),
	                book.getFileName(),
	                book.getBookId());
	    } else {
	        String SQL = "UPDATE Book SET b_name = ?, b_unitPrice = ?, b_author = ?, " +
	                     "b_description = ?, b_publisher = ?, b_category = ?, " +
	                     "b_unitsInStock = ?, b_releaseDate = ?, b_condition = ? " +
	                     "WHERE b_bookId = ?";
	        jdbcTemplate.update(SQL,
	                book.getName(),
	                book.getUnitPrice(),
	                book.getAuthor(),
	                book.getDescription(),
	                book.getPublisher(),
	                book.getCategory(),
	                book.getUnitsInStock(),
	                book.getReleaseDate(),
	                book.getCondition(),
	                book.getBookId());
	    }
	}

	
	
	
	
	public Set<Book> getBookListByFilter(Map<String, List<String>> filter) {
		 Set<Book> booksByPublisher = new HashSet<Book>();
		 Set<Book> booksByCategory = new HashSet<Book>();
		 Set<String> booksByFilter = filter.keySet();
		 if (booksByFilter.contains("publisher")) {
		 for (int j = 0; j < filter.get("publisher").size(); j++) {
		 String pubisherName = filter.get("publisher").get(j); 
	
		 String sql = "SELECT * FROM book where b_publisher LIKE '%" +
				 pubisherName + "%'"; 
				  List<Book> book = jdbcTemplate.query(sql, BeanPropertyRowMapper.
				 newInstance(Book.class));
				  booksByPublisher.addAll(book);
				  }
				  }
				  if (booksByFilter.contains("category")) {
				  for (int i = 0; i < filter.get("category").size(); i++) {
				  String category = filter.get("category").get(i);
				  String sql = "SELECT * FROM book where b_category LIKE '%" + category +
				 "%'"; 
				  List<Book> list = jdbcTemplate.query(sql, BeanPropertyRowMapper.
				 newInstance(Book.class));
				  booksByCategory.addAll(list);
				  }
				  }
				  booksByCategory.retainAll(booksByPublisher);
				  return booksByCategory;
				  }
	
	public void setDeleteBook(String bookID) {
		 String SQL = "DELETE from Book where b_bookId = ? ";
		 jdbcTemplate.update(SQL, bookID);
		 }
	
	
	
}
	
	
	
	
	
	

