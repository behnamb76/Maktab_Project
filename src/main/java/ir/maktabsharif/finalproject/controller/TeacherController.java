package ir.maktabsharif.finalproject.controller;

import ir.maktabsharif.finalproject.model.Answer;
import ir.maktabsharif.finalproject.model.Course;
import ir.maktabsharif.finalproject.model.ExamResult;
import ir.maktabsharif.finalproject.service.AnswerService;
import ir.maktabsharif.finalproject.service.CourseService;
import ir.maktabsharif.finalproject.service.ExamResultService;
import ir.maktabsharif.finalproject.service.UserService;
import ir.maktabsharif.finalproject.service.impl.CourseServiceImpl;
import ir.maktabsharif.finalproject.service.impl.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
    private final CourseService courseService;
    private final UserService userService;
    private final ExamResultService examResultService;
    private final AnswerService answerService;

    public TeacherController(CourseServiceImpl courseService, UserServiceImpl userService, ExamResultService examResultService, AnswerService answerService) {
        this.courseService = courseService;
        this.userService = userService;
        this.examResultService = examResultService;
        this.answerService = answerService;
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getTeacherCourses() {
        Long teacherId = userService.getCurrentUser().getId();
        List<Course> courses = courseService.getCoursesByTeacherId(teacherId);
        return ResponseEntity.ok(courses);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/exams/{examId}/results")
    public ResponseEntity<List<ExamResult>> getExamResults(@PathVariable Long examId) {
        List<ExamResult> results = examResultService.getExamResultsByExamId(examId);
        return ResponseEntity.ok(results);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/exams/{examId}/grade/{studentId}/answers")
    public ResponseEntity<List<Answer>> getStudentsAnswers(@PathVariable Long examId,
                                                           @PathVariable Long studentId) {
        List<Answer> answers = answerService.getAnswersByExamIdAndStudentId(examId, studentId);
        return ResponseEntity.ok(answers);
    }

    /*@GetMapping("/exams/{examId}/grade/{studentId}")
    public String gradeExam(@PathVariable Long examId, @PathVariable Long studentId, Model model) {
        List<Answer> answers = answerService.getAnswersByExamIdAndStudentId(examId, studentId);
        model.addAttribute("answers", answers);
        return "teacher/grade-exam";
    }*/

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/exams/{examId}/grade/{studentId}/grade")
    public ResponseEntity<?> gradeStudentExam(@PathVariable Long examId,
                                              @PathVariable Long studentId,
                                              @RequestBody Map<String, Double> grades) {
        List<Answer> answers = answerService.getAnswersByExamIdAndStudentId(examId, studentId);
        for (Answer answer : answers) {
            double questionMaxScore = answer.getExamQuestion().getScore();
            String key = "grade_" + answer.getExamQuestion().getId();
            if (grades.containsKey(key) && grades.get(key) > questionMaxScore) {
                return ResponseEntity.badRequest()
                        .body("Grade for question " + answer.getExamQuestion().getId() + " exceeds max score");
            }
        }

        examResultService.gradeAnswers(examId, studentId, grades);
        return ResponseEntity.ok("Grades submitted successfully");
    }

    /*@PostMapping("/exams/{examId}/grade/{studentId}")
    public String submitGrades(@PathVariable Long examId, @PathVariable Long studentId, Model model, Map<String, String> grades) {
        List<Answer> answers = answerService.getAnswersByExamIdAndStudentId(examId, studentId);
        for (Answer answer : answers) {
            String gradeKey = "grade_" + answer.getExamQuestion().getId();
            if (grades.containsKey(gradeKey)) {
                double score = Double.parseDouble(grades.get(gradeKey));
                if (score > answer.getExamQuestion().getScore()) {
                    model.addAttribute("error", "Score for answer " + answer.getExamQuestion().getId() + "is greater than question score");
                    return "teacher/grade-exam";
                }
            }
        }
        examResultService.gradeAnswers(examId, studentId, grades);
        return "redirect:/teacher/exams/" + examId + "/results";
    }*/
}
