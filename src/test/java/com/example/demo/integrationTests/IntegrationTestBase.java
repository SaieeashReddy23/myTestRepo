package com.example.demo.integrationTests;

import com.example.demo.dto.response.UserResponseDTO;
import com.example.demo.persistance.entity.User;
import com.example.demo.persistance.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.MongoDBContainer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class IntegrationTestBase {
    // Define and start the MongoDB container
    static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.4");

    static {
        mongoDBContainer.start();
        System.setProperty("spring.data.mongodb.uri", mongoDBContainer.getReplicaSetUrl());
    }

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void cleanup() {
        userRepository.deleteAll();
    }

}
