package org.minipiku.userservice.Services;

import lombok.RequiredArgsConstructor;
import org.minipiku.userservice.DTOs.RegisterRequest;
import org.minipiku.userservice.DTOs.UserResponse;
import org.minipiku.userservice.Models.UserRole;
import org.minipiku.userservice.Models.Users;
import org.minipiku.userservice.Repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    //private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered!");
        }

        Users user = new Users();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        //user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(UserRole.USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        Users saved = userRepository.save(user);

        return new UserResponse(
                saved.getId(),
                saved.getEmail(),
                saved.getFirstName() + " " + saved.getLastName(),
                saved.getRole().name()
        );
    }

    public UserResponse getUserProfile(String userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().name());
    }
    public boolean existsByUserId(String userId) {
        return userRepository.existsById(userId);
    }

}
