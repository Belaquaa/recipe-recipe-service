package dika.recipeservice.dto;


import dika.recipeservice.enums.DifficultyLevel;
import dika.recipeservice.enums.Status;


//будет использоваться для создания рецепта после добавления извлечения из Bearer юзера
public record RecipeCreateDto(
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
