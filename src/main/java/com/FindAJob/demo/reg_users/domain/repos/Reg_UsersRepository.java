package com.FindAJob.demo.reg_users.domain.repos;

import com.FindAJob.demo.reg_users.domain.entities.Reg_Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface Reg_UsersRepository extends JpaRepository<Reg_Users, UUID> {

    Optional<Reg_Users> findByEmail(String email);

}
