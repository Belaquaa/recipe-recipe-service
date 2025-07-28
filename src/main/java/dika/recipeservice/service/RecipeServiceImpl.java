package dika.recipeservice.service;


import dika.recipeservice.mapper.RecipeMapper;
import dika.recipeservice.dto.RecipeDto;
import dika.recipeservice.dto.RecipePageDto;
import dika.recipeservice.enums.DifficultyLevel;
import dika.recipeservice.enums.Status;
import dika.recipeservice.exception.RecipeNotFound;
import dika.recipeservice.model.Recipe;
import dika.recipeservice.repository.RecipeRepository;
import dika.recipeservice.service.impl.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;


@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;

    public RecipePageDto getAllRecipes(Pageable pageable) {
        Page<Recipe> recipes = recipeRepository.findAll(pageable);
        return RecipePageDto.from(recipes.map(recipeMapper::toDto));
    }

    public RecipeDto createRecipe(RecipeDto recipeDto) {
        return recipeMapper.toDto(recipeRepository.save(recipeMapper.toEntity(recipeDto)));
    }

    public RecipeDto update(Long id, RecipeDto recipeDto) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFound("Recipe not found"));
        updateFields(recipeDto.description(), recipe::setDescription);
        updateFields(recipeDto.ingredients(), recipe::setIngredients);
        updateFields(recipeDto.title(), recipe::setTitle);
        updateFields(recipeDto.instructions(), recipe::setInstructions);
        updateFields(recipeDto.difficulty(), recipe::setDifficulty);
        updateFields(recipeDto.prepTime(), recipe::setPrepTime);
        updateFields(recipeDto.cookTime(), recipe::setCookTime);
        updateFields(recipeDto.status(), recipe::setStatus);
        updateFields(recipeDto.servings(), recipe::setServings);
        recipeRepository.save(recipe);
        return recipeMapper.toDto(recipe);
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    public RecipeDto getRecipe(Long id) {
        return recipeMapper.toDto(recipeRepository
                .findById(id).orElseThrow(() -> new RecipeNotFound("recipe with id {} not found" + id)));
    }

    private void updateFields(String newParam, Consumer<String> oldParam) {
        if (newParam != null && !newParam.isEmpty()) {
            oldParam.accept(newParam);
        }
    }

    private void updateFields(List<String> newParam, Consumer<List<String>> oldParam) {
        if (newParam != null && !newParam.isEmpty()) {
            oldParam.accept(newParam);
        }
    }

    private void updateFields(DifficultyLevel newParam, Consumer<DifficultyLevel> oldParam) {
        if (newParam != null) {
            oldParam.accept(newParam);
        }
    }

    private void updateFields(Status newParam, Consumer<Status> oldParam) {
        if (newParam != null) {
            oldParam.accept(newParam);
        }
    }

    private void updateFields(Integer newParam, Consumer<Integer> oldParam) {
        if (newParam != null) {
            oldParam.accept(newParam);
        }
    }
}
