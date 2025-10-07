package ir.maktabsharif.finalproject.service.impl;

import ir.maktabsharif.finalproject.exception.NotUniqueException;
import ir.maktabsharif.finalproject.exception.ResourceNotFoundException;
import ir.maktabsharif.finalproject.model.User;
import ir.maktabsharif.finalproject.model.dto.request.UserReqDTO;
import ir.maktabsharif.finalproject.model.dto.response.UserRespDTO;
import ir.maktabsharif.finalproject.model.enums.Status;
import ir.maktabsharif.finalproject.repository.RoleRepository;
import ir.maktabsharif.finalproject.repository.UserRepository;
import ir.maktabsharif.finalproject.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User createUser(UserReqDTO userReqDTO) {
        if (isUsernameUnique(userReqDTO.getUsername())) {
            throw new NotUniqueException("Username Already exists!");
        }

        User user = User.builder()
                .firstName(userReqDTO.getFirstName())
                .lastName(userReqDTO.getLastName())
                .username(userReqDTO.getUsername())
                .password(passwordEncoder.encode(userReqDTO.getPassword()))
                .birthday(userReqDTO.getBirthday())
                .role(roleRepository.findRoleByName(userReqDTO.getRole()))
                .status(Status.PENDING_APPROVAL).build();
        return userRepository.save(user);
    }

    @Override
    public List<UserRespDTO> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream()
                .map(user -> UserRespDTO.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .username(user.getUsername())
                        .birthday(user.getBirthday())
                        .role(user.getRole().getName())
                        .status(user.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public void updateUser(Long id, UserReqDTO userReqDTO) {
        User user = getUserById(id);

        if (!user.getUsername().equals(userReqDTO.getUsername()) && isUsernameUnique(userReqDTO.getUsername())) {
            throw new NotUniqueException("Username already exists!");
        }

        user.setFirstName(userReqDTO.getFirstName());
        user.setLastName(userReqDTO.getLastName());
        user.setUsername(userReqDTO.getUsername());
        user.setRole(roleRepository.findRoleByName(userReqDTO.getRole()));
        /*user.setStatus(userReqDTO.getStatus());*/
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserRespDTO> getUsersByStatus(Status status) {
        List<User> users = userRepository.findUsersByStatus(status);
        return users.stream()
                .map(user -> UserRespDTO.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .username(user.getUsername())
                        .birthday(user.getBirthday())
                        .role(user.getRole().getName())
                        .status(user.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<UserRespDTO> getApprovedUsersByRole(String roleName) {
        List<User> users = userRepository.findUsersByRoleNameAndStatus(roleName, Status.APPROVED);
        return users.stream()
                .map(user -> UserRespDTO.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .username(user.getUsername())
                        .birthday(user.getBirthday())
                        .role(user.getRole().getName())
                        .status(user.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateUserStatus(Long id, Status status) {
        User user = getUserById(id);
        user.setStatus(status);
        userRepository.save(user);
    }

    @Override
    public Boolean isUsernameUnique(String username) {
        return userRepository.existsUserByUsername(username);
    }

    @Override
    public List<UserRespDTO> searchUsers(String role, String firstName, String lastName, Status status) {
        List<User> users = userRepository.searchUsers(role, firstName, lastName, status);
        return users.stream()
                .map(user -> UserRespDTO.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .username(user.getUsername())
                        .birthday(user.getBirthday())
                        .role(user.getRole().getName())
                        .status(user.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }

        String username = authentication.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }
}
