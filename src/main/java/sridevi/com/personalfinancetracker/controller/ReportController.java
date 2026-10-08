package sridevi.com.personalfinancetracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import sridevi.com.personalfinancetracker.entity.User;
import sridevi.com.personalfinancetracker.exception.ResourceNotFoundException;
import sridevi.com.personalfinancetracker.repository.UserRepository;
import sridevi.com.personalfinancetracker.service.ReportService;

import java.util.Map;

@RestController
@RequestMapping("/reports")
@Tag(
        name = "Financial Reports",
        description = "APIs for generating financial reports"
)
@SecurityRequirement(name = "bearerAuth")
public class ReportController {

    private final ReportService reportService;
    private final UserRepository userRepository;

    public ReportController(
            ReportService reportService,
            UserRepository userRepository) {

        this.reportService = reportService;
        this.userRepository = userRepository;
    }

    private User getLoggedInUser(
            HttpServletRequest request) {

        String email =
                (String) request.getAttribute("userEmail");

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));
    }

    @Operation(
            summary = "Get expenses by category",
            description = "Returns the logged-in user's total expenses grouped by category"
    )
    @GetMapping("/expenses-by-category")
    public Map<String, Double> getExpenseCategorySummary(
            HttpServletRequest request) {

        User user = getLoggedInUser(request);

        return reportService.getExpenseCategorySummary(
                user.getId()
        );
    }

    @Operation(
            summary = "Get expenses by date",
            description = "Returns the logged-in user's total expenses grouped by date"
    )
    @GetMapping("/expenses-by-date")
    public Map<String, Double> getExpenseDateSummary(
            HttpServletRequest request) {

        User user = getLoggedInUser(request);

        return reportService.getExpenseDateSummary(
                user.getId()
        );
    }

    @Operation(
            summary = "Get monthly financial summary",
            description = "Returns the logged-in user's income, expenses, and balance for a month"
    )
    @GetMapping("/monthly-summary/{month}")
    public Map<String, Object> getMonthlySummary(
            @PathVariable String month,
            HttpServletRequest request) {

        User user = getLoggedInUser(request);

        return reportService.getMonthlySummary(
                user.getId(),
                month
        );
    }

    @Operation(
            summary = "Get dashboard statistics",
            description = "Returns the logged-in user's income, expenses, balance, budgets, and record counts"
    )
    @GetMapping("/dashboard")
    public Map<String, Object> getDashboardStatistics(
            HttpServletRequest request) {

        User user = getLoggedInUser(request);

        return reportService.getDashboardStatistics(
                user.getId()
        );
    }

    @Operation(
            summary = "Get expense category percentages",
            description = "Returns the logged-in user's expense amount and percentage for each category"
    )
    @GetMapping("/expenses-by-category-percentage")
    public Map<String, Map<String, Double>>
    getExpenseCategoryPercentage(
            HttpServletRequest request) {

        User user = getLoggedInUser(request);

        return reportService.getExpenseCategoryPercentage(
                user.getId()
        );
    }

    @Operation(
            summary = "Get budget vs spending",
            description = "Compares the logged-in user's budget with spending for a month"
    )
    @GetMapping("/budget-vs-spending/{month}")
    public Map<String, Map<String, Object>> getBudgetVsSpending(
            @PathVariable String month,
            HttpServletRequest request) {

        User user = getLoggedInUser(request);

        return reportService.getBudgetVsSpending(
                user.getId(),
                month
        );
    }

    @Operation(
            summary = "Get savings goal progress",
            description = "Returns savings goal progress only for the logged-in user"
    )
    @GetMapping("/savings-goal/{id}")
    public Map<String, Object> getSavingsGoalProgress(
            @PathVariable Long id,
            HttpServletRequest request) {

        User user = getLoggedInUser(request);

        return reportService.getSavingsGoalProgress(
                user.getId(),
                id
        );
    }

    @Operation(
            summary = "Get overall savings summary",
            description = "Returns the logged-in user's savings goals summary"
    )
    @GetMapping("/savings-summary")
    public Map<String, Object> getSavingsSummary(
            HttpServletRequest request) {

        User user = getLoggedInUser(request);

        return reportService.getSavingsSummary(
                user.getId()
        );
    }
}