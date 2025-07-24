package dika.recipeservice.dto;


import dika.recipeservice.enums.DifficultyLevel;
import dika.recipeservice.enums.Status;


public record RecipeDto(
        String authorUsername,
        String title,
        String ingredients,
        String description,
        String instructions,
        DifficultyLevel difficulty,
        Integer prepTime,
        Integer cookTime,
        Integer servings,
        Status status
) {
}
