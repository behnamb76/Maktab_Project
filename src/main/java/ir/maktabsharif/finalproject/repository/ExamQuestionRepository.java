package ir.maktabsharif.finalproject.repository;

import ir.maktabsharif.finalproject.model.ExamQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Long> {
    ExamQuestion findExamQuestionByQuestionId(Long questionId);

    List<ExamQuestion> findExamQuestionByExamId(Long examId);

    Optional<ExamQuestion> findExamQuestionByExamIdAndQuestionId(Long examId, Long questionId);

    @Query("SELECT SUM(eq.score) FROM ExamQuestion eq WHERE eq.exam.id = ?1")
    Double sumScoresByExam(Long examId);
}
