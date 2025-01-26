package com.example.demo.controller;

import com.example.demo.dto.request.UserRequestDTO;
import com.example.demo.dto.response.PaginatedResponseDTO;
import com.example.demo.dto.response.UserResponseDTO;
import com.example.demo.persistance.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.utils.Constants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Validated
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Get all users With Pagination
    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<UserResponseDTO>> getAllUsers(@Min(value = 0 , message = "Page number should not be negative") @RequestParam(defaultValue = "0") int page,
                                                                             @Min(value = 5 , message = "Min page size should be greater than 5")  @RequestParam(defaultValue = "10") int size) {
        log.info("DEMO project | Get request for /api/users | Request - pageNumber : {} , pageSize : {}", page , size);
        PaginatedResponseDTO<UserResponseDTO> paginatedResponseDTO = userService.getAllUsers(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC , "name")));
        log.info("DEMO project | Get request for /api/users | Response - {}", paginatedResponseDTO.toString());
        return ResponseEntity.ok(paginatedResponseDTO);
    }

    // Get user by ID
    @Cacheable(value = "apiResponses" , key = "#id")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String id) {
        log.info("DEMO project | Get request for /api/users/{} | Request - pathVariable - id : {} ", id , id );
        UserResponseDTO user = userService.getUserById(id);
        log.info("DEMO project | Get request for /api/users/{} | Response - {} ",  id , user != null ? user.toString() : " Not found " );
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    // Update a user
    @CachePut(value = "apiResponses" , key = "#id")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser( @PathVariable String id ,  @Valid @RequestBody UserRequestDTO userRequestDTO) {
        log.info("DEMO project  | Put request for /api/users | Request - userId : {} , updatedUser {} ",id , userRequestDTO.toString() );
        UserResponseDTO createdUser = userService.updateUser(id , userRequestDTO);
        log.info("DEMO project  | Put request for /api/users | Response - {} ",  createdUser.toString() );
        return ResponseEntity.ok(createdUser);
    }

    // Create a new user
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        log.info("DEMO project  | Post request for /api/users | Request - {} ",  userRequestDTO.toString() );
        UserResponseDTO createdUser = userService.createUser(userRequestDTO);
        log.info("DEMO project  | Get request for /api/users | Response - {} ",  createdUser.toString() );
        return ResponseEntity.ok(createdUser);
    }

    // Delete user by ID
    @CacheEvict(value = "apiResponses" , key = "#id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable String id) {
        log.info("DEMO project  | Delete request for /api/users/{} | Request - pathVariable - id : {} ", id , id );
        userService.deleteUserById(id);
        log.info("DEMO project  | Delete request for /api/users/{} | Successfully deleted ", id );
        return ResponseEntity.noContent().build();
    }
}
