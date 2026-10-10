package com.FindAJob.demo.J_Application.requests;

import java.util.UUID;

public record ApplicationReqDTO(
        UUID jobId,
        UUID userId,
        String info
) {

}
