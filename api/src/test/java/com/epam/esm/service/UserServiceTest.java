package com.epam.esm.service;

import com.epam.esm.domain.User;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.mapper.UserMapper;
import com.epam.esm.repository.UserDao;
import com.epam.esm.service.impl.UserServiceImpl;
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
public class UserServiceTest {

    private static Paginator paginator;
    private static User user;
    private static UserDto userDto;
    @Mock
    private UserDao userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private TagService tagService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeAll
    static void initData() {
        user = User.builder().id(1L).username("name").surname("surname").build();
        paginator = new Paginator(1, 10);
        User u1 = User.builder().id(1L).username("name").surname("surname").build();
        User u2 = User.builder().id(1L).username("name").surname("surname").build();
        userDto = UserDto.builder().username("name").surname("surname").build();
    }

    @Test
    void getOne() {
        Mockito.when(userMapper.mapToDtoWithoutOrders(user)).thenReturn(userDto);
        Mockito.when(userRepository.getOne(1L)).thenReturn(Optional.of(user));

        Assertions.assertEquals(userDto, userService.getOne(1L));
        Mockito.verify(userRepository, times(1)).getOne(1L);
    }

    @Test
    void getAll() {
        List<UserDto> userDtoList = Collections.singletonList(userDto);
        List<User> userList = Collections.singletonList(user);

        Mockito.when(userMapper.mapToDtoWithoutOrders(user)).thenReturn(userDto);
        Mockito.when(userRepository.getAll(paginator)).thenReturn(userList);

        Assertions.assertEquals(userDtoList, userService.getAll(paginator));
        Mockito.verify(userRepository, times(1)).getAll(paginator);
    }


    @Test
    void getMostPopularUserTagByOrderSum() {
        Long userId = 1L;
        TagDto tagDto = TagDto.builder().id(1L).name("name").build();

        Mockito.when(userRepository.getOne(1L)).thenReturn(Optional.of(user));
        Mockito.when(tagService.getMostPopularTagByOrderSum(userId)).thenReturn(1L);
        Mockito.when(tagService.getOne(1L)).thenReturn(tagDto);

        Assertions.assertEquals(tagDto, userService.getMostPopularUserTagByOrderSum(userId));
    }
}
