package com.FindAJob.demo.jobs.web;

import com.FindAJob.demo.jobs.requests.JobAvailReqDTO;
import com.FindAJob.demo.jobs.requests.JobRequestDTO;
import com.FindAJob.demo.jobs.responses.JobResponseDTO;
import com.FindAJob.demo.jobs.services.JobsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/app/jobs")
public class JobsController {

  private final JobsService service;

  public JobsController(JobsService service){

    this.service = service;
  }

// Job creation endpoint\


  @GetMapping(path = "/all")
    public List<JobResponseDTO> getAllJobs(){

    return service.getAllJobs();
  }

  @GetMapping(path = "/one/{id}")
    public JobResponseDTO getAJob(@PathVariable UUID id){

    return service.getAJob(id);
  }

  @GetMapping(path = "/jobsbycompany")
  public List<JobResponseDTO> getJobByComp(){

    return service.getJobsByCompany();
  }

  @PostMapping(path = "/create")
    public JobResponseDTO createAJob(@RequestBody JobRequestDTO request){

    return service.addJob(request);
  }

  @PatchMapping(path = "/update")
    public JobResponseDTO updateJobDetails(@RequestBody JobRequestDTO request, UUID id){

    return service.updateJob(request, id);
  }

  @PatchMapping(path = "/setAvailStatus/{id}")
  public JobResponseDTO setAvailability(@PathVariable UUID id,
                                        @RequestBody JobAvailReqDTO req){

    return service.setAvailabilityStatus(id, req);
  }

  @DeleteMapping(path = "/delete/{id}")
  public JobResponseDTO deleteJob(@PathVariable UUID id){

    return service.deleteJob(id);
  }

}
