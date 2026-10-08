package sridevi.com.personalfinancetracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import sridevi.com.personalfinancetracker.entity.Expense;
import sridevi.com.personalfinancetracker.entity.User;
import sridevi.com.personalfinancetracker.exception.ResourceNotFoundException;
import sridevi.com.personalfinancetracker.repository.UserRepository;
import sridevi.com.personalfinancetracker.service.ExpenseService;

import java.util.List;

@RestController
@RequestMapping("/expenses")
@Tag(
        name = "Expense Management",
        description = "APIs for managing personal expenses"
)
@SecurityRequirement(name = "bearerAuth")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserRepository userRepository;

    public ExpenseController(
            ExpenseService expenseService,
            UserRepository userRepository) {

        this.expenseService = expenseService;
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
            summary = "Add a new expense",
            description = "Creates and saves a new expense for the logged-in user"
    )
    @PostMapping
    public Expense addExpense(
            @Valid @RequestBody Expense expense,
            HttpServletRequest request) {

        User user = getLoggedInUser(request);

        expense.setUser(user);

        return expenseService.addExpense(expense);
    }

    @Operation(
            summary = "Get all expenses",
            description = "Returns only expenses belonging to the logged-in user"
    )
    @GetMapping
    public List<Expense> getAllExpenses(
            HttpServletRequest request) {

        User user = getLoggedInUser(request);

        return expenseService.getExpensesByUser(
                user.getId()
        );
    }

    @Operation(
            summary = "Get expenses by category",
            description = "Returns only the logged-in user's expenses for the specified category"
    )
    @GetMapping("/category/{category}")
    public List<Expense> getExpensesByCategory(
            @PathVariable String category,
            HttpServletRequest request) {

        User user = getLoggedInUser(request);

        return expenseService.getExpensesByUserAndCategory(
                user.getId(),
                category
        );
    }

    @Operation(
            summary = "Get expenses by date",
            description = "Returns only the logged-in user's expenses for the specified date"
    )
    @GetMapping("/date/{date}")
    public List<Expense> getExpensesByDate(
            @PathVariable String date,
            HttpServletRequest request) {

        User user = getLoggedInUser(request);

        return expenseService.getExpensesByUserAndDate(
                user.getId(),
                date
        );
    }

    @Operation(
            summary = "Get expense by ID",
            description = "Returns an expense only if it belongs to the logged-in user"
    )
    @GetMapping("/{id}")
    public Expense getExpenseById(
            @PathVariable Long id,
            HttpServletRequest request) {

        User user = getLoggedInUser(request);

        Expense expense =
                expenseService.getExpenseById(id);

        if (expense.getUser() == null
                || !expense.getUser()
                .getId()
                .equals(user.getId())) {

            throw new ResourceNotFoundException(
                    "Expense not found"
            );
        }

        return expense;
    }

    @Operation(
            summary = "Update an expense",
            description = "Updates an expense only if it belongs to the logged-in user"
    )
    @PutMapping("/{id}")
    public Expense updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody Expense expense,
            HttpServletRequest request) {

        User user = getLoggedInUser(request);

        Expense existingExpense =
                expenseService.getExpenseById(id);

        if (existingExpense.getUser() == null
                || !existingExpense.getUser()
                .getId()
                .equals(user.getId())) {

            throw new ResourceNotFoundException(
                    "Expense not found"
            );
        }

        return expenseService.updateExpense(
                id,
                expense
        );
    }

    @Operation(
            summary = "Delete an expense",
            description = "Deletes an expense only if it belongs to the logged-in user"
    )
    @DeleteMapping("/{id}")
    public String deleteExpense(
            @PathVariable Long id,
            HttpServletRequest request) {

        User user = getLoggedInUser(request);

        Expense existingExpense =
                expenseService.getExpenseById(id);

        if (existingExpense.getUser() == null
                || !existingExpense.getUser()
                .getId()
                .equals(user.getId())) {

            throw new ResourceNotFoundException(
                    "Expense not found"
            );
        }

        expenseService.deleteExpense(id);

        return "Expense deleted successfully";
    }
}