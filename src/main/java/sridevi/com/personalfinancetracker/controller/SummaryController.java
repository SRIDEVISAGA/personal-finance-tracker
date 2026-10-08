package sridevi.com.personalfinancetracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sridevi.com.personalfinancetracker.entity.User;
import sridevi.com.personalfinancetracker.exception.ResourceNotFoundException;
import sridevi.com.personalfinancetracker.repository.ExpenseRepository;
import sridevi.com.personalfinancetracker.repository.IncomeRepository;
import sridevi.com.personalfinancetracker.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

@RestController
@Tag(
        name = "Finance Summary",
        description = "APIs for viewing overall financial summary"
)
@SecurityRequirement(name = "bearerAuth")
public class SummaryController {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public SummaryController(
            IncomeRepository incomeRepository,
            ExpenseRepository expenseRepository,
            UserRepository userRepository) {

        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    @Operation(
            summary = "Get financial summary",
            description = "Returns the logged-in user's total income, total expenses, and current balance"
    )
    @GetMapping("/summary")
    public Map<String, Double> getSummary(
            HttpServletRequest request) {

        String email =
                (String) request.getAttribute("userEmail");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        double totalIncome =
                incomeRepository.findByUserId(user.getId())
                        .stream()
                        .mapToDouble(income -> income.getAmount())
                        .sum();

        double totalExpenses =
                expenseRepository.findByUserId(user.getId())
                        .stream()
                        .mapToDouble(expense -> expense.getAmount())
                        .sum();

        double balance =
                totalIncome - totalExpenses;

        Map<String, Double> summary =
                new HashMap<>();

        summary.put(
                "totalIncome",
                totalIncome
        );

        summary.put(
                "totalExpenses",
                totalExpenses
        );

        summary.put(
                "balance",
                balance
        );

        return summary;
    }
}