package com.example.demo.service;

import com.example.demo.dto.mapper.UserMapper;
import com.example.demo.dto.response.PaginatedResponseDTO;
import com.example.demo.dto.response.UserResponseDTO;
import com.example.demo.persistance.entity.User;
import com.example.demo.persistance.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;



@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public PaginatedResponseDTO<UserResponseDTO> getAllUsers(PageRequest pageable) {
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
        User user = userRepository.findById(id).orElse(null);
        if(user == null){
            return null;
        }

        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail() , user.getAge());
    }

    public UserResponseDTO createUser(User user) {
        User savedUser = userRepository.save(user);
        return new UserResponseDTO(savedUser.getId(), savedUser.getName(), savedUser.getEmail() , savedUser.getAge());
    }

    public void deleteUserById(String id) {
        userRepository.deleteById(id);
    }


}
