package com.epam.esm.dao;

import com.epam.esm.domain.Tag;

import java.util.Optional;

public interface TagDao extends Dao<Tag> {

    void deleteTagLink(Long id);

    Optional<Tag> getByName(String name);
}
