package com.example.budgetty.database;


import androidx.room.RoomDatabase;

@androidx.room.Database(version = 1, entities = Expense.class, exportSchema = false)
public abstract class Database extends RoomDatabase {

    public abstract ExpenseDAO ExpenseDAO();

}
