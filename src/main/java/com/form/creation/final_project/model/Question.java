package com.form.creation.final_project.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    private String text;
    private String type;
    @OneToMany
    private List<Option> options;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore

    @JoinColumn(name = "form_id")
    private Form form;

    public Question(Form form) {
        this.form = form;
    }

    public Question(Long questionId, String text, String type, Form form) {
        this.questionId = questionId;
        this.text = text;
        this.type = type;
        this.form = form;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

}
