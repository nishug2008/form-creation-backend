package com.form.creation.final_project.controller;

import org.springframework.web.bind.annotation.RestController;

import com.form.creation.final_project.dto.LoginResponse;
import com.form.creation.final_project.exception.MyException;
import com.form.creation.final_project.jwt.JwtUtils;
import com.form.creation.final_project.model.User;
import com.form.creation.final_project.repository.UserRepository;
import com.form.creation.final_project.security.CustomUserDetails;

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
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

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
        return userRepository.findByEmail(email).orElse(null);
    }

    @GetMapping("/all")
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

}