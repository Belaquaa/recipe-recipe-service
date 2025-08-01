package dika.recipeservice.rabbit;


import dika.recipeservice.dto.RecipeCreateDto;
import dika.recipeservice.service.RabbitService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RabbitEventPublisher {

    private final RabbitService rabbitService;
    @Value("${spring.application.name}")
    private String serviceName;

    public void publishRecipeCreatedEvent(RecipeCreateDto recipe) {
        rabbitService.sendCreate(recipe);
    }
}

