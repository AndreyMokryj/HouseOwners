package UserJPA.Repositories;

import UserJPA.Entities.House;
import org.springframework.data.repository.CrudRepository;

public interface HouseRepository extends CrudRepository<House, Long> {
}
