package com.example.budgetty.presenter;

import com.example.budgetty.database.Expense;

import java.util.List;
public interface Contract {
    interface ExpensePresenter{
        void setBudgetMax(double n);
        void getExpenses();
        void updateExpense(Expense expense);
        void addExpense(Expense expense);
        void getTotalCost();
    }

    interface ExpenseView{
        void displayExpenses(List<Expense> expenses);
        void refreshBk();
        void noExpenses();
        void redraw(List<Expense> expenses);
        void displayBudget(double budget);
    }
}
