package sridevi.com.personalfinancetracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import sridevi.com.personalfinancetracker.entity.SavingsGoal;
import sridevi.com.personalfinancetracker.entity.User;
import sridevi.com.personalfinancetracker.exception.ResourceNotFoundException;
import sridevi.com.personalfinancetracker.repository.UserRepository;
import sridevi.com.personalfinancetracker.service.SavingsGoalService;

import java.util.List;

@RestController
@RequestMapping("/savings-goals")
@Tag(
        name = "Savings Goal Management",
        description = "APIs for managing personal savings goals"
)
@SecurityRequirement(name = "bearerAuth")
public class SavingsGoalController {

    private final SavingsGoalService savingsGoalService;
    private final UserRepository userRepository;

    public SavingsGoalController(
            SavingsGoalService savingsGoalService,
            UserRepository userRepository) {

        this.savingsGoalService = savingsGoalService;
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
            summary = "Add a new savings goal",
            description = "Creates a savings goal for the logged-in user"
    )
    @PostMapping
    public SavingsGoal addSavingsGoal(
            @Valid @RequestBody SavingsGoal savingsGoal,
            HttpServletRequest request) {

        User user = getLoggedInUser(request);

        savingsGoal.setUser(user);

        return savingsGoalService.addSavingsGoal(
                savingsGoal
        );
    }

    @Operation(
            summary = "Get all savings goals",
            description = "Returns only savings goals belonging to the logged-in user"
    )
    @GetMapping
    public List<SavingsGoal> getAllSavingsGoals(
            HttpServletRequest request) {

        User user = getLoggedInUser(request);

        return savingsGoalService.getSavingsGoalsByUser(
                user.getId()
        );
    }

    @Operation(
            summary = "Get savings goal by ID",
            description = "Returns a savings goal only if it belongs to the logged-in user"
    )
    @GetMapping("/{id}")
    public SavingsGoal getSavingsGoalById(
            @PathVariable Long id,
            HttpServletRequest request) {

        User user = getLoggedInUser(request);

        SavingsGoal goal =
                savingsGoalService.getSavingsGoalById(id);

        if (goal.getUser() == null
                || !goal.getUser().getId().equals(user.getId())) {

            throw new ResourceNotFoundException(
                    "Savings goal not found"
            );
        }

        return goal;
    }

    @Operation(
            summary = "Update a savings goal",
            description = "Updates a savings goal only if it belongs to the logged-in user"
    )
    @PutMapping("/{id}")
    public SavingsGoal updateSavingsGoal(
            @PathVariable Long id,
            @Valid @RequestBody SavingsGoal savingsGoal,
            HttpServletRequest request) {

        User user = getLoggedInUser(request);

        SavingsGoal existingGoal =
                savingsGoalService.getSavingsGoalById(id);

        if (existingGoal.getUser() == null
                || !existingGoal.getUser().getId().equals(user.getId())) {

            throw new ResourceNotFoundException(
                    "Savings goal not found"
            );
        }

        return savingsGoalService.updateSavingsGoal(
                id,
                savingsGoal
        );
    }

    @Operation(
            summary = "Add savings to a goal",
            description = "Adds money to a savings goal belonging to the logged-in user"
    )
    @PutMapping("/{id}/add-savings")
    public SavingsGoal addSavings(
            @PathVariable Long id,
            @RequestParam double amount,
            HttpServletRequest request) {

        User user = getLoggedInUser(request);

        SavingsGoal existingGoal =
                savingsGoalService.getSavingsGoalById(id);

        if (existingGoal.getUser() == null
                || !existingGoal.getUser().getId().equals(user.getId())) {

            throw new ResourceNotFoundException(
                    "Savings goal not found"
            );
        }

        return savingsGoalService.addSavings(
                id,
                amount
        );
    }

    @Operation(
            summary = "Delete a savings goal",
            description = "Deletes a savings goal only if it belongs to the logged-in user"
    )
    @DeleteMapping("/{id}")
    public String deleteSavingsGoal(
            @PathVariable Long id,
            HttpServletRequest request) {

        User user = getLoggedInUser(request);

        SavingsGoal existingGoal =
                savingsGoalService.getSavingsGoalById(id);

        if (existingGoal.getUser() == null
                || !existingGoal.getUser().getId().equals(user.getId())) {

            throw new ResourceNotFoundException(
                    "Savings goal not found"
            );
        }

        savingsGoalService.deleteSavingsGoal(id);

        return "Savings goal deleted successfully";
    }
}