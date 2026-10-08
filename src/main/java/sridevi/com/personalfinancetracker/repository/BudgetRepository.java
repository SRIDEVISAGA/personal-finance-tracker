package sridevi.com.personalfinancetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sridevi.com.personalfinancetracker.entity.Budget;

import java.util.List;

public interface BudgetRepository
        extends JpaRepository<Budget, Long> {

    @Query("SELECT b FROM Budget b WHERE b.category = :category")
    List<Budget> findByCategory(
            @Param("category") String category
    );

    @Query("SELECT b FROM Budget b WHERE b.month = :month")
    List<Budget> findByMonth(
            @Param("month") String month
    );

    // =========================
    // USER-SPECIFIC QUERIES
    // =========================

    List<Budget> findByUserId(Long userId);

    List<Budget> findByUserIdAndCategory(
            Long userId,
            String category
    );

    List<Budget> findByUserIdAndMonth(
            Long userId,
            String month
    );
}