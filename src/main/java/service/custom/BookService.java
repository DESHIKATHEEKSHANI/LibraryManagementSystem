package service.custom;

import dto.Book;
import entity.BookEntity;
import service.SuperService;

import java.util.List;
import java.util.Map;

public interface BookService extends SuperService {
    boolean addBook(Book book);
    String generateNewBookId();
    boolean updateBook(Book book);
    boolean deleteBook(String bookId);
    Book searchBook(String bookId);
    List<Book> getAllBooks();
    Book getBookByTitle(String bookName);

    Book getBookById(String bookID);

    int getCountBooks();

    Map<String, Integer> getBookCategoryData();
}
