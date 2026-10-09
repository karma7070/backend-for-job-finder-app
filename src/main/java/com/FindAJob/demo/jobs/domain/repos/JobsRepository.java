package com.FindAJob.demo.jobs.domain.repos;

import com.FindAJob.demo.jobs.publicenums.JobAvailability;
import com.FindAJob.demo.jobs.publicenums.JobFields;
import com.FindAJob.demo.jobs.domain.entities.Jobs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobsRepository extends JpaRepository<Jobs, UUID> {

    List<Jobs> findByCompany_id(UUID id);

    Jobs findByJobTitle(String title);

    List<Jobs> findAllByField (JobFields field);

    List<Jobs> findAllByJobTitle(String title);

    List<Jobs> findAllByAvailability(JobAvailability avail);

    List<Jobs> findAllByDescription(String des);

}
