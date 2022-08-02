package com.usu.satu.service;

import com.usu.satu.exeption.AcceptedException;
import com.usu.satu.model.User;
import com.usu.satu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> getUsers () {
        return userRepository.findAll();
    }

    public User addUser (String userId) {
        Optional<User> optional = userRepository.findUserByUserIdAndIsDeletedIsFalse(userId);
        if (optional.isPresent()) {
            throw new AcceptedException("user "+userId+" is exist");
        } else {
            User data = new User();
            data.setUserId(userId);
            data.setDeleted(false);
            userRepository.save(data);
            return data;
        }
    }

    public User deleteUser (String userId) {
        Optional<User> optional = userRepository.findUserByUserIdAndIsDeletedIsFalse(userId);
        if (optional.isPresent()) {
            throw new AcceptedException("user "+userId+" is not exist");
        } else {
            User data = optional.get();
            data.setDeleted(true);
            userRepository.save(data);
            return data;
        }
    }
}
