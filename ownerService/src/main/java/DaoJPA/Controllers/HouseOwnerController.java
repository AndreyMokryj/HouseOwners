package DaoJPA.Controllers;

import DaoJPA.Entities.HouseOwner;
import DaoJPA.Entities.Log;
import DaoJPA.Repositories.HouseOwnerRepository;
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
import vo.HouseOwnerVO;

import java.net.URI;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

@RestController    // This means that this class is a Controller
@RequestMapping(path="/house-owners")
public class HouseOwnerController {
    @Autowired
    private HouseOwnerRepository houseOwnerRepository;

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
    public @ResponseBody
    Iterable<HouseOwner> getAll() {
        // This returns a JSON or XML with the users
        return houseOwnerRepository.findAll();
    }

    @GetMapping(path="/person_id={id}")
    public @ResponseBody
    Iterable<HouseOwner> getByPersonId(@PathVariable long id) {
        // This returns a JSON or XML with the users
        return houseOwnerRepository.findByPID(id);
    }

    @GetMapping(path="/house_id={id}")
    public @ResponseBody
    Iterable<HouseOwner> getByHouseId(@PathVariable long id) {
        // This returns a JSON or XML with the users
        return houseOwnerRepository.findByHID(id);
    }

    @GetMapping("/{id}")
    public HouseOwner retrieveHouseOwner(@PathVariable long id) {
        try {
            Optional<HouseOwner> houseOwner = houseOwnerRepository.findById(id);

            return houseOwner.get();
        }
        catch (NoSuchElementException ex){
            throw new ItemNotFoundException("HouseOwner with id=" + id + " doesn't exist");
        }
    }

    @PostMapping("/")
    public ResponseEntity<Object> createHouseOwner(@RequestBody HouseOwnerVO houseOwnerVO) {

        HouseOwner houseOwner = HouseOwner.fromVO(houseOwnerVO);

        HouseOwner savedHouseOwner = houseOwnerRepository.save(houseOwner);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedHouseOwner.getId()).toUri();

        String message = "HouseOwner created: " + savedHouseOwner;
        writeLog(message);
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteHouseOwner(@PathVariable long id) {
        try {
            houseOwnerRepository.deleteById(id);

            String message = "HouseOwner with id = " + id + " deleted";
            writeLog(message);
        }
        catch (org.springframework.dao.EmptyResultDataAccessException ex){
            String message = "HouseOwner with id = " + id + " does not exist";
            writeLog(message);
            throw new ItemNotFoundException("HouseOwner with id=" + id + " doesn't exist");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateHouseOwner(@RequestBody HouseOwnerVO houseOwnerVO, @PathVariable long id) {
        try {
            Optional<HouseOwner> houseOwnerOptional = houseOwnerRepository.findById(id);

            if (houseOwnerOptional.get() == null);

            HouseOwner houseOwner = HouseOwner.fromVO(houseOwnerVO);
            houseOwner.setId(id);

            houseOwnerRepository.save(houseOwner);

            String message = "HouseOwner updated: " + houseOwner;
            writeLog(message);
            return ResponseEntity.noContent().build();
        }
        catch (NoSuchElementException ex){
            String message = "HouseOwner with id=" + id + " doesn't exist";
            writeLog(message);
            throw new ItemNotFoundException("HouseOwner with id=" + id + " doesn't exist");
        }
    }

    public void writeLog(String message){
        Log log = new Log();
        log.setText(message);
        logRepository.save(log);
    }
}
