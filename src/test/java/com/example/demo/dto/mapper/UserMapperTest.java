//package com.example.demo.dto.mapper;
//
//import com.example.demo.dto.response.UserResponseDTO;
//import com.example.demo.persistance.entity.User;
//import org.junit.jupiter.api.Test;
//import org.mapstruct.factory.Mappers;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class UserMapperTest {
//    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
//
//    @Test
//    void shouldMapUserToUserDTO() {
//        User user = new User("1", "John Doe", "john@example.com", 25);
//        UserResponseDTO userResponseDTO = userMapper.toDTO(user);
//
//        assertEquals(user.getId(), userResponseDTO.getId());
//        assertEquals(user.getName(), userResponseDTO.getName());
//        assertEquals(user.getEmail(), userResponseDTO.getEmail());
//        assertEquals(user.getAge(), userResponseDTO.getAge());
//    }
//
//}