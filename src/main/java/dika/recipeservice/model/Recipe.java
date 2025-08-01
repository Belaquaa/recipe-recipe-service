package dika.recipeservice.model;


import dika.recipeservice.enums.DifficultyLevel;
import dika.recipeservice.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;


@Getter
@Setter
@Entity
@Table(name = "recipes")

public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "author_external_id", nullable = false, updatable = false, columnDefinition = "UUID")
    private UUID authorExternalId;
    @Column(name = "author_username", nullable = false, updatable = false)
    private String authorUsername;
    @Column(name = "title", nullable = false, length = 100)
    private String title;
    @Column(name = "ingredients", nullable = false, length = 255)
    private String ingredients;
    @Column(name = "description", nullable = false, length = 255)
    private String description;
    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;
    @Column(name = "difficulty", nullable = false)
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficulty;
    @Column(name = "prep_time")
    private Integer prepTime;
    @Column(name = "cook_time")
    private Integer cookTime;
    @Column(name = "servings")
    private Integer servings;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
