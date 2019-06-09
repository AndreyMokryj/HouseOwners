package UserJPA;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@EnableDiscoveryClient
@SpringBootApplication
@RestController
@RequestMapping(path = "/config")

//@EnableAutoConfiguration
//@ComponentScan
//@Import(RabbitConfiguration.class)
public class Application {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private Environment env;

    @GetMapping(path="/", produces = "application/json; charset=UTF-8")
    public @ResponseBody
    String getProperties() throws JsonProcessingException {
        Map<String, Object> props = new HashMap<>();
        CompositePropertySource bootstrapProperties = (CompositePropertySource)  ((AbstractEnvironment) env).getPropertySources().get("bootstrapProperties");
        for (String propertyName : bootstrapProperties.getPropertyNames()) {
            props.put(propertyName, bootstrapProperties.getProperty(propertyName));
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        return mapper.writeValueAsString(props);
    }

    //Rabbit

//    public static final String topicExchangeName = "spring-boot-exchange";
//
//    public static final String queueName = "spring-boot";
//
//    public static final String queueSpecificName = "spring-boot-s";
//
//    @Bean
//    Queue queue() {
//        return new Queue(queueName);
//    }
//
//    @Bean
//    TopicExchange exchange() {
//        return new TopicExchange(topicExchangeName);
//    }
//
//    @Bean
//    Binding binding(Queue queue, TopicExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with("foo.bar.#");
//    }
//
//    @Bean
//    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
//                                             MessageListenerAdapter listenerAdapter) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames(queueName);
//        container.setMessageListener(listenerAdapter);
//        return container;
//    }
//
//    @Bean
//    MessageListenerAdapter listenerAdapter(CityController receiver) {
//        return new MessageListenerAdapter(receiver, "receiveMessage");
//    }
//
//    //Custom serializator
//    @Bean
//    public TopicExchange appExchange() {
//        return new TopicExchange("topicExchange");
//    }
//
//    @Bean
//    public Queue appQueueGeneric() {
//        return new Queue("qgeneric");
//    }
//
//    @Bean
//    public Queue appQueueSpecific() {
//        return new Queue(queueSpecificName);
//    }
//
//    @Bean
//    public Binding declareBindingGeneric() {
//        return BindingBuilder.bind(appQueueGeneric()).to(appExchange()).with("key");
//    }
//
//    @Bean
//    public Binding declareBindingSpecific() {
//        return BindingBuilder.bind(appQueueSpecific()).to(appExchange()).with("key");
//    }
//
//
//    @Bean
//    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
//        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
//        return rabbitTemplate;
//    }
//
//    @Bean
//    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }

//    public static final String EXCHANGE_NAME = "spring-boot-exchange";
//    public static final String QUEUE_GENERIC_NAME = "spring-boot1";
//    public static final String QUEUE_SPECIFIC_NAME = "spring-boot-s1";
//    public static final String ROUTING_KEY = "routing-key";
//
//    @Bean
//    public TopicExchange appExchange() {
//        return new TopicExchange(EXCHANGE_NAME);
//    }
//
//    @Bean
//    public Queue appQueueGeneric() {
//        return new Queue(QUEUE_GENERIC_NAME, false);
//    }
//
//    @Bean
//    public Queue appQueueSpecific() {
//        return new Queue(QUEUE_SPECIFIC_NAME);
//    }
//
//    @Bean
//    public Binding declareBindingGeneric() {
//        return BindingBuilder.bind(appQueueGeneric()).to(appExchange()).with(ROUTING_KEY);
//    }
//
//    @Bean
//    public Binding declareBindingSpecific() {
//        return BindingBuilder.bind(appQueueSpecific()).to(appExchange()).with(ROUTING_KEY);
//    }
//
//    @Bean
//    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
//        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
//        return rabbitTemplate;
//    }
//
//    @Bean
//    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }
}
