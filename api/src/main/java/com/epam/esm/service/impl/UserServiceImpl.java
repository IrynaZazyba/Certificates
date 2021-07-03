package com.epam.esm.service.impl;

import com.epam.esm.domain.User;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.mapper.UserMapper;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.repository.UserDao;
import com.epam.esm.service.TagService;
import com.epam.esm.service.UserService;
import com.epam.esm.util.Paginator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final UserMapper userMapper;
    private final TagService tagService;

    @Override
    public UserDto getOne(Long id) {
        User user = userDao.getOne(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found", id));
        return userMapper.mapToDtoWithoutOrders(user);
    }

    @Override
    public List<UserDto> getAll(Paginator paginator) {
        return userDao.getAll(paginator).stream().map(userMapper::mapToDtoWithoutOrders).collect(Collectors.toList());
    }

    @Override
    public TagDto getMostPopularUserTagByOrderSum(Long userId) {
        getOne(userId);
        Long tagId = tagService.getMostPopularTagByOrderSum(userId);
        return tagService.getOne(tagId);
    }
}
