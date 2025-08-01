package dika.recipeservice.service.impl;


import dika.recipeservice.rabbit.RabbitEventPublisher;
import dika.recipeservice.dto.RecipeCreateDto;
import dika.recipeservice.dto.RecipeDto;
import dika.recipeservice.dto.RecipePageDto;
import dika.recipeservice.enums.DifficultyLevel;
import dika.recipeservice.enums.Status;
import dika.recipeservice.exception.RecipeNotFound;
import dika.recipeservice.mapper.RecipeMapper;
import dika.recipeservice.model.Recipe;
import dika.recipeservice.repository.RecipeRepository;
import dika.recipeservice.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;


@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private final RabbitEventPublisher rabbitEventPublisher;

    @Transactional(readOnly = true)
    public RecipePageDto getAllRecipes(Pageable pageable) {
        Page<Recipe> recipes = recipeRepository.findAll(pageable);
        return recipeMapper.toRecipePageDto(recipes.map(recipeMapper::toDto));
    }

    @Transactional
    public RecipeDto createRecipe(RecipeCreateDto recipeDto) {
        rabbitEventPublisher.publishRecipeCreatedEvent(recipeDto);
        return recipeMapper.toDto(recipeRepository.save(recipeMapper.toEntityRecipeCreateDto(recipeDto)));
    }

    @Transactional
    public RecipeDto update(Long id, RecipeCreateDto recipeDto) {
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

    @Transactional
    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public RecipeDto getRecipe(Long id) {
        return recipeMapper.toDto(recipeRepository
                .findById(id).orElseThrow(() -> new RecipeNotFound("recipe with id not found" + id)));
    }

    private void updateFields(String newParam, Consumer<String> oldParam) {
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
