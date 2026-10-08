# Personal Finance Tracker

A RESTful backend application developed using Spring Boot and MySQL to manage personal income, expenses, budgets, savings goals, and financial reports.

## Features

- User registration and login
- Password encryption using BCrypt
- JWT-based authentication
- Add, view, update, and delete expenses
- Add, view, update, and delete income records
- Create and manage monthly budgets
- Track savings goals and add savings
- User-specific data access
- Financial summary and dashboard statistics
- Expense reports by category and date
- Monthly income and expense summary
- Budget vs spending report
- Savings goal progress report
- Input validation and exception handling
- Swagger/OpenAPI documentation

## Technologies Used

- Java 26
- Spring Boot 4.0.1
- Spring Data JPA
- Hibernate
- MySQL 8
- REST API
- JWT
- BCrypt
- Swagger / OpenAPI
- Maven
- IntelliJ IDEA

## Project Structure

```text
src/main/java/sridevi/com/personalfinancetracker
│
├── controller
│   ├── AuthController.java
│   ├── ExpenseController.java
│   ├── IncomeController.java
│   ├── BudgetController.java
│   ├── SavingsGoalController.java
│   ├── ReportController.java
│   ├── SummaryController.java
│   └── GlobalExceptionHandler.java
│
├── entity
│   ├── User.java
│   ├── Expense.java
│   ├── Income.java
│   ├── Budget.java
│   └── SavingsGoal.java
│
├── repository
│   ├── UserRepository.java
│   ├── ExpenseRepository.java
│   ├── IncomeRepository.java
│   ├── BudgetRepository.java
│   └── SavingsGoalRepository.java
│
├── service
│   ├── UserService.java
│   ├── JwtService.java
│   ├── ExpenseService.java
│   ├── IncomeService.java
│   ├── BudgetService.java
│   ├── SavingsGoalService.java
│   └── ReportService.java
│
├── security
│   └── JwtAuthenticationFilter.java
│
├── exception
│   └── ResourceNotFoundException.java
│
└── dto
    └── AuthResponse.java