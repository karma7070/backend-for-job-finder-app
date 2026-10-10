package com.FindAJob.demo.reg_users;

import com.FindAJob.demo.SecurityPackage.UserRoles;
import com.FindAJob.demo.reg_users.domain.entities.Reg_Users;
import com.FindAJob.demo.reg_users.domain.repos.Reg_UsersRepository;
import com.FindAJob.demo.reg_users.publicenums.Gen_Type;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class Reg_UsersConfig implements CommandLineRunner {

    private final Reg_UsersRepository repository;
    private final PasswordEncoder passEn;

    public Reg_UsersConfig(Reg_UsersRepository repository, PasswordEncoder passEn) {
        this.repository = repository;
        this.passEn = passEn;
    }


    @Override
    public void run(String @NonNull ... args) {
        if (repository.count() == 0) {
            List<Reg_Users> users = List.of(
                    new Reg_Users(
                            "Grace Wilson",
                            26,
                            Gen_Type.FEMALE,
                            "Accountant",
                            "grace.wilson@example.com",
                            passEn.encode("ebualnelkna"),
                            UserRoles.Reg_User
                    ),

                    new Reg_Users(
                            "Daniel Smith",
                            31,
                            Gen_Type.MALE,
                            "Lawyer",
                            "daniel.smith@example.com",
                            passEn.encode("ebualnelkna"),
                            UserRoles.Reg_User
                    ),

                    new Reg_Users(
                            "Emily Davis",
                            24,
                            Gen_Type.FEMALE,
                            "Nurse",
                            "emily.davis@example.com",
                            passEn.encode("ebualnelkna"),
                            UserRoles.Reg_User
                    ),

                    new Reg_Users(
                            "James Anderson",
                            29,
                            Gen_Type.MALE,
                            "Backend Developer",
                            "james.anderson@example.com",
                            passEn.encode("ebualnelkna"),
                            UserRoles.Reg_User
                    ),

                    new Reg_Users(
                            "Sophia Taylor",
                            23,
                            Gen_Type.FEMALE,
                            "UI/UX Designer",
                            "sophia.taylor@example.com",
                            passEn.encode("ebualnelkna"),
                            UserRoles.Reg_User
                    ),

                    new Reg_Users(
                            "David Williams",
                            33,
                            Gen_Type.MALE,
                            "Cybersecurity Analyst",
                            "david.williams@example.com",
                            passEn.encode("ebualnelkna"),
                            UserRoles.Reg_User
                    ),

                    new Reg_Users(
                            "Olivia Martinez",
                            30,
                            Gen_Type.FEMALE,
                            "Data Scientist",
                            "olivia.martinez@example.com",
                            passEn.encode("ebualnelkna"),
                            UserRoles.Reg_User
                    )

            );

            repository.saveAll(users);
        }
    }
}

