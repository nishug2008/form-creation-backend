package com.form.creation.final_project.controller;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    private final JavaMailSender mailSender;

    public EmailController(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @GetMapping("/send-email")
    public String  sendEmail(){
      try{
           SimpleMailMessage message = new SimpleMailMessage();
           message.setFrom("nishug2008@gmail.com");
           message.setTo("nishug2008@gmail.com");
           message.setSubject("Simple test message");
           message.setText("This is a sample text");

           mailSender.send(message);
        }
        catch(Exception e){
          return e.getMessage();
        }
       return "success";
    }

}
