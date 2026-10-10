package com.FindAJob.demo.jobs;

import com.FindAJob.demo.SecurityPackage.UserRoles;
import com.FindAJob.demo.companies.domain.repos.CompRepository;
import com.FindAJob.demo.companies.services.CompService;
import com.FindAJob.demo.companies.domain.entities.Companies;
import com.FindAJob.demo.jobs.domain.repos.JobsRepository;
import com.FindAJob.demo.jobs.domain.entities.Jobs;
import com.FindAJob.demo.jobs.publicenums.JobAvailability;
import com.FindAJob.demo.jobs.publicenums.JobFields;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Configuration
public class Config implements CommandLineRunner {

    private final JobsRepository repository;
    private final CompService compserv;
    private final PasswordEncoder passWE;
    private final CompRepository cRepo;

    public Config(JobsRepository repository, CompService compserv, PasswordEncoder passWE, CompRepository cRepo){
        this.repository = repository;
        this.compserv = compserv;

        this.passWE = passWE;
        this.cRepo = cRepo;
    }

    @Override
    public void run(String @NonNull ... args) throws Exception {
        if(repository.count() == 0){

            Optional<Companies> comp = Optional.of(new Companies(
                    "Google",
                    "Mountain View, California",
                    "careers@google.com",
                    passWE.encode("njcalmlkds"),
                    UserRoles.Company
            ));

            cRepo.save(comp.get());

                List<Jobs> jobs = List.of(
                        //this is how u enter elements into an array, either this oe for loops
                        new Jobs(
                                "Backend Java Developer",
                                "Develop and maintain Spring Boot applications and REST APIs.",
                                450000.0,
                                JobFields.CompSci,
                                JobAvailability.AVAILABLE,
                                Instant.now(),
                                "Google",
                                comp.get()
                        ),

                        new Jobs(
                                "Mechanical Engineer",
                                "Design and improve mechanical systems and equipment.",
                                400000.0,
                                JobFields.Engineering,
                                JobAvailability.AVAILABLE,
                                Instant.now(),
                                "Tesla",
                                comp.get()
                        ),

                        new Jobs(
                                "Medical Assistant",
                                "Assist doctors with patient care and medical procedures.",
                                300000.0,
                                JobFields.Medicine,
                                JobAvailability.AVAILABLE,
                                Instant.now(),
                                "Regional Hospital",
                                comp.get()
                        ),

                        new Jobs(
                                "Legal Advisor",
                                "Provide legal consultation and prepare legal documents.",
                                500000.0,
                                JobFields.Law,
                                JobAvailability.UNAVAILABLE,
                                Instant.now(),
                                "Law Firm Ltd",
                                comp.get()
                        ),

                        new Jobs(
                                "Accountant",
                                "Handle financial records, reports, and company budgets.",
                                350000.0,
                                JobFields.Accounting,
                                JobAvailability.AVAILABLE,
                                Instant.now(),
                                "Deloitte",
                                comp.get()
                        ),

                        new Jobs(
                                "Home Caretaker",
                                "Provide daily assistance and care for elderly clients.",
                                200000.0,
                                JobFields.Caretaker,
                                JobAvailability.AVAILABLE,
                                Instant.now(),
                                "Care Services",
                                comp.get()
                        )
                        );

                repository.saveAll(jobs);

                }

        }

}
