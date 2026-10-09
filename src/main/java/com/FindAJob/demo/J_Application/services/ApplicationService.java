package com.FindAJob.demo.J_Application.services;


import com.FindAJob.demo.J_Application.*;
import com.FindAJob.demo.J_Application.domain.repos.ApplicationRepository;
import com.FindAJob.demo.J_Application.domain.entities.Application;
import com.FindAJob.demo.J_Application.events.ApplicationMadeEvent;
import com.FindAJob.demo.J_Application.publicenum.AppStatus;
import com.FindAJob.demo.J_Application.requests.ApplicationReqDTO;
import com.FindAJob.demo.J_Application.responses.ApplicationResDTO;
import com.FindAJob.demo.jobs.domain.entities.Jobs;
import com.FindAJob.demo.jobs.services.JobsService;
import com.FindAJob.demo.reg_users.domain.entities.Reg_Users;
import com.FindAJob.demo.reg_users.services.Reg_UsersService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;

@Service
public class ApplicationService {

    private final ApplicationRepository repository;
    private final JobsService jService;
    private final Reg_UsersService uService;
    private final ApplicationEventPublisher publisher;

    public ApplicationService(ApplicationRepository repository, JobsService jService, Reg_UsersService uService, ApplicationEventPublisher publisher){
        this.repository = repository;
        this.jService = jService;
        this.uService = uService;
        this.publisher = publisher;
    }

    //get applications for ADMIN

    public List<ApplicationResDTO> getApn(){
        List<Application> apns = repository.findAll();

        List<ApplicationResDTO> response = this.getResDTO(apns);

        return response;
    }



//get specific applications by email for USERS

    public List<ApplicationResDTO> getApnByEmail(){

        //  if(email.email() != auth.email)
//finds applications based on user emails

        //uses the context holder to get the email, smart
        String email = Objects.requireNonNull(SecurityContextHolder
                        .getContext()
                        .getAuthentication())
                .getName();

        List<Application> applications =
                repository.findAllByUserEmail(email);

        List<ApplicationResDTO> responses = new ArrayList<>();

            for(int i = 0; i<applications.size(); i++){

                Application appl = applications.get(i);

                responses.add(ApplicationResDTO.from(appl));

            }
            return responses;

    }

//create application for USERS

    public ApplicationResDTO createApn(ApplicationReqDTO request){

        Optional<Application> apn = Optional.ofNullable(repository
                .findApplicationByJobIdAndUserId(request.jobId(), request.userId()));

        if(apn.isEmpty()){
            throw new ResponseStatusException
                    (HttpStatus.CONFLICT, "Application for Job:"
                            + request.jobId()
                            + "By" + request.userId()
                            + "already exists");
        }

        ApplicationReqDTO request2 = this.checkForEmptyReq(request);

        Jobs job = jService.getJob(request2.jobId());

        Reg_Users user = uService.getUser(request2.userId());

        Application app1 = new Application(job,
                                            user,
                                            request2.info(),
                                            Instant.now(),
                                            AppStatus.PENDING);
                                            
        repository.save(app1);

        ApplicationMadeEvent event = new ApplicationMadeEvent(app1.getId(),
                                                                app1.getUser().getId(),
                                                                app1.getInfo());

        publisher.publishEvent(event);

        ApplicationResDTO response = ApplicationResDTO.from(app1);

        return response;
    }

//Company gets applications (for Companies)

    public List<ApplicationResDTO> getByCompany(){

        List<Application> appns = repository.findAll();

        String email = Objects.requireNonNull(SecurityContextHolder
                        .getContext()
                        .getAuthentication())
                .getName();

       ArrayList <ApplicationResDTO> responseArray = new ArrayList<>();

        for(int i = 0; i<appns.size(); i++){

            Application app = appns.get(i);

            if(Objects.equals(app.getJob().getCompany().getCompEmail(), email)){

                responseArray.add(ApplicationResDTO.from(app));

            }

        }

            return responseArray;
    }

//Company approves/denies application

    public ApplicationResDTO setStatus(AppStatusDTO appStatus, UUID id){

        Optional<Application> apn = repository.findById(id);

        if(apn.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Application doesn't exist");
        }

        if(appStatus == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status is missing");
        }

        if(appStatus.status() == AppStatus.APPROVED) {
            apn.get().setStatus(AppStatus.APPROVED);
        }
          else if(appStatus.status() == AppStatus.DENIED){
              apn.get().setStatus(AppStatus.DENIED);
        } else {
              apn.get().setStatus(AppStatus.PENDING);
        }
          repository.save(apn.get());

     return ApplicationResDTO.from(apn.get());
    }



    //update application

    public ApplicationResDTO updateApp(ApplicationReqDTO req, UUID id){
        Application app = repository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application does not exist"));

            if(req.info() == null || req.info().isEmpty()){
              throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request can't be empty");
            }

                app.setInfo(req.info());

                    repository.save(app);

        return ApplicationResDTO.from(app);
    }

                     /////////////////////////////
///////////////////////// Service Functions  ///////////////////////////
                      //////////////////////////

    public List<ApplicationResDTO> getResDTO(List<Application> apns){

       List<ApplicationResDTO> resps = new ArrayList<>();

       for(int i = 0; i<apns.size(); i++){
           Application apn = apns.get(i);

           resps.add(ApplicationResDTO.from(apn));
       }

       return resps;
    }

    public ApplicationReqDTO checkForEmptyReq(ApplicationReqDTO request){

       if(request.jobId() == null
               || request.userId() == null
               || request.info() == null
               ){
         throw new RuntimeException("Request has an Empty value");
       }

       return request;
    }
}
