package org.minipiku.activityservice.Services;

import org.minipiku.activityservice.DTOs.ActivityRequest;
import org.minipiku.activityservice.DTOs.ActivityResponse;

public interface ActivityService {
    ActivityResponse trackActivity(ActivityRequest request);
}
