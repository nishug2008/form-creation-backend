package com.form.creation.final_project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.form.creation.final_project.exception.MyException;
import com.form.creation.final_project.model.User;
import com.form.creation.final_project.repository.UserRepository;
import com.form.creation.final_project.security.CustomUserDetails;

@Service
public class UserServices implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new CustomUserDetails(user);
    }

    public List<User> findAllUsers() throws MyException {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new MyException("Empty user");
        }
    }

    public User getUserByEmail(String email) throws MyException {
        return userRepository.findByEmail(email).orElse(null);
    }

}