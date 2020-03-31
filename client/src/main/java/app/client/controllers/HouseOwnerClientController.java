package app.client.controllers;

import OwnerJPA.Entities.HouseOwner;
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
import vo.HouseOwnerVO;

import java.util.*;

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

    @RequestMapping("/")
    public ModelAndView getHouseOwners(){
        List<HouseOwner> response = restTemplate2.exchange("http://owner-service/house-owners/",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<HouseOwner>>() {}).getBody();
        ModelAndView mav=new ModelAndView("items");
        mav.addObject("items", response);
        mav.addObject("type", "house-owners");
        ArrayList<String> list = new ArrayList<>();
        list.add("person_id");
        list.add("house_id");
        list.add("flat");
        //list.add("email");
        mav.addObject("list", list);
        return mav;
    }

    @GetMapping("/{id}")
    public ModelAndView getById(@PathVariable long id)
    {
        ModelAndView mav=new ModelAndView("item");

        try {
            HouseOwner response = restTemplate2.exchange("http://owner-service/house-owners/{id}",
                    HttpMethod.GET, null, new ParameterizedTypeReference<HouseOwner>() {}, id).getBody();

            System.out.println("Response Received as " + response);
            mav.addObject("item",response);
            mav.addObject("type", "house-owners");

            Map<String, String> map = new HashMap<>();
            map.put("person_id", response.getPerson_id().toString());
            map.put("house_id", response.getHouse_id().toString());
            map.put("flat", String.valueOf(response.getFlat()));
            //map.put("email", customer.get().getEmail());
            mav.addObject("map", map);

            return mav;
        }
        catch (HttpClientErrorException ex){
            throw new ItemNotFoundException("House with id=" + id + " doesn't exist");
        }

    }

    @PostMapping("/")
    public ModelAndView createHouse(@RequestParam(value = "person_id") long person_id,
                                    @RequestParam(value = "house_id") long house_id,
                                    @RequestParam(value = "flat") int flat) {
        HouseOwnerVO houseOwnerVO = new HouseOwnerVO();
        houseOwnerVO.setFlat(flat);
        houseOwnerVO.setHouse_id(house_id);
        houseOwnerVO.setPerson_id(person_id);

        String message = "Request to create house-owner: " + houseOwnerVO;
        sendMessage(message);

        Object response = restTemplate2.postForObject("http://owner-service/house-owners/", houseOwnerVO, Object.class);
        System.out.println("Response Received as " + response);

        ModelAndView mav = getHouseOwners();
        mav.addObject("message","House-owner added successfully!");
        return mav;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteHouseOwner(@PathVariable long id) {
        String message = "Request to delete house-owner with id = " + id;
        sendMessage(message);

        Map<String, Long> params = new HashMap<String, Long>();
        params.put("id", id);

        try {
            restTemplate2.delete("http://owner-service/house-owners/delete/{id}", params);
        }
        catch (org.springframework.web.client.HttpClientErrorException ex){
            throw new ItemNotFoundException("House-owner with id=" + id + " doesn't exist");
        }
        ModelAndView mav = getHouseOwners();
        mav.addObject("message", "House-owner deleted successfully!");
        return mav;
    }

    @PostMapping("/update/{id}")
    public ModelAndView updateHouse(@RequestParam(value = "person_id") long person_id,
                                    @RequestParam(value = "house_id") long house_id,
                                    @RequestParam(value = "flat") int flat,
                                    @PathVariable long id) {
        HouseOwnerVO houseOwnerVO = new HouseOwnerVO();
        houseOwnerVO.setFlat(flat);
        houseOwnerVO.setHouse_id(house_id);
        houseOwnerVO.setPerson_id(person_id);

        String message = "Request to update house-owner with id = " + id+ ", body:" + houseOwnerVO;
        sendMessage(message);

        Map<String, Long> params = new HashMap<String, Long>();
        params.put("id", id);

        try {
            restTemplate2.put( "http://owner-service/house-owners/{id}", houseOwnerVO, params);
        }
        catch (org.springframework.web.client.HttpClientErrorException ex){
            throw new ItemNotFoundException("House-owner with id=" + id + " doesn't exist");
        }

        ModelAndView mav = getHouseOwners();
        mav.addObject("message", "House-owner updated successfully!");
        return mav;
    }
}
