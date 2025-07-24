package dika.recipeservice.aspect;


import dika.recipeservice.RecipeMapper;
import dika.recipeservice.exception.SaveException;
import dika.recipeservice.model.Recipe;
import dika.recipeservice.repository.RecipeSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RecipeElasticsearchAspect {

    private final RecipeSearchRepository recipeSearchRepository;
    private final RecipeMapper recipeMapper;

    @AfterReturning(value = "execution(* dika.recipeservice.repository.RecipeRepository.save(..))",
            returning = "savedRecipe")
    public void afterSuccessfulSave(Recipe savedRecipe) {
        indexRecipeAsync(savedRecipe);
    }

    @AfterThrowing(value = "execution(* dika.recipeservice.repository.RecipeRepository.save(..))",
            throwing = "ex")
    public void afterSaveError(Exception ex) {
        log.error("Ошибка при сохранении рецепта в базу данных: {}", ex.getMessage(), ex);
        throw new SaveException("ошибка сохранения");
    }

    @AfterReturning(value = "execution(* dika.recipeservice.repository.RecipeRepository.deleteById(..)) && args(id)")
    public void afterDeleteById(Long id) {
        deleteFromIndexAsync(id);
    }

    @Before(value = "execution(* dika.recipeservice.repository.RecipeRepository.delete(..)) && args(recipe)")
    public void beforeDelete(Recipe recipe) {
        if (recipe != null && recipe.getId() != null) {
        }
    }

    @Async("elasticsearchTaskExecutor")
    protected void indexRecipeAsync(Recipe recipe) {
        try {
            recipeSearchRepository.save(recipeMapper.toRecipeElasticDto(recipe));
        } catch (Exception e) {
            log.error("Ошибка индексации рецепта в ES: {}. Рецепт ID: {}",
                    e.getMessage(), recipe.getId());
        }
    }

    @Async("elasticsearchTaskExecutor")
    protected void deleteFromIndexAsync(Long id) {
        try {
            recipeSearchRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Ошибка удаления рецепта из ES: {}. Рецепт ID: {}",
                    e.getMessage(), id);
        }
    }
}
