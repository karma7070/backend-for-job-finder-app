package com.FindAJob.demo.J_Application.domain.repos;

import com.FindAJob.demo.J_Application.domain.entities.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    List<Application> findAllByUserEmail(String email);

    Application findApplicationByJobIdAndUserId(UUID id, UUID id2);
}
