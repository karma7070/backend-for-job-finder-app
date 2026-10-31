package com.FindAJob.demo.Admin.domain.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "admin_table")
public class Admin {

    @Id
    @GeneratedValue( strategy = GenerationType.UUID)
    @Column(name = "id", unique = true)
    private UUID id;

    @Column(name = "admin_name")
    private String adminName;

    @Column(name = "admin_password")
    private String password;

    public Admin(String adminName, String password){
        this.adminName = adminName;
        this.password = password;
    };


    public Admin(){

    }

    public UUID getId(){
        return id;
    }

    public void setAdminName(String adminName){
        this.adminName = adminName;
    }

    public String getAdminName(){
        return adminName;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return password;
    }
}

