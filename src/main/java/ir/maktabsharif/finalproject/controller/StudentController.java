package ir.maktabsharif.finalproject.controller;

import ir.maktabsharif.finalproject.model.*;
import ir.maktabsharif.finalproject.model.enums.ExamStatus;
import ir.maktabsharif.finalproject.service.CourseService;
import ir.maktabsharif.finalproject.service.ExamService;
import ir.maktabsharif.finalproject.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/student")
public class StudentController {
    private final CourseService courseService;
    private final UserService userService;
    private final ExamService examService;

    public StudentController(CourseService courseService, UserService userService, ExamService examService) {
        this.courseService = courseService;
        this.userService = userService;
        this.examService = examService;
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getEnrolledCourses() {
        User student = userService.getCurrentUser();
        List<Course> courses = courseService.getCoursesByStudent(student);
        return ResponseEntity.ok(courses);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/courses/{courseId}/exams")
    public ResponseEntity<List<Exam>> getCourseExams(@PathVariable Long courseId) {
        List<Exam> exams = examService.getExamsByCourseId(courseId);
        return ResponseEntity.ok(exams);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/exams/{examId}/start")
    public ResponseEntity<?> startExam(@PathVariable Long examId) {
        User student = userService.getCurrentUser();

        try {
            Map<String, Object> result = examService.startExam(examId, student.getId());
            return ResponseEntity.ok(result);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/exams/{examId}/submit")
    public ResponseEntity<?> submitExam(@PathVariable Long examId,
                                        @RequestBody Map<String, String> answers) {
        User student = userService.getCurrentUser();

        ExamAttempt attempt = examService.getExamAttemptByExamIdAndStudentId(examId, student.getId());
        if (examService.isExamTimeExpired(attempt)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Exam time expired."));
        }

        attempt.setExamStatus(ExamStatus.COMPLETED);
        attempt.setEndAt(LocalDateTime.now());

        examService.processExamSubmission(examId, student, answers);

        return ResponseEntity.ok(Map.of("error", "Exam Submitted successfully"));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/exam/{examId}/resume")
    public ResponseEntity<?> resumeExam(@PathVariable Long examId) {
        User student = userService.getCurrentUser();

        ExamAttempt attempt = examService.getExamAttemptByExamIdAndStudentId(examId, student.getId());
        if (examService.isExamTimeExpired(attempt)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Exam time expired."));
        }

        Exam exam = examService.getExamById(examId);
        List<ExamQuestion> examQuestions = examService.getExamQuestionsByExamId(examId);
        List<TemporaryAnswer> tempAnswers = examService.getTemporaryAnswersByExamIdAndStudentId(examId, student.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("exam", exam);
        response.put("questions", examQuestions);
        response.put("temporaryAnswers", tempAnswers);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/exams/{examId}/save-answer")
    public ResponseEntity<String> saveTemporaryAnswer(
            @PathVariable Long examId,
            @RequestParam Long examQuestionId,
            @RequestParam String answer
    ) {
        User student = userService.getCurrentUser();

        examService.saveTemporaryAnswer(examId, student.getId(), examQuestionId, answer);

        return ResponseEntity.ok("Answer saved temporarily");
    }
}

