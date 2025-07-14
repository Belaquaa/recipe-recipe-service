package dika.recipeservice.model;

import dika.recipeservice.enums.DifficultyLevel;
import dika.recipeservice.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "recipes")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "author_external_id", nullable = false, unique = true, updatable = false, columnDefinition = "UUID")
    private UUID authorExternalId;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "ingredients", nullable = false, length = 255)
    private String ingredients;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "instructions", nullable = true, columnDefinition = "TEXT")
    @Lob
    private String instructions;

    @Column(name = "difficulty", nullable = false)
    private DifficultyLevel difficulty;

    @Column(name = "prep_time", nullable = true, length = 255)
    private Duration prepTime;

    @Column(name = "cook_time", nullable = true, length = 255)
    private Duration cookTime;

    @Column(name = "servings", nullable = true, length = 255)
    private String servings;

    @Column(name = "status", nullable = true, length = 255)
    private Status status;

    @Column(name = "created_at", nullable = true, length = 255)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = true, length = 255)
    private Instant updatedAt;

}
