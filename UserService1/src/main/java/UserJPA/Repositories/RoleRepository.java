package UserJPA.Repositories;

import UserJPA.Entities.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
//    @Query("SELECT u FROM users u where username = :un")
////    @Modifying
//    @Transactional
//    public Optional<UserE> findByUN(@PathVariable String un);
}
