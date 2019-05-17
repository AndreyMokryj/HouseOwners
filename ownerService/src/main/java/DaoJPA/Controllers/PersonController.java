package DaoJPA.Controllers;

import DaoJPA.Entities.Log;
import DaoJPA.Entities.Person;
import DaoJPA.Repositories.LogRepository;
import DaoJPA.Repositories.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vo.CustomMessage;
import vo.Exceptions.ItemNotFoundException;
import vo.PersonVO;

import java.net.URI;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

@RestController    // This means that this class is a Controller
@RequestMapping(path="/people")
public class PersonController {
    @Autowired
    private PersonRepository personRepository;

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
    Iterable<Person> getAll() {
        // This returns a JSON or XML with the users
        return personRepository.findAll();
    }

    @GetMapping("/{id}")
    public Person retrievePerson(@PathVariable long id) {
        try {
            Optional<Person> person = personRepository.findById(id);

            return person.get();
        }
        catch (NoSuchElementException ex){
            throw new ItemNotFoundException("Person with id=" + id + " doesn't exist");
        }
    }

    @PostMapping("/")
    public ResponseEntity<Object> createPerson(@RequestBody PersonVO personVO) {

        Person person = Person.fromVO(personVO);

        Person savedPerson = personRepository.save(person);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedPerson.getId()).toUri();

        String message = "Person created: " + savedPerson;
        writeLog(message);
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteHouse(@PathVariable long id) {
        try {
            personRepository.deleteHouseOwners(id);
            personRepository.deleteById(id);

            String message = "Person with id = " + id + " deleted";
            writeLog(message);
        }
        catch (org.springframework.dao.EmptyResultDataAccessException ex){
            String message = "Person with id = " + id + " does not exist";
            writeLog(message);
            throw new ItemNotFoundException("Person with id=" + id + " doesn't exist");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePerson(@RequestBody PersonVO personVO, @PathVariable long id) {
        try {
            Optional<Person> personOptional = personRepository.findById(id);

            if (personOptional.get() == null);

            Person person = Person.fromVO(personVO);
            person.setId(id);

            personRepository.save(person);

            String message = "Person updated: " + person;
            writeLog(message);
            return ResponseEntity.noContent().build();
        }
        catch (NoSuchElementException ex){
            String message = "Person with id=" + id + " doesn't exist";
            writeLog(message);
            throw new ItemNotFoundException("Person with id=" + id + " doesn't exist");
        }
    }

    public void writeLog(String message){
        Log log = new Log();
        log.setText(message);
        logRepository.save(log);
    }
}
