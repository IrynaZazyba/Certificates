package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapper.TagMapper;
import com.epam.esm.exception.InvalidUserDataException;
import com.epam.esm.exception.ResourceAlreadyExistException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;
    private final TagMapper tagMapper;
    private final Validator<TagDto> validator;

    @Override
    public TagDto getTag(Long id) {
        Tag tag = tagDao.getOne(id);
        return tagMapper.fromModelWithoutCertificate(tag);
    }

    @Override
    public List<TagDto> getAllTags() {
        return tagDao.getAllTags().stream().map(tagMapper::fromModelWithoutCertificate).collect(Collectors.toList());
    }

    @Override
    public Long createTag(TagDto tag) {
        if (!validator.validate(tag)) {
            throw new InvalidUserDataException("Invalid user data");
        }
        try {
            Tag byName = tagDao.getByName(tag.getName());
            throw new ResourceAlreadyExistException("Resource already exists", byName.getId());
        } catch (ResourceNotFoundException ex) {
            return tagDao.insertTag(tagMapper.toModelWithoutCertificate(tag));
        }
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagDao.deleteTagLink(id);
        tagDao.deleteTag(id);
    }
}
