package com.FindAJob.demo.companies.events;

import java.util.UUID;

public record CompanyCreatedEvent(UUID id,
                                  String compName,
                                  String Location,
                                  String compEmail
                                  ) {
}
