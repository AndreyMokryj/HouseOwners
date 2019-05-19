package CityJPA.Repositories;

import CityJPA.Entities.City;
import OwnerJPA.Entities.House;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import OwnerJPA.Repositories.HouseRepository;

public interface CityRepository extends CrudRepository<City, Long> {
//    @Query("update houses set deleted = 1 where city_id = :idd" )
//    @Modifying
//    @Transactional
//    public void deleteHouses(@PathVariable long idd);
}
