package dika.recipeservice.service.impl;


import dika.recipeservice.dto.RecipeCreateDto;
import dika.recipeservice.dto.RecipeDto;
import dika.recipeservice.dto.RecipePageDto;
import dika.recipeservice.enums.DifficultyLevel;
import dika.recipeservice.enums.Status;
import dika.recipeservice.exception.RecipeNotFound;
import dika.recipeservice.mapper.RecipeMapper;
import dika.recipeservice.model.Recipe;
import dika.recipeservice.rabbit.RabbitEventPublisher;
import dika.recipeservice.repository.RecipeRepository;
import dika.recipeservice.service.JwtService;
import dika.recipeservice.service.RecipeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;
import java.util.function.Consumer;


@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private final RabbitEventPublisher rabbitEventPublisher;
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    public RecipePageDto getAllRecipes(Pageable pageable) {
        Page<Recipe> recipes = recipeRepository.findAll(pageable);
        return recipeMapper.toRecipePageDto(recipes.map(recipeMapper::toDto));
    }

    @Transactional
    public RecipeDto createRecipe(RecipeCreateDto recipeDto) {
        Recipe recipe = recipeMapper.toEntityRecipeCreateDto(recipeDto);
        recipe.setAuthorExternalId(getExternalId());
        recipe.setAuthorUsername(getUsername());
        rabbitEventPublisher.publishRecipeCreatedEvent(recipeDto);
        recipeRepository.save(recipe);
        return recipeMapper.toDto(recipe);
    }

    @Transactional
    public RecipeDto update(Long id, RecipeCreateDto recipeDto) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFound("Recipe not found"));
        if (!recipe.getAuthorExternalId().equals(getExternalId())
                || !recipe.getAuthorUsername().equals(getUsername())) {
            throw new RecipeNotFound("You are not the author of this recipe");
        }
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
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFound("Recipe not found"));
        if (!recipe.getAuthorExternalId().equals(getExternalId())
                || !recipe.getAuthorUsername().equals(getUsername())) {
            throw new RecipeNotFound("You are not the author of this recipe");
        }
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

    private String getToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();
        return request.getHeader("Authorization").substring(7);
    }

    private String getUsername() {
        return jwtService.extractUsername(getToken());
    }

    private UUID getExternalId() {
        return jwtService.extractExternalId(getToken());
    }
}
