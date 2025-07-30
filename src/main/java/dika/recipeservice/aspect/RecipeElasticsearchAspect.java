package dika.recipeservice.aspect;


import dika.recipeservice.mapper.RecipeMapper;
import dika.recipeservice.exception.RecipeNotFound;
import dika.recipeservice.exception.SaveException;
import dika.recipeservice.model.Recipe;
import dika.recipeservice.repository.RecipeSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RecipeElasticsearchAspect {

    private final RecipeSearchRepository recipeSearchRepository;
    private final RecipeMapper recipeMapper;

    // Определяем точки среза для методов сохранения и удаления рецептов
    // в репозитории RecipeRepository, чтобы перехватывать их вызовы
    // и выполнять асинхронную индексацию в Elasticsearch.

    @Pointcut("execution(* dika.recipeservice.repository.RecipeRepository.save(..))")
    public void recipeSaveMethods() {}

    @Pointcut("execution(* dika.recipeservice.repository.RecipeRepository.deleteById(..))")
    public void recipeDeleteMethods() {}

    // Перехватываем успешное сохранение рецепта и выполняем асинхронную индексацию
    @AfterReturning(pointcut = "recipeSaveMethods()", returning = "savedRecipe")
    public void afterSuccessfulSave(Recipe savedRecipe) {
        indexRecipeAsync(savedRecipe);
    }
    // Перехватываем ошибку при сохранении рецепта и выбрасываем исключение SaveException
    @AfterThrowing(value = "execution(* dika.recipeservice.repository.RecipeRepository.save(..))",
            throwing = "ex")
    public void afterSaveError(Exception ex) {
        log.error("Ошибка при сохранении рецепта в базу данных: {}", ex.getMessage(), ex);
        throw new SaveException("ошибка сохранения");
    }

    // Перехватываем успешное удаление рецепта по ID и выполняем асинхронное удаление из индекса
    @AfterReturning(pointcut = "recipeDeleteMethods() && args(id)")
    public void afterDeleteById(Long id) {
        deleteFromIndexAsync(id);
    }

    // Перехватываем ошибку при удалении рецепта и выбрасываем исключение RecipeNotFound
    @Before(value = "execution(* dika.recipeservice.repository.RecipeRepository.deleteById(..)) && args(recipe)")
    public void beforeDelete(Recipe recipe) {
        if (recipe == null || recipe.getId() == null) {
            throw new RecipeNotFound("Рецепт с ID " + recipe.getId() + " не найден для удаления");
        }
    }


    // индексируем рецепт в Elasticsearch после успешного сохранения
    @Async("elasticsearchTaskExecutor")
    protected void indexRecipeAsync(Recipe recipe) {
        try {
            recipeSearchRepository.save(recipeMapper.toRecipeElasticDto(recipe));
        } catch (Exception e) {
            log.error("Ошибка индексации рецепта в ES: {}. Рецепт ID: {}",
                    e.getMessage(), recipe.getId());
        }
    }

    // удаляем рецепт из индекса Elasticsearch после успешного удаления
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
