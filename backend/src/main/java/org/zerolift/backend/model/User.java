package org.zerolift.backend.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    private String username;

    private String email;

    private String passwordHash;

    @OneToMany(mappedBy = "createdBy")
    private List<Exercise> exercises = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy")
    private List<Workout> workouts = new ArrayList<>();
}
