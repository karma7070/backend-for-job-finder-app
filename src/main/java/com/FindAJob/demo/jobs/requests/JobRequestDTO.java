package com.FindAJob.demo.jobs.requests;

import com.FindAJob.demo.jobs.publicenums.JobAvailability;
import com.FindAJob.demo.jobs.publicenums.JobFields;

import java.util.UUID;

public record JobRequestDTO (
        String job_title,
        String description,
        Double salary,
        JobFields field,
        JobAvailability availability,
        UUID compId) {

}
