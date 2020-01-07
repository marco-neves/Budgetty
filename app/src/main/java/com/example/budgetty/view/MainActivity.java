package com.example.budgetty.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.budgetty.R;
import com.example.budgetty.adapter.ExpenseAdapter;
import com.example.budgetty.database.Expense;
import com.example.budgetty.presenter.Contract;
import com.example.budgetty.presenter.Presenter;
import com.example.budgetty.util.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements Contract.ExpenseView, ExpenseAdapter.ExpenseDelegate {

    @BindView(R.id.background_percent)
    ImageView backgroundView;
    @BindView(R.id.new_expense_button)
    Button addExpenseButton;
    @BindView(R.id.layout_main)
    ConstraintLayout layout;
    @BindView(R.id.expenses_recyclerview)
    RecyclerView recyclerViewExpenses;
    @BindView(R.id.set_budget_button)
    Button budget;

    Contract.ExpensePresenter presenter;
    SharedPreferences sharedPreferences;

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Expense expense = intent.getParcelableExtra(Constants.RETRIEVE_EXPENSE_KEY);
            presenter.addExpense(expense);
        }
    };

    BroadcastReceiver receiverTwo = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Expense expense = intent.getParcelableExtra(Constants.RETRIEVE_SELECTED_EXPENSE_KEY);
            presenter.updateExpense(expense);
        }
    };

    BroadcastReceiver receiverThree = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            double budgetNum = intent.getDoubleExtra(Constants.SET_BUDGET_KEY,0);
            presenter.setBudgetMax(budgetNum);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat(Constants.GET_MY_BUDGET_SP_KEY,(float)budgetNum);
            editor.apply();
            editor.clear();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(receiver);
        this.unregisterReceiver(receiverTwo);
        this.unregisterReceiver(receiverThree);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new Presenter(this);
        presenter.getExpenses();
        sharedPreferences = getSharedPreferences(Constants.GET_SHARED_PREFERENCES,MODE_PRIVATE);
        float my_budget = sharedPreferences.getFloat(Constants.GET_MY_BUDGET_SP_KEY,0);
        if(my_budget==0){
            Toast.makeText(this, "Please enter a budget.", Toast.LENGTH_SHORT).show();
        }else{
            presenter.setBudgetMax(my_budget);
        }

        this.registerReceiver(receiver, new IntentFilter(Constants.ADD_EXPENSE_ACTION));
        this.registerReceiver(receiverTwo, new IntentFilter(Constants.VIEW_EXPENSE_ACTION));
        this.registerReceiver(receiverThree, new IntentFilter(Constants.SET_BUDGET_ACTION));
    }

    @Override
    public void displayExpenses(List<Expense> expenses) {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        DividerItemDecoration decoration = new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL);
        recyclerViewExpenses.setLayoutManager(manager);
        recyclerViewExpenses.setAdapter(new ExpenseAdapter(expenses,this));
        recyclerViewExpenses.addItemDecoration(decoration);
    }

    @Override
    public void displayBudget(double budgetNum) {
        budget.setText(String.format("$%.2f",budgetNum));
    }

    @Override
    public void noExpenses() {}

    @Override
    public void redraw(List<Expense> expenses) {
        recyclerViewExpenses.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewExpenses.setAdapter(new ExpenseAdapter(expenses,this));
    }

    @OnClick(R.id.new_expense_button)
    public void onClick(View view){
        AddFragment addFragment = new AddFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .add(R.id.main_frame, addFragment)
                .addToBackStack(addFragment.getTag())
                .commit();
    }

    @Override
    public void startNewFragment(Expense expense) {
        ViewFragment viewFragment = new ViewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.RETRIEVE_SELECTED_EXPENSE_KEY,expense);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        viewFragment.setArguments(bundle);
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .add(R.id.main_frame, viewFragment)
                .addToBackStack(viewFragment.getTag())
                .commit();
    }

    @OnClick(R.id.set_budget_button)
    public void onClickBudget(View v) {
        BudgetFragment budgetFrag = new BudgetFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .add(R.id.main_frame, budgetFrag)
                .addToBackStack(budgetFrag.getTag())
                .commit();
    }
    @Override
    public void refreshBk() {
        backgroundView.setBackgroundColor(getResources().getColor(R.color.red));
    }
}
