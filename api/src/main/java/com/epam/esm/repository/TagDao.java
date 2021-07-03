package com.epam.esm.repository;

import com.epam.esm.domain.Tag;

import java.util.Optional;

public interface TagDao extends Dao<Tag> {

    Long filterTags(Long userId);

    Optional<Tag> findByName(String name);

    Tag save(Tag tag);

    void deleteById(Long id);

}
