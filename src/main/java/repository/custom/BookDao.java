package repository.custom;

import entity.BookEntity;
import repository.CrudDao;


public interface BookDao extends CrudDao<BookEntity> {
    String getLastBookId();
}
