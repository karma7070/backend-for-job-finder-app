package com.FindAJob.demo.refreshtoken.domain.repos;

import com.FindAJob.demo.refreshtoken.domain.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByUserEmail(String email);

    Optional<RefreshToken> findByComp_CompEmail(String email);

    RefreshToken deleteAllByUserEmail(String email);

    List<RefreshToken> findAllByUserEmail(String email);

    RefreshToken findByToken(String token);
}
