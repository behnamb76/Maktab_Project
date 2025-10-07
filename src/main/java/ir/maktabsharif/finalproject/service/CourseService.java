package ir.maktabsharif.finalproject.service;

import ir.maktabsharif.finalproject.model.Course;
import ir.maktabsharif.finalproject.model.CourseMember;
import ir.maktabsharif.finalproject.model.User;
import ir.maktabsharif.finalproject.model.dto.request.CourseReqDTO;
import ir.maktabsharif.finalproject.model.dto.response.CourseRespDTO;

import java.util.List;

public interface CourseService {
    Course createCourse(CourseReqDTO courseReqDTO);

    List<CourseRespDTO> getAllCourses();

    Course getCourseById(Long id);

    void updateCourse(Long id, CourseReqDTO courseReqDTO);

    void deleteCourse(Long id);

    void addTeacherToCourse(Long courseId, Long teacherId);

    void addStudentToCourse(Long courseId, Long studentId);

    void removeMemberFromCourse(Long courseId, Long userId);

    List<CourseMember> getCourseMembers(Long courseId);

    List<Course> getCoursesByTeacherId(Long teacherId);

    List<Course> getCoursesByStudent(User student);
}
