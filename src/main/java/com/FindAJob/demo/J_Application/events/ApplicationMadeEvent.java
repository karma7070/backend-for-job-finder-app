package com.FindAJob.demo.J_Application.events;

import java.util.UUID;

public record ApplicationMadeEvent(UUID id,
                                   UUID id2,
                                   String info) {

}
