package ir.maktabsharif.finalproject.service;

import ir.maktabsharif.finalproject.model.Answer;

import java.util.List;

public interface AnswerService {
    void createAnswers(List<Answer> answers);

    List<Answer> getAnswersByExamIdAndStudentId(Long examId, Long studentId);
}
