package ir.maktabsharif.finalproject.repository;

import ir.maktabsharif.finalproject.model.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {
    List<ExamResult> findExamResultsByExamId(Long examId);

    Optional<ExamResult> findExamResultByExamIdAndStudentId(Long examId, Long studentId);
}
