package app.client.controllers;

import OwnerJPA.Entities.House;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import rabbit.RabbitApp;
import vo.CustomMessage;
import vo.Exceptions.ItemNotFoundException;
import vo.HouseVO;

import java.util.*;

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


    @RequestMapping("/")
    public ModelAndView getHouses(){
        List<House> response = restTemplate1.exchange("http://owner-service/houses/",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<House>>() {}).getBody();
        ModelAndView mav=new ModelAndView("items");
        mav.addObject("items", response);
        mav.addObject("type", "houses");
        ArrayList<String> list = new ArrayList<>();
        list.add("address");
        list.add("city_id");
        //list.add("email");
        mav.addObject("list", list);
        return mav;
    }

    @GetMapping("/{id}/details")
    public ModelAndView getById(@PathVariable long id)
    {
        ModelAndView mav=new ModelAndView("item");

        try {
            House response = restTemplate1.exchange("http://owner-service/houses/{id}",
                    HttpMethod.GET, null, new ParameterizedTypeReference<House>() {}, id).getBody();

            System.out.println("Response Received as " + response);
            mav.addObject("item",response);
            mav.addObject("type", "houses");

            Map<String, String> map = new HashMap<>();
            map.put("address", response.getAddress());
            map.put("city_id", response.getCity_id().toString());
            //map.put("email", customer.get().getEmail());
            mav.addObject("map", map);

            return mav;
        }
        catch (HttpClientErrorException ex){
            throw new ItemNotFoundException("House with id=" + id + " doesn't exist");
        }

    }


    @PostMapping("/")
    public ModelAndView createHouse(@RequestParam(value = "address") String address,
                                   @RequestParam(value = "city_id") long city_id) {
        HouseVO houseVO = new HouseVO();
        houseVO.setAddress(address);
        houseVO.setCity_id(city_id);

        String message = "Request to create house: " + houseVO;
        sendMessage(message);

        Object response = restTemplate1.postForObject("http://owner-service/houses/", houseVO, Object.class);
        System.out.println("Response Received as " + response);

        ModelAndView mav = getHouses();
        mav.addObject("message","House added successfully!");
        return mav;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteHouse(@PathVariable long id) {
        String message = "Request to delete house with id = " + id;
        sendMessage(message);

        Map<String, Long> params = new HashMap<String, Long>();
        params.put("id", id);

        try {
            restTemplate1.delete("http://owner-service/houses/delete/{id}", params);
        }
        catch (org.springframework.web.client.HttpClientErrorException ex){
            throw new ItemNotFoundException("House with id=" + id + " doesn't exist");
        }
        ModelAndView mav = getHouses();
        mav.addObject("message", "House deleted successfully!");
        return mav;
    }

    @PostMapping("/update/{id}")
    public ModelAndView updateHouse(@RequestParam(value = "address") String address,
                                   @RequestParam(value = "city_id") long city_id,
                                    @PathVariable long id) {
        HouseVO houseVO = new HouseVO();
        houseVO.setAddress(address);
        houseVO.setCity_id(city_id);

        String message = "Request to update house with id = " + id+ ", body:" + houseVO;
        sendMessage(message);

        Map<String, Long> params = new HashMap<String, Long>();
        params.put("id", id);

        try {
            restTemplate1.put( "http://owner-service/houses/{id}", houseVO, params);
        }
        catch (org.springframework.web.client.HttpClientErrorException ex){
            throw new ItemNotFoundException("House with id=" + id + " doesn't exist");
        }

        ModelAndView mav = getHouses();
        mav.addObject("message", "House updated successfully!");
        return mav;
    }
}
