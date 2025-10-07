package ir.maktabsharif.finalproject.controller;

import ir.maktabsharif.finalproject.model.User;
import ir.maktabsharif.finalproject.model.dto.request.LoginReqDTO;
import ir.maktabsharif.finalproject.model.dto.request.UserReqDTO;
import ir.maktabsharif.finalproject.service.UserService;
import ir.maktabsharif.finalproject.service.jwt.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;


    public UserController(UserService userService, AuthenticationManager authManager, JwtService jwtService, PasswordEncoder encoder) {
        this.userService = userService;
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody UserReqDTO user,
                                         BindingResult result) {
        /*if (result.hasErrors()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(result.getAllErrors());
        }*/

        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginReqDTO req) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        String token = jwtService.generateToken(req.getUsername());
        return Map.of("token", token);
    }

    /*@GetMapping("/profile")
    public String profilePage(Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);
        model.addAttribute("userUpdate", new UserReqDTO());
        model.addAttribute("userId", user.getId());
        return "user/profile";
    }*/

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UserReqDTO user,
                                           BindingResult result,
                                           @PathVariable Long id) {
        if (result.hasErrors()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(result.getAllErrors());
        }

        userService.updateUser(id, user);
        return ResponseEntity.ok().build();

    }
}
