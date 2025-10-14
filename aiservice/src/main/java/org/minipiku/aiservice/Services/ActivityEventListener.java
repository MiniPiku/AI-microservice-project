package org.minipiku.aiservice.Services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.minipiku.aiservice.Model.Activity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityEventListener {

    //TODO: activity-events-->ai-events

    @KafkaListener(topics = "activity-events", groupId = "activity-processor-group")
    public void processActivity(Activity activity) {
        log.info("📥 Received activity event for user {}", activity.getUserId());
    }
}
