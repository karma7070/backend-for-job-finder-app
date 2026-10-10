package com.FindAJob.demo;

import com.FindAJob.demo.SecurityPackage.ApiResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDenied(AccessDeniedException e) {
        ApiResponse error = new ApiResponse(403, "FORBIDDEN", "You don't have permission to access this resource");
        return ResponseEntity.status(403).body(error);
    }

}



