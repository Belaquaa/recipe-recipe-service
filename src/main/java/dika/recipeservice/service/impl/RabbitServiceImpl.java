package dika.recipeservice.service.impl;


import dika.recipeservice.config.RabbitMQConfig;
import dika.recipeservice.dto.RecipeCreateDto;
import dika.recipeservice.exception.SendingException;
import dika.recipeservice.service.RabbitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitServiceImpl implements RabbitService {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendCreate(RecipeCreateDto recipe) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.RECIPE_EXCHANGE,
                    "recipe.create",
                    recipe
            );
            log.info("Сообщение о создании рецепта {} отправлено в очередь", recipe.title());
        } catch (Exception e) {
            throw new SendingException("Ошибка отправки сообщения", e);
        }
    }
}
