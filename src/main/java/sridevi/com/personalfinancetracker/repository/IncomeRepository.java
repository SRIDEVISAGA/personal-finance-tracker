package sridevi.com.personalfinancetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sridevi.com.personalfinancetracker.entity.Income;

import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {

    @Query("SELECT i FROM Income i WHERE i.date = :date")
    List<Income> findByDate(@Param("date") String date);

    List<Income> findByUserId(Long userId);

    List<Income> findByUserIdAndDate(Long userId, String date);
}