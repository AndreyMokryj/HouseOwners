package app.client.controllers;

import CityJPA.Entities.City;
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
import org.springframework.web.servlet.ModelAndView;
import rabbit.RabbitApp;
import vo.CityVO;
import vo.CustomMessage;
import vo.Exceptions.ItemNotFoundException;

import java.util.*;

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
//    public String getCities()
//    {
//        String response = restTemplate.exchange("http://city-service/cities/",
//                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}).getBody();
//
//        System.out.println("Response Received as " + response);
//
//        return response;
//    }
    public ModelAndView getCities(){
        List<City> response = restTemplate.exchange("http://city-service/cities/",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<City>>() {}).getBody();
        ModelAndView mav=new ModelAndView("items");
        mav.addObject("items", response);
        mav.addObject("type", "cities");
        ArrayList<String> list = new ArrayList<>();
        list.add("name");
        list.add("population");
        list.add("region_id");
        //list.add("email");
        mav.addObject("list", list);
        return mav;
    }

    @GetMapping("/{id}")
//    public String getById(@PathVariable long id)
//    {
//        try {
//            String response = restTemplate.exchange("http://city-service/cities/{id}",
//                    HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}, id).getBody();
//
//            System.out.println("Response Received as " + response);
//
//            return response;
//        }
//        catch (org.springframework.web.client.HttpClientErrorException ex){
//            throw new ItemNotFoundException("City with id=" + id + " doesn't exist");
//        }
//    }
    public ModelAndView getById(@PathVariable long id)
    {
        ModelAndView mav=new ModelAndView("item");

        try {
            City response = restTemplate.exchange("http://city-service/cities/{id}",
                    HttpMethod.GET, null, new ParameterizedTypeReference<City>() {}, id).getBody();

            System.out.println("Response Received as " + response);
            mav.addObject("item",response);
            mav.addObject("type", "cities");

            Map<String, String> map = new HashMap<>();
            map.put("name", response.getName());
            map.put("population", response.getPopulation().toString());
            map.put("region_id", response.getRegion_id().toString());
            //map.put("email", customer.get().getEmail());
            mav.addObject("map", map);

            return mav;
        }
        catch (HttpClientErrorException ex){
            throw new ItemNotFoundException("City with id=" + id + " doesn't exist");
        }

    }


    @PostMapping("/")
//    public void createCity(@RequestBody CityVO city)
//    {
//        String message = "Request to create city: " + city;
//        sendMessage(message);
//
//        Object response = restTemplate.postForObject("http://city-service/cities/", city, Object.class);
//        System.out.println("Response Received as " + response);
//    }
    public ModelAndView createCity(@RequestParam(value = "name") String name,
                                   @RequestParam(value = "population") long population,
                                   @RequestParam(value = "region_id") long region_id) {
        CityVO cityVO = new CityVO();
        cityVO.setName(name);
        cityVO.setPopulation(population);
        cityVO.setRegion_id(region_id);

        String message = "Request to create city: " + cityVO;
        sendMessage(message);

        Object response = restTemplate.postForObject("http://city-service/cities/", cityVO, Object.class);
        System.out.println("Response Received as " + response);

        ModelAndView mav = getCities();
        mav.addObject("message","City added successfully!");
        return mav;
    }

//    @DeleteMapping("/delete/{id}")
//    public void deleteCity(@PathVariable long id) {
//        String message = "Request to delete city with id = " + id;
//        sendMessage(message);
//
//        Map<String, Long> params = new HashMap<String, Long>();
//        params.put("id", id);
//
//        try {
//            restTemplate.delete("http://city-service/cities/delete/{id}", params);
//        }
//        catch (org.springframework.web.client.HttpClientErrorException ex){
//            throw new ItemNotFoundException("City with id=" + id + " doesn't exist");
//        }
//    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteCity(@PathVariable long id) {
        String message = "Request to delete city with id = " + id;
        sendMessage(message);

        Map<String, Long> params = new HashMap<String, Long>();
        params.put("id", id);

        try {
            restTemplate.delete("http://city-service/cities/delete/{id}", params);
        }
        catch (org.springframework.web.client.HttpClientErrorException ex){
            throw new ItemNotFoundException("City with id=" + id + " doesn't exist");
        }
        ModelAndView mav = getCities();
        mav.addObject("message", "City deleted successfully!");
        return mav;
    }

//    @PutMapping("/{id}")
//    public void updateCity(@RequestBody CityVO city, @PathVariable long id) {
//        String message = "Request to update city with id = " + id+ ", body:" + city;
//        sendMessage(message);
//
//        Map<String, Long> params = new HashMap<String, Long>();
//        params.put("id", id);
//
//        try {
//            restTemplate.put( "http://city-service/cities/{id}", city, params);
//        }
//        catch (org.springframework.web.client.HttpClientErrorException ex){
//            throw new ItemNotFoundException("City with id=" + id + " doesn't exist");
//        }
//    }

    @PostMapping("/update/{id}")
    public ModelAndView updateCity(@RequestParam(value = "name") String name,
                                   @RequestParam(value = "population") long population,
                                   @RequestParam(value = "region_id") long region_id,
                                   @PathVariable long id) {
        CityVO cityVO = new CityVO();
        cityVO.setName(name);
        cityVO.setPopulation(population);
        cityVO.setRegion_id(region_id);

        String message = "Request to update city with id = " + id+ ", body:" + cityVO;
        sendMessage(message);

        Map<String, Long> params = new HashMap<String, Long>();
        params.put("id", id);

        try {
            restTemplate.put( "http://city-service/cities/{id}", cityVO, params);
        }
        catch (org.springframework.web.client.HttpClientErrorException ex){
            throw new ItemNotFoundException("City with id=" + id + " doesn't exist");
        }

        ModelAndView mav = getCities();
        mav.addObject("message", "City updated successfully!");
        return mav;
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
