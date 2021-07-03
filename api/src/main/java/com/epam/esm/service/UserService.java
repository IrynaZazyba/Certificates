package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.util.Paginator;

import java.util.List;

public interface UserService {

    UserDto getOne(Long id);

    List<UserDto> getAll(Paginator paginator);

    TagDto getMostPopularUserTagByOrderSum(Long userId);
}
