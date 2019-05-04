package rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RabbitApp {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(RabbitApp.class, args);
    }

    public static final String EXCHANGE_NAME = "spring-boot-exchange";
    public static final String QUEUE_GENERIC_NAME = "spring-boot";
    public static final String QUEUE_SPECIFIC_NAME = "spring-boot-s";
    public static final String ROUTING_KEY = "routing-key";

    @Bean
    public TopicExchange appExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue appQueueGeneric() {
        return new Queue(QUEUE_GENERIC_NAME, false);
    }

    @Bean
    public Queue appQueueSpecific() {
        return new Queue(QUEUE_SPECIFIC_NAME);
    }

    @Bean
    public Binding declareBindingGeneric() {
        return BindingBuilder.bind(appQueueGeneric()).to(appExchange()).with(ROUTING_KEY);
    }

    @Bean
    public Binding declareBindingSpecific() {
        return BindingBuilder.bind(appQueueSpecific()).to(appExchange()).with(ROUTING_KEY);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    //Second
//    public static final String EXCHANGE_NAME1 = "spring-boot-exchange1";
//    public static final String QUEUE_GENERIC_NAME1 = "spring-boot1";
//    public static final String QUEUE_SPECIFIC_NAME1 = "spring-boot-s1";
//    public static final String ROUTING_KEY1 = "routing-key1";
//
//    @Bean
//    public TopicExchange appExchange1() {
//        return new TopicExchange(EXCHANGE_NAME1);
//    }
//
//    @Bean
//    public Queue appQueueGeneric1() {
//        return new Queue(QUEUE_GENERIC_NAME1, false);
//    }
//
//    @Bean
//    public Queue appQueueSpecific1() {
//        return new Queue(QUEUE_SPECIFIC_NAME1);
//    }
//
//    @Bean
//    public Binding declareBindingGeneric1() {
//        return BindingBuilder.bind(appQueueGeneric1()).to(appExchange1()).with(ROUTING_KEY1);
//    }
//
//    @Bean
//    public Binding declareBindingSpecific1() {
//        return BindingBuilder.bind(appQueueSpecific1()).to(appExchange1()).with(ROUTING_KEY1);
//    }
//
//    @Bean
//    public RabbitTemplate rabbitTemplate1(final ConnectionFactory connectionFactory) {
//        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
//        return rabbitTemplate;
//    }
//
//    @Bean
//    public Jackson2JsonMessageConverter producerJackson2MessageConverter1() {
//        return new Jackson2JsonMessageConverter();
//    }
}
