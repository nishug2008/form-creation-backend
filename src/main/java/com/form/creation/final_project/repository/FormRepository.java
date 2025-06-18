package com.form.creation.final_project.repository;

import com.form.creation.final_project.model.Form;
import com.form.creation.final_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormRepository extends JpaRepository<Form, Long> {
    List<Form> findByCreatedBy(User user);

}
