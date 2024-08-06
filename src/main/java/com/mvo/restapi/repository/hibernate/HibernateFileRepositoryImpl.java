package com.mvo.restapi.repository.hibernate;

import com.mvo.restapi.exception.NotExistCrudException;
import com.mvo.restapi.model.File;
import com.mvo.restapi.repository.FileRepository;
import com.mvo.restapi.repository.dbutil.HibernateHelper;

import java.util.List;

public class HibernateFileRepositoryImpl implements FileRepository {
    private final HibernateHelper hibernateHelper = new HibernateHelper();

    @Override
    public File findById(Integer id) {
        File file = hibernateHelper.executeWithoutTransaction(session -> session.get(File.class, id));
        if (file == null) {
            throw new NotExistCrudException(id);
        }
        return file;
    }

    @Override
    public List<File> findAll() {
        return hibernateHelper.executeWithoutTransaction(session -> session.createQuery("FROM File", File.class).list());
    }

    @Override
    public File save(File file) {
        return hibernateHelper.executeWithTransaction(session -> {
            session.persist(file);
            return file;
        });
    }

    @Override
    public File update(File file) {
        return hibernateHelper.executeWithTransaction(session -> {
            session.merge(file);
            return file;
        });
    }

    @Override
    public void deleteById(Integer id) {
        hibernateHelper.executeWithTransaction(session -> {
            File file = session.get(File.class, id);
            if (file == null) {
                throw new NotExistCrudException(id);
            }
            session.remove(file);
            return null;
        });
    }
}
