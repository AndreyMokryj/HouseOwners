package UserJPA.Repositories;

import UserJPA.Entities.Region;
import org.springframework.data.repository.CrudRepository;

public interface RegionRepository extends CrudRepository<Region, Long> {
}
