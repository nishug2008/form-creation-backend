package com.form.creation.final_project.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long responseId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "form_id")
    private Form form;

    @OneToMany(mappedBy = "response", cascade = CascadeType.ALL)
    private List<ResponseEntry> responseEntries;

    @ManyToOne
    @JsonIgnore
    private User user;

    public Response(Long responseId, Form form, List<ResponseEntry> responseEntries) {
        this.responseId = responseId;
        this.form = form;
        this.responseEntries = responseEntries;
    }

    public Long getResponseId() {
        return responseId;
    }

    public void setResponseId(Long responseId) {
        this.responseId = responseId;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public List<ResponseEntry> getResponseEntries() {
        return responseEntries;
    }

    public void setResponseEntries(List<ResponseEntry> responseEntries) {
        this.responseEntries = responseEntries;
    }

}
