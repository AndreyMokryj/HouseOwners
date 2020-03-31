package CityJPA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableDiscoveryClient
@SpringBootApplication
@RestController
@RequestMapping(path = "/config")

//@EnableAutoConfiguration
//@ComponentScan
//@Import(RabbitConfiguration.class)
public class CApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(CApplication.class, args);
    }

//    @Autowired
//    private Environment env;
//
//    @GetMapping(path="/", produces = "application/json; charset=UTF-8")
//    public @ResponseBody
//    String getProperties() throws JsonProcessingException {
//        Map<String, Object> props = new HashMap<>();
//        CompositePropertySource bootstrapProperties = (CompositePropertySource)  ((AbstractEnvironment) env).getPropertySources().get("bootstrapProperties");
//        for (String propertyName : bootstrapProperties.getPropertyNames()) {
//            props.put(propertyName, bootstrapProperties.getProperty(propertyName));
//        }
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
//        return mapper.writeValueAsString(props);
//    }
}
