package sridevi.com.personalfinancetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sridevi.com.personalfinancetracker.entity.SavingsGoal;

import java.util.List;

public interface SavingsGoalRepository
        extends JpaRepository<SavingsGoal, Long> {

    List<SavingsGoal> findByUserId(Long userId);
}