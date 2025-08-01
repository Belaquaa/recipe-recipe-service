package dika.recipeservice.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@Configuration
@EnableAspectJAutoProxy
@EnableAsync
public class AopConfig {

    //создаем дополнительный поток для асинхронных задач
    //в данном случае для индексации рецептов в elasticsearch
    @Bean("elasticsearchTaskExecutor")
    public TaskExecutor elasticsearchTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("elasticsearch-");
        executor.initialize();
        return executor;
    }
}

