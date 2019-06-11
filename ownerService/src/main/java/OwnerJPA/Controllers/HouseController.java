package OwnerJPA.Controllers;

import OwnerJPA.Entities.House;
import OwnerJPA.Entities.Log;
import OwnerJPA.Repositories.HouseRepository;
import OwnerJPA.Repositories.LogRepository;
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

    @RabbitListener(queues = RabbitConfiguration.QUEUE_SPECIFIC_NAME)
    public void receiveMessage(final CustomMessage customMessage) {
        log.info("Received message as specific class: {}", customMessage.toString());
        writeLog(customMessage.toString());
    }

    public CountDownLatch getLatch() {
        return latch;
    }

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
        House house = House.fromVO(houseVO);
        House savedHouse = houseRepository.save(house);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedHouse.getId()).toUri();

        String message = "House created: " + savedHouse.toLog();
        writeLog(message);
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteHouse(@PathVariable long id) {
        try {
            houseRepository.deleteById(id);
            String message = "House with id = " + id + " deleted";
            writeLog(message);
        }
        catch (org.springframework.dao.EmptyResultDataAccessException ex){
            String message = "House with id = " + id + " does not exist";
            writeLog(message);
            throw new ItemNotFoundException("House with id=" + id + " doesn't exist");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateHouse(@RequestBody HouseVO houseVO, @PathVariable long id) {
        try {
            Optional<House> houseOptional = houseRepository.findById(id);

            if (houseOptional.get() == null);

            House house = House.fromVO(houseVO);
            house.setId(id);
            houseRepository.save(house);

            String message = "House updated: " + house.toLog();
            writeLog(message);
            return ResponseEntity.noContent().build();
        }
        catch (NoSuchElementException ex){
            String message = "House with id=" + id + " doesn't exist";
            writeLog(message);
            throw new ItemNotFoundException("House with id=" + id + " doesn't exist");
        }
    }

    public void writeLog(String message){
        Log log = new Log();
        log.setText(message);
        logRepository.save(log);
    }
}
