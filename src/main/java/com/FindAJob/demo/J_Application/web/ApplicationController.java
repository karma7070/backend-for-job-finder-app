package com.FindAJob.demo.J_Application.web;

import com.FindAJob.demo.J_Application.AppStatusDTO;
import com.FindAJob.demo.J_Application.requests.ApplicationReqDTO;
import com.FindAJob.demo.J_Application.responses.ApplicationResDTO;
import com.FindAJob.demo.J_Application.services.ApplicationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/app/application")
public class ApplicationController {

    private final ApplicationService service;


    public ApplicationController(ApplicationService service) {
        this.service = service;
    }

    @PostMapping(path = "/apply")
    public ApplicationResDTO Apply(@RequestBody ApplicationReqDTO request){

        return  service.createApn(request);
    }

    @GetMapping(path = "/Adminget")
    public List<ApplicationResDTO> getApns(){

        return service.getApn();
    }


    @GetMapping(path = "/view_applications")
        public List<ApplicationResDTO> getAppByUserEmail(){

         return service.getApnByEmail();
    }

    @GetMapping(path = "/comp_view_app")
    public List<ApplicationResDTO> getAppByCompany(){

        return service.getByCompany();
    }

    //company sets status i.e approves or denies or...
    @PatchMapping(path = "/company_assesses/{id}")
    public ApplicationResDTO setStatus(@RequestBody AppStatusDTO appStatus,
                                       @PathVariable UUID id){

        return service.setStatus(appStatus, id);
    }

}
