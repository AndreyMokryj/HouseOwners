package OwnerJPA.Repositories;

import OwnerJPA.Entities.House;
import org.springframework.data.repository.CrudRepository;

public interface HouseRepository extends CrudRepository<House, Long> {
}
