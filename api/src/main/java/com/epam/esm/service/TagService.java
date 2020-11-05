package com.epam.esm.service;

import com.epam.esm.dto.TagDto;

import java.util.List;

public interface TagService {

    TagDto getTag(Long id);

    List<TagDto> getAllTags();

    TagDto createTag(TagDto tag);

    void deleteTag(Long id);
}
