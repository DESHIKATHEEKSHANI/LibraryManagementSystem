package repository.custom.impl;

import config.HibernateConfig;
import entity.BorrowingTransactionEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repository.custom.BorrowingTransactionDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BorrowingTransactionDaoImpl implements BorrowingTransactionDao {

    @Override
    public boolean save(BorrowingTransactionEntity transactionEntity) {
        Session session = HibernateConfig.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(transactionEntity);

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
    public boolean update(BorrowingTransactionEntity transactionEntity) {
        Session session = HibernateConfig.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(transactionEntity);
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
    public boolean delete(String transactionId) {
        Session session = HibernateConfig.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            BorrowingTransactionEntity transactionEntity = session.get(BorrowingTransactionEntity.class, transactionId);
            if (transactionEntity != null) {
                session.remove(transactionEntity);
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
    public BorrowingTransactionEntity search(String transactionId) {
        Session session = HibernateConfig.getSession();
        try {
            return session.get(BorrowingTransactionEntity.class, transactionId);
        } finally {
            session.close();
        }
    }

    @Override
    public List<BorrowingTransactionEntity> getAll() {
        Session session = HibernateConfig.getSession();
        try {
            return session.createQuery("FROM BorrowingTransactionEntity", BorrowingTransactionEntity.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public String getLastTransactionId() {
        Session session = HibernateConfig.getSession();
        try {
            return session.createQuery("SELECT t.transactionID FROM BorrowingTransactionEntity t ORDER BY t.transactionID DESC", String.class)
                    .setMaxResults(1)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }

    @Override
    public int getCountByMemberAndReturnDateNull(String memberId) {
        try (Session session = HibernateConfig.getSession()) {
            Long count = session.createQuery(
                            "SELECT COUNT(bt) FROM BorrowingTransactionEntity bt WHERE bt.member.memberID = :memberId AND bt.returnDate IS NULL",
                            Long.class)
                    .setParameter("memberId", memberId)
                    .uniqueResult();
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int getCountBorrowedBooks() {
        try (Session session = HibernateConfig.getSession()) {
            Long count = session.createQuery(
                            "SELECT COUNT(bt) FROM BorrowingTransactionEntity bt WHERE bt.returnDate IS NULL",
                            Long.class)
                    .uniqueResult();
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Map<String, Integer> getBorrowingStatistics() {
        Session session = HibernateConfig.getSession();
        try {
            Map<String, Integer> stats = new HashMap<>();

            Long borrowedCount = session.createQuery(
                            "SELECT COUNT(bt) FROM BorrowingTransactionEntity bt WHERE bt.status = 'Borrowed'", Long.class)
                    .uniqueResult();
            stats.put("Borrowed", borrowedCount != null ? borrowedCount.intValue() : 0);

            Long returnedCount = session.createQuery(
                            "SELECT COUNT(bt) FROM BorrowingTransactionEntity bt WHERE bt.returnDate IS NOT NULL", Long.class)
                    .uniqueResult();
            stats.put("Returned", returnedCount != null ? returnedCount.intValue() : 0);

            Long overdueCount = session.createQuery(
                            "SELECT COUNT(bt) FROM BorrowingTransactionEntity bt WHERE bt.status = 'Borrowed' AND bt.dueDate < CURRENT_DATE", Long.class)
                    .uniqueResult();
            stats.put("Overdue", overdueCount != null ? overdueCount.intValue() : 0);

            return stats;
        } finally {
            session.close();
        }
    }



}
