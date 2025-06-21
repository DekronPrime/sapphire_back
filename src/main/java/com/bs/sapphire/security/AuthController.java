package com.bs.sapphire.security;

import com.bs.sapphire.records.EmployeePostRecord;
import jakarta.annotation.security.PermitAll;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
@RequestMapping("api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final TokenBlackListService tokenBlackListService;

    public AuthController(AuthService authService, TokenBlackListService tokenBlackListService) {
        this.authService = authService;
        this.tokenBlackListService = tokenBlackListService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signUp(
            @RequestBody EmployeePostRecord request) {
        return ResponseEntity.ok(authService.signUp(request));
    }

    @PermitAll
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> logIn(
            @RequestBody EmployeePostRecord request) {
        return ResponseEntity.ok(authService.logIn(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logOut(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        tokenBlackListService.blackListToken(token);
        return ResponseEntity.ok("Logged out successfully");
    }
}
