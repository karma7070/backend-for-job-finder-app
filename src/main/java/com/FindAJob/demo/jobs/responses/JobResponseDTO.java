package com.FindAJob.demo.jobs.responses;

import com.FindAJob.demo.companies.domain.entities.Companies;
import com.FindAJob.demo.jobs.domain.entities.Jobs;
import com.FindAJob.demo.jobs.publicenums.JobAvailability;
import com.FindAJob.demo.jobs.publicenums.JobFields;

import java.time.Instant;
import java.util.UUID;

public record JobResponseDTO (
        String job_title,
        String description,
        Double salary,
        JobFields field,
        JobAvailability availability,
        Instant posted_at,
        String posted_by,
        UUID compId){

    public static JobResponseDTO from(Jobs job){

        Companies company = job.getCompany();

        return new JobResponseDTO(job.getJob_title(),
                job.getDescription(),
                job.getSalary(),
                job.getField(),
                job.getAvailability(),
                job.getPosted_at(),
                company.getComp_name(),
                company.getId()
            );

    }

}
