package DaoJPA.Repositories;

import DaoJPA.Entities.City;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

public interface CityRepository extends CrudRepository<City, Long> {
    @Query("update houses set deleted = 1 where city_id = :idd" )
    @Modifying
    @Transactional
    public void deleteHouses(@PathVariable long idd);
}
