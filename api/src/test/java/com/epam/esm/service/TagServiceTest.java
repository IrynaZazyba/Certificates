package com.epam.esm.service;

import com.epam.esm.domain.Tag;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapper.TagMapper;
import com.epam.esm.repository.TagDao;
import com.epam.esm.service.impl.TagServiceImpl;
import com.epam.esm.util.Paginator;
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

    private static Tag tag;
    private static TagDto tagDto;
    private static Paginator paginator;
    @Mock
    private TagDao tagDao;
    @Mock
    private TagMapper tagMapper;
    @InjectMocks
    private TagServiceImpl tagService;

    @BeforeAll
    static void initData() {
        tag = Tag.builder().id(1L).name("name").build();
        tagDto = TagDto.builder().id(1L).name("name").build();
        paginator = new Paginator(1, 10);
    }

    @Test
    void getTag() {
        Mockito.when(tagMapper.fromModelWithoutCertificate(tag)).thenReturn(tagDto);
        Mockito.when(tagDao.getOne(1L)).thenReturn(Optional.of(tag));

        Assertions.assertEquals(tagDto, tagService.getOne(1L));
        Mockito.verify(tagDao, times(1)).getOne(1L);
    }

    @Test
    void getAllTags() {
        List<TagDto> tagDtoList = Collections.singletonList(tagDto);
        List<Tag> tagList = Collections.singletonList(tag);

        Mockito.when(tagMapper.fromModelWithoutCertificate(tag)).thenReturn(tagDto);
        Mockito.when(tagDao.getAll(paginator)).thenReturn(tagList);

        Assertions.assertEquals(tagDtoList, tagService.getAll(paginator));
        Mockito.verify(tagDao, times(1)).getAll(paginator);
    }

    @Test
    void createTag() {
        Mockito.when(tagMapper.toModelWithoutCertificate(tagDto)).thenReturn(tag);
        Mockito.when(tagDao.findByName(tag.getName())).thenReturn(Optional.empty());
        Mockito.when(tagDao.save(tag)).thenReturn(tag);
        Mockito.when(tagMapper.fromModelWithoutCertificate(tag)).thenReturn(tagDto);

        Assertions.assertEquals(tagDto, tagService.create(tagDto));
        Mockito.verify(tagDao, times(1)).findByName(tag.getName());
    }

    @Test
    void deleteTag() {
        Mockito.doNothing().when(tagDao).deleteById(5L);

        tagService.delete(5L);
        Mockito.verify(tagDao, times(1)).deleteById(5L);
    }

    @Test
    void getMostPopularTagByOrderSum() {
        Long userId = 5L;
        Mockito.when(tagDao.filterTags(userId)).thenReturn(1L);

        Assertions.assertEquals(1L, tagService.getMostPopularTagByOrderSum(userId));
        Mockito.verify(tagDao, times(1)).filterTags(5L);
    }
}
