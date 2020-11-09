package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapper.TagMapper;
import com.epam.esm.service.impl.TagServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

    @Mock
    private TagDao mockTagDao;
    @Mock
    private TagMapper tagMapper;
    @InjectMocks
    private TagServiceImpl tagService;
    private static Tag tag;
    private static TagDto tagDto;

    @BeforeAll
    static void initData() {
        tag = Tag.builder().id(1L).name("name").build();
        tagDto = TagDto.builder().id(1L).name("name").build();
    }

    @Test
    void getTag() {
        Mockito.when(tagMapper.fromModelWithoutCertificate(tag)).thenReturn(tagDto);
        Mockito.when(mockTagDao.getOne(1L)).thenReturn(Optional.of(tag));

        Assertions.assertEquals(tagDto, tagService.getOne(1L));
        Mockito.verify(mockTagDao, times(1)).getOne(1L);
    }

    @Test
    void getAllTags() {
        List<TagDto> tagDtoList = Collections.singletonList(tagDto);
        List<Tag> tagList = Collections.singletonList(tag);

        Mockito.when(tagMapper.fromModelWithoutCertificate(tag)).thenReturn(tagDto);
        Mockito.when(mockTagDao.getAll()).thenReturn(tagList);

        Assertions.assertEquals(tagDtoList, tagService.getAll());
        Mockito.verify(mockTagDao, times(1)).getAll();
    }

    @Test
    void createTag() {
        Mockito.when(tagMapper.toModelWithoutCertificate(tagDto)).thenReturn(tag);
        Mockito.when(mockTagDao.getByName(tag.getName())).thenReturn(Optional.empty());
        Mockito.when(mockTagDao.insert(tag)).thenReturn(tag);
        Mockito.when(tagMapper.fromModelWithoutCertificate(tag)).thenReturn(tagDto);

        Assertions.assertEquals(tagDto, tagService.create(tagDto));
        Mockito.verify(mockTagDao, times(1)).getByName(tag.getName());
    }

    @Test
    void deleteTag() {
        Mockito.doNothing().when(mockTagDao).delete(5L);
        Mockito.doNothing().when(mockTagDao).deleteTagLink(5L);

        tagService.delete(5L);

        Mockito.verify(mockTagDao, times(1)).delete(5L);
        Mockito.verify(mockTagDao, times(1)).deleteTagLink(5L);
    }


}
