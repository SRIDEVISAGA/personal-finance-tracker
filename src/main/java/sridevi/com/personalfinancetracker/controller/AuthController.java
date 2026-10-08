package sridevi.com.personalfinancetracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import sridevi.com.personalfinancetracker.dto.AuthResponse;
import sridevi.com.personalfinancetracker.entity.User;
import sridevi.com.personalfinancetracker.service.JwtService;
import sridevi.com.personalfinancetracker.service.UserService;

@RestController
@RequestMapping("/auth")
@Tag(
        name = "Authentication",
        description = "APIs for user registration and login"
)
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(
            UserService userService,
            JwtService jwtService) {

        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account"
    )
    @PostMapping("/register")
    public AuthResponse registerUser(
            @Valid @RequestBody User user) {

        User savedUser =
                userService.registerUser(user);

        return new AuthResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                null
        );
    }

    @Operation(
            summary = "Login user",
            description = "Authenticates a user and returns a JWT token"
    )
    @PostMapping("/login")
    public AuthResponse loginUser(
            @RequestParam String email,
            @RequestParam String password) {

        User user =
                userService.loginUser(email, password);

        String token =
                jwtService.generateToken(user.getEmail());

        return new AuthResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                token
        );
    }
}