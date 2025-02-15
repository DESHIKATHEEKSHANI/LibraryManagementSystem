package repository.custom.impl;

import config.HibernateConfig;
import entity.BookEntity;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repository.custom.BookDao;

import java.util.List;

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
}
