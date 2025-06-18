package com.form.creation.final_project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.form.creation.final_project.model.User;
import com.form.creation.final_project.repository.UserRepository;

@Service
public class UserServices {
    @Autowired
    private UserRepository userRepository;

    public User checkCredentials(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password).orElseThrow(null);

    }
}
