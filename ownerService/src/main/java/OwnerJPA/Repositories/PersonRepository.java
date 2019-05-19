package OwnerJPA.Repositories;

import OwnerJPA.Entities.Person;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

public interface PersonRepository extends CrudRepository<Person, Long> {
    @Query("update house_owners set deleted = 1 where person_id = :idd" )
    @Modifying
    @Transactional
    public void deleteHouseOwners(@PathVariable long idd);
}
