package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
import com.epam.esm.util.Paginator;

import java.util.List;

public interface TagService {

    TagDto getOne(Long id);

    List<TagDto> getAll(Paginator paginator);

    TagDto create(TagDto tag);

    void delete(Long id);

    Long getMostPopularTagByOrderSum(Long userId);
}
