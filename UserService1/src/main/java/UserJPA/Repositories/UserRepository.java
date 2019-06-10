package UserJPA.Repositories;

import UserJPA.Entities.UserE;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserE, Long> {
    @Query("SELECT u FROM users u where username = :un")
//    @Modifying
    @Transactional
    public Optional<UserE> findByUN(@PathVariable String un);

    @Query("SELECT role FROM user_roles r where username = :un")
//    @Modifying
    @Transactional
    public Iterable<String> findRolesByUN(@PathVariable String un);
}
