package UserJPA.Controllers;

import UserJPA.Entities.Log;
import UserJPA.Entities.UserE;
import UserJPA.Entities.UserVO;
import UserJPA.Repositories.LogRepository;
import UserJPA.Repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vo.CustomMessage;
import vo.Exceptions.ItemNotFoundException;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

@RestController    // This means that this class is a Controller
@RequestMapping(path = "/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LogRepository logRepository;


    //Rabbit receive
    private static final Logger log = LoggerFactory.getLogger(HouseController.class);
    private CountDownLatch latch = new CountDownLatch(1);

    @RabbitListener(queues = RabbitConfiguration.QUEUE_SPECIFIC_NAME)
    public void receiveMessage(final CustomMessage customMessage) {
        log.info("Received message as specific class: {}", customMessage.toString());
        writeLog(customMessage.toString());
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    @GetMapping(path = "/findByUP")
    public UserE getUserByUP(@RequestBody UserVO userVO) {
        try {
            Optional<UserE> userOptional = userRepository.findByUP(userVO.getUsername(), userVO.getPassword());
            return userOptional.get();
        }
        catch (NoSuchElementException ex){
            String message = "Invalid username and password!";
            writeLog(message);
            throw new ItemNotFoundException(message);
        }
    }

    public void writeLog(String message){
        Log log = new Log();
        log.setText(message);
        logRepository.save(log);
    }
}
