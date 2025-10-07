package ir.maktabsharif.finalproject.repository;

import ir.maktabsharif.finalproject.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAnswersByExamIdAndStudentId(Long examId, Long studentId);
}
