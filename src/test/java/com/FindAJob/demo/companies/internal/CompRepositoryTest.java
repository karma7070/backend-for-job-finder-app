package com.FindAJob.demo.companies.internal;

import com.FindAJob.demo.SecurityPackage.UserRoles;
import com.FindAJob.demo.companies.domain.entities.Companies;
import com.FindAJob.demo.companies.domain.repos.CompRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@DataJpaTest
class CompRepositoryTest {

    @Autowired
    private CompRepository underTest;

    @Test
    void checkExistenceByEmail(){
  //Using a 3 step testing method

        //given
        String email = "google@gmail.com";

        Companies comp = new Companies(
                "Google",
                "California",
                email,
                "woimaowimdaoimw",
                UserRoles.Company
        );
        underTest.save(comp);

        //when
        boolean exists = underTest.existsByCompEmail(email);

        //then
        assertThat(exists).isTrue();

    }
}