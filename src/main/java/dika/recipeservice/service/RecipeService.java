package dika.recipeservice.service;


import dika.recipeservice.dto.RecipeCreateDto;
import dika.recipeservice.dto.RecipeDto;
import dika.recipeservice.dto.RecipePageDto;
import org.springframework.data.domain.Pageable;


public interface RecipeService {

    RecipePageDto getAllRecipes(Pageable pageable);

    RecipeDto createRecipe(RecipeCreateDto recipeDto);

    RecipeDto update(Long id, RecipeCreateDto recipeDto);

    void deleteRecipe(Long id);

    RecipeDto getRecipe(Long id);
}
