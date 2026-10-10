package com.FindAJob.demo.jobs.events;

import java.util.UUID;

public record JobCreatedEvent(UUID id,
                              String title,
                              String email) {
}
