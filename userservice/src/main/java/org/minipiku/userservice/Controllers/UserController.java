package org.minipiku.userservice.Controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.minipiku.userservice.DTOs.RegisterRequest;
import org.minipiku.userservice.DTOs.UserResponse;
import org.minipiku.userservice.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }
}
