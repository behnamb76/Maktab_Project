package ir.maktabsharif.finalproject.controller;

import ir.maktabsharif.finalproject.model.Exam;
import ir.maktabsharif.finalproject.model.ExamQuestion;
import ir.maktabsharif.finalproject.model.dto.request.ExamReqDTO;
import ir.maktabsharif.finalproject.service.CourseService;
import ir.maktabsharif.finalproject.service.ExamService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {
    private final ExamService examService;
    private final CourseService courseService;

    public ExamController(ExamService examService, CourseService courseService) {
        this.examService = examService;
        this.courseService = courseService;
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/course/{courseId}")
    public ResponseEntity<Exam> createExam(@PathVariable Long courseId,
                                           @RequestBody ExamReqDTO req) {
        Exam exam = examService.createExam(courseId, req);
        return ResponseEntity.ok(exam);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateExam(@RequestBody ExamReqDTO req,
                                           @PathVariable Long id) {
        examService.updateExam(id, req);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExam(@PathVariable Long id) {
        examService.deleteExam(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/{id}")
    public ResponseEntity<Exam> getExam(@PathVariable Long id) {
        return ResponseEntity.ok(examService.getExamById(id));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Exam>> getExamsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(examService.getExamsByCourseId(courseId));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/{examId}/questions")
    public ResponseEntity<List<ExamQuestion>> getExamQuestions(@PathVariable Long examId) {
        return ResponseEntity.ok(examService.getExamQuestionsByExamId(examId));
    }
}
