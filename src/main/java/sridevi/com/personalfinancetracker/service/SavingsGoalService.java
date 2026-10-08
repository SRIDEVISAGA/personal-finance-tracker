package sridevi.com.personalfinancetracker.service;

import org.springframework.stereotype.Service;
import sridevi.com.personalfinancetracker.entity.SavingsGoal;
import sridevi.com.personalfinancetracker.exception.ResourceNotFoundException;
import sridevi.com.personalfinancetracker.repository.SavingsGoalRepository;

import java.util.List;

@Service
public class SavingsGoalService {

    private final SavingsGoalRepository savingsGoalRepository;

    public SavingsGoalService(
            SavingsGoalRepository savingsGoalRepository) {

        this.savingsGoalRepository = savingsGoalRepository;
    }

    public SavingsGoal addSavingsGoal(
            SavingsGoal savingsGoal) {

        return savingsGoalRepository.save(savingsGoal);
    }

    public List<SavingsGoal> getAllSavingsGoals() {

        return savingsGoalRepository.findAll();
    }

    public List<SavingsGoal> getSavingsGoalsByUser(
            Long userId) {

        return savingsGoalRepository.findByUserId(userId);
    }

    public SavingsGoal getSavingsGoalById(Long id) {

        return savingsGoalRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Savings goal not found with id: " + id
                        ));
    }

    public SavingsGoal updateSavingsGoal(
            Long id,
            SavingsGoal savingsGoal) {

        SavingsGoal existingGoal =
                getSavingsGoalById(id);

        existingGoal.setGoalName(
                savingsGoal.getGoalName()
        );

        existingGoal.setTargetAmount(
                savingsGoal.getTargetAmount()
        );

        existingGoal.setSavedAmount(
                savingsGoal.getSavedAmount()
        );

        existingGoal.setTargetDate(
                savingsGoal.getTargetDate()
        );

        return savingsGoalRepository.save(
                existingGoal
        );
    }

    public void deleteSavingsGoal(Long id) {

        SavingsGoal existingGoal =
                getSavingsGoalById(id);

        savingsGoalRepository.delete(
                existingGoal
        );
    }

    public SavingsGoal addSavings(
            Long id,
            double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Savings amount must be greater than zero"
            );
        }

        SavingsGoal goal =
                getSavingsGoalById(id);

        double newSavedAmount =
                goal.getSavedAmount() + amount;

        if (newSavedAmount > goal.getTargetAmount()) {
            throw new IllegalArgumentException(
                    "Savings amount exceeds target amount"
            );
        }

        goal.setSavedAmount(newSavedAmount);

        return savingsGoalRepository.save(goal);
    }
}