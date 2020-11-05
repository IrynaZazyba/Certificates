package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface TagService {

    TagDto getOne(Long id);

    List<TagDto> getAll();

    TagDto create(TagDto tag);

    void delete(Long id);
}
