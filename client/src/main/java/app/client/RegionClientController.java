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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import rabbit.RabbitApp;
import vo.CityVO;
import vo.CustomMessage;
import vo.Exceptions.ItemNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping(path="/regions")
public class RegionClientController {
    @Autowired
    RestTemplate restTemplate4;

    //Rabbit send
    private static final Logger log = LoggerFactory.getLogger(RegionClientController.class);

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(String x) {
        final CustomMessage message = new CustomMessage(x, new Random().nextInt(50), false);
        log.info("Sending message...");
        rabbitTemplate.convertAndSend(RabbitApp.EXCHANGE_NAME, RabbitApp.ROUTING_KEY, message);
    }

    public RegionClientController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping(path = "/")
    public String getVendors()
    {
        String response = restTemplate4.exchange("http://city-service/regions/",
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}).getBody();

        System.out.println("Response Received as " + response);

        return response;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable long id)
    {
        try {
            String response = restTemplate4.exchange("http://service0/cities/{id}",
                    HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}, id).getBody();

            System.out.println("Response Received as " + response);

            return response;
        }
        catch (HttpClientErrorException ex){
            throw new ItemNotFoundException("City with id=" + id + " doesn't exist");
        }

    }

    @PostMapping("/")
    public void createVendor(@RequestBody CityVO cityVO)
    {
        String message = "Request to create city: " + cityVO;
        //rabbitTemplate.convertAndSend(Application.EXCHANGE_NAME, "foo.bar.baz", message);
        sendMessage(message);

        Object response = restTemplate4.postForObject("http://service0/cities/", cityVO, Object.class);
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
            restTemplate4.delete("http://service0/cities/delete/{id}", params);
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
            restTemplate4.put( "http://city-service/cities/{id}", cityVO, params);
        }
        catch (org.springframework.web.client.HttpClientErrorException ex){
            throw new ItemNotFoundException("City with id=" + id + " doesn't exist");
        }
    }

//    @Bean
//    @LoadBalanced
//    private RestTemplate restTemplate4() {
//        return new RestTemplate();
//    }
}
