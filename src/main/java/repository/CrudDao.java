package repository;

import java.util.List;

public interface CrudDao<T> extends SuperDao {
    boolean save(T entity);
    boolean update(T entity);
    boolean delete(String id);
    T search(String id);
    List<T> getAll();
}
