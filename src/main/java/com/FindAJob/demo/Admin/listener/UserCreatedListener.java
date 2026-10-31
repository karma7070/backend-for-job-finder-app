package com.FindAJob.demo.Admin.listener;

import com.FindAJob.demo.companies.events.CompanyCreatedEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
public class UserCreatedListener {

    @ApplicationModuleListener
    public void on (CompanyCreatedEvent companyCreatedEvent){

    }
}
