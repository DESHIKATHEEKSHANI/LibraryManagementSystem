package repository.custom.impl;

import config.HibernateConfig;
import entity.FineEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repository.custom.FineDao;

import java.util.List;
import java.util.Optional;

public class FineDaoImpl implements FineDao {
    @Override
    public boolean save(FineEntity fineEntity) {
        Session session = HibernateConfig.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(fineEntity);

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
    public String getLastFineId() {
        Session session = HibernateConfig.getSession();
        try {
            return session.createQuery(
                            "SELECT f.fineID FROM FineEntity f ORDER BY CAST(SUBSTRING(f.fineID, 3) AS int) DESC", String.class)
                    .setMaxResults(1)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }
    @Override
    public Optional<FineEntity> findByTransactionID(String transactionID) {
        Session session = HibernateConfig.getSession();
        try {
            FineEntity fine = session.createQuery(
                            "FROM FineEntity f WHERE f.transaction.transactionID = :transactionID", FineEntity.class)
                    .setParameter("transactionID", transactionID)
                    .uniqueResult();
            return Optional.ofNullable(fine);
        } finally {
            session.close();
        }
    }


    @Override
    public boolean update(FineEntity entity) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public FineEntity search(String id) {
        return null;
    }

    @Override
    public List<FineEntity> getAll() {
        return List.of();
    }
}
