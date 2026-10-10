package com.FindAJob.demo.companies.services;

import com.FindAJob.demo.SecurityPackage.AuthDTO;
import com.FindAJob.demo.SecurityPackage.JWTService;
import com.FindAJob.demo.companies.requests.CompRequestDTO;
import com.FindAJob.demo.companies.responses.CompResponseDTO;
import com.FindAJob.demo.companies.domain.entities.Companies;
import com.FindAJob.demo.SecurityPackage.AuthResDTO;
import com.FindAJob.demo.companies.domain.repos.CompRepository;
import com.FindAJob.demo.refreshtoken.domain.entities.RefreshToken;
import com.FindAJob.demo.refreshtoken.domain.repos.RefreshRepository;
import com.FindAJob.demo.refreshtoken.services.RefreshService;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class CompService {

    private final CompRepository serv_repository;
    private final PasswordEncoder passWE;
    private final JWTService jwt;
    private final RefreshService rTokenSv;
    private final RefreshRepository refRepo;


    public CompService(CompRepository serv_repository,
                       PasswordEncoder passWE,
                       JWTService jwt,
                       RefreshService rTokenSv, RefreshRepository refRepo){
        this.serv_repository = serv_repository;
        this.passWE = passWE;
        this.jwt = jwt;
        this.rTokenSv = rTokenSv;
        this.refRepo = refRepo;
    }

    // ////ADD A COMPANY


    public CompResponseDTO addCompany(CompRequestDTO request) {

        Optional<Companies> company = serv_repository.findByCompEmail(request.comp_email());

        if(company.isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Company already exists");
        }

        if (createCheck(request)) {

                Companies company1 = this.RequestToComp(request);

                    serv_repository.save(company1);

                CompResponseDTO resp1 = CompResponseDTO.from(company1);

                    return resp1;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fill all Fields");
        }
    }

    //Get company by ID
    public CompResponseDTO getCompByID(UUID id){
        Companies comp = serv_repository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));

        return CompResponseDTO.from(comp);
    }

    //Get all companies
    public List<CompResponseDTO> getAllComps(){
        List<Companies> comps = serv_repository.findAll();

        ArrayList<CompResponseDTO> responses = new ArrayList<>();

        for(int i = 0; i < comps.size(); i++ ){
           Companies comp = comps.get(i);

           responses.add(CompResponseDTO.from(comp));
        }

        return responses;
    }




//Company logs in
@RateLimiter(name = "loginRateLimiter", fallbackMethod = "fallbacklogin")
    public AuthResDTO logIn(AuthDTO auth){

        Companies company = serv_repository.findByCompEmail(auth.email())
                .orElseThrow(()->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found!"));

        Optional<RefreshToken> refreshToken = refRepo.findByUserEmail(auth.email());

            long num = checkUserTokens(auth.email());

                if (passWE.matches(auth.password(), company.getPassword())) {

                    String token = jwt.generateToken(company.getCompEmail(),
                            company.getComp_name(),
                            company.getRole());

                    String refToken = rTokenSv.checkForExistingCompRefToken(company);

                    return new AuthResDTO(jwt.extractUsername(token),
                            jwt.extractEmail(token),
                            ("Access Token|" + token +
                                    "| RefreshToken |" + refToken));
                } else {

                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Credentials");

                }

    }

    public AuthResDTO fallbacklogin(AuthDTO auth, Throwable throwable){

        if (throwable instanceof RequestNotPermitted) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many log in attempts. Please try again later.");
        }

        if (throwable instanceof ResponseStatusException rse) {
            throw rse; // rethrow the real error (404, 409, etc.)
        }

        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong during login");
    }

    //Update Company details

    public CompResponseDTO updateCompany(CompRequestDTO request, UUID id){
        Optional<Companies> opt_comp1 = serv_repository.findById(id);

            if(opt_comp1.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company doesn't exist!!");
            }

                Companies comp2 = this.checkAndReturn(request, opt_comp1.get());

                   serv_repository.save(comp2);

                      return CompResponseDTO.from(comp2);
    }


    //Delete Company by id
    public CompResponseDTO deleteCompany(UUID id){
        Companies comp = serv_repository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found!"));

        CompResponseDTO responseDTO = new CompResponseDTO(
                comp.getComp_name(),
                comp.getLocation(),
                (comp.getCompEmail() + "_____DELETED")
        );

        serv_repository.deleteById(id);

        return responseDTO;
    }








                ////////////////////////////
    /////////////  Service functions    /////////////////////////////////
                ///////////////////////////
//Checks for null request before creating user
    public boolean createCheck(CompRequestDTO request){
        if((request.comp_name() != null && !(request.comp_name().isBlank()))
             && ((request.location() != null) && !(request.location().isBlank()))
             && ((request.comp_email() != null) && !(request.comp_email().isBlank()))
             && ((request.password() != null) && !(request.password().isBlank()))
                && ((request.confPass() != null) && !(request.confPass().isBlank()))
                && (request.role() != null)){

            return true;

        } else {
            return false;
        }
    }

    //Converting requestDTO to company object
    public Companies RequestToComp(CompRequestDTO request){

        if(Objects.equals(request.confPass(), request.password())){

            String passW = passWE.encode(request.password());


            Companies comp1 = new Companies(
                    request.comp_name(),
                    request.location(),
                    request.comp_email(),
                    passW,
                    request.role()
            );

            return comp1;
        } else {
            throw new RuntimeException("Re-Enter Passwords!");
        }

    }

    //Checking if a request is empty before updating

    public Companies checkAndReturn(CompRequestDTO request, Companies company){

        if(request.comp_name() != null && !(request.comp_name().isBlank())){
            company.setComp_name(request.comp_name());
        }

        if(request.location() != null && !(request.location().isBlank())){
            company.setLocation(request.location());
        }

        if(request.comp_email() != null && !(request.comp_email().isBlank())){
            company.setCompEmail(request.comp_email());
        }

        return company;
    }

    //Job uses this to get company

    public Companies getCompById(UUID id){
       Optional <Companies> comp = Optional.of(serv_repository.findById(id)
               .orElseThrow(()-> new UsernameNotFoundException("User does not exist")));


           return comp.get();

    }

    public Optional<Companies> getUserByEmail(String email){
        return serv_repository.
                findByCompEmail(email);
    }


//Count number of tokens user has

    public Long checkUserTokens(String email) {

        List<RefreshToken> refTokens = refRepo.findAllByUserEmail(email);

        long num = 0L;

        for (int i = 0; i < refTokens.size(); i++) {
            if (refTokens.get(i) != null) {
                num += 1;
            }
        }

        return num;
    }

}



