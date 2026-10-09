package com.FindAJob.demo.companies.listeners;


import com.FindAJob.demo.J_Application.events.ApplicationMadeEvent;
import com.FindAJob.demo.jobs.events.JobCreatedEvent;
import com.FindAJob.demo.jobs.events.JobDeletedEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import java.io.PrintStream;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
public class CompanyListener {

    @ApplicationModuleListener
    public CompletableFuture<PrintStream> on(JobCreatedEvent event){
        PrintStream statement = System.out.printf(" You created job with ID %d and title %s", event.id(), event.title());
        return CompletableFuture.completedFuture(statement);
    }

    @ApplicationModuleListener
    public void on(JobDeletedEvent event){
        String msg = "Job DELETED";
        UUID id = event.id();

        System.out.println(msg);
        System.out.println(id);
    }

    @ApplicationModuleListener
    public void on(ApplicationMadeEvent event){
        String info = event.info();
        System.out.println(info);

    }

}
