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
import vo.HouseOwnerVO;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping(path="/house-owners")
public class HouseOwnerClientController {
    @Autowired
    RestTemplate restTemplate2;

    //Rabbit send
    private static final Logger log = LoggerFactory.getLogger(HouseOwnerClientController.class);

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(String x) {
        final CustomMessage message = new CustomMessage(x, new Random().nextInt(50), false);
        log.info("Sending message...");
        rabbitTemplate.convertAndSend(RabbitApp.EXCHANGE_NAME, RabbitApp.ROUTING_KEY, message);
    }

    public HouseOwnerClientController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping(path = "/")
    public String getHouseOwners()
    {
        String response = restTemplate2.exchange("http://owner-service/house-owners/",
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}).getBody();

        System.out.println("Response Received as " + response);

        return response;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable long id)
    {
        try {
            String response = restTemplate2.exchange("http://owner-service/house-owners/{id}",
                    HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}, id).getBody();

            System.out.println("Response Received as " + response);

            return response;
        }
        catch (org.springframework.web.client.HttpClientErrorException ex){
            throw new ItemNotFoundException("HouseOwner with id=" + id + " doesn't exist");
        }

    }

    @PostMapping("/")
    public void createHouseOwner(@RequestBody HouseOwnerVO houseOwnerVO)
    {
        String message = "Request to create houseOwner: " + houseOwnerVO;
        sendMessage(message);

        Object response = restTemplate2.postForObject("http://owner-service/house-owners/", houseOwnerVO, Object.class);
        System.out.println("Response Received as " + response);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteHouseOwner(@PathVariable long id) {
        String message = "Request to delete houseOwner with id = " + id;
        sendMessage(message);

        Map<String, Long> params = new HashMap<String, Long>();
        params.put("id", id);

        try {
            restTemplate2.delete("http://owner-service/house-owners/delete/{id}", params);
        }
        catch (org.springframework.web.client.HttpClientErrorException ex){
            throw new ItemNotFoundException("HouseOwner with id=" + id + " doesn't exist");
        }
    }

    @PutMapping("/{id}")
    public void updateHouseOwner(@RequestBody HouseOwnerVO houseOwnerVO, @PathVariable long id) {
        String message = "Request to update houseOwner with id = " + id+ ", body:" + houseOwnerVO;
        sendMessage(message);

        Map<String, Long> params = new HashMap<String, Long>();
        params.put("id", id);

        try {
            restTemplate2.put( "http://owner-service/house-owners/{id}", houseOwnerVO, params);
        }
        catch (org.springframework.web.client.HttpClientErrorException ex){
            throw new ItemNotFoundException("HouseOwner with id=" + id + " doesn't exist");
        }
    }
}
