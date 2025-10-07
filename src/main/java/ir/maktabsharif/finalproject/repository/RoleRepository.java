package ir.maktabsharif.finalproject.repository;

import ir.maktabsharif.finalproject.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByName(String name);
}
