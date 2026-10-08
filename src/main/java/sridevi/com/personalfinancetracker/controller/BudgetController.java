package sridevi.com.personalfinancetracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import sridevi.com.personalfinancetracker.entity.Budget;
import sridevi.com.personalfinancetracker.entity.User;
import sridevi.com.personalfinancetracker.exception.ResourceNotFoundException;
import sridevi.com.personalfinancetracker.repository.UserRepository;
import sridevi.com.personalfinancetracker.service.BudgetService;

import java.util.List;

@RestController
@RequestMapping("/budgets")
@Tag(
        name = "Budget Management",
        description = "APIs for managing monthly budgets"
)
@SecurityRequirement(name = "bearerAuth")
public class BudgetController {

    private final BudgetService budgetService;
    private final UserRepository userRepository;

    public BudgetController(
            BudgetService budgetService,
            UserRepository userRepository) {

        this.budgetService = budgetService;
        this.userRepository = userRepository;
    }

    // =========================
    // ADD BUDGET
    // =========================

    @Operation(
            summary = "Add a new budget",
            description = "Creates a new budget for the logged-in user"
    )
    @PostMapping
    public Budget addBudget(
            @Valid @RequestBody Budget budget,
            HttpServletRequest request) {

        String email =
                (String) request.getAttribute("userEmail");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        budget.setUser(user);

        return budgetService.addBudget(budget);
    }

    // =========================
    // GET ALL BUDGETS
    // =========================

    @Operation(
            summary = "Get all budgets",
            description = "Returns only budgets belonging to the logged-in user"
    )
    @GetMapping
    public List<Budget> getAllBudgets(
            HttpServletRequest request) {

        String email =
                (String) request.getAttribute("userEmail");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        return budgetService.getBudgetsByUser(
                user.getId()
        );
    }

    // =========================
    // GET BUDGET BY ID
    // =========================

    @Operation(
            summary = "Get budget by ID",
            description = "Returns a budget only if it belongs to the logged-in user"
    )
    @GetMapping("/{id}")
    public Budget getBudgetById(
            @PathVariable Long id,
            HttpServletRequest request) {

        String email =
                (String) request.getAttribute("userEmail");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        Budget budget =
                budgetService.getBudgetById(id);

        if (!budget.getUser().getId().equals(user.getId())) {

            throw new ResourceNotFoundException(
                    "Budget not found"
            );
        }

        return budget;
    }

    // =========================
    // GET BUDGETS BY CATEGORY
    // =========================

    @Operation(
            summary = "Get budgets by category",
            description = "Returns only the logged-in user's budgets for the specified category"
    )
    @GetMapping("/category/{category}")
    public List<Budget> getBudgetsByCategory(
            @PathVariable String category,
            HttpServletRequest request) {

        String email =
                (String) request.getAttribute("userEmail");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        return budgetService.getBudgetsByUserAndCategory(
                user.getId(),
                category
        );
    }

    // =========================
    // GET BUDGETS BY MONTH
    // =========================

    @Operation(
            summary = "Get budgets by month",
            description = "Returns only the logged-in user's budgets for the specified month"
    )
    @GetMapping("/month/{month}")
    public List<Budget> getBudgetsByMonth(
            @PathVariable String month,
            HttpServletRequest request) {

        String email =
                (String) request.getAttribute("userEmail");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        return budgetService.getBudgetsByUserAndMonth(
                user.getId(),
                month
        );
    }

    // =========================
    // UPDATE BUDGET
    // =========================

    @Operation(
            summary = "Update a budget",
            description = "Updates a budget only if it belongs to the logged-in user"
    )
    @PutMapping("/{id}")
    public Budget updateBudget(
            @PathVariable Long id,
            @Valid @RequestBody Budget budget,
            HttpServletRequest request) {

        String email =
                (String) request.getAttribute("userEmail");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        Budget existingBudget =
                budgetService.getBudgetById(id);

        if (!existingBudget.getUser().getId().equals(user.getId())) {

            throw new ResourceNotFoundException(
                    "Budget not found"
            );
        }

        return budgetService.updateBudget(
                id,
                budget
        );
    }

    // =========================
    // DELETE BUDGET
    // =========================

    @Operation(
            summary = "Delete a budget",
            description = "Deletes a budget only if it belongs to the logged-in user"
    )
    @DeleteMapping("/{id}")
    public String deleteBudget(
            @PathVariable Long id,
            HttpServletRequest request) {

        String email =
                (String) request.getAttribute("userEmail");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        Budget existingBudget =
                budgetService.getBudgetById(id);

        if (!existingBudget.getUser().getId().equals(user.getId())) {

            throw new ResourceNotFoundException(
                    "Budget not found"
            );
        }

        budgetService.deleteBudget(id);

        return "Budget deleted successfully";
    }
}