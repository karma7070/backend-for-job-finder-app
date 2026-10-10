package com.FindAJob.demo.companies;

import com.FindAJob.demo.SecurityPackage.UserRoles;
import com.FindAJob.demo.companies.domain.repos.CompRepository;
import com.FindAJob.demo.companies.domain.entities.Companies;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class CompConfig implements CommandLineRunner{
//CommandLineRunner is an interface that makes sure whatever is written inside runs at boot or is executed at boot

  private final CompRepository comprepo;
  private final PasswordEncoder passWE;

  public CompConfig(CompRepository comprepo, PasswordEncoder passWE){
    this.comprepo = comprepo;
      this.passWE = passWE;
  }

  @Override
  public void run(String @NonNull ... args) throws Exception {
    if(comprepo.count() == 0){//counts number of rows in a table and if there are non it inserts data else... yknow.
        List<Companies> companies = List.of(


                new Companies(
                        "Microsoft",
                        "Redmond, Washington",
                        "jobs@microsoft.com",
                        passWE.encode("njcalmlkds"),
                        UserRoles.Company
                ),

                new Companies(
                        "Tesla",
                        "Austin, Texas",
                        "recruitment@tesla.com",
                        passWE.encode("njcalmlkds"),
                        UserRoles.Company
                ),

                new Companies(
                        "Deloitte",
                        "London, United Kingdom",
                        "hr@deloitte.com",
                        passWE.encode("njcalmlkds"),
                        UserRoles.Company
                ),

                new Companies(
                        "Regional Hospital Buea",
                        "Buea, Cameroon",
                        "careers@rhb.cm",
                        passWE.encode("njcalmlkds"),
                        UserRoles.Company
                ),

                new Companies(
                        "Care Services Ltd",
                        "Douala, Cameroon",
                        "jobs@careservices.cm",
                        passWE.encode("njcalmlkds"),
                        UserRoles.Company
                )

        );

        comprepo.saveAll(companies);
  }

}
}
