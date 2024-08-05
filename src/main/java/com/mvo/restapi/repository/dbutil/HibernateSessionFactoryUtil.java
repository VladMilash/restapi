package com.mvo.restapi.repository.dbutil;

import com.mvo.restapi.model.Event;
import com.mvo.restapi.model.File;
import com.mvo.restapi.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.io.InputStream;
import java.util.Properties;

public class HibernateSessionFactoryUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private HibernateSessionFactoryUtil() {
    }

    private static SessionFactory buildSessionFactory() {
        try {
            Properties properties = new Properties();
            try (InputStream inputStream = HibernateSessionFactoryUtil.class
                    .getClassLoader().getResourceAsStream("hibernate.properties")) {
                if (inputStream != null) {
                    properties.load(inputStream);
                } else {
                    throw new RuntimeException("hibernate.properties file not found");
                }
            }

            StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();
            serviceRegistryBuilder.applySettings(properties);
            ServiceRegistry serviceRegistry = serviceRegistryBuilder.build();

            MetadataSources metadataSources = new MetadataSources(serviceRegistry);
            metadataSources.addAnnotatedClass(User.class);
            metadataSources.addAnnotatedClass(Event.class);
            metadataSources.addAnnotatedClass(File.class);

            Metadata metadata = metadataSources.getMetadataBuilder().build();
            return metadata.getSessionFactoryBuilder().build();

        } catch (Exception ex) {
            throw new ExceptionInInitializerError("Initial SessionFactory creation failed." + ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
