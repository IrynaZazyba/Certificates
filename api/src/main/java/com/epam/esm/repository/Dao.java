package com.epam.esm.repository;

import com.epam.esm.util.Paginator;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    Optional<T> getOne(Long id);

    List<T> getAll(Paginator paginator);

}
