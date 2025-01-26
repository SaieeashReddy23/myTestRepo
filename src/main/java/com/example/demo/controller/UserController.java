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
        log.info("DEMO project | CorrelationId : {} | Get request for /api/users | Request - pageNumber : {} , pageSize : {}", MDC.get(Constants.CORRELATIONID) ,page , size);
        PaginatedResponseDTO<UserResponseDTO> paginatedResponseDTO = userService.getAllUsers(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC , "name")));
        log.info("DEMO project | CorrelationId : {} | Get request for /api/users | Response - {}", MDC.get(Constants.CORRELATIONID) , paginatedResponseDTO.toString());
        return ResponseEntity.ok(paginatedResponseDTO);
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String id) {
        log.info("DEMO project | CorrelationId : {} | Get request for /api/users/{} | Request - pathVariable - id : {} ", MDC.get(Constants.CORRELATIONID) , id , id );
        UserResponseDTO user = userService.getUserById(id);
        log.info("DEMO project | CorrelationId : {} | Get request for /api/users/{} | Response - {} ",  MDC.get(Constants.CORRELATIONID) ,  id , user != null ? user.toString() : " Not found " );
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    // Create a new user
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        // Map UserRequest to User entity
        User user = new User();
        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        user.setAge(userRequestDTO.getAge());
        log.info("DEMO project | CorrelationId : {} | Post request for /api/users | Request - {} ",  MDC.get(Constants.CORRELATIONID) ,  userRequestDTO.toString() );
        UserResponseDTO createdUser = userService.createUser(user);
        log.info("DEMO project | CorrelationId : {} | Get request for /api/users | Response - {} ",  MDC.get(Constants.CORRELATIONID) ,  createdUser.toString() );
        return ResponseEntity.ok(createdUser);
    }

    // Delete user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable String id) {
        log.info("DEMO project | CorrelationId : {} | Delete request for /api/users/{} | Request - pathVariable - id : {} ",  MDC.get(Constants.CORRELATIONID) , id , id );
        userService.deleteUserById(id);
        log.info("DEMO project | CorrelationId : {} | Delete request for /api/users/{} | Successfully deleted ",  MDC.get(Constants.CORRELATIONID) , id );
        return ResponseEntity.noContent().build();
    }
}
