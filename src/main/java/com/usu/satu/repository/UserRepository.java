package com.usu.satu.repository;

import com.usu.satu.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findUserByUserIdAndIsDeletedIsFalse(String userId);
}
