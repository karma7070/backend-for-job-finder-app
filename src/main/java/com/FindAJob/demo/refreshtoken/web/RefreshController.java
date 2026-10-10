package com.FindAJob.demo.refreshtoken.web;

import com.FindAJob.demo.SecurityPackage.AuthResDTO;
import com.FindAJob.demo.refreshtoken.requests.RefreshReqDTO;
import com.FindAJob.demo.refreshtoken.response.RefreshResDTO;
import com.FindAJob.demo.refreshtoken.services.RefreshService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/app/refresh")
public class RefreshController {

    private final RefreshService servRef;


    public RefreshController(RefreshService servRef) {

        this.servRef = servRef;
    }

    @PostMapping(path = "/userrefAcc")
    public RefreshResDTO refreshAccessToken(@RequestBody RefreshReqDTO req){

      return  servRef.refreshAccessToken(req);

    }

    @PostMapping(path = "/comprefAcc")
    public RefreshResDTO refreshCompAccessToken(@RequestBody RefreshReqDTO req){
        return servRef.refreshCompAccToken(req);
    }

    @DeleteMapping(path = "/deleteRefTokens")
    public AuthResDTO deleteRefToken(@RequestBody String email){
        return servRef.revokeOrSuspendRefToken(email);
    }
}
