package DaoJPA.Repositories;

import DaoJPA.Entities.HouseOwner;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

public interface HouseOwnerRepository extends CrudRepository<HouseOwner, Long> {
    @Query("SELECT ho FROM house_owners ho where person_id = :idd")
    @Modifying
    @Transactional
    public Iterable<HouseOwner> findByPID(@PathVariable long idd);

    @Query("SELECT ho FROM house_owners ho where house_id = :idd")
    @Modifying
    @Transactional
    public Iterable<HouseOwner> findByHID(@PathVariable long idd);
}
