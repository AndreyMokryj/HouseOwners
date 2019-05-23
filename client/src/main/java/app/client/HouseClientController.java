package app.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import rabbit.RabbitApp;
import vo.CustomMessage;
import vo.Exceptions.ItemNotFoundException;
import vo.HouseVO;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping(path="/houses")
public class HouseClientController {
    @Autowired
    RestTemplate restTemplate1;

    //Rabbit send
    private static final Logger log = LoggerFactory.getLogger(HouseClientController.class);

    private final RabbitTemplate rabbitTemplate;

    public HouseClientController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String x) {
        final CustomMessage message = new CustomMessage(x, new Random().nextInt(50), false);
        log.info("Sending message...");
        rabbitTemplate.convertAndSend(RabbitApp.EXCHANGE_NAME, RabbitApp.ROUTING_KEY, message);
    }

    @GetMapping(path = "/")
    public String getHouses()
    {
        String response = restTemplate1.exchange("http://owner-service/houses/",
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}).getBody();

        System.out.println("Response Received as " + response);

        return response;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable long id)
    {
        try {
            String response = restTemplate1.exchange("http://owner-service/houses/{id}",
                    HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}, id).getBody();

            System.out.println("Response Received as " + response);

            return response;
        }
        catch (org.springframework.web.client.HttpClientErrorException ex){
            throw new ItemNotFoundException("House with id=" + id + " doesn't exist");
        }
    }

    @PostMapping("/")
    public void createHouse(@RequestBody HouseVO house)
    {
        String message = "Request to create house: " + house;
        sendMessage(message);
//        rabbitTemplate.convertAndSend(Application.topicExchangeName, "foo.bar.baz", message);

        Object response = restTemplate1.postForObject("http://owner-service/houses/", house, Object.class);
        System.out.println("Response Received as " + response);

        //return response;
    }

    @DeleteMapping("/delete/{id}")
    public void deleteHouse(@PathVariable long id) {
        String message = "Request to delete house with id = " + id;
        sendMessage(message);
        //rabbitTemplate.convertAndSend(Application.EXCHANGE_NAME, "foo.bar.baz", message);

        Map<String, Long> params = new HashMap<String, Long>();
        params.put("id", id);

        try {
            restTemplate1.delete("http://owner-service/houses/delete/{id}", params);
        }
        catch (org.springframework.web.client.HttpClientErrorException ex){
            throw new ItemNotFoundException("House with id=" + id + " doesn't exist");
        }
    }

    @PutMapping("/{id}")
    public void updateHouse(@RequestBody HouseVO house, @PathVariable long id) {
        String message = "Request to update house with id = " + id+ ", body:" + house;
        sendMessage(message);
        //rabbitTemplate.convertAndSend(Application.EXCHANGE_NAME, "foo.bar.baz", message);

        Map<String, Long> params = new HashMap<String, Long>();
        params.put("id", id);

        try {
            restTemplate1.put( "http://owner-service/houses/{id}", house, params);
        }
        catch (org.springframework.web.client.HttpClientErrorException ex){
            throw new ItemNotFoundException("House with id=" + id + " doesn't exist");
        }
    }

//    @Bean
//    @LoadBalanced
//    private RestTemplate restTemplate1() {
//        return new RestTemplate();
//    }
}
