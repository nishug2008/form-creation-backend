// package com.form.creation.final_project.repository;

// import java.util.List;

// import org.springframework.stereotype.Repository;

// import com.form.creation.final_project.model.Form;
// import com.form.creation.final_project.model.Question;

// import jakarta.persistence.EntityManager;
// import jakarta.persistence.PersistenceContext;

// @Repository
// public class QuestionRepository {
// @PersistenceContext
// private EntityManager entityManager;

// public void save(Question question) {
// entityManager.persist(question);
// }

// public List<Question> findByForm(Form form) {
// return entityManager.createQuery("from Question q where q.form = : form",
// Question.class)
// .setParameter("form", form).getResultList();
// }

// public Question findById(int id) {
// return entityManager.find(Question.class, id);
// }
// }
package com.form.creation.final_project.repository;

import com.form.creation.final_project.model.Question;
import com.form.creation.final_project.model.Form;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByForm(Form form);
}
