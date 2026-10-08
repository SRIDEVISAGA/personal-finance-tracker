package sridevi.com.personalfinancetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sridevi.com.personalfinancetracker.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}