package com.FindAJob.demo;

import com.FindAJob.demo.SecurityPackage.ApiResponse;
import com.FindAJob.demo.SecurityPackage.Exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice // watches every controller in this folder
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApiResponse> ResponseStatusExceptionHandler(ResponseStatusException e){
                ApiResponse error = new ApiResponse(
                       e.getStatusCode().value(),
                       e.getStatusCode().toString(),
                       e.getReason());

                return ResponseEntity.status(e.getStatusCode()).body(error);
    }

}



