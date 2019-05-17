package DaoJPA.Controllers;

import DaoJPA.Entities.House;
import DaoJPA.Entities.Log;
import DaoJPA.Repositories.HouseRepository;
import DaoJPA.Repositories.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vo.CustomMessage;
import vo.Exceptions.ItemNotFoundException;
import vo.HouseVO;

import java.net.URI;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

@RestController    // This means that this class is a Controller
@RequestMapping(path="/houses")
public class HouseController {
    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private LogRepository logRepository;


    //Rabbit receive
    private static final Logger log = LoggerFactory.getLogger(HouseController.class);
    private CountDownLatch latch = new CountDownLatch(1);

//    public void receiveMessage(String message) {
//        System.out.println("Received <" + message + ">");
//        latch.countDown();
//    }

//    @RabbitListener(queues = RabbitApp.QUEUE_GENERIC_NAME)
//    public void receiveMessage(final Message message) {
//        log.info("Received message as generic: {}", message.toString());
//        Log log1 = new Log();
//        log1.setText(message.toString());
//        logRepository.save(log1);
//    }

    @RabbitListener(queues = RabbitConfiguration.QUEUE_SPECIFIC_NAME)
    public void receiveMessage(final CustomMessage customMessage) {
        log.info("Received message as specific class: {}", customMessage.toString());
        Log log1 = new Log();
        log1.setText(customMessage.toString());
        logRepository.save(log1);
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    //Rabbit send
//    private final RabbitTemplate rabbitTemplate;
//
//    public HouseController(RabbitTemplate rabbitTemplate) {
//        this.rabbitTemplate = rabbitTemplate;
//    }
//
//    public void sendMessage(String x) {
//        final CustomMessage message = new CustomMessage(x, new Random().nextInt(50), false);
//        log.info("Sending message...");
//        rabbitTemplate.convertAndSend(RabbitApp.EXCHANGE_NAME, RabbitApp.ROUTING_KEY, message);
//    }


    @GetMapping(path="/")
    public @ResponseBody Iterable<House> getAll() {
        // This returns a JSON or XML with the users
        return houseRepository.findAll();
    }

    @GetMapping("/{id}")
    public House retrieveHouse(@PathVariable long id) {
        try {
            Optional<House> house = houseRepository.findById(id);

            return house.get();
        }
        catch (NoSuchElementException ex){
            throw new ItemNotFoundException("House with id=" + id + " doesn't exist");
        }
    }

    @PostMapping("/")
    public ResponseEntity<Object> createHouse(@RequestBody HouseVO houseVO) {
        House house = new House();
        //house.setId(houseVO.getId());
        house.setCity_id(houseVO.getCity_id());
        house.setAddress(houseVO.getAddress());

        House savedHouse = houseRepository.save(house);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedHouse.getId()).toUri();

        String message = "House created: " + savedHouse;
        Log log3 = new Log();
        log3.setText(message);
        logRepository.save(log3);

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteHouse(@PathVariable long id) {
        try {
            houseRepository.deleteById(id);

            String message = "House with id = " + id + " deleted";
            Log log3 = new Log();
            log3.setText(message);
            logRepository.save(log3);
        }
        catch (org.springframework.dao.EmptyResultDataAccessException ex){
            String message = "House with id = " + id + " does not exist";
            Log log3 = new Log();
            log3.setText(message);
            logRepository.save(log3);
            throw new ItemNotFoundException("House with id=" + id + " doesn't exist");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateHouse(@RequestBody HouseVO houseVO, @PathVariable long id) {
        try {
            Optional<House> houseOptional = houseRepository.findById(id);

            if (houseOptional.get() == null);
            //return ResponseEntity.notFound().build();

            House house = new House();
            house.setId(id);
            house.setAddress(houseVO.getAddress());
//            house.setPrice(houseVO.getPrice());
            house.setCity_id(houseVO.getCity_id());

            houseRepository.save(house);
            String message = "Houce updated: " + house;
            Log log3 = new Log();
            log3.setText(message);
            logRepository.save(log3);

            return ResponseEntity.noContent().build();
        }
        catch (NoSuchElementException ex){
            String message = "Houce with id=" + id + " doesn't exist";
            Log log3 = new Log();
            log3.setText(message);
            logRepository.save(log3);
            throw new ItemNotFoundException("House with id=" + id + " doesn't exist");
        }
    }
}
