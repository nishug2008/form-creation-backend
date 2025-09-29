package com.form.creation.final_project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.form.creation.final_project.exception.MyException;
import com.form.creation.final_project.model.Form;
import com.form.creation.final_project.model.Response;
import com.form.creation.final_project.repository.ResponseRepository;

@Service
public class ResponseService {
    @Autowired
    private ResponseRepository responseRepository;

    public List<Response> getResponsesByForm(Form form) throws MyException {
        try {
            return responseRepository.findByForm(form);
        } catch (Exception e) {
            throw new MyException("Form not found");
        }
    }
}