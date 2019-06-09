package UserJPA.Repositories;

import UserJPA.Entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    @Query("SELECT u FROM users u where username = :un")
//    @Modifying
    @Transactional
    public Optional<User> findByUN(@PathVariable String un);
}
