package com.form.creation.final_project.controller;

import com.form.creation.final_project.model.*;
import com.form.creation.final_project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/forms")
public class FormController {

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create/{userId}")
    public String createForm(@PathVariable Long userId, @RequestBody Form form) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null)
            return "User not found!";
        form.setCreatedBy(user);
        formRepository.save(form);
        return "Form created successfully!";
    }

    @GetMapping("/all")
    public List<Form> getAllForms() {
        return formRepository.findAll();
    }

    @GetMapping("/user/{userId}")
    public List<Form> getFormsByUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return (user == null) ? null : formRepository.findByCreatedBy(user);
    }

    @GetMapping("/{formId}")
    public Form getFormById(@PathVariable Long formId) {
        return formRepository.findById(formId).orElse(null);
    }

    @DeleteMapping("/delete/{formId}")
    public String deleteForm(@PathVariable Long formId) {
        if (!formRepository.existsById(formId))
            return "Form not found!";
        formRepository.deleteById(formId);
        return "Form deleted successfully!";
    }
}