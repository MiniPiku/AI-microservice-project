package org.minipiku.activityservice.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
public class UserValidationService {

    private final WebClient webClient;

    /**
     * Validates if a user exists by calling user-service.
     * @param userId The ID of the user to check.
     * @return true if user exists, false otherwise.
     */
    public boolean validateUser(String userId) {
        try {
            Boolean exists = webClient.get()
                    .uri("/api/user/{userId}/validate", userId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            return Boolean.TRUE.equals(exists);
        } catch (WebClientResponseException.NotFound e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException("User validation failed: " + e.getMessage());
        }
    }
}