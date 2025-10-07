package ir.maktabsharif.finalproject.service;

import ir.maktabsharif.finalproject.model.*;
import ir.maktabsharif.finalproject.model.dto.request.ExamReqDTO;

import java.util.List;
import java.util.Map;

public interface ExamService {
    Exam createExam(Long courseId, ExamReqDTO examReqDTO);

    void updateExam(Long id, ExamReqDTO examReqDTO);

    void deleteExam(Long id);

    Exam getExamById(Long id);

    List<Exam> getExamsByCourseId(Long courseId);

//    List<Exam> getExamsByCourseIdAndStudent(Long courseId, User student);

    void addQuestionToExam(Long examId, Long questionId, Double score);

    ExamQuestion getExamQuestionByQuestionId(Long questionId);

    List<ExamQuestion> getExamQuestionsByExamId(Long examId);

    Double getTotalExamScore(Long examId);

    void removeQuestionFromExam(Long examQuestionId);

    void processExamSubmission(Long examId, User Student, Map<String, String> allParams);

    ExamAttempt recordExamAttempt(Long examId, Long studentId);

    boolean hasStudentSubmittedExam(Long examId, Long studentId);

    ExamAttempt getExamAttemptByExamIdAndStudentId(Long examId, Long studentId);

    void saveTemporaryAnswer(Long examId, Long studentId, Long examQuestionId, String answer);

    boolean isExamTimeExpired(ExamAttempt examAttempt);

    List<TemporaryAnswer> getTemporaryAnswersByExamIdAndStudentId(Long examId, Long studentId);

    Map<String, Object> startExam(Long examId, Long studentId);
}
