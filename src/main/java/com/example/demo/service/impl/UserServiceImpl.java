package com.example.demo.service.impl;

import com.example.demo.dto.mapper.UserMapper;
import com.example.demo.dto.response.PaginatedResponseDTO;
import com.example.demo.dto.response.UserResponseDTO;
import com.example.demo.persistance.entity.User;
import com.example.demo.persistance.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public PaginatedResponseDTO<UserResponseDTO> getAllUsers(PageRequest pageable) {
        log.info("DEMO project | processing request : {} ", pageable.toString());
        Page<User> usersPage = userRepository.findAll(pageable);
        List<UserResponseDTO> userDTOs = usersPage.getContent().stream()
                .map( user -> {
//                    System.out.println(user.toString());
//                    UserResponseDTO userResponseDTO = userMapper.toDTO(user);
//                    System.out.println(userResponseDTO.toString());
//                    return userResponseDTO;
                    return new UserResponseDTO(user.getId(), user.getName(), user.getEmail() , user.getAge());

                } )
                .toList();
        return new PaginatedResponseDTO<>(
                userDTOs,
                usersPage.getTotalPages(),
                usersPage.getTotalElements(),
                usersPage.getNumber(),
                usersPage.getSize(),
                usersPage.isLast(),
                usersPage.isFirst()
        );
    }

    public UserResponseDTO getUserById(String id) {
        log.info("DEMO project | processing id : {} ", id);
        User user = userRepository.findById(id).orElse(null);
        if(user == null){
            return null;
        }
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail() , user.getAge());
    }

    public UserResponseDTO createUser(User user) {
        log.info("DEMO project | processing Request : {} ", user.toString());
        User savedUser = userRepository.save(user);
        return new UserResponseDTO(savedUser.getId(), savedUser.getName(), savedUser.getEmail() , savedUser.getAge());
    }

    public void deleteUserById(String id) {
        log.info("DEMO project | processing id : {} ", id);
        userRepository.deleteById(id);
    }


}
