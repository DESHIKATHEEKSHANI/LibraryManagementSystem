package config;

import entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateConfig {
    private static final SessionFactory sessionFactory = createSessionFactory();

    private static SessionFactory createSessionFactory() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();

        Metadata metadata = new MetadataSources(registry)
                .addAnnotatedClass(AdminEntity.class)
                .addAnnotatedClass(MemberEntity.class)
                .addAnnotatedClass(CategoryEntity.class)
                .addAnnotatedClass(BookEntity.class)
                .addAnnotatedClass(BorrowingTransactionEntity.class)
                .addAnnotatedClass(FineEntity.class)
                .getMetadataBuilder()
                .applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE)
                .build();

        return metadata.getSessionFactoryBuilder().build();
    }

    public static Session getSession() {
        return sessionFactory.openSession();
    }
}
