package com.epam.esm.dto.mapper;

import com.epam.esm.domain.User;
import com.epam.esm.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Data
@AllArgsConstructor
public class UserMapper {

    private final OrderMapper orderMapper;

    public UserDto mapToDtoWithOrders(User user) {
        return UserDto.builder()
                .id(user.getId())
                .surname(user.getSurname())
                .username(user.getUsername())
                .orders(Objects.isNull(user.getOrders()) ? null : orderMapper.toDtoList(user.getOrders()))
                .build();
    }

    public UserDto mapToDtoWithoutOrders(User user) {
        return UserDto.builder()
                .id(user.getId())
                .surname(user.getSurname())
                .username(user.getUsername())
                .build();
    }

    public User mapToModel(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .surname(userDto.getSurname())
                .username(userDto.getUsername())
                .orders(Objects.isNull(userDto.getOrders()) ? null : orderMapper.toModelList(userDto.getOrders()))
                .build();
    }


}
