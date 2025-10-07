package ir.maktabsharif.finalproject.service.impl;

import ir.maktabsharif.finalproject.exception.ResourceNotFoundException;
import ir.maktabsharif.finalproject.model.Course;
import ir.maktabsharif.finalproject.model.CourseMember;
import ir.maktabsharif.finalproject.model.User;
import ir.maktabsharif.finalproject.model.dto.request.CourseReqDTO;
import ir.maktabsharif.finalproject.model.dto.response.CourseRespDTO;
import ir.maktabsharif.finalproject.model.enums.Status;
import ir.maktabsharif.finalproject.repository.CourseMemberRepository;
import ir.maktabsharif.finalproject.repository.CourseRepository;
import ir.maktabsharif.finalproject.repository.UserRepository;
import ir.maktabsharif.finalproject.service.CourseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseMemberRepository courseMemberRepository;

    public CourseServiceImpl(CourseRepository courseRepository, UserRepository userRepository, CourseMemberRepository courseMemberRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.courseMemberRepository = courseMemberRepository;
    }

    @Override
    @Transactional
    public Course createCourse(CourseReqDTO courseReqDTO) {
        Course course = Course.builder()
                .title(courseReqDTO.getTitle())
                .startDate(courseReqDTO.getStartDate())
                .endDate(courseReqDTO.getEndDate())
                .build();
        return courseRepository.save(course);
    }

    @Override
    public List<CourseRespDTO> getAllCourses() {
        List<Course> allCourses = courseRepository.findAll();
        return allCourses.stream()
                .map(course -> CourseRespDTO.builder()
                        .id(course.getId())
                        .title(course.getTitle())
                        .startDate(course.getStartDate())
                        .endDate(course.getEndDate())
                        .studentCount(course.getCourseMembers().size())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    @Override
    @Transactional
    public void updateCourse(Long id, CourseReqDTO courseReqDTO) {
        Course course = getCourseById(id);
        course.setTitle(courseReqDTO.getTitle());
        course.setStartDate(courseReqDTO.getStartDate());
        course.setEndDate(courseReqDTO.getEndDate());
        courseRepository.save(course);
    }

    @Override
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    @Override
    public void addTeacherToCourse(Long courseId, Long teacherId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        if (teacher.getStatus() != Status.APPROVED) {
            throw new RuntimeException("Only approved teachers can be added to a course");
        }

        if (!courseMemberRepository.findCourseMembersByCourseIdAndRole(courseId, "ROLE_TEACHER").isEmpty()) {
            throw new RuntimeException("Course already has a teacher");
        }

        CourseMember courseMember = new CourseMember();
        courseMember.setUser(teacher);
        courseMember.setCourse(course);
        courseMember.setRole("ROLE_TEACHER");
        courseMemberRepository.save(courseMember);
    }

    @Override
    public void addStudentToCourse(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        if (student.getStatus() != Status.APPROVED) {
            throw new RuntimeException("Only approved students can be added to a course");
        }

        if (courseMemberRepository.findCourseMembersByUserIdAndCourseId(studentId, courseId).isPresent()) {
            throw new RuntimeException("Student is already enrolled in the course");
        }

        CourseMember courseMember = new CourseMember();
        courseMember.setUser(student);
        courseMember.setCourse(course);
        courseMember.setRole("ROLE_STUDENT");
        courseMemberRepository.save(courseMember);
    }

    @Override
    public void removeMemberFromCourse(Long courseId, Long userId) {
        CourseMember courseMember = courseMemberRepository.findCourseMembersByUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found in course"));
        courseMemberRepository.delete(courseMember);
    }

    @Override
    public List<CourseMember> getCourseMembers(Long courseId) {
        return courseMemberRepository.findCourseMembersByCourseId(courseId);
    }

    @Override
    public List<Course> getCoursesByTeacherId(Long teacherId) {
        return courseMemberRepository.findCourseMembersByUserId(teacherId)
                .stream()
                .map(CourseMember::getCourse)
                .collect(Collectors.toList());
    }

    @Override
    public List<Course> getCoursesByStudent(User student) {
        CourseMember courseMember = courseMemberRepository.findCourseMemberByUser(student);
        return courseRepository.findCoursesByCourseMembersContaining(courseMember);
    }
}
