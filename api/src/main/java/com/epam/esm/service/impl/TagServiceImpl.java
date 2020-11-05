package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapper.TagMapper;
import com.epam.esm.exception.ResourceAlreadyExistException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;
    private final TagMapper tagMapper;

    @Override
    public TagDto getTag(Long id) {
        Tag tag = tagDao.getOne(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found", id));
        return tagMapper.fromModelWithoutCertificate(tag);
    }

    @Override
    public List<TagDto> getAllTags() {
        return tagDao.getAll().stream().map(tagMapper::fromModelWithoutCertificate).collect(Collectors.toList());
    }

    @Override
    public TagDto createTag(TagDto tag) {
        Optional<Tag> byName = tagDao.getByName(tag.getName());
        if (byName.isEmpty()) {
            Tag insertedTag = tagDao.insert(tagMapper.toModelWithoutCertificate(tag));
            return tagMapper.fromModelWithoutCertificate(insertedTag);
        } else {
            throw new ResourceAlreadyExistException("Resource already exists", byName.get().getId());
        }
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagDao.deleteTagLink(id);
        tagDao.delete(id);
    }
}
