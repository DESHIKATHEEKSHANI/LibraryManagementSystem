package service.custom.impl;

import dto.Book;
import entity.BookEntity;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.custom.BookDao;
import service.custom.BookService;
import util.DaoType;

import java.util.List;
import java.util.stream.Collectors;

public class BookServiceImpl implements BookService {

    private final BookDao dao = DaoFactory.getInstance().getDaoType(DaoType.BOOK);
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public boolean addBook(Book book) {
        try {
            BookEntity bookEntity = mapToEntity(book);
            return dao.save(bookEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String generateNewBookId() {
        String lastId = dao.getLastBookId();

        if (lastId == null || !lastId.matches("BK\\d{3}")) {
            return "BK001"; // Default ID
        }

        try {
            int num = Integer.parseInt(lastId.substring(2)) + 1;
            return String.format("BK%03d", num);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Failed to parse last book ID", e);
        }
    }

    @Override
    public boolean updateBook(Book book) {
        try {
            BookEntity bookEntity = mapToEntity(book);
            return dao.update(bookEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteBook(String bookId) {
        return dao.delete(bookId);
    }

    @Override
    public Book searchBook(String bookId) {
        BookEntity entity = dao.search(bookId);
        return entity != null ? modelMapper.map(entity, Book.class) : null;
    }

    @Override
    public List<Book> getAllBooks() {
        return dao.getAll().stream()
                .map(entity -> modelMapper.map(entity, Book.class))
                .collect(Collectors.toList());
    }

    private BookEntity mapToEntity(Book book) {
        BookEntity bookEntity = modelMapper.map(book, BookEntity.class);
        if (bookEntity.getAvailabilityStatus() == null) {
            bookEntity.setAvailabilityStatus("Available"); // Ensure default is set
        }
        return bookEntity;
    }

}
