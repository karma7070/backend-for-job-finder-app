package com.FindAJob.demo.reg_users.domain.entities;

import com.FindAJob.demo.SecurityPackage.UserRoles;
import com.FindAJob.demo.reg_users.publicenums.Gen_Type;
import jakarta.persistence.*;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "regular_users")
public class Reg_Users implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "gender", nullable = false)
    private Gen_Type gender;

    @Column(name = "profession", nullable = false)
    private String profession;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "roles", nullable = false)
    private UserRoles roles;

    public Reg_Users(String name,
                     Integer age,
                     Gen_Type gender,
                     String profession,
                     String email,
                     String password, UserRoles roles){
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.profession = profession;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public Reg_Users(){

    }

    public UUID getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Gen_Type getGender() {
        return gender;
    }

    public void setGender(Gen_Type gender) {
        this.gender = gender;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return password;
    }

    public UserRoles getRole(){
        return roles;
    }

    @Override
    public @NonNull Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"
        + this.roles.name()));
    }

    @Override
    public @NonNull String getUsername() {

        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
