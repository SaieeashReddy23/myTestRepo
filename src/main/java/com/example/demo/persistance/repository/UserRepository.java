package com.example.demo.persistance.repository;

import com.example.demo.persistance.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User , String> {
    User findByEmail(String email);
}
