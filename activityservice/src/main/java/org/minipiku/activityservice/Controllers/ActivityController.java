package org.minipiku.activityservice.Controllers;

import lombok.AllArgsConstructor;
import org.minipiku.activityservice.DTOs.ActivityRequest;
import org.minipiku.activityservice.DTOs.ActivityResponse;
import org.minipiku.activityservice.Services.ActivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/activity")
@RestController
@AllArgsConstructor
public class ActivityController {
    private ActivityService activityService;

    @PostMapping
    public ResponseEntity<ActivityResponse> trackActivity(@RequestBody ActivityRequest request) {
        return ResponseEntity.ok(activityService.trackActivity(request));
    }
}
