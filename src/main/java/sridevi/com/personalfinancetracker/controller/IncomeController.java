package sridevi.com.personalfinancetracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import sridevi.com.personalfinancetracker.entity.Income;
import sridevi.com.personalfinancetracker.entity.User;
import sridevi.com.personalfinancetracker.exception.ResourceNotFoundException;
import sridevi.com.personalfinancetracker.repository.UserRepository;
import sridevi.com.personalfinancetracker.service.IncomeService;

import java.util.List;

@RestController
@RequestMapping("/incomes")
@Tag(
        name = "Income Management",
        description = "APIs for managing personal income"
)
@SecurityRequirement(name = "bearerAuth")
public class IncomeController {

    private final IncomeService incomeService;
    private final UserRepository userRepository;

    public IncomeController(
            IncomeService incomeService,
            UserRepository userRepository) {

        this.incomeService = incomeService;
        this.userRepository = userRepository;
    }

    // =========================
    // ADD INCOME
    // =========================

    @Operation(
            summary = "Add a new income",
            description = "Creates and saves a new income record for the logged-in user"
    )
    @PostMapping
    public Income addIncome(
            @Valid @RequestBody Income income,
            HttpServletRequest request) {

        String email =
                (String) request.getAttribute("userEmail");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        income.setUser(user);

        return incomeService.addIncome(income);
    }

    // =========================
    // GET ALL INCOMES
    // =========================

    @Operation(
            summary = "Get all incomes",
            description = "Returns only income records belonging to the logged-in user"
    )
    @GetMapping
    public List<Income> getAllIncome(
            HttpServletRequest request) {

        String email =
                (String) request.getAttribute("userEmail");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        return incomeService.getIncomesByUser(
                user.getId()
        );
    }

    // =========================
    // GET INCOMES BY DATE
    // =========================

    @Operation(
            summary = "Get incomes by date",
            description = "Returns only the logged-in user's income records for the specified date"
    )
    @GetMapping("/date/{date}")
    public List<Income> getIncomesByDate(
            @PathVariable String date,
            HttpServletRequest request) {

        String email =
                (String) request.getAttribute("userEmail");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        return incomeService.getIncomesByUserAndDate(
                user.getId(),
                date
        );
    }

    // =========================
    // GET INCOME BY ID
    // =========================

    @Operation(
            summary = "Get income by ID",
            description = "Returns an income record only if it belongs to the logged-in user"
    )
    @GetMapping("/{id}")
    public Income getIncomeById(
            @PathVariable Long id,
            HttpServletRequest request) {

        String email =
                (String) request.getAttribute("userEmail");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        Income income =
                incomeService.getIncomeById(id);

        if (!income.getUser().getId().equals(user.getId())) {

            throw new ResourceNotFoundException(
                    "Income not found"
            );
        }

        return income;
    }

    // =========================
    // UPDATE INCOME
    // =========================

    @Operation(
            summary = "Update an income",
            description = "Updates an income record only if it belongs to the logged-in user"
    )
    @PutMapping("/{id}")
    public Income updateIncome(
            @PathVariable Long id,
            @Valid @RequestBody Income income,
            HttpServletRequest request) {

        String email =
                (String) request.getAttribute("userEmail");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        Income existingIncome =
                incomeService.getIncomeById(id);

        if (!existingIncome.getUser().getId().equals(user.getId())) {

            throw new ResourceNotFoundException(
                    "Income not found"
            );
        }

        return incomeService.updateIncome(
                id,
                income
        );
    }

    // =========================
    // DELETE INCOME
    // =========================

    @Operation(
            summary = "Delete an income",
            description = "Deletes an income record only if it belongs to the logged-in user"
    )
    @DeleteMapping("/{id}")
    public String deleteIncome(
            @PathVariable Long id,
            HttpServletRequest request) {

        String email =
                (String) request.getAttribute("userEmail");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        Income existingIncome =
                incomeService.getIncomeById(id);

        if (!existingIncome.getUser().getId().equals(user.getId())) {

            throw new ResourceNotFoundException(
                    "Income not found"
            );
        }

        incomeService.deleteIncome(id);

        return "Income deleted successfully";
    }
}