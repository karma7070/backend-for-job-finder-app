package com.FindAJob.demo.jobs.requests;

import com.FindAJob.demo.jobs.publicenums.JobAvailability;
import com.FindAJob.demo.jobs.publicenums.JobFields;

public record SearchReqDTO(String jobTitle,
                           JobFields field,
                           String description,
                           JobAvailability avail) {

}
