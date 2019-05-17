package DaoJPA.Repositories;

import DaoJPA.Entities.HouseOwner;
import org.springframework.data.repository.CrudRepository;

public interface HouseOwnerRepository extends CrudRepository<HouseOwner, Long> {
}
