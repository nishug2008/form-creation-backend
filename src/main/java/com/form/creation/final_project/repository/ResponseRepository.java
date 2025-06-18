package com.form.creation.final_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.form.creation.final_project.model.Form;
import com.form.creation.final_project.model.Response;
import com.form.creation.final_project.model.User;

public interface ResponseRepository extends JpaRepository<Response, Long> {
    List<Response> findByForm(Form form);

    List<Response> findByUser(User user);
}