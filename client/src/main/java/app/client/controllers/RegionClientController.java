package app.client.controllers;

import CityJPA.Entities.Region;
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
import vo.RegionVO;

import java.util.*;

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

    @RequestMapping("/")
    public ModelAndView getRegions(){
        List<Region> response = restTemplate4.exchange("http://city-service/regions/",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Region>>() {}).getBody();
        ModelAndView mav=new ModelAndView("items");
        mav.addObject("items", response);
        mav.addObject("type", "regions");
        ArrayList<String> list = new ArrayList<>();
        list.add("name");
        //list.add("email");
        mav.addObject("list", list);
        return mav;
    }

    @GetMapping("/{id}/details")
    public ModelAndView getById(@PathVariable long id)
    {
        ModelAndView mav=new ModelAndView("item");

        try {
            Region response = restTemplate4.exchange("http://city-service/regions/{id}",
                    HttpMethod.GET, null, new ParameterizedTypeReference<Region>() {}, id).getBody();

            System.out.println("Response Received as " + response);
            mav.addObject("item",response);
            mav.addObject("type", "regions");

            Map<String, String> map = new HashMap<>();
            map.put("name", response.getName());
            //map.put("email", customer.get().getEmail());
            mav.addObject("map", map);

            return mav;
        }
        catch (HttpClientErrorException ex){
            throw new ItemNotFoundException("Region with id=" + id + " doesn't exist");
        }

    }

    @PostMapping("/")
    public ModelAndView createRegion(@RequestParam(value = "name") String name) {
        RegionVO regionVO = new RegionVO();
        regionVO.setName(name);

        String message = "Request to create region: " + regionVO;
        sendMessage(message);

        Object response = restTemplate4.postForObject("http://city-service/regions/", regionVO, Object.class);
        System.out.println("Response Received as " + response);

        ModelAndView mav = getRegions();
        mav.addObject("message","Region added successfully!");
        return mav;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteRegion(@PathVariable long id) {
        String message = "Request to delete region with id = " + id;
        sendMessage(message);

        Map<String, Long> params = new HashMap<String, Long>();
        params.put("id", id);

        try {
            restTemplate4.delete("http://city-service/regions/delete/{id}", params);
        }
        catch (org.springframework.web.client.HttpClientErrorException ex){
            throw new ItemNotFoundException("Region with id=" + id + " doesn't exist");
        }
        ModelAndView mav = getRegions();
        mav.addObject("message", "Region deleted successfully!");
        return mav;
    }

    @PostMapping("/update/{id}")
    public ModelAndView updateRegion(@RequestParam(value = "name") String name, @PathVariable long id) {
        RegionVO regionVO = new RegionVO();
        regionVO.setName(name);
        regionVO.setId(id);

        String message = "Request to update region with id = " + id+ ", body:" + regionVO;
        sendMessage(message);

        Map<String, Long> params = new HashMap<String, Long>();
        params.put("id", id);

        try {
            restTemplate4.put( "http://city-service/regions/{id}", regionVO, params);
        }
        catch (org.springframework.web.client.HttpClientErrorException ex){
            throw new ItemNotFoundException("Region with id=" + id + " doesn't exist");
        }

        ModelAndView mav = getRegions();
        mav.addObject("message", "Region updated successfully!");
        return mav;
    }
}
