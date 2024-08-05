package com.mvo.restapi.repository;

import java.util.List;

public interface GenericRepository <T,ID> {
    T findById(ID id);
    List<T> findAll();
    T save(T entity);
    T update(T entity);
    void deleteById(ID id);
}
