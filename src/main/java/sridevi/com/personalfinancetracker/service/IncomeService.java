package sridevi.com.personalfinancetracker.service;

import org.springframework.stereotype.Service;
import sridevi.com.personalfinancetracker.entity.Income;
import sridevi.com.personalfinancetracker.exception.ResourceNotFoundException;
import sridevi.com.personalfinancetracker.repository.IncomeRepository;

import java.util.List;

@Service
public class IncomeService {

    private final IncomeRepository incomeRepository;

    public IncomeService(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }

    // ADD INCOME
    public Income addIncome(Income income) {
        return incomeRepository.save(income);
    }

    // GET ALL INCOME
    public List<Income> getAllIncome() {
        return incomeRepository.findAll();
    }

    // GET INCOME BY ID
    public Income getIncomeById(Long id) {

        return incomeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Income not found with id: " + id
                        ));
    }

    // GET INCOME BY DATE
    public List<Income> getIncomesByDate(String date) {

        return incomeRepository.findByDate(date);
    }

    // GET INCOME BY USER
    public List<Income> getIncomesByUser(Long userId) {

        return incomeRepository.findByUserId(userId);
    }

    // GET INCOME BY USER AND DATE
    public List<Income> getIncomesByUserAndDate(
            Long userId,
            String date) {

        return incomeRepository.findByUserIdAndDate(
                userId,
                date
        );
    }

    // UPDATE INCOME
    public Income updateIncome(
            Long id,
            Income income) {

        Income existingIncome =
                getIncomeById(id);

        existingIncome.setSource(
                income.getSource()
        );

        existingIncome.setAmount(
                income.getAmount()
        );

        existingIncome.setDate(
                income.getDate()
        );

        return incomeRepository.save(
                existingIncome
        );
    }

    // DELETE INCOME
    public void deleteIncome(Long id) {

        Income existingIncome =
                getIncomeById(id);

        incomeRepository.delete(
                existingIncome
        );
    }
}