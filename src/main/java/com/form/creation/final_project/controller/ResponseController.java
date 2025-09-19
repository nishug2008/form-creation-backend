package com.form.creation.final_project.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.form.creation.final_project.model.Form;
import com.form.creation.final_project.model.Question;
import com.form.creation.final_project.model.Response;
import com.form.creation.final_project.model.ResponseEntry;
import com.form.creation.final_project.model.User;
import com.form.creation.final_project.repository.FormRepository;
import com.form.creation.final_project.repository.QuestionRepository;
import com.form.creation.final_project.repository.ResponseRepository;
import com.form.creation.final_project.repository.UserRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/responses")
public class ResponseController {
    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private FormRepository formRepository;

    @PostMapping("/submit/{formId}/{userId}")
    public ResponseEntity<String> submitForm(
            @PathVariable Long formId,
            @PathVariable Long userId,
            @RequestBody Map<Long, String> answers) {
        System.out.println(userId);
        Form form = formRepository.findById(formId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        System.out.println(form.toString());
        System.out.println("code reached here");
        if (user == null || form == null) {
            return ResponseEntity.badRequest().body("Invalid form Id or user Id");
        }

        Response response = new Response();
        response.setForm(form);
        response.setUser(user);

        List<ResponseEntry> responseEntries = new ArrayList<>();
        for (Map.Entry<Long, String> entry : answers.entrySet()) {
            Long questionId = entry.getKey();
            String answer = entry.getValue();

            Question question = questionRepository.findById(questionId).orElse(null);
            if (question != null) {

                ResponseEntry responseEntry = new ResponseEntry();
                responseEntry.setQuestion(question);
                responseEntry.setAnswertext(answer);
                responseEntry.setResponse(response);
                responseEntries.add(responseEntry);
            }
        }
        response.setResponseEntries(responseEntries);
        responseRepository.save(response);
        return ResponseEntity.ok("Form Response submitted successfully");
    }

    @GetMapping("/user/{userId}")
    public List<Response> getResponsesByUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return responseRepository.findByUser(user);
    }
}
