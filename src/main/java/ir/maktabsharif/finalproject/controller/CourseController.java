package ir.maktabsharif.finalproject.controller;

import ir.maktabsharif.finalproject.model.Course;
import ir.maktabsharif.finalproject.model.CourseMember;
import ir.maktabsharif.finalproject.model.dto.request.CourseReqDTO;
import ir.maktabsharif.finalproject.model.dto.response.CourseRespDTO;
import ir.maktabsharif.finalproject.service.CourseService;
import ir.maktabsharif.finalproject.service.impl.CourseServiceImpl;
import ir.maktabsharif.finalproject.service.impl.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseServiceImpl courseService, UserServiceImpl userService) {
        this.courseService = courseService;
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping
    public ResponseEntity<List<CourseRespDTO>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/{id}")
    public ResponseEntity<CourseRespDTO> getCourseById(@PathVariable Long id) {
        Course course = courseService.getCourseById(id);
        CourseRespDTO resp = CourseRespDTO.builder()
                .id(course.getId())
                .title(course.getTitle())
                .startDate(course.getStartDate())
                .endDate(course.getEndDate())
                .build();
        return ResponseEntity.ok(resp);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody CourseReqDTO req) {
        Course course = courseService.createCourse(req);
        return ResponseEntity.ok(course);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCourse(@RequestBody CourseReqDTO req,
                                             @PathVariable Long id) {
        courseService.updateCourse(id, req);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{courseId}/teachers/{teacherId}")
    public ResponseEntity<Void> addTeacherToCourse(@PathVariable Long courseId,
                                                   @PathVariable Long teacherId) {
        courseService.addTeacherToCourse(courseId, teacherId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("{courseId}/teachers/{studentId}")
    public ResponseEntity<Void> addStudentToCourse(@PathVariable Long courseId,
                                                   @PathVariable Long studentId) {
        courseService.addStudentToCourse(courseId, studentId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{courseId}/members/{userId}")
    public ResponseEntity<Void> removeMemberFromCourse(@PathVariable Long courseId,
                                                       @PathVariable Long userId) {
        courseService.removeMemberFromCourse(courseId, userId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("{courseId}/members")
    public ResponseEntity<List<CourseMember>> getCourseMembers(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getCourseMembers(courseId));
    }
}
