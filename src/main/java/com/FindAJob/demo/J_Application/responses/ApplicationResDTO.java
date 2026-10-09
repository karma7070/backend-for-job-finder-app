package com.FindAJob.demo.J_Application.responses;

import com.FindAJob.demo.J_Application.publicenum.AppStatus;
import com.FindAJob.demo.J_Application.domain.entities.Application;

import java.time.Instant;
import java.util.UUID;

public record ApplicationResDTO(
        UUID jobId,
        UUID userId,
        String info,
        Instant applied_at,
        AppStatus status
) {
    public static ApplicationResDTO from(Application app){

      ApplicationResDTO response = new ApplicationResDTO(
              app.getJob().getId(),
              app.getUser().getId(),
              app.getInfo(),
              app.getApplied_at(),
              app.getStatus()
      )  ;

      return response;
    }

}
