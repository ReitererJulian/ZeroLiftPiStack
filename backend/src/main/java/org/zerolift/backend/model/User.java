package org.zerolift.backend.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users") // 'user' ist ein reserviertes SQL-Wort, daher 'users'
@Data
public class User {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false) // Match mit DB-Feld
    private String passwordHash;

    @OneToMany(mappedBy = "createdBy")
    private List<Exercise> exercises = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy")
    private List<Workout> workouts = new ArrayList<>();
}