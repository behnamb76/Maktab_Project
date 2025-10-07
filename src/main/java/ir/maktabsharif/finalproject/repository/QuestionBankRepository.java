package ir.maktabsharif.finalproject.repository;

import ir.maktabsharif.finalproject.model.QuestionBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionBankRepository extends JpaRepository<QuestionBank, Long> {
    Optional<QuestionBank> findQuestionBankByCourseId(Long courseId);
}
