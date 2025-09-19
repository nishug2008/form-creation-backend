package com.form.creation.final_project.dto;

import java.util.List;

import com.form.creation.final_project.model.Question;

public class FormDTO {
    private String formTitle;
    private String formDescription;
    private List<Question> questions;

    private List<Long> permittedUserIds;

    public String getFormTitle() {
        return formTitle;
    }

    public void setFormTitle(String formTitle) {
        this.formTitle = formTitle;
    }

    public String getFormDescription() {
        return formDescription;
    }

    public void setFormDescription(String formDescription) {
        this.formDescription = formDescription;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public List<Long> getPermittedUserIds() {
        return permittedUserIds;
    }

    public void setPermittedUserIds(List<Long> permittedUserIds) {
        this.permittedUserIds = permittedUserIds;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

}
