package ir.maktabsharif.finalproject.repository;

import ir.maktabsharif.finalproject.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findOptionsByQuestionId(Long questionId);

    List<Option> findOptionsByQuestionIdAndCorrect(Long questionId, boolean isCorrect);
}
