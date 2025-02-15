package repository.custom.impl;

import config.HibernateConfig;
import entity.CategoryEntity;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repository.custom.CategoryDao;

import java.util.List;

public class CategoryDaoImpl implements CategoryDao {

    @Transactional
    @Override
    public boolean save(CategoryEntity categoryEntity) {
        Session session = HibernateConfig.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(categoryEntity);
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
    public boolean update(CategoryEntity categoryEntity) {
        Session session = HibernateConfig.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(categoryEntity);
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
    public boolean delete(String categoryName) {
        Session session = HibernateConfig.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            CategoryEntity categoryEntity = session.get(CategoryEntity.class, categoryName);
            if (categoryEntity != null) {
                session.remove(categoryEntity);
                transaction.commit();
                return true;
            }
            return false;
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
    public List<CategoryEntity> getAll() {
        Session session = HibernateConfig.getSession();
        try {
            return session.createQuery("FROM CategoryEntity", CategoryEntity.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public CategoryEntity search(String categoryName) {
        Session session = HibernateConfig.getSession();
        try {
            return session.get(CategoryEntity.class, categoryName);
        } finally {
            session.close();
        }
    }
}
