package com.FindAJob.demo.refreshtoken.domain.entities;

import com.FindAJob.demo.companies.domain.entities.Companies;
import com.FindAJob.demo.reg_users.domain.entities.Reg_Users;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "regular_users_id", nullable = false, unique = true)
    private Reg_Users user;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false, unique = true)
    private Companies comp;

    public RefreshToken(String token,
                        Instant expiresAt,
                        Instant createdAt,
                        Reg_Users user){
        this.token = token;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
        this.user = user;
    }

    public RefreshToken(String token,
                        Instant expiresAt,
                        Instant createdAt,
                        Companies comp){
        this.token = token;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
        this.comp = comp;
    }



    public RefreshToken(){


    }

    public UUID getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Reg_Users getUser() {
        return user;
    }

    public void setUser(Reg_Users user) {
        this.user = user;
    }

    public Companies getComp() {
        return comp;
    }

    public void setComp(Companies comp) {
        this.comp = comp;
    }
}
