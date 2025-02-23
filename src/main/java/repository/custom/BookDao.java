package repository.custom;

import entity.BookEntity;
import repository.CrudDao;

import java.util.Map;
import java.util.Optional;


public interface BookDao extends CrudDao<BookEntity> {
    String getLastBookId();
    BookEntity getBookByTitle(String title);

    Optional<BookEntity> findById(String bookID);

    int getCountBooks();

    Map<String, Integer> getBookCategoryCounts();
}
