package com.FindAJob.demo.notifications;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    public Notification (String name, String email){

        this.name = name;
        this.email = email;

    }

    public Notification(){

    }

    public UUID getId(){
        return id;
    }

    public void setName(String name){
       this.name = name;

    }

    public String getName(){
        return name;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }

}
