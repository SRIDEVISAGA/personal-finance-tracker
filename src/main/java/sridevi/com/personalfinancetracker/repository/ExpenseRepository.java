package sridevi.com.personalfinancetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sridevi.com.personalfinancetracker.entity.Expense;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query("SELECT e.category, SUM(e.amount) FROM Expense e GROUP BY e.category")
    List<Object[]> getExpenseByCategory();

    @Query("SELECT e.date, SUM(e.amount) FROM Expense e GROUP BY e.date ORDER BY e.date")
    List<Object[]> getExpenseByDate();

    @Query("SELECT e FROM Expense e WHERE e.category = :category")
    List<Expense> findByCategory(@Param("category") String category);

    @Query("SELECT e FROM Expense e WHERE e.date = :date")
    List<Expense> findByDate(@Param("date") String date);

    List<Expense> findByUserId(Long userId);

    List<Expense> findByUserIdAndCategory(
            Long userId,
            String category
    );

    List<Expense> findByUserIdAndDate(
            Long userId,
            String date
    );
}