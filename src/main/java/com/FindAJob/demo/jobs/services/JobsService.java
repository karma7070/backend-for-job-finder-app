package com.FindAJob.demo.jobs.services;

import com.FindAJob.demo.companies.services.CompService;
import com.FindAJob.demo.companies.domain.entities.Companies;
import com.FindAJob.demo.jobs.domain.repos.JobsRepository;
import com.FindAJob.demo.jobs.domain.entities.Jobs;
import com.FindAJob.demo.jobs.events.JobCreatedEvent;
import com.FindAJob.demo.jobs.events.JobDeletedEvent;
import com.FindAJob.demo.jobs.publicenums.JobAvailability;
import com.FindAJob.demo.jobs.requests.FieldReqDTO;
import com.FindAJob.demo.jobs.requests.JobAvailReqDTO;
import com.FindAJob.demo.jobs.requests.JobRequestDTO;
import com.FindAJob.demo.jobs.requests.SearchReqDTO;
import com.FindAJob.demo.jobs.responses.JobResponseDTO;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;

@Service
public class JobsService {
    private final JobsRepository repository;
    private final CompService compservice;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public JobsService(JobsRepository repository, CompService compservice, ApplicationEventPublisher publisher){
        this.repository = repository;
        this.compservice = compservice;
        this.publisher = publisher;
    }
            ////////////////////////////////////////////
///////////////     END-POINT LOGIC STARTS HERE   /////////////////////////////////////////////////////
            //////////////////////////////////////////


//Return list of jobs

    public List<JobResponseDTO> getAllJobs(){

        List<Jobs> jobs = repository.findAll();

        List<JobResponseDTO> responses = this.getList(jobs);

     return responses;
    }

//Return jobs by specific company

