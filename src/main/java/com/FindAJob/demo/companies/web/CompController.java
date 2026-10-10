package com.FindAJob.demo.companies.web;

import com.FindAJob.demo.SecurityPackage.AuthDTO;
import com.FindAJob.demo.SecurityPackage.AuthResDTO;
import com.FindAJob.demo.companies.requests.CompRequestDTO;
import com.FindAJob.demo.companies.responses.CompResponseDTO;
import com.FindAJob.demo.companies.services.CompService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/app/comp")
@Tag(name = "Companies", description = "Create, read, update and delete company accounts")
public class CompController {

    private final CompService service;

    public CompController(CompService service){
        this.service = service;

    }

    @PostMapping(path = "/create")
    @Operation(summary = "create a company account")
    public CompResponseDTO createCompany(@RequestBody CompRequestDTO request){

        return service.addCompany(request);
    }

    @GetMapping(path = "/compById/{id}")
    @Operation(summary = "retrieve a company account")
    public CompResponseDTO getCompanyById(@PathVariable UUID id){

        return service.getCompByID(id);
    }

    @GetMapping(path = "/allcompanies")
    @Operation(summary = "get a list company account")
    public List<CompResponseDTO> getAllCompanies(){

        return service.getAllComps();
    }

    @PostMapping(path = "/logIn")
    @Operation(summary = "login to a company account")
    public AuthResDTO logIn(@RequestBody AuthDTO auth){

        return service.logIn(auth);
    }
    @PatchMapping(path = "/update/{id}")
    public CompResponseDTO updateCompany(@RequestBody CompRequestDTO request,
                                         @PathVariable UUID id){

        return service.updateCompany(request, id);
    }

    @DeleteMapping(path = "/delete/{id}")
    @Operation(summary = "delete a company account")
    public CompResponseDTO deleteCompany(@PathVariable UUID id){
        return service.deleteCompany(id);
    }




}
