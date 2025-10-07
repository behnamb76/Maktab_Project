package ir.maktabsharif.finalproject.repository;

import ir.maktabsharif.finalproject.model.CourseMember;
import ir.maktabsharif.finalproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseMemberRepository extends JpaRepository<CourseMember, Long> {
    List<CourseMember> findCourseMembersByCourseId(Long courseId);

    List<CourseMember> findCourseMembersByCourseIdAndRole(Long courseId, String role);

    Optional<CourseMember> findCourseMembersByUserIdAndCourseId(Long userId, Long courseId);

    List<CourseMember> findCourseMembersByUserId(Long userId);

    CourseMember findCourseMemberByUser(User user);
}
