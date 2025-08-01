package dika.recipeservice.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableRabbit
@Slf4j
public class RabbitMQConfig {

    public static final String DLX_EXCHANGE = "dlx.exchange";
    public static final String RECIPE_CREATE_DLQ = "recipe.create.dlq";
    public static final String RECIPE_CREATE_QUEUE = "recipe.create.queue";
    public static final String RECIPE_EXCHANGE = "recipe.exchange";

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public TopicExchange recipeExchange() {
        return new TopicExchange(RECIPE_EXCHANGE);
    }

    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(DLX_EXCHANGE);
    }

    @Bean
    public Queue recipeCreatedQueue() {
        return QueueBuilder.durable(RECIPE_CREATE_QUEUE)
                .withArgument("x-message-ttl", 300000)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "recipe.create.failed")
                .build();
    }

    @Bean
    public Queue recipeCreateDeadLetterQueue() {
        return QueueBuilder.durable(RECIPE_CREATE_DLQ)
                .withArgument("x-message-ttl", 86400000)
                .build();
    }

    @Bean
    public Binding recipeCreatedBinding() {
        return BindingBuilder
                .bind(recipeCreatedQueue())
                .to(recipeExchange())
                .with("recipe.create");
    }

    @Bean
    public Binding recipeCreateDLQBinding() {
        return BindingBuilder
                .bind(recipeCreateDeadLetterQueue())
                .to(deadLetterExchange())
                .with("recipe.create.failed");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }
}