    public List<JobResponseDTO> getJobsByCompany() {

        String err = " No homo";

        String email = Objects.requireNonNull(SecurityContextHolder
                .getContext()
                .getAuthentication())
                .getName();

        Optional<Companies> comp = Optional.of(compservice.getUserByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company Not Found")));

            List<Jobs> job =
                    repository.findByCompany_id(comp.get().getId());

                ArrayList<JobResponseDTO> responses = new ArrayList<>();

                for (int i = 0; i < job.size(); i++) {

                    Jobs job_n = job.get(i);

                    responses.add(JobResponseDTO.from(job_n));
                }

                return responses;
    }

// Get job by Id
    public JobResponseDTO getAJob(UUID id) {

        Optional<Jobs> job = Optional.of(repository.findById(id).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found!")));

        return JobResponseDTO.from(job.get());

    }
//Add job to DB (for companies)
    @RateLimiter(name = "JobListingRL", fallbackMethod = "JobListingFBM")
    public JobResponseDTO addJob(JobRequestDTO request){

         Companies comp = compservice.getCompById(request.compId());

            if(comp == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Company not Found");
            }

           Optional<Jobs> jobCheck = Optional.ofNullable
                   (repository.findByJobTitle(request.job_title()));

            if(jobCheck.isEmpty()){
               throw new ResponseStatusException(HttpStatus.CONFLICT,
                       "Job already exists");
            }

                Jobs job = new Jobs(
                        request.job_title(),
                        request.description(),
                        request.salary(),
                        request.field(),
                        request.availability(),
                        Instant.now(),
                        comp.getComp_name(),
                        comp
                );

                   // job.setPosted_by(comp.getComp_name());

                     repository.save(job);

                        JobCreatedEvent event = new JobCreatedEvent(job.getId(),
                                job.getJob_title(),
                                comp.getCompEmail());

                        publisher.publishEvent(event);

                            JobResponseDTO response = new JobResponseDTO(
                                    job.getJob_title(),
                                    job.getDescription(),
                                    job.getSalary(),
                                    job.getField(),
                                    job.getAvailability(),
                                    job.getPosted_at(),
                                    job.getPosted_by(),
                                    job.getCompany().getId()
                            );

        return response;

    }

    public JobResponseDTO JobListingFBM(JobRequestDTO req){
        throw new ResponseStatusException
                (HttpStatus.TOO_MANY_REQUESTS, "Too many job listing or creation attempts. Please try again later");
    }

//Update Job Details


    public JobResponseDTO updateJob(JobRequestDTO request, UUID id){

       Optional<Jobs> optionaljob = repository.findById(id);

            //Optional is container which holds instantiated objects and can call methods like
            //.isEmpty() for checking if a specific object in database exists

               if(optionaljob.isEmpty()){
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Job doesn't exist");
                }

                    //since optional<> is a container we have to return 'job'
                    // as a 'Jobs' object using '.get()' method to manipulate its data, we can't directly manipulate
                    //from inside the container.


                    //note: you'll have to make sure fields aren't empty when reassigning values

                         Jobs updatedJob = this.checkAndReturn(request,optionaljob.get());

                         repository.save(updatedJob);

      return JobResponseDTO.from(updatedJob);

    }

    //Set Availability status

    public JobResponseDTO setAvailabilityStatus(UUID id, JobAvailReqDTO req){

      Jobs job = repository.findById(id)
              .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found!"));

      if(req.status() == null){
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status missing");
      }

        switch (req.status()) {
            case JobAvailability.AVAILABLE -> job.setAvailability(JobAvailability.AVAILABLE);
            case JobAvailability.UNAVAILABLE -> job.setAvailability(JobAvailability.UNAVAILABLE);
            case JobAvailability.PENDING_AVAILABILITY -> job.setAvailability(JobAvailability.PENDING_AVAILABILITY);
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status invalid");
        }

      repository.save(job);

      return JobResponseDTO.from(job);
    }
    
//Delete job

    public JobResponseDTO deleteJob(UUID id){

        Optional<Jobs> job = Optional.of(repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found")));

                 JobResponseDTO jobRes = new JobResponseDTO(
                         job.get().getJob_title(),
                         job.get().getDescription(),
                         job.get().getSalary(),
                         job.get().getField(),
                         job.get().getAvailability(),
                         job.get().getPosted_at(),
                         (job.get().getCompany().getComp_name() + "______DELETED"),
                         job.get().getCompany().getId()
                 );

                JobDeletedEvent event = new JobDeletedEvent(job.get().getId(), job.get().getJob_title());

                publisher.publishEvent(event);

                repository.delete(job.get());
    return jobRes;
}

//Return based on field
    public List<JobResponseDTO> getByField(FieldReqDTO req){

        List<Jobs> jobs = repository.findAllByField(req.field());

        ArrayList<JobResponseDTO> responses = new ArrayList<>();

            for(int i = 0; i < jobs.size(); i++){

                Jobs job = jobs.get(i);

                responses.add(JobResponseDTO.from(job));
            }

        return responses;

    }

    //Search through jobs by specific categories

    public List<JobResponseDTO> getJobsAttribute(SearchReqDTO req){

    List<JobResponseDTO> response = new ArrayList<>();

    if(req.jobTitle() != null){
        List<Jobs> jobsByTitle =
                repository.findAllByJobTitle(req.jobTitle());

        for(int i = 0; i<jobsByTitle.size(); i++){
            Jobs job = jobsByTitle.get(i);

            response.add(JobResponseDTO.from(job));


        }
        return response;
    }

    if(req.avail() != null){
        List<Jobs> jobsByAvail =
                repository.findAllByAvailability(req.avail());

        for(int i = 0; i<jobsByAvail.size(); i++){
            Jobs job = jobsByAvail.get(i);

            response.add(JobResponseDTO.from(job));


        }
        return response;
    }

    if(req.description() != null){
        List<Jobs> jobsByDes =
                repository.findAllByDescription(req.description());

        for(int i = 0; i<jobsByDes.size(); i++){
            Jobs job = jobsByDes.get(i);

            response.add(JobResponseDTO.from(job));


        }

        return response;
    }


      return response;
    }











        /////////////////////////////////////////////////////
/////////   Function for checking if request is empty     ////////////////////////////////////////////////
       //////////////////////////////////////////////////////



        public Jobs createCheck(JobRequestDTO request, Jobs job){
//so check for empty JSON with .isBlank() and empty string with null
        if((request.job_title() != null && !request.job_title().isBlank())
                && (request.description() != null && !request.description().isBlank())
                && (request.field() != null)
                && (request.availability() != null)){
            job.setJob_title(request.job_title());
            job.setDescription(request.description());
            job.setField(request.field());
            job.setAvailability(request.availability());

        } else {
            throw new RuntimeException("Fill all Fields (A field is empty)");
        }

        return job;

    }

    // ////////////////////update function here

    public Jobs checkAndReturn(JobRequestDTO request, Jobs job){
//so check for empty JSON with .isBlank() and empty string with null

        if(request.job_title() != null && !request.job_title().isBlank()){
            job.setJob_title(request.job_title());
        }

        if(request.description() != null && !request.description().isBlank()){
            job.setDescription(request.description());
        }

        if(request.field() != null){
            job.setField(request.field());
        }

        if(request.availability() != null){
            job.setAvailability(request.availability());
        }

        return job;

    }


//Returns Arraylist of users to the response

    public List<JobResponseDTO> getList(List<Jobs> jobs){

    List<JobResponseDTO> responses = new ArrayList<>();

     for(int i = 0; i<jobs.size(); i++){

         Jobs job = jobs.get(i);

         responses.add(
                 new JobResponseDTO(
                         job.getJob_title(),
                         job.getDescription(),
                         job.getSalary(),
                         job.getField(),
                         job.getAvailability(),
                         job.getPosted_at(),
                         job.getPosted_by(),
                         job.getCompany().getId()
                 )

         );
     }

     return responses;

    }

    public Jobs getJob(UUID id){
     return repository.findById(id).
             orElseThrow(() -> new RuntimeException("Job not found"));
    };
         //////////////////////////
/////////////////   DRAFT   ////////////////////////////
          ////////////////////////

    /*
        for(int i = 0; i < jobs.size(); i++){
            Jobs job_n = jobs.get(i);
            JobResponseDTO response_n = JobResponseDTO.from(job_n);//static 'from()' method used to turn an
            // object (instance of a class) into a data transfer object. It's simply like declaring a function or method but just
            //adding static before the datatype and 'from' instead of a method name (check ResponseDTO)
            responses.add(response_n);
        }
*/

}
