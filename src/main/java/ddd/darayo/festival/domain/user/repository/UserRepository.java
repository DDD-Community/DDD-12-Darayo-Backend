package ddd.darayo.festival.domain.user.repository;

import ddd.darayo.festival.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
}
