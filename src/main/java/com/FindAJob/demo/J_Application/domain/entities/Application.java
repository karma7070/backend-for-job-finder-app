package com.FindAJob.demo.J_Application.domain.entities;

import com.FindAJob.demo.J_Application.publicenum.AppStatus;
import com.FindAJob.demo.jobs.domain.entities.Jobs;
import com.FindAJob.demo.reg_users.domain.entities.Reg_Users;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "applications")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private UUID apnId;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false, unique = true)
    private Jobs job;

    @ManyToOne
    @JoinColumn(name = "regular_users_id", nullable = false, unique = true)
    private Reg_Users user;

    @Column(name = "info", nullable = false)
    private String info;

    @Column(name = "applied_at", nullable = false)
    private Instant applied_at;

    @Column(name = "status", nullable = false)
    private AppStatus status;

    public Application(Jobs job, Reg_Users user, String info, Instant applied_at, AppStatus status){
        this.job = job;
        this.user = user;
        this.info = info;
        this.applied_at = Instant.now();
        this.status = AppStatus.PENDING;
    }

    public Application(){

    }

    public UUID getId() {
        return apnId;
    }

    public Jobs getJob() {
        return job;
    }

    public void setJob(Jobs job) {
        this.job = job;
    }

    public Reg_Users getUser() {
        return user;
    }

    public void setUser(Reg_Users user) {
        this.user = user;
    }

    public String getInfo() {
        return info;
    }


    public void setInfo(String info) {
        this.info = info;
    }

    public Instant getApplied_at() {
        return applied_at;
    }

    public AppStatus getStatus() {
        return status;
    }

    public void setStatus(AppStatus status){
        this.status = status;
    }

}
