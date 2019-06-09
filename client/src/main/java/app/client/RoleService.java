package app.client;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleService extends JpaRepository<Role,Long> {
}
