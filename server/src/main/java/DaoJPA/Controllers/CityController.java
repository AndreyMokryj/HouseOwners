package DaoJPA.Controllers;

import DaoJPA.Entities.City;

import DaoJPA.Entities.Log;
import DaoJPA.Repositories.CityRepository;
import DaoJPA.Repositories.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rabbit.RabbitApp;
import vo.CustomMessage;
import vo.Exceptions.ItemNotFoundException;
import vo.CityVO;

import java.net.URI;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

@RestController    // This means that this class is a Controller
@RequestMapping(path="/cities")
@Component
public class CityController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private CityRepository cityRepository;

    @Autowired
    private LogRepository logRepository;

    //Rabbit receive
    private static final Logger log = LoggerFactory.getLogger(CityController.class);

    private CountDownLatch latch = new CountDownLatch(1);

    private Log log2 = new Log();

//    public void receiveMessage(String message) {
//        System.out.println("Received <" + message + ">");
//        latch.countDown();
//    }

//    @RabbitListener(queues = RabbitApp.QUEUE_GENERIC_NAME)
//    public void receiveMessage(final Message message) {
//        log.info("Received message as generic: {}", message.toString());
//    }

    @RabbitListener(queues = RabbitApp.QUEUE_SPECIFIC_NAME)
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
//    public CityController(RabbitTemplate rabbitTemplate) {
//        this.rabbitTemplate = rabbitTemplate;
//    }
//
//    public void sendMessage(String x) {
//        final CustomMessage message = new CustomMessage(x, new Random().nextInt(50), false);
//        log.info("Sending message...");
//        rabbitTemplate.convertAndSend(RabbitApp.EXCHANGE_NAME1, RabbitApp.ROUTING_KEY1, message);
//    }

    @GetMapping(path="/")
    public @ResponseBody Iterable<City> getAll() {
        // This returns a JSON or XML with the users
        return cityRepository.findAll();
    }

    @GetMapping("/{id}")
    public City retrieveCity(@PathVariable long id) throws ItemNotFoundException {
        try {
            Optional<City> city = cityRepository.findById(id);

//            if(city.get() == null)
//                throw new ItemNotFoundException("City with id=" + id + " doesn't exist");
            return city.get();
        }
        catch (NoSuchElementException ex){
            throw new ItemNotFoundException("City with id=" + id + " doesn't exist");
        }

    }

    @PostMapping("/")
    public ResponseEntity<Object> createCity(@RequestBody CityVO cityVO) {
        City city = new City();
        city.setName(cityVO.getName());
        //city.setSite(cityVO.getSite());
        city.setPopulation(cityVO.getPopulation());

        //logRepository.save(log2);
        City savedCity = cityRepository.save(city);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedCity.getId()).toUri();

        String message = "City created: " + savedCity;

        Log log3 = new Log();
        log3.setText(message);
        logRepository.save(log3);
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCity(@PathVariable long id) {
        try {
            cityRepository.deleteHouses(id);
            cityRepository.deleteById(id);

            String message = "City with id = " + id + " deleted";
            Log log3 = new Log();
            log3.setText(message);
            logRepository.save(log3);

        }
        catch (org.springframework.dao.EmptyResultDataAccessException ex){
            String message = "City with id = " + id + " does not exist";
            Log log3 = new Log();
            log3.setText(message);
            logRepository.save(log3);

            throw new ItemNotFoundException("City with id=" + id + " doesn't exist");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCity(@RequestBody CityVO cityVO, @PathVariable Long id) {
        try {
            Optional<City> cityOptional = cityRepository.findById(id);

            if (cityOptional.get() == null) ;

            City city = new City();
            city.setName(cityVO.getName());
            //city.setSite(cityVO.getSite());
            city.setPopulation(cityVO.getPopulation());

            city.setId(id);

            cityRepository.save(city);
            String message = "City updated: " + city;
            Log log3 = new Log();
            log3.setText(message);
            logRepository.save(log3);

            return ResponseEntity.noContent().build();
        }
        catch (NoSuchElementException ex){
            String message = "City with id = " + id + " does not exist";
            Log log3 = new Log();
            log3.setText(message);
            logRepository.save(log3);

            throw new ItemNotFoundException("City with id=" + id + " doesn't exist");
        }

    }
}
