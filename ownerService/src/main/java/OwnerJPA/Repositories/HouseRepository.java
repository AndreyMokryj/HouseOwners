package OwnerJPA.Repositories;

import OwnerJPA.Entities.House;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

public interface HouseRepository extends CrudRepository<House, Long> {
}
