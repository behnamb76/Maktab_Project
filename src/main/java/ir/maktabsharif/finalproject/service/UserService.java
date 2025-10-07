package ir.maktabsharif.finalproject.service;

import ir.maktabsharif.finalproject.model.User;
import ir.maktabsharif.finalproject.model.dto.request.UserReqDTO;
import ir.maktabsharif.finalproject.model.dto.response.UserRespDTO;
import ir.maktabsharif.finalproject.model.enums.Status;

import java.util.List;

public interface UserService {
    User createUser(UserReqDTO userReqDTO);

    List<UserRespDTO> getAllUsers();

    User getUserById(Long id);

    void updateUser(Long id, UserReqDTO userReqDTO);

    void deleteUser(Long id);

    List<UserRespDTO> getUsersByStatus(Status status);

    List<UserRespDTO> getApprovedUsersByRole(String roleName);

    void updateUserStatus(Long id, Status status);

    Boolean isUsernameUnique(String username);

    List<UserRespDTO> searchUsers(String role, String firstName, String lastName, Status status);

    User getUserByUsername(String username);

    User getCurrentUser();

    /*String processProfilePicture(MultipartFile file) throws IOException;*/
}
