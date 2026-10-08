package sridevi.com.personalfinancetracker.service;

import org.springframework.stereotype.Service;
import sridevi.com.personalfinancetracker.entity.Budget;
import sridevi.com.personalfinancetracker.entity.Expense;
import sridevi.com.personalfinancetracker.entity.Income;
import sridevi.com.personalfinancetracker.entity.SavingsGoal;
import sridevi.com.personalfinancetracker.exception.ResourceNotFoundException;
import sridevi.com.personalfinancetracker.repository.BudgetRepository;
import sridevi.com.personalfinancetracker.repository.ExpenseRepository;
import sridevi.com.personalfinancetracker.repository.IncomeRepository;
import sridevi.com.personalfinancetracker.repository.SavingsGoalRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
    private final BudgetRepository budgetRepository;
    private final SavingsGoalRepository savingsGoalRepository;

    public ReportService(
            ExpenseRepository expenseRepository,
            IncomeRepository incomeRepository,
            BudgetRepository budgetRepository,
            SavingsGoalRepository savingsGoalRepository) {

        this.expenseRepository = expenseRepository;
        this.incomeRepository = incomeRepository;
        this.budgetRepository = budgetRepository;
        this.savingsGoalRepository = savingsGoalRepository;
    }

    // Get expense summary by category for logged-in user
    public Map<String, Double> getExpenseCategorySummary(
            Long userId) {

        Map<String, Double> summary = new HashMap<>();

        List<Expense> expenses =
                expenseRepository.findByUserId(userId);

        for (Expense expense : expenses) {

            String category = expense.getCategory();

            summary.put(
                    category,
                    summary.getOrDefault(category, 0.0)
                            + expense.getAmount()
            );
        }

        return summary;
    }

    // Get expense summary by date for logged-in user
    public Map<String, Double> getExpenseDateSummary(
            Long userId) {

        Map<String, Double> summary = new HashMap<>();

        List<Expense> expenses =
                expenseRepository.findByUserId(userId);

        for (Expense expense : expenses) {

            String date = expense.getDate();

            summary.put(
                    date,
                    summary.getOrDefault(date, 0.0)
                            + expense.getAmount()
            );
        }

        return summary;
    }

    // Get monthly summary for logged-in user
    public Map<String, Object> getMonthlySummary(
            Long userId,
            String month) {

        List<Income> incomes =
                incomeRepository.findByUserId(userId);

        List<Expense> expenses =
                expenseRepository.findByUserId(userId);

        double totalIncome = incomes
                .stream()
                .filter(income ->
                        income.getDate() != null
                                && income.getDate().startsWith(month))
                .mapToDouble(Income::getAmount)
                .sum();

        double totalExpenses = expenses
                .stream()
                .filter(expense ->
                        expense.getDate() != null
                                && expense.getDate().startsWith(month))
                .mapToDouble(Expense::getAmount)
                .sum();

        double balance =
                totalIncome - totalExpenses;

        Map<String, Object> summary =
                new HashMap<>();

        summary.put("month", month);
        summary.put("totalIncome", totalIncome);
        summary.put("totalExpenses", totalExpenses);
        summary.put("balance", balance);

        return summary;
    }

    // Get dashboard statistics for logged-in user
    public Map<String, Object> getDashboardStatistics(
            Long userId) {

        List<Income> incomes =
                incomeRepository.findByUserId(userId);

        List<Expense> expenses =
                expenseRepository.findByUserId(userId);

        List<Budget> budgets =
                budgetRepository.findByUserId(userId);

        double totalIncome = incomes
                .stream()
                .mapToDouble(Income::getAmount)
                .sum();

        double totalExpenses = expenses
                .stream()
                .mapToDouble(Expense::getAmount)
                .sum();

        double balance =
                totalIncome - totalExpenses;

        long incomeCount =
                incomes.size();

        long expenseCount =
                expenses.size();

        long budgetCount =
                budgets.size();

        long overBudgetCount = 0;

        for (Budget budget : budgets) {

            String category =
                    budget.getCategory();

            String month =
                    budget.getMonth();

            double spentAmount = expenses
                    .stream()
                    .filter(expense ->
                            expense.getCategory() != null
                                    && expense.getCategory()
                                    .equalsIgnoreCase(category)
                                    && expense.getDate() != null
                                    && expense.getDate()
                                    .startsWith(month))
                    .mapToDouble(Expense::getAmount)
                    .sum();

            if (spentAmount > budget.getAmount()) {
                overBudgetCount++;
            }
        }

        Map<String, Object> dashboard =
                new HashMap<>();

        dashboard.put(
                "totalIncome",
                totalIncome
        );

        dashboard.put(
                "totalExpenses",
                totalExpenses
        );

        dashboard.put(
                "balance",
                balance
        );

        dashboard.put(
                "incomeCount",
                incomeCount
        );

        dashboard.put(
                "expenseCount",
                expenseCount
        );

        dashboard.put(
                "budgetCount",
                budgetCount
        );

        dashboard.put(
                "overBudgetCount",
                overBudgetCount
        );

        return dashboard;
    }

    // Get expense category percentage for logged-in user
    public Map<String, Map<String, Double>>
    getExpenseCategoryPercentage(Long userId) {

        Map<String, Map<String, Double>> result =
                new HashMap<>();

        List<Expense> expenses =
                expenseRepository.findByUserId(userId);

        double totalExpenses = expenses
                .stream()
                .mapToDouble(Expense::getAmount)
                .sum();

        Map<String, Double> categoryAmounts =
                new HashMap<>();

        for (Expense expense : expenses) {

            String category =
                    expense.getCategory();

            categoryAmounts.put(
                    category,
                    categoryAmounts.getOrDefault(
                            category,
                            0.0
                    ) + expense.getAmount()
            );
        }

        for (Map.Entry<String, Double> entry :
                categoryAmounts.entrySet()) {

            String category =
                    entry.getKey();

            double amount =
                    entry.getValue();

            double percentage = 0;

            if (totalExpenses > 0) {

                percentage =
                        (amount / totalExpenses) * 100;
            }

            Map<String, Double> categoryDetails =
                    new HashMap<>();

            categoryDetails.put(
                    "amount",
                    amount
            );

            categoryDetails.put(
                    "percentage",
                    percentage
            );

            result.put(
                    category,
                    categoryDetails
            );
        }

        return result;
    }

    // Get budget vs spending for logged-in user
    public Map<String, Map<String, Object>>
    getBudgetVsSpending(
            Long userId,
            String month) {

        Map<String, Map<String, Object>> result =
                new HashMap<>();

        List<Budget> budgets =
                budgetRepository.findByUserIdAndMonth(
                        userId,
                        month
                );

        List<Expense> expenses =
                expenseRepository.findByUserId(userId);

        for (Budget budget : budgets) {

            String category =
                    budget.getCategory();

            double budgetAmount =
                    budget.getAmount();

            double spentAmount = expenses
                    .stream()
                    .filter(expense ->
                            expense.getCategory() != null
                                    && expense.getCategory()
                                    .equalsIgnoreCase(category)
                                    && expense.getDate() != null
                                    && expense.getDate()
                                    .startsWith(month))
                    .mapToDouble(Expense::getAmount)
                    .sum();

            double remainingAmount =
                    budgetAmount - spentAmount;

            String status;

            if (remainingAmount >= 0) {
                status = "Within Budget";
            } else {
                status = "Over Budget";
            }

            Map<String, Object> details =
                    new HashMap<>();

            details.put(
                    "budget",
                    budgetAmount
            );

            details.put(
                    "spent",
                    spentAmount
            );

            details.put(
                    "remaining",
                    remainingAmount
            );

            details.put(
                    "status",
                    status
            );

            result.put(
                    category,
                    details
            );
        }

        return result;
    }

    // Get savings goal progress
    public Map<String, Object> getSavingsGoalProgress(
            Long userId,
            Long id) {

        SavingsGoal savingsGoal =
                savingsGoalRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Savings goal not found with id: "
                                                + id
                                ));

        if (savingsGoal.getUser() == null
                || !savingsGoal.getUser()
                .getId()
                .equals(userId)) {

            throw new ResourceNotFoundException(
                    "Savings goal not found"
            );
        }

        double targetAmount =
                savingsGoal.getTargetAmount();

        double savedAmount =
                savingsGoal.getSavedAmount();

        double remainingAmount =
                targetAmount - savedAmount;

        double progressPercentage = 0;

        if (targetAmount > 0) {

            progressPercentage =
                    (savedAmount / targetAmount) * 100;
        }

        String status;

        if (savedAmount >= targetAmount) {
            status = "Goal Achieved";
        } else {
            status = "In Progress";
        }

        Map<String, Object> result =
                new HashMap<>();

        result.put(
                "goalName",
                savingsGoal.getGoalName()
        );

        result.put(
                "targetAmount",
                targetAmount
        );

        result.put(
                "savedAmount",
                savedAmount
        );

        result.put(
                "remainingAmount",
                remainingAmount
        );

        result.put(
                "progressPercentage",
                progressPercentage
        );

        result.put(
                "targetDate",
                savingsGoal.getTargetDate()
        );

        result.put(
                "status",
                status
        );

        return result;
    }

    // Get overall savings summary for logged-in user
    public Map<String, Object> getSavingsSummary(
            Long userId) {

        List<SavingsGoal> savingsGoals =
                savingsGoalRepository.findByUserId(userId);

        double totalTargetAmount = 0;

        double totalSavedAmount = 0;

        long completedGoals = 0;

        for (SavingsGoal savingsGoal :
                savingsGoals) {

            totalTargetAmount +=
                    savingsGoal.getTargetAmount();

            totalSavedAmount +=
                    savingsGoal.getSavedAmount();

            if (savingsGoal.getSavedAmount()
                    >= savingsGoal.getTargetAmount()) {

                completedGoals++;
            }
        }

        double totalRemainingAmount =
                totalTargetAmount - totalSavedAmount;

        double overallProgressPercentage = 0;

        if (totalTargetAmount > 0) {

            overallProgressPercentage =
                    (totalSavedAmount
                            / totalTargetAmount) * 100;
        }

        long totalGoals =
                savingsGoals.size();

        long inProgressGoals =
                totalGoals - completedGoals;

        Map<String, Object> result =
                new HashMap<>();

        result.put(
                "totalGoals",
                totalGoals
        );

        result.put(
                "totalTargetAmount",
                totalTargetAmount
        );

        result.put(
                "totalSavedAmount",
                totalSavedAmount
        );

        result.put(
                "totalRemainingAmount",
                totalRemainingAmount
        );

        result.put(
                "overallProgressPercentage",
                overallProgressPercentage
        );

        result.put(
                "completedGoals",
                completedGoals
        );

        result.put(
                "inProgressGoals",
                inProgressGoals
        );

        return result;
    }
}