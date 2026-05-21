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
public class Workout {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    private String title;

    private String description;

    @ManyToOne
    private User createdBy;

    @ManyToMany
    private List<Exercise> exercises = new ArrayList<>();
}
