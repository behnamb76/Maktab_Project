package ir.maktabsharif.finalproject.controller;

import ir.maktabsharif.finalproject.model.Question;
import ir.maktabsharif.finalproject.model.dto.request.QuestionReqDTO;
import ir.maktabsharif.finalproject.service.ExamService;
import ir.maktabsharif.finalproject.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    private final QuestionService questionService;
    private final ExamService examService;

    @Autowired
    public QuestionController(QuestionService questionService, ExamService examService) {
        this.questionService = questionService;
        this.examService = examService;
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/course/{examId}")
    public ResponseEntity<Question> createQuestion(@PathVariable Long examId,
                                                   @RequestBody QuestionReqDTO req) {
        Question question = questionService.createQuestion(req, examId);
        return ResponseEntity.status(HttpStatus.CREATED).body(question);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/{examId}/questions/{questionId}")
    public ResponseEntity<Void> addQuestionToExam(@PathVariable Long examId,
                                                  @PathVariable Long questionId,
                                                  @RequestParam Double score) {
        examService.addQuestionToExam(examId, questionId, score);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateQuestion(@PathVariable Long id,
                                               @RequestBody QuestionReqDTO req) {
        questionService.updateQuestion(id, req);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

    /*@GetMapping("/delete/{id}")
    public String deleteQuestion(@PathVariable Long id) {
        ExamQuestion examQuestion = examService.getExamQuestionByQuestionId(id);
        Long examId = examQuestion.getExam().getId();
        questionService.deleteQuestion(id);
        return "redirect:/teacher/exams/" + examId + "/questions";
    }*/

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/{questionId}/reuse/{examId}")
    public ResponseEntity<Void> reuseQuestionInExam(@PathVariable Long questionId,
                                                    @PathVariable Long examId,
                                                    @RequestParam Double score) {
        questionService.reuseQuestionInExam(examId, questionId, score);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/{examId}/questions/{examQuestionId}")
    public ResponseEntity<Void> removeQuestionFromExam(@PathVariable Long examId,
                                                       @PathVariable Long examQuestionId) {
        examService.removeQuestionFromExam(examQuestionId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/question-bank/{courseId}")
    public String showQuestionBank(
            @PathVariable Long courseId,
            @RequestParam(required = false) Long examId,
            Model model) {
        List<Question> questions = questionService.getQuestionsByCourseId(courseId);
        model.addAttribute("questions", questions);
        model.addAttribute("courseId", courseId);
        model.addAttribute("examId", examId);
        return "teacher/question-bank";
    }
}
