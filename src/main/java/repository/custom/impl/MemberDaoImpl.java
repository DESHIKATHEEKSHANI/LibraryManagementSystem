package repository.custom.impl;

import config.HibernateConfig;
import entity.MemberEntity;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repository.custom.MemberDao;

import java.util.List;

public class MemberDaoImpl implements MemberDao {

    @Transactional
    @Override
    public boolean save(MemberEntity member) {
        Session session = HibernateConfig.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(member);
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
    public boolean update(MemberEntity member) {
        Session session = HibernateConfig.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(member);
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
    public boolean delete(String memberId) {
        Session session = HibernateConfig.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            MemberEntity member = session.get(MemberEntity.class, memberId);
            if (member != null) {
                session.remove(member);
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
    public MemberEntity search(String memberId) {
        Session session = HibernateConfig.getSession();
        try {
            return session.get(MemberEntity.class, memberId);
        } finally {
            session.close();
        }
    }

    @Override
    public List<MemberEntity> getAll() {
        Session session = HibernateConfig.getSession();
        try {
            return session.createQuery("FROM MemberEntity", MemberEntity.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public String getLastMemberId() {
        Session session = HibernateConfig.getSession();
        try {
            return session.createQuery("SELECT m.memberID FROM MemberEntity m ORDER BY m.memberID DESC", String.class)
                    .setMaxResults(1)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }

    @Override
    public MemberEntity getMemberByName(String name) {
        Session session = HibernateConfig.getSession();
        try {
            return session.createQuery("FROM MemberEntity m WHERE m.name = :name", MemberEntity.class)
                    .setParameter("name", name)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }

    @Override
    public int getCountMembers() {
        Session session = HibernateConfig.getSession();
        try {
            return ((Long) session.createQuery("SELECT COUNT(m) FROM MemberEntity m")
                    .uniqueResult()).intValue();
        } finally {
            session.close();
        }
    }


}
