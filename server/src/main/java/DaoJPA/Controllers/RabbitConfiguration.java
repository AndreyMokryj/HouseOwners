package DaoJPA.Controllers;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static rabbit.RabbitApp.QUEUE_SPECIFIC_NAME;

@Configuration
public class RabbitConfiguration {
    @Bean
    public Queue appQueueSpecific() {
        return new Queue(QUEUE_SPECIFIC_NAME);
    }
}
