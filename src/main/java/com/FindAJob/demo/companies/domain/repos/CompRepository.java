package com.FindAJob.demo.companies.domain.repos;

import com.FindAJob.demo.companies.domain.entities.Companies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompRepository extends JpaRepository<Companies, UUID> {

 //   public default Companies findByTitle(CompRequestDTO request, Companies comp){

   //     ArrayList<Companies> companies;
     //   for(int i = 0; )
    //}

    Optional<Companies> findByCompEmail(String compEmail);

    boolean existsByCompEmail(String email);

}
