package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapper.TagMapper;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.impl.TagServiceImpl;
import com.epam.esm.service.validation.impl.TagValidatorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

    @Mock
    private TagDao mockTagDao;
    @Mock
    private TagMapper tagMapper;
    @Mock
    private TagValidatorImpl mockTagValidator;
    @InjectMocks
    private TagServiceImpl tagService;

    @Test
    void getTag() {
        Tag tag = Tag.builder().id(1L).name("name").build();
        TagDto tagDto = TagDto.builder().id(1L).name("name").build();
        Mockito.when(tagMapper.fromModelWithoutCertificate(tag)).thenReturn(tagDto);
        Mockito.when(mockTagDao.getOne(1L)).thenReturn(tag);
        Assertions.assertEquals(tagDto, tagService.getTag(1L));
    }

    @Test
    void getAllTags() {
        Tag tag = Tag.builder().id(1L).name("name").build();
        TagDto tagDto = TagDto.builder().id(1L).name("name").build();
        List<TagDto> tagDtoList = Collections.singletonList(tagDto);
        List<Tag> tagList = Collections.singletonList(tag);
        Mockito.when(tagMapper.fromModelWithoutCertificate(tag)).thenReturn(tagDto);
        Mockito.when(mockTagDao.getAllTags()).thenReturn(tagList);
        Assertions.assertEquals(tagDtoList, tagService.getAllTags());
    }

    @Test
    void createTag() {
        Tag tag = Tag.builder().name("names").build();
        TagDto tagDto = TagDto.builder().id(5L).name("names").build();
        Mockito.when(mockTagValidator.validate(tagDto)).thenReturn(true);
        Mockito.when(tagMapper.toModelWithoutCertificate(tagDto)).thenReturn(tag);
        Mockito.when(mockTagDao.getByName(tag.getName())).thenThrow(ResourceNotFoundException.class);
        Mockito.when(mockTagDao.insertTag(tag)).thenReturn(5L);
        Assertions.assertEquals(5, tagService.createTag(tagDto));
    }

    @Test
    void deleteTag() {
        Mockito.doNothing().when(mockTagDao).deleteTag(5L);
        Mockito.doNothing().when(mockTagDao).deleteTagLink(5L);
        tagService.deleteTag(5L);
        Mockito.verify(mockTagDao, times(1)).deleteTag(5L);
        Mockito.verify(mockTagDao, times(1)).deleteTagLink(5L);
    }

}
