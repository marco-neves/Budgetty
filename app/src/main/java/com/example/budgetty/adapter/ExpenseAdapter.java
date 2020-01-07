package com.example.budgetty.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetty.R;
import com.example.budgetty.database.Expense;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    public interface ExpenseDelegate {
        void startNewFragment(Expense expense);
    }
    private List<Expense> expenses;
    private ExpenseDelegate delegate;
    public ExpenseAdapter(List<Expense> expenses, ExpenseDelegate delegate) {
        this.expenses = expenses;
        this.delegate = delegate;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_expenses_recyclerview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expense expense = expenses.get(position);
        holder.location.setText(expense.getLocation());
        holder.date.setText(expense.getDate());
        holder.cost.setText(String.format("$%.2f",expense.getCost()));
        holder.itemView.setOnClickListener(v -> delegate.startNewFragment(expense));
    }
    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView location;
        TextView date;
        TextView cost;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            location = itemView.findViewById(R.id.location_textview_main_recyclerview);
            date = itemView.findViewById(R.id.date_textview_main_recyclerview);
            cost = itemView.findViewById(R.id.cost_textview_main_recyclerview);
        }
    }
}
