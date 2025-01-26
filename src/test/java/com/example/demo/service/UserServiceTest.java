package com.example.demo.service;

import com.example.demo.dto.mapper.UserMapper;
import com.example.demo.dto.response.PaginatedResponseDTO;
import com.example.demo.dto.response.UserResponseDTO;
import com.example.demo.persistance.entity.User;
import com.example.demo.persistance.repository.UserRepository;
import com.example.demo.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testGetAllUsers() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 10);
        Page<User> mockPage = new PageImpl<>(List.of(
                new User("1", "John Doe", "john@example.com", 30),
                new User("2", "Jane Doe", "jane@example.com", 25)
        ));
        when(userRepository.findAll(pageable)).thenReturn(mockPage);

        // Act
        PaginatedResponseDTO<UserResponseDTO> result = userService.getAllUsers(pageable);

        // Assert
        assertEquals(2, result.getContent().size());
        assertEquals(1, result.getTotalPages());
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getPageSize());
        assertTrue(result.isFirst());
        assertTrue(result.isLast());
    }

    @Test
    void testGetUserById_ValidId() {
        // Arrange
        String userId = "1";
        User mockUser = new User(userId, "John Doe", "john@example.com", 30);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        UserResponseDTO result = userService.getUserById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        assertEquals(30, result.getAge());
    }

    @Test
    void testGetUserById_InvalidId() {
        // Arrange
        String userId = "999";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        UserResponseDTO result = userService.getUserById(userId);

        // Assert
        assertNull(result);
    }

    @Test
    void testCreateUser() {
        // Arrange
        User newUser = new User(null, "John Doe", "john@example.com", 30);
        User savedUser = new User("1", "John Doe", "john@example.com", 30);
        when(userRepository.save(newUser)).thenReturn(savedUser);

        // Act
        UserResponseDTO result = userService.createUser(newUser);

        // Assert
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        assertEquals(30, result.getAge());
    }

    @Test
    void testDeleteUserById() {
        // Arrange
        String userId = "1";
        doNothing().when(userRepository).deleteById(userId);

        // Act
        userService.deleteUserById(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testGetAllUsers_EmptyResult() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 10);
        Page<User> mockPage = new PageImpl<>(List.of());
        when(userRepository.findAll(pageable)).thenReturn(mockPage);

        // Act
        PaginatedResponseDTO<UserResponseDTO> result = userService.getAllUsers(pageable);

        // Assert
        assertEquals(0, result.getContent().size());
        assertEquals(1, result.getTotalPages());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getPageSize());
        assertTrue(result.isFirst());
        assertTrue(result.isLast());
    }

}