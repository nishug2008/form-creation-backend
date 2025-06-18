package com.form.creation.final_project.controller;

import org.springframework.web.bind.annotation.RestController;

import com.form.creation.final_project.model.User;
import com.form.creation.final_project.repository.UserRepository;
import com.form.creation.final_project.service.UserServices;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServices userServices;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> userDetails) {

        String email = (String) userDetails.get("email");
        String password = (String) userDetails.get("password");
        String name = (String) userDetails.get("name");
        String role = (String) userDetails.get("role");
        User user = new User(name, email, password, role);

        try {
            userRepository.save(user);
            return ResponseEntity.ok("User Created Successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("User Can not be created at this moment");
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> postMethodName(@RequestBody Map<String, Object> credentials) {
        String email = (String) credentials.get("email");
        String password = (String) credentials.get("password");

        User myUser = userServices.checkCredentials(email, password);
        if (myUser != null) {
            return ResponseEntity.ok(myUser);
        } else {
            return ResponseEntity.status(401).body("Invalid Credential");
        }
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

}
