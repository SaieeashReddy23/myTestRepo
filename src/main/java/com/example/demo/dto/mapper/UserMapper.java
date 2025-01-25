package com.example.demo.dto.mapper;

import com.example.demo.dto.response.UserResponseDTO;
import com.example.demo.persistance.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserResponseDTO userResponseDTO);
    UserResponseDTO toDTO(User user);
}
