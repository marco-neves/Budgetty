package com.example.budgetty.database;

public enum ExpenseType {
    FOOD ("FOOD"),
    HOUSING ("HOUSING"),
    TRANSPORTATION ("TRANSPORTATION"),
    UTILITIES ("UTILITIES"),
    MEDICAL ("MEDICAL"),
    RECREATION ("RECREATION"),
    MISC("MISC");
    private final String type;
    ExpenseType(String type) {
        this.type = type;
    }
}
