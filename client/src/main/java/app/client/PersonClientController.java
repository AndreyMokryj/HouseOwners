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
import vo.PersonVO;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping(path="/people")
public class PersonClientController {
    @Autowired
    RestTemplate restTemplate3;

    //Rabbit send
    private static final Logger log = LoggerFactory.getLogger(PersonClientController.class);

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(String x) {
        final CustomMessage message = new CustomMessage(x, new Random().nextInt(50), false);
        log.info("Sending message...");
        rabbitTemplate.convertAndSend(RabbitApp.EXCHANGE_NAME, RabbitApp.ROUTING_KEY, message);
    }

    public PersonClientController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping(path = "/")
    public String getPeople()
    {
        String response = restTemplate3.exchange("http://owner-service/people/",
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}).getBody();

        System.out.println("Response Received as " + response);

        return response;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable long id)
    {
        try {
            String response = restTemplate3.exchange("http://owner-service/people/{id}",
                    HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}, id).getBody();

            System.out.println("Response Received as " + response);

            return response;
        }
        catch (org.springframework.web.client.HttpClientErrorException ex){
            throw new ItemNotFoundException("Person with id=" + id + " doesn't exist");
        }

    }

    @PostMapping("/")
    public void createPerson(@RequestBody PersonVO personVO)
    {
        String message = "Request to create person: " + personVO;
        //rabbitTemplate.convertAndSend(Application.EXCHANGE_NAME, "foo.bar.baz", message);
        sendMessage(message);

        Object response = restTemplate3.postForObject("http://owner-service/people/", personVO, Object.class);
        System.out.println("Response Received as " + response);

        //return response;
    }

    @DeleteMapping("/delete/{id}")
    public void deletePerson(@PathVariable long id) {
        String message = "Request to delete person with id = " + id;
        sendMessage(message);
        //rabbitTemplate.convertAndSend(Application.EXCHANGE_NAME, "foo.bar.baz", message);

        Map<String, Long> params = new HashMap<String, Long>();
        params.put("id", id);

        try {
            restTemplate3.delete("http://owner-service/people/delete/{id}", params);
        }
        catch (org.springframework.web.client.HttpClientErrorException ex){
            throw new ItemNotFoundException("Person with id=" + id + " doesn't exist");
        }
    }

    @PutMapping("/{id}")
    public void updatePerson(@RequestBody PersonVO personVO, @PathVariable long id) {
        String message = "Request to update person with id = " + id+ ", body:" + personVO;
        //rabbitTemplate.convertAndSend(Application.EXCHANGE_NAME, "foo.bar.baz", message);
        sendMessage(message);

        Map<String, Long> params = new HashMap<String, Long>();
        params.put("id", id);

        try {
            restTemplate3.put( "http://owner-service/people/{id}", personVO, params);
        }
        catch (org.springframework.web.client.HttpClientErrorException ex){
            throw new ItemNotFoundException("Person with id=" + id + " doesn't exist");
        }
    }

//    @Bean
//    @LoadBalanced
//    private RestTemplate restTemplate3() {
//        return new RestTemplate();
//    }
}
