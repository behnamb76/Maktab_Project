package ir.maktabsharif.finalproject.repository;

import ir.maktabsharif.finalproject.model.TemporaryAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemporaryAnswerRepository extends JpaRepository<TemporaryAnswer, Long> {
    List<TemporaryAnswer> findByExamIdAndStudentId(Long examId, Long studentId);

    Optional<TemporaryAnswer> findByExamIdAndStudentIdAndExamQuestionId(Long examId, Long studentId, Long examQuestionId);

    void deleteByExamIdAndStudentId(Long examId, Long studentId);
}
