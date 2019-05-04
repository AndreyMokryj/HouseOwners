package app.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import rabbit.RabbitApp;
import vo.CityVO;
import vo.CustomMessage;
import vo.Exceptions.ItemNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

@RestController
@RequestMapping(path="/cities")
public class CityServiceController {
    @Autowired
    RestTemplate restTemplate1;

    //Rabbit send
    private static final Logger log = LoggerFactory.getLogger(CityServiceController.class);

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(String x) {
        final CustomMessage message = new CustomMessage(x, new Random().nextInt(50), false);
        log.info("Sending message...");
        rabbitTemplate.convertAndSend(RabbitApp.EXCHANGE_NAME, RabbitApp.ROUTING_KEY, message);
    }

    public CityServiceController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    //Rabbit receive
//    private CountDownLatch latch = new CountDownLatch(1);
//
//    public void receiveMessage(String message) {
//        System.out.println("Received <" + message + ">");
//        latch.countDown();
//    }
//
//    @RabbitListener(queues = RabbitApp.QUEUE_GENERIC_NAME1)
//    public void receiveMessage(final Message message) {
//        log.info("Received message as generic: {}", message.toString());
//    }
//
//    @RabbitListener(queues = RabbitApp.QUEUE_SPECIFIC_NAME1)
//    public void receiveMessage(final CustomMessage customMessage) {
//        log.info("Received message as specific class: {}", customMessage.toString());
//    }
//
//    public CountDownLatch getLatch() {
//        return latch;
//    }


    @GetMapping(path = "/")
    public String getVendors()
    {
        String response = restTemplate1.exchange("http://service0/cities/",
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}).getBody();

        System.out.println("Response Received as " + response);

        return response;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable long id)
    {
        try {
            String response = restTemplate1.exchange("http://service0/cities/{id}",
                    HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}, id).getBody();

            System.out.println("Response Received as " + response);

            return response;
        }
        catch (org.springframework.web.client.HttpClientErrorException ex){
            throw new ItemNotFoundException("City with id=" + id + " doesn't exist");
        }

    }

    @PostMapping("/")
    public void createVendor(@RequestBody CityVO cityVO)
    {
        String message = "Request to create city: " + cityVO;
        //rabbitTemplate.convertAndSend(Application.EXCHANGE_NAME, "foo.bar.baz", message);
        sendMessage(message);

        Object response = restTemplate1.postForObject("http://service0/cities/", cityVO, Object.class);
        System.out.println("Response Received as " + response);

        //return response;
    }

    @DeleteMapping("/delete/{id}")
    public void deleteVendor(@PathVariable long id) {
        String message = "Request to delete city with id = " + id;
        sendMessage(message);
        //rabbitTemplate.convertAndSend(Application.EXCHANGE_NAME, "foo.bar.baz", message);

        Map<String, Long> params = new HashMap<String, Long>();
        params.put("id", id);

        try {
            restTemplate1.delete("http://service0/cities/delete/{id}", params);
        }
        catch (org.springframework.web.client.HttpClientErrorException ex){
            throw new ItemNotFoundException("City with id=" + id + " doesn't exist");
        }
    }

    @PutMapping("/{id}")
    public void updateVendor(@RequestBody CityVO cityVO, @PathVariable long id) {
        String message = "Request to update city with id = " + id+ ", body:" + cityVO;
        //rabbitTemplate.convertAndSend(Application.EXCHANGE_NAME, "foo.bar.baz", message);
        sendMessage(message);

        Map<String, Long> params = new HashMap<String, Long>();
        params.put("id", id);

        try {
            restTemplate1.put( "http://service0/cities/{id}", cityVO, params);
        }
        catch (org.springframework.web.client.HttpClientErrorException ex){
            throw new ItemNotFoundException("City with id=" + id + " doesn't exist");
        }
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate1() {
        return new RestTemplate();
    }
}
