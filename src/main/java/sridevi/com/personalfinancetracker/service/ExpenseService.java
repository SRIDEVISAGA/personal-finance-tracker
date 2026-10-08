package sridevi.com.personalfinancetracker.service;

import org.springframework.stereotype.Service;
import sridevi.com.personalfinancetracker.entity.Expense;
import sridevi.com.personalfinancetracker.exception.ResourceNotFoundException;
import sridevi.com.personalfinancetracker.repository.ExpenseRepository;

import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(
            ExpenseRepository expenseRepository) {

        this.expenseRepository = expenseRepository;
    }

    public Expense addExpense(Expense expense) {

        return expenseRepository.save(expense);
    }

    public List<Expense> getAllExpenses() {

        return expenseRepository.findAll();
    }

    public List<Expense> getExpensesByCategory(
            String category) {

        return expenseRepository.findByCategory(category);
    }

    public List<Expense> getExpensesByDate(
            String date) {

        return expenseRepository.findByDate(date);
    }

    public List<Expense> getExpensesByUser(
            Long userId) {

        return expenseRepository.findByUserId(userId);
    }

    public List<Expense> getExpensesByUserAndCategory(
            Long userId,
            String category) {

        return expenseRepository.findByUserIdAndCategory(
                userId,
                category
        );
    }

    public List<Expense> getExpensesByUserAndDate(
            Long userId,
            String date) {

        return expenseRepository.findByUserIdAndDate(
                userId,
                date
        );
    }

    public Expense getExpenseById(Long id) {

        return expenseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Expense not found with id: " + id
                        ));
    }

    public Expense updateExpense(
            Long id,
            Expense expense) {

        Expense existingExpense =
                expenseRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Expense not found with id: " + id
                                ));

        existingExpense.setTitle(
                expense.getTitle()
        );

        existingExpense.setAmount(
                expense.getAmount()
        );

        existingExpense.setCategory(
                expense.getCategory()
        );

        existingExpense.setDate(
                expense.getDate()
        );

        return expenseRepository.save(
                existingExpense
        );
    }

    public void deleteExpense(Long id) {

        Expense existingExpense =
                expenseRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Expense not found with id: " + id
                                ));

        expenseRepository.delete(
                existingExpense
        );
    }
}