package com.epam.esm.service.impl;

import com.epam.esm.domain.Tag;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapper.TagMapper;
import com.epam.esm.exception.ResourceAlreadyExistException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.repository.TagDao;
import com.epam.esm.service.TagService;
import com.epam.esm.util.Paginator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagMapper tagMapper;
    private final TagDao tagDao;

    @Override
    public TagDto getOne(Long id) {
        Tag tag = tagDao.getOne(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found", id));
        return tagMapper.fromModelWithoutCertificate(tag);
    }

    @Override
    public List<TagDto> getAll(Paginator paginator) {
        return tagDao.getAll(paginator)
                .stream()
                .map(tagMapper::fromModelWithoutCertificate)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TagDto create(TagDto tag) {
        Optional<Tag> byName = tagDao.findByName(tag.getName());
        if (byName.isEmpty()) {
            Tag insertedTag = tagDao.save(tagMapper.toModelWithoutCertificate(tag));
            return tagMapper.fromModelWithoutCertificate(insertedTag);
        } else {
            throw new ResourceAlreadyExistException("Resource already exists", byName.get().getId());
        }
    }

    @Transactional
    @Override
    public void delete(Long id) {
        tagDao.deleteById(id);
    }

    @Override
    public Long getMostPopularTagByOrderSum(Long userId) {
        return tagDao.filterTags(userId);
    }
}
