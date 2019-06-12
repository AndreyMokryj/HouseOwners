package app.client.controllers;

import OwnerJPA.Entities.Person;
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
import vo.PersonVO;

import java.util.*;

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
    public ModelAndView getPeople(){
        List<Person> response = restTemplate3.exchange("http://owner-service/people/",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Person>>() {}).getBody();
        ModelAndView mav=new ModelAndView("items");
        mav.addObject("items", response);
        mav.addObject("type", "people");
        ArrayList<String> list = new ArrayList<>();
        list.add("name");
        list.add("passport");
        list.add("registered_at");
        //list.add("email");
        mav.addObject("list", list);
        return mav;
    }

    @GetMapping("/{id}")
    public ModelAndView getById(@PathVariable long id)
    {
        ModelAndView mav=new ModelAndView("item");

        try {
            Person response = restTemplate3.exchange("http://owner-service/people/{id}",
                    HttpMethod.GET, null, new ParameterizedTypeReference<Person>() {}, id).getBody();

            System.out.println("Response Received as " + response);
            mav.addObject("item",response);
            mav.addObject("type", "people");

            Map<String, String> map = new HashMap<>();
            map.put("name", response.getName());
            map.put("passport", response.getPassport());
            map.put("registered_at", response.getRegistered_at().toString());
            //map.put("email", customer.get().getEmail());
            mav.addObject("map", map);

            return mav;
        }
        catch (HttpClientErrorException ex){
            throw new ItemNotFoundException("Person with id=" + id + " doesn't exist");
        }

    }

    @PostMapping("/")
    public ModelAndView createPerson(@RequestParam(value = "name") String name,
                                     @RequestParam(value = "passport") String passport,
                                    @RequestParam(value = "registered_at") long registered_at) {
        PersonVO personVO = new PersonVO();
        personVO.setName(name);
        personVO.setPassport(passport);
        personVO.setRegistered_at(registered_at);

        String message = "Request to create person: " + personVO;
        sendMessage(message);

        Object response = restTemplate3.postForObject("http://owner-service/people/", personVO, Object.class);
        System.out.println("Response Received as " + response);

        ModelAndView mav = getPeople();
        mav.addObject("message","Person added successfully!");
        return mav;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deletePerson(@PathVariable long id) {
        String message = "Request to delete person with id = " + id;
        sendMessage(message);

        Map<String, Long> params = new HashMap<String, Long>();
        params.put("id", id);

        try {
            restTemplate3.delete("http://owner-service/people/delete/{id}", params);
        }
        catch (org.springframework.web.client.HttpClientErrorException ex){
            throw new ItemNotFoundException("Person with id=" + id + " doesn't exist");
        }
        ModelAndView mav = getPeople();
        mav.addObject("message", "Person deleted successfully!");
        return mav;
    }

    @PostMapping("/update/{id}")
    public ModelAndView updateHouse(@RequestParam(value = "name") String name,
                                    @RequestParam(value = "passport") String passport,
                                    @RequestParam(value = "registered_at") long registered_at,
                                    @PathVariable long id) {
        PersonVO personVO = new PersonVO();
        personVO.setName(name);
        personVO.setPassport(passport);
        personVO.setRegistered_at(registered_at);

        String message = "Request to update person with id = " + id+ ", body:" + personVO;
        sendMessage(message);

        Map<String, Long> params = new HashMap<String, Long>();
        params.put("id", id);

        try {
            restTemplate3.put( "http://owner-service/people/{id}", personVO, params);
        }
        catch (org.springframework.web.client.HttpClientErrorException ex){
            throw new ItemNotFoundException("Person with id=" + id + " doesn't exist");
        }

        ModelAndView mav = getPeople();
        mav.addObject("message", "Person updated successfully!");
        return mav;
    }
}
