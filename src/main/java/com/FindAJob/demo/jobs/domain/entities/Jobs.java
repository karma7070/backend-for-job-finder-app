package com.FindAJob.demo.jobs.domain.entities;


import com.FindAJob.demo.companies.domain.entities.Companies;
import com.FindAJob.demo.jobs.publicenums.JobAvailability;
import com.FindAJob.demo.jobs.publicenums.JobFields;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "jobs")
public class Jobs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "salary")
    private Double salary;

    @Column(name = "field", nullable = false)
    private JobFields field;

    @Column(name = "availability")
    private JobAvailability availability;

    @Column(name = "posted_at", nullable = false)
    private Instant posted_at;

    @Column(name = "posted_by", nullable = false)
    private String posted_by;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false, unique = true)
    private Companies company;

    public Jobs(String jobTitle,
                String description,
                Double salary,
                JobFields field,
                JobAvailability availability,
                Instant posted_at,
                String posted_by,
                Companies company){
        this.jobTitle = jobTitle;
        this.description = description;
        this.salary = salary;
        this.field = field;
        this.availability = availability;
        this.posted_at = Instant.now();
        this.posted_by= posted_by;
        this.company = company;
    }

    public Jobs(){

    }

    public UUID getId() {
        return id;
    }

    public String getJob_title() {
        return jobTitle;
    }

    public void setJob_title(String job_title) {
        this.jobTitle = jobTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public JobFields getField() {
        return field;
    }

    public void setField(JobFields field) {
        this.field = field;
    }

    public JobAvailability getAvailability() {
        return availability;
    }

    public void setAvailability(JobAvailability availability) {
        this.availability = availability;
    }

    public Instant getPosted_at() {
        return posted_at;
    }

    public String getPosted_by() {
        return posted_by;
    }

    public void setPosted_by(String posted_by) {
        this.posted_by = posted_by;
    }

    public Companies getCompany() {
        return company;
    }
}
