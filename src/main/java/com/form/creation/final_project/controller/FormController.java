package com.form.creation.final_project.controller;

import com.form.creation.final_project.dto.FormDTO;
import com.form.creation.final_project.jwt.JwtUtils;
import com.form.creation.final_project.model.*;
import com.form.creation.final_project.repository.*;
import com.form.creation.final_project.service.ResponseService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/forms")
public class FormController {

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    ResponseService responseService;

    @Autowired
    public JwtUtils jwtUtils;

    @PostMapping("/create/{email}")
    public String createForm(@PathVariable String email,
            @RequestBody FormDTO formDTO) {

        User creator = userRepository.findByEmail(email).orElse(null);

        if (creator == null)
            return "User not found!";

        Form form = new Form();
        form.setTitle(formDTO.getFormTitle());
        form.setDescription(formDTO.getFormDescription());
        form.setCreatedBy(creator);
        Form savedForm = formRepository.save(form);
        System.out
                .println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        if (formDTO.getPermittedUserIds() != null && !formDTO.getPermittedUserIds().isEmpty()) {
            List<User> permittedUsers = userRepository.findAllById(formDTO.getPermittedUserIds());
            savedForm.setPermittedUsers(permittedUsers);
        }

        if (formDTO.getQuestions() != null) {

            for (Question question : formDTO.getQuestions()) {

                question.setForm(savedForm);
                if (question.getOptions() != null) {

                    question.getOptions().forEach(opt -> opt.setQuestion(question));

                }
            }
            savedForm.setQuestions(formDTO.getQuestions());
        }
        formRepository.save(savedForm);

        return "Form created successfully!";

    }

    @GetMapping("/all")
    public List<Form> getAllForms(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtils.getUsernameFromJwtToken(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getAccessibleForm();
    }

    @GetMapping("/user")
    public List<Form> getFormsByUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtils.getUsernameFromJwtToken(token);
        User user = userRepository.findByEmail(email).orElse(null);
        return (user == null) ? null : formRepository.findByCreatedBy(user);
    }

    @GetMapping("/{formId}")
    public Form getFormById(@PathVariable Long formId) {
        return formRepository.findById(formId).orElse(null);
    }

    // ------------------------ GET RESPONSES AS JSON ------------------------
    @GetMapping("/{formId}/responses")
    public ResponseEntity<List<Map<String, Object>>> getResponseAsJson(@PathVariable Long formId) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("Form Not Found"));

        List<Response> responses = responseService.getResponsesByForm(form);

        List<Map<String, Object>> result = new ArrayList<>();

        for (Response r : responses) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("userId", r.getUser().getId());
            for (ResponseEntry entry : r.getResponseEntries()) {
                row.put(entry.getQuestion().getText(), entry.getAnswertext());
            }
            result.add(row);
        }
        return ResponseEntity.ok(result);
    }

    // ------------------------ DOWNLOAD RESPONSES AS CSV--------------------------
    @GetMapping("/{formId}/responses/csv")
    public void downloadResponsesCsv(@PathVariable Long formId, HttpServletResponse response) throws IOException {
        Form form = formRepository.findById(formId).orElseThrow(() -> new RuntimeException("Form Not Found"));
        List<Response> responses = responseService.getResponsesByForm(form);

        // Collect all unique questions
        Set<String> allQuestions = new LinkedHashSet<>();
        for (Response r : responses) {
            for (ResponseEntry entry : r.getResponseEntries()) {
                allQuestions.add(entry.getQuestion().getText());
            }
        }

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"form_" + formId + "_responses.csv\"");
        PrintWriter writer = response.getWriter();

        // CSV header
        writer.print("Count");
        writer.print("," + "UserId");
        for (String q : allQuestions) {
            writer.print("," + q);
        }
        int i = 1;
        writer.println();
        for (Response r : responses) {
            Map<String, String> answersMap = new HashMap<>();

            for (ResponseEntry entry : r.getResponseEntries()) {
                answersMap.put(entry.getQuestion().getText(), entry.getAnswertext());
            }
            // writer.print(i);
            writer.print(i + "," + r.getUser().getId());
            for (String q : allQuestions) {
                writer.print("," + answersMap.getOrDefault(q, ""));
            }
            writer.println();
            i++;
        }
        writer.flush();
        writer.close();
    }

    @GetMapping("{formId}/response/count")
    public int getMethodName(@PathVariable Long formId) {
        Form form = formRepository.findById(formId).orElseThrow(() -> new RuntimeException("Form Not found"));
        List<Response> responses = responseService.getResponsesByForm(form);
        int i = 0;
        for (Response r : responses) {
            i++;
        }
        return i;
    }

    @DeleteMapping("/delete/{formId}")
    public String deleteForm(@PathVariable Long formId) {
        if (!formRepository.existsById(formId))
            return "Form not found!";
        formRepository.deleteById(formId);
        return "Form deleted successfully!";
    }

}