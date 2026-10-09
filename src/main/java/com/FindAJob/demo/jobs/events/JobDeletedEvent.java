package com.FindAJob.demo.jobs.events;

import java.util.UUID;

public record JobDeletedEvent(UUID id, String title) {
}
