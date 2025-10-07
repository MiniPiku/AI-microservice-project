package org.minipiku.userservice.Services;

import org.minipiku.userservice.DTOs.RegisterRequest;
import org.minipiku.userservice.DTOs.UserResponse;

public interface UserService {
    UserResponse register(RegisterRequest request);
    UserResponse getUserProfile(String userId);
}
