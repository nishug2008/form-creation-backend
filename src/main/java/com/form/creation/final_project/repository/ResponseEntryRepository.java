package com.form.creation.final_project.repository;

import com.form.creation.final_project.model.ResponseEntry;
import com.form.creation.final_project.model.Response;
import com.form.creation.final_project.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResponseEntryRepository extends JpaRepository<ResponseEntry, Long> {
    List<ResponseEntry> findByResponse(Response response);

    List<ResponseEntry> findByQuestion(Question question);
}
