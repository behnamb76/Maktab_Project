package ir.maktabsharif.finalproject.service.impl;

import ir.maktabsharif.finalproject.exception.ResourceNotFoundException;
import ir.maktabsharif.finalproject.model.Answer;
import ir.maktabsharif.finalproject.model.ExamResult;
import ir.maktabsharif.finalproject.repository.AnswerRepository;
import ir.maktabsharif.finalproject.repository.ExamResultRepository;
import ir.maktabsharif.finalproject.service.ExamResultService;
import ir.maktabsharif.finalproject.service.ExamService;
import ir.maktabsharif.finalproject.service.UserService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class ExamResultServiceImpl implements ExamResultService {
    private final ExamResultRepository examResultRepository;
    private final AnswerRepository answerRepository;
    private final ExamService examService;
    private final UserService userService;

    public ExamResultServiceImpl(ExamResultRepository examResultRepository, AnswerRepository answerRepository, ExamService examService, UserService userService) {
        this.examResultRepository = examResultRepository;
        this.answerRepository = answerRepository;
        this.examService = examService;
        this.userService = userService;
    }

    public List<ExamResult> getExamResultsByExamId(Long examId) {
        return examResultRepository.findExamResultsByExamId(examId);
    }

    public ExamResult getExamResultByExamIdAndStudentId(Long examId, Long studentId) {
        return examResultRepository.findExamResultByExamIdAndStudentId(examId, studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam Result Not Found"));
    }

    @Override
    @Transactional
    public void gradeAnswers(Long examId, Long studentId, Map<String, Double> grades) {
        List<Answer> answers = answerRepository.findAnswersByExamIdAndStudentId(examId, studentId);
        double totalScore = 0.0;

        for (Answer answer : answers) {
            String gradeKey = "grade_" + answer.getExamQuestion().getId();
            if (grades.containsKey(gradeKey)) {
                double score = grades.get(gradeKey);
                answer.setScore(score);
                totalScore += score;
            }
        }
        answerRepository.saveAll(answers);

        ExamResult result = examResultRepository.findExamResultByExamIdAndStudentId(examId, studentId)
                .orElse(new ExamResult());
        result.setExam(examService.getExamById(examId));
        result.setStudent(userService.getUserById(studentId));
        result.setTotalScore(totalScore);
        examResultRepository.save(result);
    }
}
