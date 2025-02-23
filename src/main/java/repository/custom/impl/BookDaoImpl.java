package repository.custom.impl;

import config.HibernateConfig;
import entity.BookEntity;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repository.custom.BookDao;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookDaoImpl implements BookDao {

    @Override
    public boolean save(BookEntity bookEntity) {
        Session session = HibernateConfig.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(bookEntity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(BookEntity bookEntity) {
        Session session = HibernateConfig.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(bookEntity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(String bookId) {
        Session session = HibernateConfig.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            BookEntity book = session.get(BookEntity.class, bookId);
            if (book != null) {
                session.remove(book);
                transaction.commit();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public BookEntity search(String bookId) {
        Session session = HibernateConfig.getSession();
        try {
            return session.get(BookEntity.class, bookId);
        } finally {
            session.close();
        }
    }

    @Override
    public List<BookEntity> getAll() {
        Session session = HibernateConfig.getSession();
        try {
            return session.createQuery("FROM BookEntity", BookEntity.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public String getLastBookId() {
        Session session = HibernateConfig.getSession();
        try {
            return session.createQuery("SELECT b.bookID FROM BookEntity b ORDER BY b.bookID DESC", String.class)
                    .setMaxResults(1)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }

    @Override
    public BookEntity getBookByTitle(String title) {
        Session session = HibernateConfig.getSession();
        try {
            return session.createQuery("FROM BookEntity b WHERE b.title = :title", BookEntity.class)
                    .setParameter("title", title)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }

    @Override
    public Optional<BookEntity> findById(String bookID) {
        Session session = HibernateConfig.getSession();
        try {
            return Optional.ofNullable(session.get(BookEntity.class, bookID));
        } finally {
            session.close();
        }
    }

    @Override
    public int getCountBooks() {
        try (Session session = HibernateConfig.getSession()) {
            Long count = session.createQuery(
                            "SELECT COUNT(b) FROM BookEntity b",
                            Long.class)
                    .uniqueResult();
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Map<String, Integer> getBookCategoryCounts() {
        Session session = HibernateConfig.getSession();
        try {
            List<Object[]> results = session.createQuery(
                            "SELECT b.category, COUNT(b) FROM BookEntity b GROUP BY b.category", Object[].class)
                    .getResultList();

            return results.stream()
                    .collect(Collectors.toMap(
                            row -> (String) row[0],
                            row -> ((Long) row[1]).intValue()
                    ));
        } finally {
            session.close();
        }
    }



}
