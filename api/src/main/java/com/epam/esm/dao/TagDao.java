package com.epam.esm.dao;

import com.epam.esm.domain.Tag;

import java.util.List;

public interface TagDao {

    Tag getOne(Long id);

    List<Tag> getAllTags();

    Long insertTag(Tag tag);

    void deleteTag(Long id);

    void deleteTagLink(Long id);

    Tag getByName(String name);
}
