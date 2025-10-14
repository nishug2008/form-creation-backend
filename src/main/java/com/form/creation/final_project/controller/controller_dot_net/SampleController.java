package com.form.creation.final_project.controller.controller_dot_net;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class SampleController {

    private final String DOTNET_API_URL = "http://localhost:5022/api/books";

    @GetMapping("/getBooks")
    public String getBookFromDotNet() {
        System.out.println("Get Book Controller");
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.getForEntity(DOTNET_API_URL, String.class);

        return response.getBody();
    }

}
