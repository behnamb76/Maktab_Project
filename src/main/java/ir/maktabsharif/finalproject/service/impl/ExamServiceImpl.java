package ir.maktabsharif.finalproject.service.impl;

import ir.maktabsharif.finalproject.exception.ExamCompletedException;
import ir.maktabsharif.finalproject.exception.ResourceNotFoundException;
import ir.maktabsharif.finalproject.model.*;
import ir.maktabsharif.finalproject.model.dto.request.ExamReqDTO;
import ir.maktabsharif.finalproject.model.enums.ExamStatus;
import ir.maktabsharif.finalproject.repository.*;
import ir.maktabsharif.finalproject.service.CourseService;
import ir.maktabsharif.finalproject.service.ExamService;
import ir.maktabsharif.finalproject.service.QuestionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ExamServiceImpl implements ExamService {
    private final ExamRepository examRepository;
    private final CourseService courseService;
    private final ExamQuestionRepository examQuestionRepository;
    private final QuestionService questionService;
    private final AnswerRepository answerRepository;
    private final ExamAttemptRepository examAttemptRepository;
    private final UserRepository userRepository;
    private final TemporaryAnswerRepository temporaryAnswerRepository;
    private final CourseMemberRepository courseMemberRepository;

    @Autowired
    public ExamServiceImpl(ExamRepository examRepository, CourseServiceImpl courseService, ExamQuestionRepository examQuestionRepository, QuestionService questionService, AnswerRepository answerRepository, ExamAttemptRepository examAttemptRepository, UserRepository userRepository, TemporaryAnswerRepository temporaryAnswerRepository, CourseMemberRepository courseMemberRepository) {
        this.examRepository = examRepository;
        this.courseService = courseService;
        this.examQuestionRepository = examQuestionRepository;
        this.questionService = questionService;
        this.answerRepository = answerRepository;
        this.examAttemptRepository = examAttemptRepository;
        this.userRepository = userRepository;
        this.temporaryAnswerRepository = temporaryAnswerRepository;
        this.courseMemberRepository = courseMemberRepository;
    }

    @Override
    @Transactional
    public Exam createExam(Long courseId, ExamReqDTO examReqDTO) {
        Course course = courseService.getCourseById(courseId);
        Exam exam = Exam.builder()
                .title(examReqDTO.getTitle())
                .description(examReqDTO.getDescription())
                .duration(examReqDTO.getDuration())
                .course(course)
                .build();
        return examRepository.save(exam);
    }

    @Override
    @Transactional
    public void updateExam(Long id, ExamReqDTO examReqDTO) {
        Exam exam = getExamById(id);
        exam.setTitle(examReqDTO.getTitle());
        exam.setDescription(examReqDTO.getDescription());
        exam.setDuration(examReqDTO.getDuration());
        examRepository.save(exam);
    }

    @Override
    public void deleteExam(Long id) {
        examRepository.deleteById(id);
    }


    @Override
    public Exam getExamById(Long id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));
    }

    @Override
    public List<Exam> getExamsByCourseId(Long courseId) {
        return examRepository.findExamByCourseId(courseId);
    }

    @Override
    @Transactional
    public void addQuestionToExam(Long examId, Long questionId, Double score) {
        Optional<ExamQuestion> existingExamQuestion = examQuestionRepository.findExamQuestionByExamIdAndQuestionId(examId, questionId);
        if (existingExamQuestion.isPresent()) {
            throw new RuntimeException("Question already exists in this exam");
        }

        ExamQuestion examQuestion = ExamQuestion.builder()
                .exam(getExamById(examId))
                .question(questionService.getQuestionById(questionId))
                .score(score)
                .build();

        examQuestionRepository.save(examQuestion);
    }

    @Override
    public ExamQuestion getExamQuestionByQuestionId(Long questionId) {
        return examQuestionRepository.findExamQuestionByQuestionId(questionId);
    }

    @Override
    public List<ExamQuestion> getExamQuestionsByExamId(Long examId) {
        return examQuestionRepository.findExamQuestionByExamId(examId);
    }

    @Override
    public Double getTotalExamScore(Long examId) {
        return examQuestionRepository.sumScoresByExam(examId);
    }

    @Override
    @Transactional
    public void removeQuestionFromExam(Long examQuestionId) {
        examQuestionRepository.deleteById(examQuestionId);
    }

    @Override
    @Transactional
    public void processExamSubmission(Long examId, User student, Map<String, String> answers) {
        Course course = getExamById(examId).getCourse();
        boolean isEnrolled = courseMemberRepository
                .findCourseMembersByUserIdAndCourseId(student.getId(), course.getId())
                .isPresent();

        if (!isEnrolled) {
            throw new RuntimeException("Student is not enrolled in this course");
        }

        List<ExamQuestion> examQuestions = examQuestionRepository.findExamQuestionByExamId(examId);
        List<Answer> answerList = new ArrayList<>();

        for (ExamQuestion examQuestion : examQuestions) {
            String paramKey = "question_" + examQuestion.getId();
            if (answers.containsKey(paramKey)) {
                String answerText = answers.get(paramKey);

                Answer answer = Answer.builder()
                        .examQuestion(examQuestion)
                        .student(student)
                        .exam(examQuestion.getExam())
                        .answer(answerText)
                        .score(0.0)
                        .build();
                answerList.add(answer);
            }
        }

        answerRepository.saveAll(answerList);

        ExamAttempt attempt = examAttemptRepository
                .findExamAttemptByExamIdAndStudentId(examId, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Exam attempt not found"));

        attempt.setExamStatus(ExamStatus.COMPLETED);
        attempt.setEndAt(LocalDateTime.now());
        examAttemptRepository.save(attempt);

        temporaryAnswerRepository.deleteByExamIdAndStudentId(examId, student.getId());
    }

    @Override
    @Transactional
    public ExamAttempt recordExamAttempt(Long examId, Long studentId) {
        ExamAttempt attempt = new ExamAttempt();
        attempt.setExam(getExamById(examId));
        attempt.setStudent(userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found")));
        attempt.setExamStatus(ExamStatus.IN_PROGRESS);
        attempt.setAttemptAt(LocalDateTime.now());
        attempt.setEndAt(LocalDateTime.now().plusMinutes(getExamById(examId).getDuration()));
        return examAttemptRepository.save(attempt);
    }

    @Override
    public boolean hasStudentSubmittedExam(Long examId, Long studentId) {
        Optional<ExamAttempt> examAttempt = examAttemptRepository.findExamAttemptByExamIdAndStudentId(examId, studentId);
        return examAttempt
                .filter(attempt -> attempt.getExamStatus() == ExamStatus.COMPLETED)
                .isPresent();
    }

    @Override
    public ExamAttempt getExamAttemptByExamIdAndStudentId(Long examId, Long studentId) {
        return examAttemptRepository.findExamAttemptByExamIdAndStudentId(examId, studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam attempt not found"));
    }

    @Override
    public void saveTemporaryAnswer(Long examId, Long studentId, Long examQuestionId, String answer) {
        TemporaryAnswer temporaryAnswer = temporaryAnswerRepository
                .findByExamIdAndStudentIdAndExamQuestionId(examId, studentId, examQuestionId)
                .orElse(new TemporaryAnswer());

        temporaryAnswer.setExam(getExamById(examId));
        temporaryAnswer.setStudent(userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found")));
        temporaryAnswer.setExamQuestion(examQuestionRepository.findById(examQuestionId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam question not found")));
        temporaryAnswer.setAnswer(answer);
        temporaryAnswer.setLastUpdated(LocalDateTime.now());

        temporaryAnswerRepository.save(temporaryAnswer);
    }

    @Override
    public boolean isExamTimeExpired(ExamAttempt examAttempt) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime examEndTime = examAttempt.getEndAt();
        return now.isAfter(examEndTime);
    }

    @Override
    public List<TemporaryAnswer> getTemporaryAnswersByExamIdAndStudentId(Long examId, Long studentId) {
        return temporaryAnswerRepository.findByExamIdAndStudentId(examId, studentId);
    }

    @Override
    @Transactional
    public Map<String, Object> startExam(Long examId, Long studentId) {
        if (hasStudentSubmittedExam(examId, studentId)) {
            throw new ExamCompletedException("Exam already taken");
        }

        ExamAttempt examAttempt = examAttemptRepository.findExamAttemptByExamIdAndStudentId(examId, studentId)
                .orElseGet(() -> recordExamAttempt(examId, studentId));

        if (isExamTimeExpired(examAttempt)) {
            examAttempt.setExamStatus(ExamStatus.COMPLETED);
            examAttemptRepository.save(examAttempt);
            throw new ExamCompletedException("Exam time expired");
        }

        Exam exam = getExamById(examId);
        List<ExamQuestion> examQuestions = getExamQuestionsByExamId(examId);

        List<TemporaryAnswer> temporaryAnswers = getTemporaryAnswersByExamIdAndStudentId(examId, studentId);

        /*List<Question> questions = examQuestions.stream()
                .map(ExamQuestion::getQuestion)
                .toList();*/

        Map<String, Object> response = new HashMap<>();
        response.put("exam", exam);
        response.put("questions", examQuestions);
        response.put("temporaryAnswers", temporaryAnswers);
        response.put("timeRemaining", Duration.between(LocalDateTime.now(), examAttempt.getEndAt()).toMinutes());

        return response;
    }
}
