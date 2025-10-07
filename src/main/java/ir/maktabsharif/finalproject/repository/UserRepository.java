package ir.maktabsharif.finalproject.repository;

import ir.maktabsharif.finalproject.model.User;
import ir.maktabsharif.finalproject.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findUsersByStatus(Status status);

    List<User> findUsersByRoleNameAndStatus(String roleName, Status status);

    Boolean existsUserByUsername(String username);

    @Query("SELECT u FROM User u WHERE " +
            "(:role IS NULL OR u.role = :role) AND " +
            "(:firstName IS NULL OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
            "(:lastName IS NULL OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
            "(:status IS NULL OR u.status = :status)")
    List<User> searchUsers(@Param("role") String role,
                           @Param("firstName") String firstName,
                           @Param("lastName") String lastName,
                           @Param("status") Status status);

    Optional<User> findUserByUsername(String username);
}
