package DaoJPA.Repositories;

import DaoJPA.Entities.Log;
import org.springframework.data.repository.CrudRepository;

public interface LogRepository extends CrudRepository<Log, Long> {
}
