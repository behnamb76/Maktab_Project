/*
package ir.maktabsharif.finalproject.service;

import ir.maktabsharif.finalproject.model.Role;
import ir.maktabsharif.finalproject.model.User;
import ir.maktabsharif.finalproject.model.dto.request.UserReqDTO;
import ir.maktabsharif.finalproject.repository.RoleRepository;
import ir.maktabsharif.finalproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public DataInitializer(UserRepository userRepository, UserService userService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        Role adminUserRole = Role.builder()
                .name("ROLE_ADMIN")
                .build();
        roleRepository.save(adminUserRole);

        Role teacherUserRole = Role.builder()
                .name("ROLE_TEACHER")
                .build();
        roleRepository.save(teacherUserRole);

        Role studentUserRole = Role.builder()
                .name("ROLE_STUDENT")
                .build();
        roleRepository.save(studentUserRole);

        userService.createUser(UserReqDTO.builder()
                .username("admin")
                .password("admin")
                .role("ROLE_ADMIN")
                .build());
    }
}
*/
