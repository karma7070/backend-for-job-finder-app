package com.FindAJob.demo.reg_users.web;

import com.FindAJob.demo.SecurityPackage.AuthDTO;
import com.FindAJob.demo.SecurityPackage.AuthResDTO;
import com.FindAJob.demo.reg_users.requests.Reg_UserRequestDTO;
import com.FindAJob.demo.reg_users.responses.Reg_UserResponseDTO;
import com.FindAJob.demo.reg_users.services.Reg_UsersService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/app/users")
public class Reg_UserController {

    private final Reg_UsersService service;

    public Reg_UserController(Reg_UsersService service) {
        this.service = service;
    }

    @GetMapping(path = "/userById/{id}")
    public Reg_UserResponseDTO getUserByID(@PathVariable UUID id){

        return service.getUserByID(id);
    }

    @GetMapping(path = "/allusers")
    public List<Reg_UserResponseDTO> getAllUsers(){
        return service.getAllUsers();
    }

    @PostMapping(path = "/create")
    public Reg_UserResponseDTO createUser(@Valid @RequestBody Reg_UserRequestDTO request){
        System.out.println("Controller reached");
        return service.CreateUser(request);
    }

    @PostMapping(path = "/logIn")
    public AuthResDTO userLogIn(@Valid @RequestBody AuthDTO auth){

        return service.logIn(auth);
    }

    @PatchMapping(path = "/update/{id}")
    public Reg_UserResponseDTO updateUser(@RequestBody Reg_UserRequestDTO request,
                                          @PathVariable UUID id){

        return service.updateUser(request, id);
    }

    @DeleteMapping (path = "/delete/{id}")
    public Reg_UserResponseDTO deleteUser(@PathVariable UUID id){

        return service.deleteUser(id);
    }


}
