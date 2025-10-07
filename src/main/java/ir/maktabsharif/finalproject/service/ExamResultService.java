package ir.maktabsharif.finalproject.service;

import ir.maktabsharif.finalproject.model.ExamResult;

import java.util.List;
import java.util.Map;

public interface ExamResultService {
    List<ExamResult> getExamResultsByExamId(Long examId);

    ExamResult getExamResultByExamIdAndStudentId(Long examId, Long studentId);

    void gradeAnswers(Long examId, Long studentId, Map<String, Double> grades);
}
