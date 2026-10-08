package sridevi.com.personalfinancetracker.service;

import org.springframework.stereotype.Service;
import sridevi.com.personalfinancetracker.entity.Budget;
import sridevi.com.personalfinancetracker.exception.ResourceNotFoundException;
import sridevi.com.personalfinancetracker.repository.BudgetRepository;

import java.util.List;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    // =========================
    // ADD BUDGET
    // =========================

    public Budget addBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    // =========================
    // GET ALL BUDGETS
    // =========================

    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    // =========================
    // GET BUDGET BY ID
    // =========================

    public Budget getBudgetById(Long id) {

        return budgetRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Budget not found with id: " + id
                        ));
    }

    // =========================
    // GET BY CATEGORY
    // =========================

    public List<Budget> getBudgetsByCategory(
            String category) {

        return budgetRepository.findByCategory(category);
    }

    // =========================
    // GET BY MONTH
    // =========================

    public List<Budget> getBudgetsByMonth(
            String month) {

        return budgetRepository.findByMonth(month);
    }

    // =========================
    // GET BY USER
    // =========================

    public List<Budget> getBudgetsByUser(
            Long userId) {

        return budgetRepository.findByUserId(userId);
    }

    // =========================
    // GET BY USER + CATEGORY
    // =========================

    public List<Budget> getBudgetsByUserAndCategory(
            Long userId,
            String category) {

        return budgetRepository.findByUserIdAndCategory(
                userId,
                category
        );
    }

    // =========================
    // GET BY USER + MONTH
    // =========================

    public List<Budget> getBudgetsByUserAndMonth(
            Long userId,
            String month) {

        return budgetRepository.findByUserIdAndMonth(
                userId,
                month
        );
    }

    // =========================
    // UPDATE BUDGET
    // =========================

    public Budget updateBudget(
            Long id,
            Budget budget) {

        Budget existingBudget =
                getBudgetById(id);

        existingBudget.setCategory(
                budget.getCategory()
        );

        existingBudget.setAmount(
                budget.getAmount()
        );

        existingBudget.setMonth(
                budget.getMonth()
        );

        return budgetRepository.save(
                existingBudget
        );
    }

    // =========================
    // DELETE BUDGET
    // =========================

    public void deleteBudget(Long id) {

        Budget existingBudget =
                getBudgetById(id);

        budgetRepository.delete(
                existingBudget
        );
    }
}