package dika.recipeservice.config;


import dika.recipeservice.dto.RecipeElasticDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;


@Slf4j
@Configuration
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    @Value("${spring.elasticsearch.uris}")
    private String elasticsearchHost;

    // Настройка подключения к Elasticsearch
    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(elasticsearchHost)
                .build();
    }

    // Создание индекса при запуске приложения
    // Если индекс уже существует, он не будет пересоздан
    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        try {
            // Получаем экземпляр ElasticsearchOperations для работы с индексами
            //ElasticsearchOperations это интерфейс, который предоставляет методы для работы с индексами и документами в Elasticsearch
            ElasticsearchOperations operations = event.getApplicationContext()
                    .getBean(ElasticsearchOperations.class);

            IndexOperations indexOps = operations.indexOps(RecipeElasticDto.class);
            if (!indexOps.exists()) {
                indexOps.create();
                indexOps.putMapping();
            }
        } catch (Exception e) {
            log.error("Ошибка при создании индекса: {}", e.getMessage());
        }
    }
}