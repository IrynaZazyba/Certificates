package com.epam.esm.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    Optional<T> getOne(Long id);

    List<T> getAll();

    T insert(T entity);

    void delete(Long id);

}
