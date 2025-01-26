package com.example.demo.service;

import com.example.demo.dto.request.UserRequestDTO;
import com.example.demo.dto.response.PaginatedResponseDTO;
import com.example.demo.dto.response.UserResponseDTO;
import com.example.demo.persistance.entity.User;
import org.springframework.data.domain.PageRequest;

public interface UserService {
    PaginatedResponseDTO<UserResponseDTO> getAllUsers(PageRequest pageable);
    UserResponseDTO getUserById(String id);
    UserResponseDTO createUser(UserRequestDTO userRequestDTO);
    UserResponseDTO updateUser(String id , UserRequestDTO userRequestDTO);
    void deleteUserById(String id);
}
