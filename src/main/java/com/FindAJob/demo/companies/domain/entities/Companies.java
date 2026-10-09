package com.FindAJob.demo.companies.domain.entities;

import com.FindAJob.demo.SecurityPackage.UserRoles;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "companies_or_employers")
public class Companies implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Valid
    @Column(name = "comp_name", nullable = false, unique = true)
    private String comp_name;

    @Valid
    @Column(name = "location")
    private String location;

    @Column(name = "comp_email", nullable = false)
    private String compEmail;

    @Valid
    @Column(name = "password", nullable = false)
    private String password;

    @Valid
    @Column(name = "role", nullable = false)
    private UserRoles role;

    public Companies(String comp_name,
                     String location,
                     String compEmail,
                     String password,
                     UserRoles role) {
        this.comp_name = comp_name;
        this.location = location;
        this.compEmail = compEmail;
        this.password = password;
        this.role = UserRoles.Company;
    }

    public Companies() {

    }

    public UUID getId() {
        return id;
    }

    public String getComp_name() {
        return comp_name;
    }

    public void setComp_name(String comp_name) {
        this.comp_name = comp_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCompEmail() {
        return compEmail;
    }

    public void setCompEmail(String compEmail) {
        this.compEmail = compEmail;
    }

    @Override
    public @NonNull  Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"
                + this.role.name()));
    }

    public String getPassword() {
        return password;
    }

    @Override
    public @NonNull String getUsername() {
        return this.compEmail;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }
}