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
import vo.CityVO;
import vo.CustomMessage;
import vo.Exceptions.ItemNotFoundException;
import vo.HouseVO;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping(path="/cities")
public class CityClientController {
    @Autowired
    RestTemplate restTemplate;

    //Rabbit send
    private static final Logger log = LoggerFactory.getLogger(CityClientController.class);

    private final RabbitTemplate rabbitTemplate;

    public CityClientController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String x) {
        final CustomMessage message = new CustomMessage(x, new Random().nextInt(50), false);
        log.info("Sending message...");
        rabbitTemplate.convertAndSend(RabbitApp.EXCHANGE_NAME, RabbitApp.ROUTING_KEY, message);
    }

    @GetMapping(path = "/")
    public String getCities()
    {
        String response = restTemplate.exchange("http://city-service/cities/",
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}).getBody();

        System.out.println("Response Received as " + response);

        return response;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable long id)
    {
        try {
            String response = restTemplate.exchange("http://city-service/cities/{id}",
                    HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}, id).getBody();

            System.out.println("Response Received as " + response);

            return response;
        }
        catch (org.springframework.web.client.HttpClientErrorException ex){
            throw new ItemNotFoundException("City with id=" + id + " doesn't exist");
        }
    }

    @PostMapping("/")
    public void createCity(@RequestBody CityVO city)
    {
        String message = "Request to create city: " + city;
        sendMessage(message);
//        rabbitTemplate.convertAndSend(Application.topicExchangeName, "foo.bar.baz", message);

        Object response = restTemplate.postForObject("http://city-service/cities/", city, Object.class);
        System.out.println("Response Received as " + response);

        //return response;
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCity(@PathVariable long id) {
        String message = "Request to delete city with id = " + id;
        sendMessage(message);
        //rabbitTemplate.convertAndSend(Application.EXCHANGE_NAME, "foo.bar.baz", message);

        Map<String, Long> params = new HashMap<String, Long>();
        params.put("id", id);

        try {
            restTemplate.delete("http://city-service/cities/delete/{id}", params);
        }
        catch (org.springframework.web.client.HttpClientErrorException ex){
            throw new ItemNotFoundException("City with id=" + id + " doesn't exist");
        }
    }

    @PutMapping("/{id}")
    public void updateCity(@RequestBody CityVO city, @PathVariable long id) {
        String message = "Request to update city with id = " + id+ ", body:" + city;
        sendMessage(message);
        //rabbitTemplate.convertAndSend(Application.EXCHANGE_NAME, "foo.bar.baz", message);

        Map<String, Long> params = new HashMap<String, Long>();
        params.put("id", id);

        try {
            restTemplate.put( "http://city-service/cities/{id}", city, params);
        }
        catch (org.springframework.web.client.HttpClientErrorException ex){
            throw new ItemNotFoundException("City with id=" + id + " doesn't exist");
        }
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
