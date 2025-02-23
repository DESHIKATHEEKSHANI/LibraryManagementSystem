package repository.custom.impl;

import config.HibernateConfig;
import entity.AdminEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repository.custom.AdminDao;

import java.util.List;

public class AdminDaoImpl implements AdminDao {

    @Override
    public AdminEntity findByEmail(String email) {
        Session session = HibernateConfig.getSession();
        AdminEntity admin = session.createQuery("FROM AdminEntity WHERE email = :email", AdminEntity.class)
                .setParameter("email", email)
                .uniqueResult();
        session.close();
        return admin;
    }

    @Override
    public boolean save(AdminEntity entity) {
        Session session = HibernateConfig.getSession();
        session.beginTransaction();
        session.persist(entity);
        session.flush();
        session.getTransaction().commit();
        session.close();
        return true;
    }
    @Override
    public boolean update(AdminEntity adminEntity) {
        Session session = HibernateConfig.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(adminEntity);
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
    public boolean delete(String id) {
        return false;
    }

    @Override
    public AdminEntity search(String id) {
        return null;
    }

    @Override
    public List<AdminEntity> getAll() {
        return List.of();
    }


}
