package ir.maktabsharif.finalproject.controller;

import ir.maktabsharif.finalproject.model.dto.request.UserReqDTO;
import ir.maktabsharif.finalproject.model.dto.response.UserRespDTO;
import ir.maktabsharif.finalproject.model.enums.Status;
import ir.maktabsharif.finalproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserRespDTO>> getPendingUsers() {
        List<UserRespDTO> pendingUsers = userService.getUsersByStatus(Status.PENDING_APPROVAL);
        return ResponseEntity.ok(pendingUsers);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/approve/{id}")
    public ResponseEntity<Void> approveUser(@PathVariable Long id) {
        userService.updateUserStatus(id, Status.APPROVED);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/reject/{id}")
    public ResponseEntity<Void> rejectUser(@PathVariable Long id) {
        userService.updateUserStatus(id, Status.REJECTED);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody UserReqDTO userReqDTO) {
        userService.updateUser(id, userReqDTO);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<List<UserRespDTO>> searchUsers(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Status status
    ) {
        return ResponseEntity.ok(userService.searchUsers(role, firstName, lastName, status));
    }
}
