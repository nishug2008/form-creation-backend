package com.form.creation.final_project.repository;

import com.form.creation.final_project.model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);
}
