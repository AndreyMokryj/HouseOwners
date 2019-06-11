package CityJPA.Controllers;

import CityJPA.Entities.Log;
import CityJPA.Entities.Region;
import CityJPA.Repositories.LogRepository;
import CityJPA.Repositories.RegionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vo.CustomMessage;
import vo.Exceptions.ItemNotFoundException;
import vo.RegionVO;

import java.net.URI;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

@RestController    // This means that this class is a Controller
@RequestMapping(path="/regions")
@Component
public class RegionController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private RegionRepository regionRepository;

    @Autowired
    private LogRepository logRepository;

    //Rabbit receive
    private static final Logger log = LoggerFactory.getLogger(RegionController.class);

    private CountDownLatch latch = new CountDownLatch(1);

    private Log log2 = new Log();

    @RabbitListener(queues = RabbitConfiguration.QUEUE_SPECIFIC_NAME)
    public void receiveMessage(final CustomMessage customMessage) {
        log.info("Received message as specific class: {}", customMessage.toString());
        writeLog(customMessage.toString());
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    @GetMapping(path="/")
    public @ResponseBody Iterable<Region> getAll() {
        // This returns a JSON or XML with the users
        return regionRepository.findAll();
    }

    @GetMapping("/{id}")
    public Region retrieveRegion(@PathVariable long id) throws ItemNotFoundException {
        try {
            Optional<Region> region = regionRepository.findById(id);
            return region.get();
        }
        catch (NoSuchElementException ex){
            throw new ItemNotFoundException("Region with id=" + id + " doesn't exist");
        }

    }

    @PostMapping("/")
    public ResponseEntity<Object> createRegion(@RequestBody RegionVO regionVO) {

        Region region = Region.fromVO(regionVO);
        Region savedRegion = regionRepository.save(region);
        regionVO.setId(savedRegion.getId());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedRegion.getId()).toUri();

        String message = "Region created: " + regionVO;
        writeLog(message);
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteRegion(@PathVariable long id) {
        try {
            regionRepository.deleteById(id);
            String message = "Region with id = " + id + " deleted";
            writeLog(message);

        }
        catch (org.springframework.dao.EmptyResultDataAccessException ex){
            String message = "Region with id = " + id + " does not exist";
            writeLog(message);
            throw new ItemNotFoundException("Region with id=" + id + " doesn't exist");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateRegion(@RequestBody RegionVO regionVO, @PathVariable Long id) {
        try {
            Optional<Region> regionOptional = regionRepository.findById(id);

            if (regionOptional.get() == null) ;

            Region region = Region.fromVO(regionVO);
            region.setId(id);
            regionVO.setId(id);

            regionRepository.save(region);
            String message = "Region updated: " + regionVO;

            writeLog(message);
            return ResponseEntity.noContent().build();
        }
        catch (NoSuchElementException ex){
            String message = "Region with id = " + id + " does not exist";
            writeLog(message);
            throw new ItemNotFoundException("Region with id=" + id + " doesn't exist");
        }
    }

    public void writeLog(String message){
        Log log = new Log();
        log.setText(message);
        logRepository.save(log);
    }
}
