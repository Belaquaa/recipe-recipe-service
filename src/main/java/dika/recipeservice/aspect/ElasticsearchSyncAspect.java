package dika.recipeservice.aspect;


import dika.recipeservice.repository.RecipeSearchRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


@Aspect
@Component
@RequiredArgsConstructor
public class ElasticsearchSyncAspect {

    private final RecipeSearchRepository recipeSearchRepository;

    @Pointcut("execution(* dika.recipeservice.repository.RecipeRepository.save(..))")
    public void recipeSaveMethods() {}

    @Pointcut("execution(* dika.recipeservice.repository.RecipeRepository.deleteById(..))")
    public void recipeDeleteMethods() {}

    @Pointcut("execution(* dika.recipeservice.service.RecipeService.*(..))")
    public void recipeServiceMethods() {}
}
