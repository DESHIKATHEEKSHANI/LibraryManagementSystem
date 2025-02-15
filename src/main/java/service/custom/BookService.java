package service.custom;

import dto.Book;
import service.SuperService;

import java.util.List;

public interface BookService extends SuperService {
    boolean addBook(Book book);
    String generateNewBookId();
    boolean updateBook(Book book);
    boolean deleteBook(String bookId);
    Book searchBook(String bookId);
    List<Book> getAllBooks();
}
