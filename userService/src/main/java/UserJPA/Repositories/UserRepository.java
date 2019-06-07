package UserJPA.Repositories;

import UserJPA.Entities.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    @Query("SELECT u FROM users u where username = :un and password = :pw")
//    @Modifying
    @Transactional
    public Optional<User> findByUP(String un, String pw);

}
