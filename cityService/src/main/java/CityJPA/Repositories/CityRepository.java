package CityJPA.Repositories;

import CityJPA.Entities.City;
import org.springframework.data.repository.CrudRepository;

public interface CityRepository extends CrudRepository<City, Long> {
}
