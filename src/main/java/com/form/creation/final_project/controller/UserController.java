package com.form.creation.final_project.controller;

import org.springframework.web.bind.annotation.RestController;

import com.form.creation.final_project.dto.LoginResponse;
import com.form.creation.final_project.exception.MyException;
import com.form.creation.final_project.jwt.JwtUtils;
import com.form.creation.final_project.model.User;
import com.form.creation.final_project.repository.UserRepository;
import com.form.creation.final_project.security.CustomUserDetails;
import com.form.creation.final_project.service.UserServices;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserServices userServices;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> userDetails) {

        String email = (String) userDetails.get("email");
        String rawPassword = (String) userDetails.get("password");
        String firstName = (String) userDetails.get("firstName");
        String lastName = (String) userDetails.get("lastName");
        String role = (String) userDetails.get("role");

        String encodedPassword = passwordEncoder.encode(rawPassword);

        User user = new User(firstName, lastName, email, encodedPassword, role);

        try {
            userRepository.save(user);
            return ResponseEntity.ok("User Created Successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("User cannot be created at this moment");
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> loginRequest) {
        String email = (String) loginRequest.get("email");
        String password = (String) loginRequest.get("password");

        try {
            // Authenticate using AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Get user details from CustomUserDetails
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            boolean matches = passwordEncoder.matches(password, userDetails.getPassword());

            System.out.println("DEBUG: Raw password = " + password);
            System.out.println("DEBUG: Encoded password from DB = " + userDetails.getPassword());
            System.out.println("DEBUG: Matches? " + matches);

            // Generate JWT
            String jwt = jwtUtils.generateTokenFromUsername(userDetails);

            // Create LoginResponse DTO
            LoginResponse response = new LoginResponse(
                    userDetails.getUsername(),
                    userDetails.getUser().getRole(),
                    jwt);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid credentials");

        }
    }

    @GetMapping("/getUser")
    public User getUser(@RequestBody Map<String, Object> requestBody) {
        String email = (String) requestBody.get("email");
        return userServices.getUserByEmail(email);
    }

    @GetMapping("/all")
    public List<User> getAllUser() {
        return userServices.findAllUsers();
    }

    @PutMapping("/forgot/password")
    public ResponseEntity<?> putMethodName(@RequestBody Map<String, Object> userDetails) {
        // TODO: process PUT request
        String email = (String) userDetails.get("email");
        String newPassword = (String) userDetails.get("newPassword");
        if (email == null || newPassword == null) {
            return ResponseEntity.badRequest().body("Email and new password are required");
        }
        User user = userServices.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body("User with the given email not found");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return ResponseEntity.ok("Password updated successfully. Please login with new password");
    }

    @PatchMapping("/update/username")
    public ResponseEntity<?> updateUserName(@RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> updates) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtils.getUsernameFromJwtToken(token);
        User user = userServices.getUserByEmail(email);
        if (updates.containsKey("firstName")) {
            user.setFirstName((String) updates.get("firstName"));
        }
        userRepository.save(user);
        return ResponseEntity.ok("User Name has been succesfully updated");
    }
}