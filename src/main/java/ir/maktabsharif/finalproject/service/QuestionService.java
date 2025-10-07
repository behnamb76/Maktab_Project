package ir.maktabsharif.finalproject.service;

import ir.maktabsharif.finalproject.model.MultipleChoiceQuestion;
import ir.maktabsharif.finalproject.model.Option;
import ir.maktabsharif.finalproject.model.Question;
import ir.maktabsharif.finalproject.model.dto.request.QuestionReqDTO;

import java.util.List;
import java.util.Set;

public interface QuestionService {
    Question createQuestion(QuestionReqDTO questionReqDTO, Long examId);

    void updateQuestion(Long id, QuestionReqDTO questionReqDTO);

    Question getQuestionById(Long id);

    Set<Option> createOption(QuestionReqDTO questionReqDTO, MultipleChoiceQuestion mcq);

    List<Option> getOptionsByQuestionId(Long questionId);

    List<Option> getCorrectOptions(Long questionId);

    void deleteQuestion(Long id);

    void deleteOption(Long optionId);

    void reuseQuestionInExam(Long examId, Long questionId, Double score);

    List<Question> getQuestionsByCourseId(Long courseId);

}
