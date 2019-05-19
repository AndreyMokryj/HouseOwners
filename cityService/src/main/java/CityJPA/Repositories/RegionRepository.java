package CityJPA.Repositories;

import CityJPA.Entities.Region;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

public interface RegionRepository extends CrudRepository<Region, Long> {
    @Query("update cities set deleted = 1 where region_id = :idd" )
    @Modifying
    @Transactional
    public void deleteCities(@PathVariable long idd);
}
