// package com.form.creation.final_project.repository;

// import java.util.List;

// import org.springframework.stereotype.Repository;

// import com.form.creation.final_project.model.Option;
// import com.form.creation.final_project.model.Question;

// import jakarta.persistence.EntityManager;
// import jakarta.persistence.PersistenceContext;

// @Repository
// public class OptionRepository {
// @PersistenceContext
// private EntityManager entityManager;

// public void save(Option option) {
// entityManager.persist(option);
// }

// public List<Option> findByQuestion(Question question) {

// return entityManager.createQuery("From option o where o.question=:question",
// Option.class)
// .setParameter("question", question).getResultList();
// }
// }

package com.form.creation.final_project.repository;

import com.form.creation.final_project.model.Option;
import com.form.creation.final_project.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Integer> {
    List<Option> findByQuestion(Question question);
}
