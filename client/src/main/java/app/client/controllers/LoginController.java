package app.client.controllers;

import UserJPA.Entities.UserE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import vo.UserVO;

@Controller
public class LoginController {
    @Autowired
    private RestTemplate loginRestTemplate;

    @GetMapping("/login")
    //public String home(){
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("errorMsg", "Your username or password are invalid.");

        if (logout != null)
            model.addAttribute("msg", "You have been logged out successfully.");
        return "login";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String registration(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("errorMsg", "Your username and password are invalid.");

        return "signup";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String registration_post(@ModelAttribute UserE user, String role) {
        UserVO userVO = new UserVO();
        userVO.setUsername(user.getUsername());
        userVO.setPassword(user.getPassword());
        userVO.setIsadmin(role.toUpperCase().equals("ADMIN"));

        loginRestTemplate.postForObject("http://user-service/users/", userVO, Object.class);
        return "redirect:/login";
    }


//    @LoadBalanced
//    @Bean
//    private RestTemplate loginRestTemplate(){
//        return new RestTemplate();
//    }
}
