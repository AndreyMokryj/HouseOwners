package app.client.controllers;

import UserJPA.Entities.UserE;
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
import vo.UserVO;

import java.util.*;

@RestController
@RequestMapping(path="/users")
public class UserClientController {
    @Autowired
    RestTemplate restTemplate5;

    //Rabbit send
    private static final Logger log = LoggerFactory.getLogger(UserClientController.class);

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(String x) {
        final CustomMessage message = new CustomMessage(x, new Random().nextInt(50), false);
        log.info("Sending message...");
        rabbitTemplate.convertAndSend(RabbitApp.EXCHANGE_NAME, RabbitApp.ROUTING_KEY, message);
    }

    public UserClientController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RequestMapping("/")
    public ModelAndView getUsers(){
        List<UserE> response = restTemplate5.exchange("http://user-service/users/",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<UserE>>() {}).getBody();
        ModelAndView mav=new ModelAndView("items");
        mav.addObject("items", response);
        mav.addObject("type", "users");
        ArrayList<String> list = new ArrayList<>();
        list.add("username");
        list.add("password");
        list.add("isadmin");
        //list.add("email");
        mav.addObject("list", list);
        return mav;
    }

    @GetMapping("/{id}")
    public ModelAndView getById(@PathVariable long id)
    {
        ModelAndView mav=new ModelAndView("item");

        try {
            UserE response = restTemplate5.exchange("http://user-service/users/{id}",
                    HttpMethod.GET, null, new ParameterizedTypeReference<UserE>() {}, id).getBody();

            System.out.println("Response Received as " + response);
            mav.addObject("item",response);
            mav.addObject("type", "users");

            Map<String, String> map = new HashMap<>();
            map.put("username", response.getUsername());
            map.put("isadmin", (response.getIsadmin())?"1":"0");
            map.put("password", response.getPassword());

            //map.put("email", customer.get().getEmail());
            mav.addObject("map", map);

            return mav;
        }
        catch (HttpClientErrorException ex){
            throw new ItemNotFoundException("Region with id=" + id + " doesn't exist");
        }

    }

//    @PostMapping("/")
//    public void createRegion(@RequestBody RegionVO regionVO)
//    {
//        String message = "Request to create region: " + regionVO;
//        sendMessage(message);
//
//        Object response = restTemplate4.postForObject("http://city-service/regions/", regionVO, Object.class);
//        System.out.println("Response Received as " + response);
//    }

    @PostMapping("/")
    public ModelAndView createUser(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password, @RequestParam(value = "isadmin") int isadmin) {
        UserVO userVO = new UserVO();
        userVO.setUsername(username);
        userVO.setPassword(password);
        userVO.setIsadmin(isadmin > 0);

        String message = "Request to create user: " + userVO;
        sendMessage(message);

        Object response = restTemplate5.postForObject("http://user-service/users/", userVO, Object.class);
        System.out.println("Response Received as " + response);

        ModelAndView mav = getUsers();
        mav.addObject("message","User added successfully!");
        return mav;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteRegion(@PathVariable long id) {
        String message = "Request to delete user with id = " + id;
        sendMessage(message);

        Map<String, Long> params = new HashMap<String, Long>();
        params.put("id", id);

        try {
            restTemplate5.delete("http://user-service/users/delete/{id}", params);
        }
        catch (HttpClientErrorException ex){
            throw new ItemNotFoundException("User with id=" + id + " doesn't exist");
        }
        ModelAndView mav = getUsers();
        mav.addObject("message", "User deleted successfully!");
        return mav;
    }

    @PostMapping("/update/{id}")
    public ModelAndView updateUser(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password, @RequestParam(value = "isadmin") int isadmin, @PathVariable long id) {
        UserVO userVO = new UserVO();
        userVO.setUsername(username);
        userVO.setPassword(password);
        userVO.setIsadmin(isadmin > 0);
        userVO.setId(id);


        String message = "Request to update user with id = " + id+ ", body:" + userVO;
        sendMessage(message);

        Map<String, Long> params = new HashMap<String, Long>();
        params.put("id", id);

        try {
            restTemplate5.put( "http://user-service/users/{id}", userVO, params);
        }
        catch (HttpClientErrorException ex){
            throw new ItemNotFoundException("User with id=" + id + " doesn't exist");
        }

        ModelAndView mav = getUsers();
        mav.addObject("message", "User updated successfully!");
        return mav;
    }
}
