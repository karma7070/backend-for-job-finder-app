package com.FindAJob.demo.reg_users.services;


import com.FindAJob.demo.SecurityPackage.AuthDTO;
import com.FindAJob.demo.SecurityPackage.AuthResDTO;
import com.FindAJob.demo.SecurityPackage.JWTService;
import com.FindAJob.demo.refreshtoken.domain.entities.RefreshToken;
import com.FindAJob.demo.refreshtoken.domain.repos.RefreshRepository;
import com.FindAJob.demo.refreshtoken.services.RefreshService;
import com.FindAJob.demo.reg_users.domain.entities.Reg_Users;
import com.FindAJob.demo.reg_users.domain.repos.Reg_UsersRepository;
import com.FindAJob.demo.reg_users.requests.Reg_UserRequestDTO;
import com.FindAJob.demo.reg_users.responses.Reg_UserResponseDTO;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class Reg_UsersService {

    private final Reg_UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwt;
    private final RefreshService refTServ;
    private final RefreshRepository refRepo;

    public Reg_UsersService(Reg_UsersRepository userRepository,
                            PasswordEncoder passwordEncoder,
                            JWTService jwt,
                            RefreshService refTServ, RefreshRepository refRepo){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwt = jwt;
        this.refTServ = refTServ;
        this.refRepo = refRepo;
    }


// ////ADD A USER

    public Reg_UserResponseDTO CreateUser(Reg_UserRequestDTO request){

        Optional<Reg_Users> user = userRepository.findByEmail(request.email());

        if(user.isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }

        Reg_Users user1 = this.createCheck(request);

        userRepository.save(user1);

        Reg_UserResponseDTO resp1 = Reg_UserResponseDTO.from(user1);

        jwt.generateToken(request.email(),
                request.name(),
                request.role());

        return resp1;
    }

    //Get user by ID
    public Reg_UserResponseDTO getUserByID(UUID id){
        Reg_Users user = userRepository.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

        return Reg_UserResponseDTO.from(user);
    }

    //Get all users
    public List<Reg_UserResponseDTO> getAllUsers(){
        List<Reg_Users> users = userRepository.findAll();

        ArrayList<Reg_UserResponseDTO> responses = new ArrayList<>();

        for(int i = 0; i < users.size(); i++){

            Reg_Users user = users.get(i);

            responses.add(Reg_UserResponseDTO.from(user));

        }

        return responses;
    }

//User logs in
    @RateLimiter(name = "loginRateLimiter", fallbackMethod = "fallbacklogin")
    public AuthResDTO logIn(AuthDTO auth) {

        Reg_Users user = userRepository.findByEmail(auth.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User doesn't exist"));

        //this encodes the entered password with same key and compares to the stored one
        if (passwordEncoder.matches(auth.password(), user.getPassword())) {

            String token = jwt.generateToken(user.getEmail(),
                                                user.getName(),
                                                user.getRole());

            long num = this.checkUserTokens(auth.email());

            String refT = refTServ.checkForExistingRefToken(user);

            return new AuthResDTO(user.getUsername(),
                    jwt.extractEmail(token),
                    (token + "|||" + refT));
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Invalid Credentials");
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

//Update User info

    public Reg_UserResponseDTO updateUser(Reg_UserRequestDTO request, UUID id){
        //add empty request exception handler

        Optional<Reg_Users> opt_user1 = userRepository.findById(id);

        if(opt_user1.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist!!");
        }

        Reg_Users user2 = this.updateCheck(request, opt_user1.get());

        userRepository.save(user2);

        return Reg_UserResponseDTO.from(user2);
    }
//Delete user

    public Reg_UserResponseDTO deleteUser(UUID id){
       Reg_Users user = userRepository.findById(id)
           .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));


        Reg_UserResponseDTO response = Reg_UserResponseDTO.from(user);

        userRepository.deleteById(id);

        return response;
    }











               ////////////////////////////
    /////////////  Service functions    /////////////////////////////////
               ///////////////////////////

//Converting requestDTO to company object
    public Reg_Users RequestToUser(Reg_UserRequestDTO request){

        String passW = passwordEncoder.encode(request.password());

        Reg_Users user = new Reg_Users(
                request.name(),
                request.age(),
                request.gender(),
                request.profession(),
                request.email(),
                passW,
                request.role()
        );

        return user;
    }

//Checking user request before creating user account and verifying password as well

    public boolean confirmPswrd(String pass, String cfPass){

            if(Objects.equals(pass, cfPass)){//used to compare if strings are equal
                return true;
            } else {
                return false;
            }
    }


    public Reg_Users createCheck(Reg_UserRequestDTO request) {

        if ((request.name() != null && !(request.name().isBlank()))
                && (request.age() != null)
                && (request.gender() != null)
                && (request.profession() != null && !(request.profession().isBlank()))
                && ((request.password() != null) && !(request.password().isBlank()))
                && ((request.confPassword() != null) && !(request.confPassword().isBlank()))
                && (request.email() != null && !(request.email().isBlank()))
                && (request.role() != null)) {

            if (confirmPswrd(request.password(), request.confPassword())) {

                Reg_Users user = this.RequestToUser(request);

                return user;

            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords don't match");
            }


        } else {
            throw new RuntimeException("Fill all fields!");
        }
    }


//Checking if a request is empty before updating

    public Reg_Users updateCheck(Reg_UserRequestDTO request, Reg_Users user){

        if(request.name() != null && !(request.name().isBlank())){
            user.setName(request.name());
        }

        if(request.age() != null){
            user.setAge(request.age());
        }

        if(request.gender() != null){
            user.setGender(request.gender());
        }

        if(request.profession() != null && !(request.profession().isBlank())){
            user.setProfession(request.profession());
        }

        if(request.email() != null && !(request.email().isBlank())){
            user.setEmail(request.email());
        }

        return user;
    }

    public Reg_Users getUser(UUID id){
        return userRepository.findById(id).
                orElseThrow(() -> new RuntimeException("User not Found"));
    }

    public Optional<Reg_Users> getUserByEmail(String email){
        return userRepository
                .findByEmail(email);
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



