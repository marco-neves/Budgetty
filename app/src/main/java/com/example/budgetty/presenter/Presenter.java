package com.example.budgetty.presenter;

import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Room;

import com.example.budgetty.database.Expense;
import com.example.budgetty.database.Database;
import com.example.budgetty.util.Logger;
import com.example.budgetty.view.MainActivity;

import java.util.List;

public class Presenter implements Contract.ExpensePresenter {

    private Database database;
    private List<Expense> expenses;
    private Contract.ExpenseView expenseView;
    private double cost, budgetMax;

    public Presenter(Contract.ExpenseView expenseView) {
        this.expenseView = expenseView;
        try {
            new readAllThread().execute();
        } catch (Exception e) {
            Logger.logIt("Could not build database");
        }
    }

    @Override
    public void getExpenses() {
        try {
            if (expenses.isEmpty()) {
                expenseView.noExpenses();
                cost = 0;
            } else {
                expenseView.displayExpenses(expenses);
                getTotalCost();
            }
        } catch (NullPointerException e) {
            Logger.logError("Could not get expenses: " + e);
        }
    }

    @Override
    public void addExpense(Expense expense) {
        try {
            new addThread().execute(expense);
            getTotalCost();
        } catch (Exception e) {
            Logger.logError("Could not add expense " + e);
        }
    }

    @Override
    public void updateExpense(Expense expense) {
        try {
            new updateThread().execute(expense);
            getTotalCost();
        } catch (Exception e) {
            Logger.logError("Could not update expense: " + e);
        }
    }

    @Override
    public void getTotalCost() {
        try {
            Logger.logIt("getTotalCost: ");
            new readCostThread().execute();
        } catch (Exception e) {
            Logger.logError("Could not get total cost " + e);
        }
    }

    @Override
    public void setBudgetMax(double budget) {
        this.budgetMax = budget;
        expenseView.displayBudget(budget);
        expenseView.refreshBk();
    }
    //****** INNER CLASSES TO SPAWN HELPER THREADS *******//

    private class readAllThread extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            database = Room.databaseBuilder(((MainActivity) expenseView),
                    Database.class, "room.db").
                    build();
            expenses = database.ExpenseDAO().getAllExpenses();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getExpenses();
        }
    }

    private class readCostThread extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            cost = database.ExpenseDAO().getTotalCost();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private class addThread extends AsyncTask<Expense, Void, Void> {

        @Override
        protected Void doInBackground(Expense... expense) {
            database.ExpenseDAO().insertNewExpense(expense[0]);
            expenses = database.ExpenseDAO().getAllExpenses();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            expenseView.redraw(expenses);
        }
    }

    private class updateThread extends AsyncTask<Expense, Void, Void> {

        @Override
        protected Void doInBackground(Expense... expense) {
            database.ExpenseDAO().updateExpense(expense[0]);
            expenses = database.ExpenseDAO().getAllExpenses();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            expenseView.redraw(expenses);
        }
    }

    private class deleteThread extends AsyncTask<Expense, Void, Void> {

        @Override
        protected Void doInBackground(Expense... expense) {
            database.ExpenseDAO().removeExpense(expense[0]);
            return null;
        }
    }
}
