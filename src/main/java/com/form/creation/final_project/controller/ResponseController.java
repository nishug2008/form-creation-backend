package com.form.creation.final_project.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.form.creation.final_project.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.form.creation.final_project.model.Form;
import com.form.creation.final_project.model.Question;
import com.form.creation.final_project.model.Response;
import com.form.creation.final_project.model.ResponseEntry;
import com.form.creation.final_project.model.User;
import com.form.creation.final_project.repository.FormRepository;
import com.form.creation.final_project.repository.QuestionRepository;
import com.form.creation.final_project.repository.ResponseRepository;
import com.form.creation.final_project.repository.UserRepository;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

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

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/submit/{formId}")
    public ResponseEntity<String> submitForm(
            @PathVariable Long formId,
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<Long, String> answers) {

        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtils.getUsernameFromJwtToken(token);

        Form form = formRepository.findById(formId).orElse(null);
        User user = userRepository.findByEmail(email).orElse(null);

        System.out.println("-------------------" + token);
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

    @GetMapping("/user")
    public List<Response> getResponsesByUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtils.getUsernameFromJwtToken(token);

        System.out.println(token);
        System.out.println(email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return responseRepository.findByUser(user);
    }

    @PutMapping("/update/{responseId}")
    public ResponseEntity<String> editResponses(@PathVariable Long responseId,
                                                @RequestBody Map<Long, String> newAnswers) {
        Response response = responseRepository.findById(responseId).orElse(null);

        List<ResponseEntry> responseEntry = response.getResponseEntries();

        if(responseEntry == null){
            return ResponseEntity.badRequest().body("Data not found");
        }

        for(ResponseEntry entry : responseEntry){
            Long questionId = entry.getQuestion().getQuestionId();
            if(newAnswers.containsKey(questionId)){
                entry.setAnswertext(newAnswers.get(questionId));
            }
        }

        responseRepository.save(response);
        return ResponseEntity.ok("Updated Successfully");
    }
}
