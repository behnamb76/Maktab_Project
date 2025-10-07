package ir.maktabsharif.finalproject.repository;

import ir.maktabsharif.finalproject.model.Course;
import ir.maktabsharif.finalproject.model.CourseMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findCoursesByCourseMembersContaining(CourseMember courseMember);

    Optional<Course> findCourseById(Long id);
}
