package OwnerJPA.Repositories;

import OwnerJPA.Entities.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Long> {
}
