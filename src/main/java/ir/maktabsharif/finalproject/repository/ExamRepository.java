package ir.maktabsharif.finalproject.repository;

import ir.maktabsharif.finalproject.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findExamByCourseId(Long courseId);

    Optional<Exam> findExamById(Long id);
}
