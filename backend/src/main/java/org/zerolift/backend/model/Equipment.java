package org.zerolift.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.UUID;

@Entity
@Data
public class Equipment {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    @Column(columnDefinition = "TEXT")
    private String description;
}